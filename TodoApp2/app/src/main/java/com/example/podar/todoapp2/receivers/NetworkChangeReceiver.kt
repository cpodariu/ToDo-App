package com.example.podar.todoapp2.receivers

import android.annotation.SuppressLint
import android.net.NetworkInfo
import android.content.Context.CONNECTIVITY_SERVICE
import android.support.v4.content.ContextCompat.getSystemService
import android.net.ConnectivityManager
import android.content.Intent
import android.content.BroadcastReceiver
import android.content.Context
import android.util.Log
import com.example.podar.todoapp2.controller.TodoItemsController
import org.jetbrains.anko.doAsync


class NetworkChangeReceiver(val ctrl: TodoItemsController, cb: () -> Unit) : BroadcastReceiver() {
    val callback = cb;

    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    override fun onReceive(context: Context, intent: Intent) {
        try {
            if (isOnline(context)) {
                doAsync {
                    ctrl.syncWithServer(context, callback);
                }
            } else {
                Log.d(this.javaClass.name, "OFFLINE")
            }
        } catch (e: NullPointerException) {
            e.printStackTrace()
        }

    }

    companion object {
        public fun isOnline(context: Context): Boolean {
            try {
                val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                val netInfo = cm.activeNetworkInfo
                //should check null because in airplane mode it will be null
                return netInfo != null && netInfo.isConnected
            } catch (e: NullPointerException) {
                e.printStackTrace()
                return false
            }
        }
    }
}