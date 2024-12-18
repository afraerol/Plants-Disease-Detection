package com.example.loginsqllite

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import android.view.View
import android.os.AsyncTask
import android.os.Build
import android.widget.ImageView
import org.json.JSONObject
import java.net.URL
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.Calendar
import java.util.Date
import java.util.Locale


class MainActivity : AppCompatActivity(), View.OnClickListener {

    val API: String = "93f66b29f987a88eb6579d4deae36c40"
    val CITY: String = "Istanbul"

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val viewPager: ViewPager = findViewById(R.id.viewPager)
        viewPager.adapter = MyPagerAdapter(supportFragmentManager)

        val tabLayout: TabLayout = findViewById(R.id.tabLayout)
        tabLayout.setupWithViewPager(viewPager)


        val service = CounterNotificationService(applicationContext)
        val notificationImageview: ImageView = findViewById(R.id.notification_imageView)
        notificationImageview.setOnClickListener {
            service.showNotification()

        }
        FetchWeatherTask().execute()

        val calendar = Calendar.getInstance()

        val current = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LocalDateTime.of(
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH),
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                calendar.get(Calendar.SECOND)
            )
        } else {
            TODO("VERSION.SDK_INT < O")
        }
        weatherTask().execute()
        if(6==calendar.get(Calendar.MONTH)){
            service.showNotification2()
        }
        /*if(temperature>40){
            service.showNotification
        }*/


    }

    inner class weatherTask() : AsyncTask<String, Void, String>() {
        override fun doInBackground(vararg params: String?): String {
            var wresponse: String?
            try {
                wresponse =
                    URL("https://api.openweathermap.org/data/2.5/weather?q=$CITY&units=metric&appid=$API").readText(
                        Charsets.UTF_8
                    )
            } catch (e: Exception) {
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

        val updatedAt: Long = jsonObj.getLong("dt")
        val updatedAtText =
            "Updated at: " + SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.ENGLISH).format(
                Date(updatedAt * 1000)
            )
        val temp = main.getInt("temp")
        val tempMin = "Min Temp: " + main.getString("temp_min") + "°C"
        val tempMax = "Max Temp: " + main.getString("temp_max") + "°C"
        val pressure = main.getString("pressure")
        val humidity = main.getString("humidity")



        val address = jsonObj.getString("name") + ", " + sys.getString("country")

        val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
        val currentDate = sdf.format(Date())

        val c = Calendar.getInstance()

        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val hour = c.get(Calendar.HOUR_OF_DAY)
        val minute = c.get(Calendar.MINUTE)




    }

    private class MyPagerAdapter(fm: androidx.fragment.app.FragmentManager) :
        FragmentPagerAdapter(fm) {
        override fun getItem(position: Int): androidx.fragment.app.Fragment {
            return when (position) {
                0 -> Homepage()
                1 -> MyPlantsFragment()
                else -> Homepage() // Default fragment
            }
        }

        override fun getCount(): Int {
            return 2 // Number of tabs
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return when (position) {
                0 -> "Homepage"
                1 -> "My Plants"
                else -> ""
            }
        }
    }

    override fun onClick(v: View?) {
        TODO("Not yet implemented")
    }
}


