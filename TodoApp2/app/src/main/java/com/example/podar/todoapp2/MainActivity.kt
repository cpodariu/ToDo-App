package com.example.podar.todoapp2

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.LinearLayout
import com.example.podar.todoapp2.controller.TodoItemsController
import com.example.podar.todoapp2.model.TodoItem
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.coroutines.onClick


class MainActivity : AppCompatActivity() {

    private val todoItemsArrayList = ArrayList<TodoItem>()
    private val controller = com.example.podar.todoapp2.controller.TodoItemsController(this);
    private val adapter = TodoItemsAdapter(todoItemsArrayList, this, controller)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        todoItemsList.layoutManager = LinearLayoutManager(this)
        toolbar.title = "TODOS"


        todoItemsList.adapter = adapter


        fab.onClick {
            alert {
                title = "New todoItems:"
                lateinit var et: EditText
                positiveButton("Add") {
                    controller.add(et.text.toString())
                    adapter.updateListItems()
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

        adapter.updateListItems()
    }
}
