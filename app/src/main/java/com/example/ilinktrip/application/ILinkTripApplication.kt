package com.example.ilinktrip.application

import android.app.Application
import android.content.Context

class ILinkTripApplication : Application() {
    object Globals {
        var appContext: Context? = null
    }

    override fun onCreate() {
        super.onCreate()
        Globals.appContext = applicationContext
    }
}