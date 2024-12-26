package com.mobile_computing.halifaxexplore.event_calendar

import EventResponse
import com.mobile_computing.halifaxexplore.LoginActivity
import com.mobile_computing.halifaxexplore.R

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
import com.google.gson.Gson

import com.mobile_computing.halifaxexplore.MainActivity
import com.mobile_computing.halifaxexplore.alerts.AlertsActivity
import com.mobile_computing.halifaxexplore.blogs.BlogsActivity
import com.mobile_computing.halifaxexplore.business_directory.BusinessDirectoryActivity
import com.mobile_computing.halifaxexplore.forums.ForumsActivity
import com.mobile_computing.halifaxexplore.marketplace.MarketplaceActivity
import com.mobile_computing.halifaxexplore.newsfeed.NewsFeedActivity
import com.mobile_computing.halifaxexplore.public_transport.PublicTransportActivity
import com.mobile_computing.halifaxexplore.service_finder.ServiceFinderActivity
import com.mobile_computing.halifaxexplore.traffic_updates.TrafficUpdatesActivity
import com.mobile_computing.halifaxexplore.user_management.UserManagementActivity
import com.mobile_computing.halifaxexplore.weather.WeatherActivity

class EventCalendarActivity : AppCompatActivity() {
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView
    private lateinit var recyclerView: RecyclerView
    private lateinit var categorySpinner: Spinner
    private val gson = Gson()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        drawerLayout = findViewById(R.id.drawer_layout)
        navView = findViewById(R.id.nav_view)
        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setupNavigationDrawer()

        categorySpinner = findViewById(R.id.categorySpinner)
        val categories = arrayOf("sports", "concerts","politics")
        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        categorySpinner.adapter = spinnerAdapter

        categorySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val selectedCategory = parent.getItemAtPosition(position).toString()
                fetchEvents(selectedCategory)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        fetchEvents(categories[0])
    }

    private fun fetchEvents(category: String) {
        val apiService = EventApiService()
        apiService.getEvents(category) { response ->
            response?.let {
                val eventResponse = gson.fromJson(it, EventResponse::class.java)
                runOnUiThread {
                    // Here we use EventAdapter instead of EventDetailActivity
                    recyclerView.adapter = EventAdapter(eventResponse.results ?: listOf(), this)
                }
            } ?: run {
                // Handle the case where response is null
            }
        }
    }



    private fun setupNavigationDrawer() {
        navView.setNavigationItemSelectedListener {
            // Navigation drawer item selection handling
            handleNavigationDrawerSelection(it.itemId)
            true
        }
    }

    private fun handleNavigationDrawerSelection(itemId: Int) {
        when (itemId) {
            R.id.logout -> startActivity(Intent(this, LoginActivity::class.java))
            R.id.home -> startActivity(Intent(this, MainActivity::class.java))
            R.id.newsfeed -> startActivity(Intent(this, NewsFeedActivity::class.java))
            R.id.blogs -> startActivity(Intent(this, BlogsActivity::class.java))
            R.id.businesses -> startActivity(Intent(this, BusinessDirectoryActivity::class.java))
            R.id.events -> startActivity(Intent(this, EventCalendarActivity::class.java))
            R.id.forums -> startActivity(Intent(this, ForumsActivity::class.java))
            R.id.marketplace -> startActivity(Intent(this, MarketplaceActivity::class.java))
            R.id.transport -> startActivity(Intent(this, PublicTransportActivity::class.java))
            R.id.services -> startActivity(Intent(this, ServiceFinderActivity::class.java))
            R.id.traffic -> startActivity(Intent(this, TrafficUpdatesActivity::class.java))
            R.id.profile -> startActivity(Intent(this, UserManagementActivity::class.java))
            R.id.weather -> startActivity(Intent(this, WeatherActivity::class.java))
        }
        Toast.makeText(this, "Navigation Item Selected", Toast.LENGTH_SHORT).show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}