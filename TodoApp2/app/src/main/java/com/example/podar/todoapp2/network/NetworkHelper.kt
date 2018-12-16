package com.example.podar.todoapp2.network

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.beust.klaxon.Klaxon
import com.example.podar.todoapp2.model.TodoItem
import com.example.podar.todoapp2.receivers.NetworkChangeReceiver
import khttp.responses.Response
import org.json.JSONArray
import org.json.JSONObject
import java.lang.Exception
import java.net.URL

object NetworkHelper {
    final val URL = "http://192.168.43.138:8080/api"

    fun getTodoItems(): List<TodoItem> {
        val res = Klaxon().parseArray<TodoItem>(khttp.get(this.URL + "/todos").text)

        if (res != null)
            return res;
        return ArrayList<TodoItem>();


    }

    fun addTodoItem(item: TodoItem): TodoItem? {
        return Klaxon().parse<TodoItem>(khttp.post(URL + "/todos", json = JSONObject(Klaxon().toJsonString(item))).text)
    }

    fun deleteTodoItem(id: Long) {
        khttp.delete(URL + "/todos/" + id)
    }

    fun updateTodoItem(item: TodoItem): TodoItem? {
        return Klaxon().parse<TodoItem>(khttp.put(URL + "/todos", json = JSONObject(Klaxon().toJsonString(item))).text)
    }

    fun isOnline(ctx: Context): Boolean {
        if (!NetworkChangeReceiver.isOnline(ctx))
            return false;
        return true;
    }

    fun checkNetwork(ctx: Context): Boolean {
        if (!NetworkChangeReceiver.isOnline(ctx))
            return false;
        var status: Int;
        try {
            status = khttp.get(URL + "/todos", timeout = 1.0).statusCode
        } catch (e: Exception) {
            status = 400;
        }
        return status == 200;
    }
}