// ServiceFinderActivity.kt
package com.mobile_computing.halifaxexplore.service_finder

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.mobile_computing.halifaxexplore.LoginActivity
import com.mobile_computing.halifaxexplore.MainActivity
import com.mobile_computing.halifaxexplore.R
import com.mobile_computing.halifaxexplore.alerts.AlertsActivity
import com.mobile_computing.halifaxexplore.blogs.BlogsActivity
import com.mobile_computing.halifaxexplore.business_directory.BusinessDirectoryActivity
import com.mobile_computing.halifaxexplore.databinding.ActivityServiceFinderBinding
import com.mobile_computing.halifaxexplore.event_calendar.EventCalendarActivity
import com.mobile_computing.halifaxexplore.forums.ForumsActivity
import com.mobile_computing.halifaxexplore.marketplace.MarketplaceActivity
import com.mobile_computing.halifaxexplore.newsfeed.NewsFeedActivity
import com.mobile_computing.halifaxexplore.public_transport.PublicTransportActivity
import com.mobile_computing.halifaxexplore.traffic_updates.TrafficUpdatesActivity
import com.mobile_computing.halifaxexplore.user_management.UserManagementActivity
import com.mobile_computing.halifaxexplore.weather.WeatherActivity

class ServiceFinderActivity : AppCompatActivity() {

    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView
    private lateinit var binding: ActivityServiceFinderBinding
    private lateinit var scrollView: ScrollView
    private lateinit var searchEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityServiceFinderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        scrollView = findViewById(R.id.servicesScrollView)
        searchEditText = findViewById(R.id.searchEditText)

        drawerLayout = findViewById(R.id.drawer_layout)
        navView = findViewById(R.id.nav_view)

        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        toggle.syncState()
        drawerLayout.addDrawerListener(toggle)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        navView.setNavigationItemSelectedListener {
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

        // Set up a listener for searchEditText
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) {
                // Not needed in this case
            }

            override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {
                // Not needed in this case
            }

            override fun afterTextChanged(editable: Editable?) {
                // Handle query text change
                filterServices(editable.toString())
            }
        })

        // Your logic for finding and displaying services
        findServices()
    }

    private fun findServices() {
        // Access the list of service providers from the ServiceDataProvider
        val services = ServiceDataProvider.allServiceProviders

        // Display all services initially
        displayServices(services)
    }

    private fun filterServices(query: String) {
        // Access the list of service providers from the ServiceDataProvider
        val allServices = ServiceDataProvider.allServiceProviders

        // Clear existing views in the ScrollView
        scrollView.removeAllViews()

        // Create a linear layout for the filtered services
        val linearLayout = LinearLayout(this)
        linearLayout.orientation = LinearLayout.VERTICAL

        // Add CardViews for each filtered service
        for (service in allServices) {
            // Check if any details of the service contain the query (case-insensitive)
            if (containsQuery(service, query)) {
                val cardView = createCardView(service)
                linearLayout.addView(cardView)
            }
        }

        // Add the linear layout to the ScrollView
        scrollView.addView(linearLayout)
    }

    private fun displayServices(services: List<ServiceProvider>) {
        // Clear existing views in the ScrollView
        scrollView.removeAllViews()

        // Create a linear layout for the services
        val linearLayout = LinearLayout(this)
        linearLayout.orientation = LinearLayout.VERTICAL

        // Add CardViews for each service
        for (service in services) {
            val cardView = createCardView(service)
            linearLayout.addView(cardView)
        }

        // Add the linear layout to the ScrollView
        scrollView.addView(linearLayout)
    }

    private fun containsQuery(service: ServiceProvider, query: String): Boolean {
        // Check if any details of the service contain the query (case-insensitive)
        return service.getDetailsAsString().toLowerCase().contains(query.toLowerCase())
    }

    private fun createCardView(service: ServiceProvider): CardView {
        val cardView = CardView(this)
        val cardLayoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        cardLayoutParams.setMargins(
            resources.getDimensionPixelSize(R.dimen.card_margin_start),
            resources.getDimensionPixelSize(R.dimen.card_margin_top),
            resources.getDimensionPixelSize(R.dimen.card_margin_end),
            resources.getDimensionPixelSize(R.dimen.card_margin_bottom)
        )
        cardView.layoutParams = cardLayoutParams
        cardView.radius = resources.getDimension(R.dimen.card_corner_radius)
        cardView.cardElevation = resources.getDimension(R.dimen.card_elevation)

        // Add a linear layout inside the CardView for better control over card content
        val linearLayout = LinearLayout(this)
        linearLayout.orientation = LinearLayout.VERTICAL
        linearLayout.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        // Add TextViews or other views for each service detail inside the CardView
        val nameTextView = TextView(this)
        nameTextView.text = "Name: ${service.name}"
        linearLayout.addView(nameTextView)

        val emailTextView = TextView(this)
        emailTextView.text = "Email: ${service.email}"
        emailTextView.setOnClickListener {
            sendEmail(service.email)
        }
        linearLayout.addView(emailTextView)

        val phoneTextView = TextView(this)
        phoneTextView.text = "Phone: ${service.phoneNumber}"
        phoneTextView.setOnClickListener {
            dialPhoneNumber(service.phoneNumber)
        }
        linearLayout.addView(phoneTextView)

        val locationTextView = TextView(this)
        locationTextView.text = "Location: ${service.location}"
        linearLayout.addView(locationTextView)

        val websiteTextView = TextView(this)
        websiteTextView.text = "Website: ${service.website}"
        websiteTextView.setOnClickListener {
            openWebsite(service.website)
        }
        linearLayout.addView(websiteTextView)

        // Set the content of the CardView to the linear layout
        cardView.addView(linearLayout)

        return cardView
    }

    private fun dialPhoneNumber(phoneNumber: String) {
        val dialIntent = Intent(Intent.ACTION_DIAL)
        dialIntent.data = Uri.parse("tel:$phoneNumber")
        startActivity(dialIntent)
    }

    private fun sendEmail(email: String) {
        val emailIntent = Intent(Intent.ACTION_SENDTO)
        emailIntent.data = Uri.parse("mailto:$email")
        startActivity(emailIntent)
    }

    private fun openWebsite(website: String) {
        val websiteIntent = Intent(Intent.ACTION_VIEW)
        websiteIntent.data = Uri.parse(website)
        startActivity(websiteIntent)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
