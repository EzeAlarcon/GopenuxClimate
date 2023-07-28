package com.example.weatherapp

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences

class SharedPrefs internal constructor(private val context: Context) {

    companion object {
        private const val SHARED_PREFS_NAME = "my_prefs"
        private const val KEY_CITY = "city"

        @SuppressLint("StaticFieldLeak")
        private var instance: SharedPrefs? = null

        fun getInstance(context: Context): SharedPrefs {
            if (instance == null) {
                instance = SharedPrefs(context.applicationContext)
            }
            return instance!!
        }
    }

    private val prefs: SharedPreferences by lazy {
        context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun setValue(key: String, value: String) {
        prefs.edit().putString(key, value).apply()
    }

    fun getValue(key: String): String? {
        return prefs.getString(key, null)
    }

    fun setValueOrNull(key: String?, value: String?) {
        if (key != null && value != null) {
            prefs.edit().putString(key, value).apply()
        }
    }

    fun getValueOrNull(key: String?): String? {
        if (key != null) {
            return prefs.getString(key, null)
        }
        return null
    }

    fun clearCityValue() {
        prefs.edit().remove(KEY_CITY).apply()
    }
}