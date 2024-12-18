package com.example.loginsqllite

import android.os.AsyncTask
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONObject
import java.net.URL
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class WeatherInfo : AppCompatActivity() {

    val API: String = "93f66b29f987a88eb6579d4deae36c40"
    val CITY: String = "Istanbul"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        weatherTask().execute()

    }

    inner class weatherTask(): AsyncTask<String, Void, String>(){
        override fun doInBackground(vararg params: String?): String {
            var wresponse:String?
            try{
                wresponse = URL("https://api.openweathermap.org/data/2.5/weather?q=$CITY&units=metric&appid=$API").readText(
                    Charsets.UTF_8
                )
            }catch (e: Exception){
                wresponse = null
            }
            return wresponse.toString()
            }
        }

    fun onPostExecute(result: String?) {
        //super.onPostExecute(result)

        /* Extracting JSON returns from the API */
        val jsonObj = JSONObject(result)
        val main = jsonObj.getJSONObject("main")
        val sys = jsonObj.getJSONObject("sys")
        val wind = jsonObj.getJSONObject("wind")
        val weather = jsonObj.getJSONArray("weather").getJSONObject(0)

        val updatedAt:Long = jsonObj.getLong("dt")
        val updatedAtText = "Updated at: "+ SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.ENGLISH).format(
            Date(updatedAt*1000)
        )
        val temp = main.getInt("temp")
        val tempMin = "Min Temp: " + main.getString("temp_min")+"°C"
        val tempMax = "Max Temp: " + main.getString("temp_max")+"°C"
        val pressure = main.getString("pressure")
        val humidity = main.getString("humidity")

        val sunrise:Long = sys.getLong("sunrise")
        val sunset:Long = sys.getLong("sunset")
        val windSpeed = wind.getString("speed")
        val weatherDescription = weather.getString("description")

        val address = jsonObj.getString("name")+", "+sys.getString("country")







    }
}




