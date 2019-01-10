package com.example.podar.todoapp2

import android.content.IntentFilter
import android.net.ConnectivityManager
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
import com.example.podar.todoapp2.network.NetworkHelper
import com.example.podar.todoapp2.receivers.NetworkChangeReceiver
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import javax.inject.Inject
import android.support.v4.widget.SwipeRefreshLayout
import android.util.Log
import com.bugsnag.android.Bugsnag
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.InterstitialAd




class MainActivity : AppCompatActivity() {

    private val todoItemsArrayList = ArrayList<TodoItem>()
    private val controller = TodoItemsController(this,  fun() { update() });
    private val adapter = TodoItemsAdapter(todoItemsArrayList, this, controller)

    private val mInterstitialAd: InterstitialAd = InterstitialAd(this);

    fun update(){
        adapter.updateListItems();
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Bugsnag.init(this)
        Bugsnag.notify(RuntimeException("Test error"))
        todoItemsList.layoutManager = LinearLayoutManager(this)
        toolbar.title = "TODOS"

        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        mInterstitialAd.loadAd(AdRequest.Builder().build())
        mInterstitialAd.adListener = object : AdListener(){
                override fun onAdLoaded() {
                    super.onAdLoaded()
                    mInterstitialAd.show();
                }
        }

        doAsync {
            controller.syncWithServer(ctx, fun() { adapter.updateListItems() })
        }


        registerReceiver(
            NetworkChangeReceiver(controller, fun() { adapter.updateListItems() }),
            IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        );

        todoItemsList.adapter = adapter


        fab.onClick {
//            if (mInterstitialAd.isLoaded) {
//                mInterstitialAd.show()
//            } else {
//                Log.d("TAG", "The interstitial wasn't loaded yet.")
//            };
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
