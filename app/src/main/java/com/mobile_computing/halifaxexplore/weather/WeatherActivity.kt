package com.mobile_computing.halifaxexplore.weather

import com.mobile_computing.halifaxexplore.LoginActivity
import com.mobile_computing.halifaxexplore.R

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request
import kotlinx.coroutines.*
import java.io.IOException
import kotlin.math.roundToInt

class WeatherActivity : AppCompatActivity() {


    private lateinit var tvTemperature: TextView
    private lateinit var ivWeatherIcon: ImageView
    private lateinit var tvFeelsLikeTemperature: TextView
    private lateinit var tvPrecipitation: TextView
    private lateinit var tvWind: TextView
    private lateinit var tvHumidity: TextView
    private lateinit var tvVisibility: TextView
    private lateinit var tvUVIndex: TextView
    private lateinit var tvPressure: TextView
    private lateinit var tvWeatherCondition: TextView
    private lateinit var tvSunset: TextView
    private lateinit var tvSunrise: TextView
    private lateinit var tvDewPoint: TextView
    private lateinit var tvDayNight: TextView
    // Declare other TextViews

    private lateinit var dailyForecastRecyclerView: RecyclerView
    private lateinit var forecastAdapter: ForecastAdapter

    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather)

        tvTemperature = findViewById(R.id.tvTemperature)
        ivWeatherIcon = findViewById(R.id.ivWeatherIcon)
        tvFeelsLikeTemperature = findViewById(R.id.tvFeelsLikeTemperature)
        tvDayNight = findViewById(R.id.tvDayNight)
        tvPrecipitation = findViewById(R.id.tvPrecipitation)
        tvWind = findViewById(R.id.tvWind)
        tvHumidity = findViewById(R.id.tvHumidity)
        tvVisibility = findViewById(R.id.tvVisibility)
        tvUVIndex = findViewById(R.id.tvUVIndex)
        tvPressure = findViewById(R.id.tvPressure)
        tvWeatherCondition = findViewById(R.id.tvWeatherCondition)
        tvSunrise = findViewById(R.id.tvSunrise)
        tvSunset = findViewById(R.id.tvSunset)
        tvDewPoint = findViewById(R.id.tvDewPoint)

        dailyForecastRecyclerView = findViewById(R.id.rvDailyForecast)
        dailyForecastRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        forecastAdapter = ForecastAdapter(emptyList())
        dailyForecastRecyclerView.adapter = forecastAdapter

        fetchWeatherData()

        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout)
        swipeRefreshLayout.setOnRefreshListener {
            fetchWeatherData()
            swipeRefreshLayout.isRefreshing  = false
        }

    }

    private fun fetchWeatherData() {
        CoroutineScope(Dispatchers.IO).launch {
            val client = OkHttpClient()
            val request = Request.Builder()
                .url("https://api.weatherapi.com/v1/forecast.json?key=a7b3082ad2e942aa88941143232011&q=Halifax&days=5&aqi=yes&alerts=no")
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) throw IOException("Unexpected code $response")

                val responseBody = response.body?.string()
                print(responseBody)
                val weatherResponse = Gson().fromJson(responseBody, WeatherData::class.java)

                withContext(Dispatchers.Main) {
                    updateUI(weatherResponse)
                }
            }
        }
    }

    private fun updateUI(weatherResponse: WeatherData) {
        // Set the main temperature with a larger text size
        tvTemperature.text = "${weatherResponse.current.temp_c.roundToInt()}°"
        tvFeelsLikeTemperature.text = "Feels like ${weatherResponse.current.feelslike_c.roundToInt()}°"
        tvDayNight.text = "High ${weatherResponse.forecast.forecastday[0].day.maxtemp_c.roundToInt()}° • Low ${weatherResponse.forecast.forecastday[0].day.mintemp_c.roundToInt()}°"

        // Update additional weather details
        tvPrecipitation.text = "${weatherResponse.current.precip_mm} mm"
        tvWind.text = "${weatherResponse.current.wind_kph} km/h ${weatherResponse.current.wind_dir}"
        tvHumidity.text = "${weatherResponse.current.humidity}%"
        tvVisibility.text = "${weatherResponse.current.vis_km} km"
        tvUVIndex.text = "${weatherResponse.current.uv}"
        tvPressure.text = "${weatherResponse.current.pressure_mb} hPa"
        tvWeatherCondition.text = weatherResponse.current.condition.text
        tvSunrise.text = weatherResponse.forecast.forecastday[0].astro.sunrise
        tvSunset.text = weatherResponse.forecast.forecastday[0].astro.sunset

        tvDewPoint.text = "${weatherResponse.forecast.forecastday[0].hour[0].dewpoint_c.roundToInt()}°"

        val iconUrl = "https:${weatherResponse.current.condition.icon.replace("64x64","128x128")}" // Ensure the URL is correct
        Glide.with(this)
            .load(iconUrl)
            .into(ivWeatherIcon)

        forecastAdapter.updateData(weatherResponse.forecast.forecastday)

    }



}

