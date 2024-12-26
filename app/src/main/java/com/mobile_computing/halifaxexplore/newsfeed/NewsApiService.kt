package com.mobile_computing.halifaxexplore.newsfeed

import java.io.IOException


import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request

class NewsApiService {
    private val client = OkHttpClient()
    private val gson = Gson()

    fun getNews(category: String, callback: (List<Article>?) -> Unit) {
        val baseUrl = "https://newsapi.org/v2/everything"
        val query = "q=Halifax+Canada+$category"
        val apiKey = "apiKey=165638a32907454c8a3cc0136a956e82"
        val url = "$baseUrl?$query&$apiKey"

        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: okhttp3.Call, e: IOException) {
                e.printStackTrace()
                callback(null)
            }

            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                if (!response.isSuccessful) {
                    callback(null)
                    return
                }

                response.body?.string()?.let { responseBody ->
                    val newsResponse = gson.fromJson(responseBody, NewsResponse::class.java)
                    callback(newsResponse.articles)
                }
            }
        })
    }
}
