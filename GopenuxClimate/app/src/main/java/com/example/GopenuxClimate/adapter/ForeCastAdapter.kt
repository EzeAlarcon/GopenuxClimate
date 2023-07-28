package com.example.weatherapp.adapter

import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.weatherapp.R

import com.example.weatherapp.WeatherList
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class ForeCastAdapter : RecyclerView.Adapter<ForeCastHolder>() {


    private var listofforecast = listOf<WeatherList>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForeCastHolder {


        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.fourdaylistitem, parent, false)
        return ForeCastHolder(view)




    }

    override fun getItemCount(): Int {

        return listofforecast.size




    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ForeCastHolder, position: Int) {
        val forecastObject = listofforecast[position]



        for (i in forecastObject.weather){

            holder.description.text = i.description!!



        }





        holder.humiditiy.text = forecastObject.main!!.humidity.toString()
        holder.windspeed.text = forecastObject.wind?.speed.toString()


        val temperatureFahrenheit = forecastObject.main?.temp
        val temperatureCelsius = (temperatureFahrenheit?.minus(273.15))
        val temperatureFormatted = String.format("%.2f", temperatureCelsius)


        holder.temp.text = "$temperatureFormatted °C"


        val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        val date = inputFormat.parse(forecastObject.dtTxt!!)
        val outputFormat = SimpleDateFormat("d MMMM EEEE", Locale.getDefault())
         val dateanddayname = outputFormat.format(date!!)

        holder.dateDayName.text = dateanddayname





        for (i in forecastObject.weather) {


            if (i.icon == "01d") {


                holder.imageGraphic.setImageResource(R.drawable.oned)
                holder.smallIcon.setImageResource(R.drawable.oned)
            }

            if (i.icon == "01n") {
                holder.imageGraphic.setImageResource(R.drawable.onen)
                holder.smallIcon.setImageResource(R.drawable.onen)


            }

            if (i.icon == "02d") {

                holder.imageGraphic.setImageResource(R.drawable.twod)
                holder.smallIcon.setImageResource(R.drawable.twod)


            }


            if (i.icon == "02n") {

                holder.imageGraphic.setImageResource(R.drawable.twon)
                holder.smallIcon.setImageResource(R.drawable.twon)


            }


            if (i.icon == "03d" || i.icon == "03n") {

                holder.imageGraphic.setImageResource(R.drawable.threedn)
                holder.smallIcon.setImageResource(R.drawable.threedn)


            }



            if (i.icon == "10d") {

                holder.imageGraphic.setImageResource(R.drawable.tend)
                holder.smallIcon.setImageResource(R.drawable.tend)


            }


            if (i.icon == "10n") {

                holder.imageGraphic.setImageResource(R.drawable.tenn)
                holder.smallIcon.setImageResource(R.drawable.tenn)


            }


            if (i.icon == "04d" || i.icon == "04n") {

                holder.imageGraphic.setImageResource(R.drawable.fourdn)
                holder.smallIcon.setImageResource(R.drawable.fourdn)



            }


            if (i.icon == "09d" || i.icon == "09n") {

                holder.imageGraphic.setImageResource(R.drawable.ninedn)
                holder.smallIcon.setImageResource(R.drawable.ninedn)



            }


            if (i.icon == "11d" || i.icon == "11n") {


                holder.imageGraphic.setImageResource(R.drawable.elevend)
                holder.smallIcon.setImageResource(R.drawable.elevend)



            }


            if (i.icon == "13d" || i.icon == "13n") {

                holder.imageGraphic.setImageResource(R.drawable.thirteend)
                holder.smallIcon.setImageResource(R.drawable.thirteend)


            }

            if (i.icon == "50d" || i.icon == "50n") {


                holder.imageGraphic.setImageResource(R.drawable.fiftydn)
                holder.smallIcon.setImageResource(R.drawable.fiftydn)


            }

        }

    }





    fun setList(newlist: List<WeatherList>) {

        this.listofforecast = newlist

    }


}

class ForeCastHolder(itemView: View) : ViewHolder(itemView){

    val imageGraphic: ImageView = itemView.findViewById(R.id.imageGraphic)
    val description : TextView = itemView.findViewById(R.id.weatherDescr)
    val humiditiy : TextView = itemView.findViewById(R.id.humidity)
    val windspeed : TextView = itemView.findViewById(R.id.windSpeed)

    val temp : TextView = itemView.findViewById(R.id.tempDisplayForeCast)
    val smallIcon : ImageView = itemView.findViewById(R.id.smallIcon)


    val dateDayName : TextView = itemView.findViewById(R.id.dayDateText)





}