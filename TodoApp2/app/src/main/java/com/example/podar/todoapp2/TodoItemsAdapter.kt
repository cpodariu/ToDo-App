package com.example.podar.todoapp2

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import com.daimajia.swipe.SwipeLayout
import com.example.podar.todoapp2.controller.TodoItemsController
import com.example.podar.todoapp2.model.TodoItem
import kotlinx.android.synthetic.main.list_item_layout.view.*
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.sdk25.coroutines.onLongClick


class TodoItemsAdapter(val todoItemsList: ArrayList<TodoItem>, val ctx: Context, val controller: TodoItemsController) :
    RecyclerView.Adapter<TodoItemsAdapter.ListItemViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoItemsAdapter.ListItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_layout, parent, false)
        return TodoItemsAdapter.ListItemViewHolder(view)
    }

    override fun getItemCount(): Int {
        return todoItemsList.size;
    }

    override fun onBindViewHolder(holder: ListItemViewHolder, position: Int) {
        holder.bindTodoItem(
            todoItemsList[position],
            ctx,
            fun(k: TodoItem) {
                controller.delete(k.id)
                updateListItems()
            },
            fun(k: TodoItem, s: String) {
                controller.update(k.id, s)
                updateListItems()
            })
    }

    public fun updateListItems() {
        todoItemsList.clear()
        todoItemsList.addAll(controller.getAll())
        this.notifyDataSetChanged()
    }

    class ListItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindTodoItem(
            todoItem: TodoItem,
            ctx: Context,
            deleteElem: (TodoItem) -> Unit,
            updateElem: (TodoItem, String) -> Unit
        ) {
            itemView.key_words_text_view.text = todoItem.word
            itemView.swipe_layout.addDrag(SwipeLayout.DragEdge.Right, itemView.bottom_wrapper);
            itemView.delete_button.onClick {
                deleteElem(todoItem)
            }
            itemView.onLongClick {
                ctx.alert {
                    title = "Update todoItems:"
                    lateinit var et: EditText
                    positiveButton("Update") {
                        updateElem(todoItem, et.text.toString())

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
}