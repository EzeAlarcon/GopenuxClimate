package com.example.weatherapp

import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.adapter.ForeCastAdapter
import com.example.weatherapp.mvvm.WeatherVm

class ForeCastActivity : AppCompatActivity() {


    private lateinit var adapterForeCastAdapter: ForeCastAdapter
    lateinit var viM : WeatherVm
    lateinit var rvForeCast: RecyclerView


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fourdayforecast)


        viM = ViewModelProvider(this).get(WeatherVm::class.java)


        adapterForeCastAdapter = ForeCastAdapter()

        rvForeCast = findViewById<RecyclerView>(R.id.rvForeCast)


        val sharedPrefs = SharedPrefs.getInstance(this)
        val city = sharedPrefs.getValueOrNull("city")


        Log.d("Prefs", city.toString())



        if (city!=null){


            viM.getForecastUpcoming(city)

        } else {

            viM.getForecastUpcoming()


        }




        viM.forecastWeatherLiveData.observe(this, Observer {

            val setNewlist = it as List<WeatherList>



            Log.d("Forecast LiveData", setNewlist.toString())



            adapterForeCastAdapter.setList(setNewlist)


            rvForeCast.adapter = adapterForeCastAdapter



        })





    }






}