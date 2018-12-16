package com.example.podar.todoapp2.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.example.podar.todoapp2.model.TodoItem
import com.example.podar.todoapp2.network.NetworkHelper
import dagger.Component
import kotlinx.coroutines.experimental.selects.select
import org.jetbrains.anko.db.*
import kotlin.math.log

/**
 * Created by cpodariu on 17-Mar-18.
 * For any questions please contact me at podariucatalin97@gmail.com
 */

class MySqlHelper(ctx: Context) : ManagedSQLiteOpenHelper(ctx, "TodoAppDatabaseAndroid", null, MySqlHelper.DB_VERSION) {

    val context = ctx;

    companion object {
        val DB_VERSION = 11

        private var instance: MySqlHelper? = null

        @Synchronized
        fun getInstance(ctx: Context): MySqlHelper {
            if (instance == null) {
                instance = MySqlHelper(ctx.applicationContext)
            }
            return instance!!
        }
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.createTable(
            "TodoItem", true,
            "id" to INTEGER + PRIMARY_KEY + AUTOINCREMENT,
            "serverId" to INTEGER + DEFAULT("0"),
            "value" to TEXT,
            "synced" to INTEGER + DEFAULT("1")
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        Log.e(
            this.javaClass.simpleName + ": ",
            "Database upgraded from " + oldVersion.toString() + " to " + newVersion.toString()
        );
        db.dropTable("TodoItem")
        onCreate(db)
//        if (oldVersion < 2) {
//            val rowParser = classParser<TodoItem>()
//            val itemsList: ArrayList<TodoItem> = ArrayList<TodoItem>()
//
//            val querryResult = db.select("TodoItem").parseList(rowParser)
//            itemsList.addAll(querryResult)
//
//            Log.i(MySqlHelper.javaClass.name, "database upgraded to version 2")
//            db.createTable(
//                "TodoItem", true,
//                "_id" to INTEGER + PRIMARY_KEY + AUTOINCREMENT,
//                "value" to TEXT,
//                "synced" to INTEGER
//            )
//
//            db.insert("TodoItem", "value" to value)
//
//
//        }
    }

}

// Access property for Context
val Context.database: MySqlHelper
    get() = MySqlHelper.getInstance(applicationContext)