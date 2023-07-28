package com.example.weatherapp

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.Utils.Companion.PERMISSION_REQUEST_CODE
import com.example.weatherapp.adapter.ForeCastAdapter
import com.example.weatherapp.adapter.WeatherToday
import com.example.weatherapp.databinding.TestlayoutBinding
import com.example.weatherapp.mvvm.WeatherVm
import com.example.weatherapp.service.RetrofitInstance

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {

    lateinit var viM: WeatherVm

    lateinit var adapter: WeatherToday

    private lateinit var binding: TestlayoutBinding


    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        binding = DataBindingUtil.setContentView(this, R.layout.testlayout)
        viM = ViewModelProvider(this).get(WeatherVm::class.java)



        viM.getWeather()


        adapter = WeatherToday()


        val sharedPrefs = SharedPrefs.getInstance(this@MainActivity)
        sharedPrefs.clearCityValue()




        viM.todayWeatherLiveData.observe(this, Observer {

            val setNewlist = it as List<WeatherList>


            adapter.setList(setNewlist)
            binding.forecastRecyclerView.adapter = adapter


        })


        binding.lifecycleOwner = this
        binding.vm = viM







        viM.closetorexactlysameweatherdata.observe(this, Observer {


            val temperatureFahrenheit = it!!.main?.temp
            val temperatureCelsius = (temperatureFahrenheit?.minus(273.15))
            val temperatureFormatted = String.format("%.2f", temperatureCelsius)


            for (i in it.weather) {


                binding.descMain.text = i.description


            }

            binding.tempMain.text = "$temperatureFormatted°"


            binding.humidityMain.text = it.main!!.humidity.toString()
            binding.windSpeed.text = it.wind?.speed.toString()


            val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
            val date = inputFormat.parse(it.dtTxt!!)
            val outputFormat = SimpleDateFormat("d MMMM EEEE", Locale.getDefault())
            val dateanddayname = outputFormat.format(date!!)

            binding.dateDayMain.text = dateanddayname

            binding.chanceofrain.text = "${it.pop.toString()}%"


            // setting the icon
            for (i in it.weather) {


                if (i.icon == "01d") {


                    binding.imageMain.setImageResource(R.drawable.oned)

                }

                if (i.icon == "01n") {
                    binding.imageMain.setImageResource(R.drawable.onen)


                }

                if (i.icon == "02d") {

                    binding.imageMain.setImageResource(R.drawable.twod)


                }


                if (i.icon == "02n") {
                    binding.imageMain.setImageResource(R.drawable.twon)


                }


                if (i.icon == "03d" || i.icon == "03n") {


                    binding.imageMain.setImageResource(R.drawable.threedn)


                }



                if (i.icon == "10d") {

                    binding.imageMain.setImageResource(R.drawable.tend)


                }


                if (i.icon == "10n") {

                    binding.imageMain.setImageResource(R.drawable.tenn)


                }


                if (i.icon == "04d" || i.icon == "04n") {


                    binding.imageMain.setImageResource(R.drawable.fourdn)


                }


                if (i.icon == "09d" || i.icon == "09n") {


                    binding.imageMain.setImageResource(R.drawable.ninedn)


                }



                if (i.icon == "11d" || i.icon == "11n") {


                    binding.imageMain.setImageResource(R.drawable.elevend)


                }


                if (i.icon == "13d" || i.icon == "13n") {

                    binding.imageMain.setImageResource(R.drawable.thirteend)


                }

                if (i.icon == "50d" || i.icon == "50n") {


                    binding.imageMain.setImageResource(R.drawable.fiftydn)


                }

            }


        })








        // Check for location permissions
        if (checkLocationPermissions()) {
            // Permissions are granted, proceed to get the current location
            getCurrentLocation()
        } else {
            // Request location permissions
            requestLocationPermissions()
        }


        val searchEditText = binding.searchView.findViewById<EditText>(androidx.appcompat.R.id.search_src_text)
        searchEditText.setTextColor(Color.WHITE)


        binding.next5Days.setOnClickListener {


            startActivity(Intent(this, ForeCastActivity::class.java))


        }


        binding.searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{


            override fun onQueryTextSubmit(query: String?): Boolean {



                val sharedPrefs = SharedPrefs.getInstance(this@MainActivity)
                sharedPrefs.setValueOrNull("city", query!!)


                if (!query.isNullOrEmpty()) {

                    viM.getWeather(query)




                    binding.searchView.setQuery("", false)
                    binding.searchView.clearFocus()
                    binding.searchView.isIconified = true
                }


                return true


            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true


            }


        })





    }


    private fun checkLocationPermissions(): Boolean {
        val fineLocationPermission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        val coarseLocationPermission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        return fineLocationPermission == PackageManager.PERMISSION_GRANTED &&
                coarseLocationPermission == PackageManager.PERMISSION_GRANTED
    }

    // Function to request location permissions
    private fun requestLocationPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ),
            Utils.PERMISSION_REQUEST_CODE
        )
    }

    // Handle the permission request result
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Utils.PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                grantResults[1] == PackageManager.PERMISSION_GRANTED
            ) {
                // Permissions granted, get the current location
                getCurrentLocation()
            } else {
                // Permissions denied, handle accordingly
                // For example, show an error message or disable location-based features
            }
        }
    }

    // Function to get the current location
    @RequiresApi(Build.VERSION_CODES.O)
    private fun getCurrentLocation() {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            val location: Location? =
                locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
            if (location != null) {
                val latitude = location.latitude
                val longitude = location.longitude
                // Use the latitude and longitude values as needed
                // ...

                val myprefs = SharedPrefs(this)
                myprefs.setValue("lon", longitude.toString())
                myprefs.setValue("lat", latitude.toString())


                // Example: Display latitude and longitude in logs





                Toast.makeText(this, "Latitude: $latitude, Longitude: $longitude", Toast.LENGTH_SHORT).show()


                Log.d("Current Location", "Latitude: $latitude, Longitude: $longitude")

                // Reverse geocode the location to get address information
                reverseGeocodeLocation(latitude, longitude)
            } else {
                // Location is null, handle accordingly
                // For example, request location updates or show an error message
            }
        } else {
            // Location permission not granted, handle accordingly
            // For example, show an error message or disable location-based features
        }
    }

    // Function to reverse geocode the location and get address information
    private fun reverseGeocodeLocation(latitude: Double, longitude: Double) {
        val geocoder = Geocoder(this, Locale.getDefault())
        val addresses = geocoder.getFromLocation(latitude, longitude, 1)
        if (addresses!!.isNotEmpty()) {
            val address = addresses[0]
            val addressLine = address.getAddressLine(0)
            // Use the addressLine as needed
            // ...
            // Example: Display address in logs
            Log.d("Current Address", addressLine)
        } else {
            // No address found, handle accordingly
            // For example, show an error message or use default address values
        }
    }




}




