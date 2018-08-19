package com.sensei.signal

import io.socket.client.IO
import io.socket.client.Socket
import timber.log.Timber

object RTCSignal {

    private val socket: Socket by lazy { IO.socket(initialize()) }
    private external fun initialize(): String

    init {
        System.loadLibrary(configuration)
        Timber.plant(Timber.DebugTree())
    }

    fun initialize(id: String, signals: Signals) {
        socket.on(Socket.EVENT_CONNECT) {
            Timber.d("Signalling Server: Connect")
            socket.emit(CREATE, id)
            signals.onConnected()
        }.on(CREATED) {
            Timber.d("Signalling Server: created")
            signals.onCreated()
        }.on(FULL) {
            Timber.d("Signalling Server: full")
            signals.onFull()
        }.on(JOIN) {
            Timber.d("Signalling Server: join")
            Timber.d("Signalling Server: Another peer made a request to join call")
            Timber.d("Signalling Server: This peer is the initiator of call")
            signals.onJoin()
        }.on(JOINED) {
            signals.onJoined()
        }.on(LOG) { arguments ->
            Timber.d("Logging server log")
            arguments.forEach { Timber.d("Signalling Server: $it") }
            signals.onLog(arguments)
        }.on(MESSAGE) { arguments ->
            Timber.d("Signalling Server: Got a message")
            arguments.forEach { Timber.d(it.toString()) }
            signals.onMessage(arguments)
        }.on(Socket.EVENT_DISCONNECT) {
            Timber.d("Signalling Server: disconnect")
            signals.onDisconnected()
        }
    }

    fun connect() {
        socket.connect()
    }

    fun disconnect() {
        socket.disconnect()
    }
}