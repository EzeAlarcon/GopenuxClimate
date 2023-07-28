package com.example.weatherapp

import android.app.Application
import android.content.Context
import android.util.Log

class MyApplication : Application() {

    companion object{


        lateinit var instance : MyApplication




    }

    override fun onCreate(){
        super.onCreate()

        instance = this



    }

    override fun onTerminate() {
        super.onTerminate()

        val sharedPrefs = SharedPrefs.getInstance(this)
        sharedPrefs.clearCityValue()
    }





}