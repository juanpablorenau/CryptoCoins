package com.example.listadodecryptomonedas

import android.app.Application

class App : Application() {

    companion object {
        lateinit var instance: App private set
        lateinit var preferences: AppPreferences
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        preferences = AppPreferences(instance)
    }
}
