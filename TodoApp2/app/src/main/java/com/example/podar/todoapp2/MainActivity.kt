package com.example.podar.todoapp2

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.LinearLayout
import com.example.podar.todoapp2.model.TodoItem
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.coroutines.onClick


class MainActivity : AppCompatActivity() {

    private val todoItemsArrayList = ArrayList<TodoItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        todoItemsList.layoutManager = LinearLayoutManager(this)

        toolbar.title = "TODOS"
//        database.use { update("TodoItem", "lastID" to "MEMO/18/2601")
//                .whereSimple("_id = ?", "1")
//                .exec()  }

        todoItemsList.adapter = TodoItemsAdapter(todoItemsArrayList, this)
//        todoItemsArrayList.add(TodoItem(1, "asds"))

//        updateTodoItemList()
//
        fab.onClick {
            alert {
                title = "New todoItems:"
                lateinit var et: EditText
                positiveButton("Add") {
                    todoItemsArrayList.add(TodoItem(et.text.toString()))
                }
                customView {
                    linearLayout {
                        orientation = LinearLayout.VERTICAL
                        padding = dip(16)
                        linearLayout {
                            padding = dip(16)
                            val tv = textView()
                            tv.text = context.getString(R.string.add_alert_message)
                            tv.textSize = 16f
                        }
                        linearLayout {
                            et = editText()
                            et.height = dip(64)
                            padding = dip(0)
                            et.hint = "Add todo here"
                            gravity = Gravity.CENTER
                        }
                    }
                }
            }.show()
        }
    }
}
