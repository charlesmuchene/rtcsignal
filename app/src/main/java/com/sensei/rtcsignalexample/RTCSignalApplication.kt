package com.sensei.rtcsignalexample

import android.app.Application
import timber.log.Timber

class RTCSignalApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }

}