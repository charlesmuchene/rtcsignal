package com.sensei.signal

interface Signals {

    fun onFull() {}
    fun onJoin() {}
    fun onJoined() {}
    fun onCreated() {}
    fun onConnected() {}
    fun onDisconnected() {}
    fun onLog(data: Array<out Any>) {}
    fun onMessage(data: Array<out Any>) {}

}