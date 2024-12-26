package com.mobile_computing.halifaxexplore.event_calendar

import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException

class EventApiService {
    private val client = OkHttpClient()

    fun getEvents(categories: String, callback: (String?) -> Unit) {
        val baseUrl = "https://api.predicthq.com/v1/events"
        val url = "$baseUrl?location_around.origin=44.6488,-63.5752&category=$categories&limit=20"

        val request = Request.Builder()
            .url(url)
            .addHeader("Authorization", "Bearer 75qWbNod2B6Xt-r277JDFciPa-639fobmjs7nZmc")
            .addHeader("Accept", "application/json")
            .build()

        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: okhttp3.Call, e: IOException) {
                Log.e("EventApiService", "API Request Failed: ${e.message}")
                callback(null)
            }

            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                if (response.isSuccessful) {
                    val responseBody = response.body?.string()
                    Log.d("EventApiService", "API Response: $responseBody")
                    callback(responseBody)
                } else {
                    Log.e("EventApiService", "API Request Error: ${response.message}")
                    callback(null)
                }
            }
        })
    }
}
