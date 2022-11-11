package com.example.listadodecryptomonedas

import android.content.Context
import android.content.SharedPreferences
import com.example.listadodecryptomonedas.data.model.SharedPrefObject
import com.google.gson.Gson

class AppPreferences(val context: Context) {

    private val PREF_FILE = "MyPreferences"

    private fun getSharedPreferences(): SharedPreferences? {
        return context.getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE)
    }

    fun getObject(key: String): SharedPrefObject? {
        return Gson().fromJson(
            getSharedPreferences()?.getString(key, null),
            SharedPrefObject::class.java
        )
    }

    fun setObject(key: String, value: SharedPrefObject) {
        this.getSharedPreferences()?.edit()?.putString(key, Gson().toJson(value))?.apply()
    }

    fun clear() {
        this.getSharedPreferences()?.edit()?.clear()?.apply()
    }
}
