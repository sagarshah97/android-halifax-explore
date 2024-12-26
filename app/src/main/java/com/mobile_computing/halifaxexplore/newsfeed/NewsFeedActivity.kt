package com.mobile_computing.halifaxexplore.newsfeed

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
import com.mobile_computing.halifaxexplore.R
import com.mobile_computing.halifaxexplore.LoginActivity
import com.mobile_computing.halifaxexplore.MainActivity
import com.mobile_computing.halifaxexplore.alerts.AlertsActivity
import com.mobile_computing.halifaxexplore.blogs.BlogsActivity
import com.mobile_computing.halifaxexplore.business_directory.BusinessDirectoryActivity
import com.mobile_computing.halifaxexplore.event_calendar.EventCalendarActivity
import com.mobile_computing.halifaxexplore.forums.ForumsActivity
import com.mobile_computing.halifaxexplore.marketplace.MarketplaceActivity
import com.mobile_computing.halifaxexplore.public_transport.PublicTransportActivity
import com.mobile_computing.halifaxexplore.service_finder.ServiceFinderActivity
import com.mobile_computing.halifaxexplore.traffic_updates.TrafficUpdatesActivity
import com.mobile_computing.halifaxexplore.user_management.UserManagementActivity
import com.mobile_computing.halifaxexplore.weather.WeatherActivity
// ... other imports ...

class NewsFeedActivity : AppCompatActivity() {
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView
    private lateinit var recyclerView: RecyclerView
    private lateinit var newsAdapter: NewsAdapter
    private lateinit var categorySpinner: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Navigation Drawer Setup
        drawerLayout = findViewById(R.id.drawer_layout)
        navView = findViewById(R.id.nav_view)
        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setupDrawerNavigation()

        // Setup category spinner with custom layout
        categorySpinner = findViewById(R.id.categorySpinner)
        val categories = arrayOf("All", "Sports", "Entertainment", "Technology")
        val adapter = ArrayAdapter(this, R.layout.custom_spinner_item, categories)
        adapter.setDropDownViewResource(R.layout.custom_spinner_item)
        categorySpinner.adapter = adapter
        categorySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val selectedCategory = if (categories[position] == "All") "" else categories[position]
                fetchNews(selectedCategory)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Optional: Handle no selection
            }
        }

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        fetchNews("") // Initial fetch for default category
    }

    private fun setupDrawerNavigation() {
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

    private fun fetchNews(category: String) {
        val apiService = NewsApiService()
        apiService.getNews(category) { articles ->
            articles?.let {
                runOnUiThread {
                    newsAdapter = NewsAdapter(this@NewsFeedActivity, it)
                    recyclerView.adapter = newsAdapter
                }
            } ?: runOnUiThread {
                Toast.makeText(this, "Failed to fetch news", Toast.LENGTH_LONG).show()
            }
        }
    }
}
