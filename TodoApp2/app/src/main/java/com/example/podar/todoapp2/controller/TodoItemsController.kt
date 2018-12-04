package com.example.podar.todoapp2.controller

import android.content.Context
import com.example.podar.todoapp2.db.database
import com.example.podar.todoapp2.model.TodoItem
import org.jetbrains.anko.db.*

class TodoItemsController(val ctx: Context) {
    fun add(word: String) {
        ctx.database.use {
            insert("TodoItem", "word" to word)
        }
    }

    fun update(id: Long, word: String) {
        ctx.database.use {
            update("TodoItem", "word" to word)
                .whereSimple("_id = ?", id.toString())
                .exec()
        }
    }

    fun delete(id: Long) {
        ctx.database.use {
            ctx.database.use { execSQL("DELETE FROM TodoItem WHERE _id is " + id) }
        }
    }

    fun getAll() : List<TodoItem> {
        val rowParser = classParser<TodoItem>()

        val itemsList: ArrayList<TodoItem> = ArrayList<TodoItem>()

        ctx.database.use {
            val querryResult = select("TodoItem").parseList(rowParser)
            itemsList.addAll(querryResult)
        }
        return itemsList;
    }
}