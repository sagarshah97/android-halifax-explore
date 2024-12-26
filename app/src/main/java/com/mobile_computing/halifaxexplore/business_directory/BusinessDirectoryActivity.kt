package com.mobile_computing.halifaxexplore.business_directory

import com.mobile_computing.halifaxexplore.LoginActivity
import com.mobile_computing.halifaxexplore.R

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.mobile_computing.halifaxexplore.MainActivity
import com.mobile_computing.halifaxexplore.alerts.AlertsActivity
import com.mobile_computing.halifaxexplore.blogs.BlogsActivity
import com.mobile_computing.halifaxexplore.event_calendar.EventCalendarActivity
import com.mobile_computing.halifaxexplore.forums.ForumsActivity
import com.mobile_computing.halifaxexplore.marketplace.MarketplaceActivity
import com.mobile_computing.halifaxexplore.newsfeed.NewsFeedActivity
import com.mobile_computing.halifaxexplore.public_transport.PublicTransportActivity
import com.mobile_computing.halifaxexplore.service_finder.ServiceFinderActivity
import com.mobile_computing.halifaxexplore.traffic_updates.TrafficUpdatesActivity
import com.mobile_computing.halifaxexplore.user_management.UserManagementActivity
import com.mobile_computing.halifaxexplore.weather.WeatherActivity

import android.util.Log
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import android.widget.ProgressBar

class BusinessDirectoryActivity : AppCompatActivity() {
    private lateinit var progressBar: ProgressBar
    private val TAG = "MainActivity"
    private lateinit var toggle : ActionBarDrawerToggle
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView : NavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_business)

        getDataFromFirestore()
    }

    private fun getDataFromFirestore(){
        setContentView(R.layout.activity_business)
        progressBar = findViewById(R.id.progressBar)
        drawerLayout = findViewById(R.id.drawer_layout)
        navView = findViewById(R.id.nav_view)
        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        toggle.syncState()
        drawerLayout.addDrawerListener(toggle)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        navView.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.logout -> {
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    Toast.makeText(
                        this, // in which context the toast should show up
                        "Logged Out", //toast message content
                        Toast.LENGTH_SHORT //stay on screen for a short duration only
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
        progressBar.visibility = View.VISIBLE
        val db = Firebase.firestore

        db.collection("local_businesses")
            .get()
            .addOnSuccessListener { result ->
                progressBar.visibility = View.GONE
                val businesses = mutableListOf<LocalBusiness>()

                for (document in result) {
                    val business = document.toObject(LocalBusiness::class.java)
                    // Fetch reviews for each business
                    db.collection("local_businesses/${document.id}/reviews")
                        .get()
                        .addOnSuccessListener { reviewsResult ->
                            val reviews = mutableListOf<Review>()

                            for (reviewDocument in reviewsResult) {
                                val review = reviewDocument.toObject(Review::class.java)
                                reviews.add(review)
                            }

                            business.reviews = reviews
                            business.id = document.id
                            businesses.add(business)

                            val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
                            recyclerView.layoutManager = GridLayoutManager(this, 2)
                            recyclerView.adapter = BusinessAdapter(businesses) { selectedBusiness ->
                                // Handle item click, navigate to business detail page
                                val intent = Intent(this, BusinessDetailActivity::class.java).apply {
                                    putExtra(BusinessDetailActivity.EXTRA_BUSINESS_ID, selectedBusiness.id)
                                    putExtra(BusinessDetailActivity.EXTRA_BUSINESS_NAME, selectedBusiness.name)
                                    putExtra(BusinessDetailActivity.EXTRA_BUSINESS_ADDRESS, selectedBusiness.address)
                                    putExtra(BusinessDetailActivity.EXTRA_BUSINESS_RATING, selectedBusiness.rating.toFloat())
                                    putExtra(BusinessDetailActivity.EXTRA_BUSINESS_PHONE, selectedBusiness.phone)
                                    putExtra(BusinessDetailActivity.EXTRA_BUSINESS_DESCRIPTION, selectedBusiness.description)
                                    putExtra(BusinessDetailActivity.EXTRA_BUSINESS_EMAIL, selectedBusiness.email)
                                    putExtra(BusinessDetailActivity.EXTRA_BUSINESS_IMAGE, selectedBusiness.image)
                                    putExtra(BusinessDetailActivity.EXTRA_BUSINESS_REVIEWS, selectedBusiness.reviews.toTypedArray())
                                }
                                startActivity(intent)
                            }
                        }
                        .addOnFailureListener { exception ->
                            Log.w(TAG, "Error getting reviews for ${document.id}.", exception)
                            businesses.add(business)
                        }
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        const val RESULT_REFRESH = 1001
    }
}
