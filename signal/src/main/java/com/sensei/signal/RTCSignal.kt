package com.sensei.signal

import io.socket.client.IO
import io.socket.client.Socket
import timber.log.Timber

object RTCSignal {

    private val socket: Socket by lazy { IO.socket(initialize()) }
    private external fun initialize(): String
    private var isInitialized = false
    private var isConnected = false

    init {
        System.loadLibrary(configuration)
        Timber.plant(Timber.DebugTree())
    }

    fun initialize(id: String, signals: Signals) {
        if (isInitialized) return
        isInitialized = true
        socket.on(Socket.EVENT_CONNECT) {
            socket.emit(CREATE, id)
            signals.onConnected()
        }.on(CREATED) {
            signals.onCreated()
        }.on(FULL) {
            signals.onFull()
        }.on(JOIN) {
            signals.onJoin()
        }.on(JOINED) {
            signals.onJoined()
        }.on(LOG) { arguments ->
            signals.onLog(arguments)
        }.on(MESSAGE) { arguments ->
            arguments.forEach { Timber.d(it.toString()) }
            signals.onMessage(arguments)
        }.on(Socket.EVENT_DISCONNECT) {
            signals.onDisconnected()
        }
    }

    fun connect() {
        if (isConnected) return
        isConnected = true
        socket.connect()
    }

    fun disconnect() {
        if (isConnected) socket.disconnect()
        isConnected = false
    }

    @Throws(IllegalStateException::class)
    fun sendMessage(message: Any) {
        if (!isConnected)
            throw IllegalStateException("Bad state! Did you forget to call initialize?")
        socket.emit(MESSAGE, message)
    }
}