package com.mobile_computing.halifaxexplore.alerts

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.app.NotificationCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.google.firebase.FirebaseApp
import com.mobile_computing.halifaxexplore.LoginActivity
import com.mobile_computing.halifaxexplore.MainActivity
import com.mobile_computing.halifaxexplore.R
import com.mobile_computing.halifaxexplore.blogs.BlogsActivity
import com.mobile_computing.halifaxexplore.business_directory.BusinessDirectoryActivity
import com.mobile_computing.halifaxexplore.event_calendar.EventCalendarActivity
import com.mobile_computing.halifaxexplore.forums.ForumsActivity
import com.mobile_computing.halifaxexplore.marketplace.MarketplaceActivity
import com.mobile_computing.halifaxexplore.newsfeed.NewsFeedActivity
import com.mobile_computing.halifaxexplore.public_transport.PublicTransportActivity
import com.mobile_computing.halifaxexplore.service_finder.ServiceFinderActivity
import com.mobile_computing.halifaxexplore.traffic_updates.TrafficUpdatesActivity
import com.mobile_computing.halifaxexplore.user_management.UserManagementActivity
import com.mobile_computing.halifaxexplore.weather.WeatherActivity
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class AlertsActivity : AppCompatActivity() {
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var drawerLayout2: DrawerLayout
    private lateinit var navView2: NavigationView
    private lateinit var scrollView2: ScrollView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alerts)

        // Fetch data from Node.js API and update UI
        FetchDataTask().execute()
        FirebaseApp.initializeApp(this)

        drawerLayout2 = findViewById(R.id.drawer_layout2)

        scrollView2 = findViewById(R.id.scrollView2)
        navView2 = findViewById(R.id.nav_view2)

        toggle = ActionBarDrawerToggle(this, drawerLayout2, R.string.open, R.string.close)
        toggle.syncState()
        drawerLayout2.addDrawerListener(toggle)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        navView2.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.logout -> {
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    Toast.makeText(
                        this,
                        "Logged Out",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                R.id.home -> {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }
                R.id.alerts -> {
                    val intent = Intent(this, AlertsActivity::class.java)
                    startActivity(intent)
                }
                R.id.blogs -> {
                    val intent = Intent(this, BlogsActivity::class.java)
                    startActivity(intent)
                }
                R.id.businesses -> {
                    val intent = Intent(this, BusinessDirectoryActivity::class.java)
                    startActivity(intent)
                }
                R.id.events -> {
                    val intent = Intent(this, EventCalendarActivity::class.java)
                    startActivity(intent)
                }
                R.id.forums -> {
                    val intent = Intent(this, ForumsActivity::class.java)
                    startActivity(intent)
                }
                R.id.marketplace -> {
                    val intent = Intent(this, MarketplaceActivity::class.java)
                    startActivity(intent)
                }
                R.id.newsfeed -> {
                    val intent = Intent(this, NewsFeedActivity::class.java)
                    startActivity(intent)
                }
                R.id.transport -> {
                    val intent = Intent(this, PublicTransportActivity::class.java)
                    startActivity(intent)
                }
                R.id.services -> {
                    val intent = Intent(this, ServiceFinderActivity::class.java)
                    startActivity(intent)
                }
                R.id.traffic -> {
                    val intent = Intent(this, TrafficUpdatesActivity::class.java)
                    startActivity(intent)
                }
                R.id.profile -> {
                    val intent = Intent(this, UserManagementActivity::class.java)
                    startActivity(intent)
                }
                R.id.weather -> {
                    val intent = Intent(this, WeatherActivity::class.java)
                    startActivity(intent)
                }
            }
            true
        }
    }

    private fun fetchDataAndPopulateUI(alertsData: String) {
        try {
            if (alertsData.isNotEmpty()) {
                val jsonArray = JSONArray(alertsData)

                for (i in 0 until jsonArray.length()) {
                    val jsonObject: JSONObject = jsonArray.getJSONObject(i)

                    // Extract data from JSON object
                    val alertTitle = jsonObject.getString("title")
                    val alertDescription = jsonObject.getString("description")

                    // Create a new CardView and add it to the ScrollView in your XML layout
                    val cardView = CardView(this)
                    val cardContent = TextView(this)
                    cardContent.text = "Title: $alertTitle\nDescription: $alertDescription"
                    cardView.addView(cardContent)

                    // Add the CardView to the ScrollView
                    scrollView2.addView(cardView)
                }
            } else {
                // Handle the case where the result is empty
                Log.e("AlertsActivity", "Empty result string")
            }
        } catch (e: JSONException) {
            // Handle JSON parsing exception
            e.printStackTrace()
        }
    }


    private inner class FetchDataTask : AsyncTask<Void, Void, String>() {
        override fun doInBackground(vararg params: Void?): String {
            try {
                val url = URL("https://mobileprojectalertsfeatureapi.onrender.com/api/alerts")
                val connection = url.openConnection() as HttpURLConnection

                try {
                    if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                        val reader = BufferedReader(InputStreamReader(connection.inputStream))
                        val result = StringBuilder()
                        var line: String?

                        while (reader.readLine().also { line = it } != null) {
                            result.append(line)
                        }

                        println(">>>> RESULTS:: ${result}")

                        Log.d("FetchDataTask", "Response: $result")  // Log the entire response

                        return result.toString()
                    } else {
                        Log.e("FetchDataTask", "HTTP error: ${connection.responseCode}")
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    connection.disconnect()
                }

                return ""
            } catch (e: Exception) {
                // Print the stack trace
                e.printStackTrace()
                return "" // Return an empty string or handle the error appropriately
            }
        }


        override fun onPostExecute(result: String?) {
            result?.let {
                fetchDataAndPopulateUI(it)
                handlePushNotification("New Alert", "A new alert has been added.")
            }
        }

    }

    private fun handlePushNotification(title: String, body: String) {
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "Channel_Id" // Unique channel id for your app

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Channel_Name",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setContentTitle(title)
            .setContentText(body)
            .setSmallIcon(R.drawable.ic_notification_icon) // Replace with your notification icon
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        notificationManager.notify(0, notificationBuilder.build())
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        // ... (handle onDestroy)
    }
}