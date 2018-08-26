package com.sensei.rtcsignalexample

import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import com.sensei.signal.RTCSignal
import com.sensei.signal.Signals
import timber.log.Timber

class HomeActivity : AppCompatActivity() {

    private lateinit var handler: Handler
    private lateinit var callback: () -> Unit

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        RTCSignal.initialize("123", object : Signals {
            override fun onFull() {
                Timber.e("onFull!")
            }

            override fun onJoin() {
                Timber.e("onJoin!")
            }

            override fun onJoined() {
                Timber.e("onJoined!")
            }

            override fun onCreated() {
                Timber.e("onCreated!")
            }

            override fun onConnected() {
                Timber.e("onConnected!")
            }

            override fun onDisconnected() {
                Timber.e("onDisconnected!")
            }

            override fun onLog(data: Array<out Any>) {
                Timber.e("onLog!")
            }

            override fun onMessage(data: Array<out Any>) {
                Timber.e("onMessage!")
            }

        })

        RTCSignal.connect()

        handler = Handler(mainLooper)
        callback = { RTCSignal.sendMessage("We are one!") }
        handler.postDelayed(callback, 1000L)
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(callback)
        RTCSignal.disconnect()
    }
}
