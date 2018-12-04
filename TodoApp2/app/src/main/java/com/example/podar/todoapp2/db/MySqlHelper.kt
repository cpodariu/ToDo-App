package com.example.podar.todoapp2.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import org.jetbrains.anko.db.*
import kotlin.math.log

/**
 * Created by cpodariu on 17-Mar-18.
 * For any questions please contact me at podariucatalin97@gmail.com
 */
class MySqlHelper(ctx: Context) : ManagedSQLiteOpenHelper(ctx, "TodoAppDatabaseAndroid", null, MySqlHelper.DB_VERSION) {

    companion object {
        val DB_VERSION = 1

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
            "_id" to INTEGER + PRIMARY_KEY + AUTOINCREMENT,
            "word" to TEXT
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
//        Log.e(this.javaClass.simpleName + ": ", "Database upgraded from " + oldVersion.toString() + " to " + newVersion.toString());
//        db.dropTable("KeyWord")
//        onCreate(db)
//        if (oldVersion < 4)
//        {
//            Log.i(MySqlHelper.javaClass.name, "database upgraded to version 4")
//            db.createTable("SentNotifications", true,
//                    "_id" to TEXT + UNIQUE)
//        }
    }

}

// Access property for Context
val Context.database: MySqlHelper
    get() = MySqlHelper.getInstance(applicationContext)