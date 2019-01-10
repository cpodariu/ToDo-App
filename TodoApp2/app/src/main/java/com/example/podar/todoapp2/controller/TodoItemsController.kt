package com.example.podar.todoapp2.controller

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.widget.Toast
import com.example.podar.todoapp2.db.database
import com.example.podar.todoapp2.model.TodoItem
import com.example.podar.todoapp2.network.NetworkHelper
import com.example.podar.todoapp2.network.NetworkHelper.deleteTodoItem
import org.jetbrains.anko.db.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.runOnUiThread
import java.util.concurrent.locks.ReentrantLock

class TodoItemsController(val ctx: Context, val callback: () -> Unit) {
    var isSynced = false;
    private val lock = ReentrantLock()


    fun add(word: String) {
        val values = ContentValues();
        values.put("value", word);
        values.put("synced", false);

        var id: Long = 0;

        ctx.database.use {
            id = insert("TodoItem", null, values)
        }

        doAsync {
            if (NetworkHelper.checkNetwork(ctx)) {
                ctx.database.use { execSQL("DELETE FROM TodoItem WHERE id is " + id) }
                val item = NetworkHelper.addTodoItem(TodoItem(value = word))
                values.put("synced", true)
                values.put("id", item?.id)
                ctx.database.use { insert("TodoItem", null, values) }
                callback()
            }
        }
    }

    fun update(id: Long, word: String): Boolean {
        if (NetworkHelper.isOnline(ctx)) {
            doAsync { }
            ctx.database.use {
                update("TodoItem", "value" to word)
                    .whereSimple("id = ?", id.toString())
                    .exec()
            }
            doAsync {
                NetworkHelper.updateTodoItem(TodoItem(id = id, value = word));
            }
            return true
        }
        return false;

    }

    fun delete(id: Long): Boolean {
        if (NetworkHelper.isOnline(ctx)) {
            ctx.database.use { execSQL("DELETE FROM TodoItem WHERE id is " + id) }
            doAsync {
                deleteTodoItem(id);
            }
            return true;
        }
        return false;
    }

    fun getAll(): List<TodoItem> {
        val rowParser = classParser<TodoItem>()

        val itemsList: ArrayList<TodoItem> = ArrayList<TodoItem>()

        ctx.database.use {
            val querryResult = select("TodoItem").parseList(rowParser)
            itemsList.addAll(querryResult)
        }
        return itemsList;
    }

    fun syncWithServer(ctx: Context, callback: () -> Unit) {
        lock.lock();
//        if (!isSynced) {
        if (NetworkHelper.checkNetwork(ctx)) {
            getAll().forEach { it ->
                run {
                    if (!it.synced) {
                        ctx.database.use { execSQL("DELETE FROM TodoItem WHERE id is " + it.id) }
                        NetworkHelper.addTodoItem(TodoItem(value = it.value))
                    }
                }
            }

            val items = NetworkHelper.getTodoItems()
            ctx.database.use {
                beginTransaction()
                try {
                    items.forEach { it ->
                        run {
                            val values = ContentValues();
                            values.put("id", it.id);
                            values.put("value", it.value);
                            values.put("synced", it.synced);
                            insertWithOnConflict(
                                "TodoItem", null,
                                values, SQLiteDatabase.CONFLICT_REPLACE
                            )
                        }
                    }
                    setTransactionSuccessful()
                } finally {
                    endTransaction()
                }
            }
            this.isSynced = true;
            callback()
        } else {
            ctx.runOnUiThread {
                Toast.makeText(ctx, "No internet connection", Toast.LENGTH_LONG).show()
            }
//            }
        }
        lock.unlock();
    }
}