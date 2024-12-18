package com.example.loginsqllite

import android.os.AsyncTask
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

class FetchWeatherTask : AsyncTask<Void, Void, String>() {
    override fun doInBackground(vararg params: Void?): String? {
        val urlString = "https://api.open-meteo.com/v1/forecast?latitude=52.52&longitude=13.41&hourly=temperature_2m,relative_humidity_2m"
        var response: String? = null

        try {
            val url = URL(urlString)
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"

            if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                response = connection.inputStream.bufferedReader().use { it.readText() }
            }

            connection.disconnect()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return response
    }

    override fun onPostExecute(result: String?) {
        super.onPostExecute(result)
        result?.let {
            parseWeatherData(it)
        }
    }

    private fun parseWeatherData(jsonString: String) {
        val jsonObject = JSONObject(jsonString)
        val hourly = jsonObject.getJSONObject("hourly")
        val temperatures = hourly.getJSONArray("temperature_2m")

        // Now you have the temperatures array, you can process it as needed
        for (i in 0 until temperatures.length()) {
            val temperature = temperatures.getDouble(i)
            // Do something with the temperature
            println("Temperature at hour $i: $temperature")
        }
    }
}
