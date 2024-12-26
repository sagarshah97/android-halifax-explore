package com.mobile_computing.halifaxexplore.marketplace

import com.mobile_computing.halifaxexplore.LoginActivity
import com.mobile_computing.halifaxexplore.R

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.navigation.NavigationView
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import com.mobile_computing.halifaxexplore.MainActivity
import com.mobile_computing.halifaxexplore.alerts.AlertsActivity
import com.mobile_computing.halifaxexplore.blogs.BlogsActivity
import com.mobile_computing.halifaxexplore.business_directory.BusinessDirectoryActivity
import com.mobile_computing.halifaxexplore.event_calendar.EventCalendarActivity
import com.mobile_computing.halifaxexplore.forums.ForumsActivity
import com.mobile_computing.halifaxexplore.newsfeed.NewsFeedActivity
import com.mobile_computing.halifaxexplore.public_transport.PublicTransportActivity
import com.mobile_computing.halifaxexplore.service_finder.ServiceFinderActivity
import com.mobile_computing.halifaxexplore.traffic_updates.TrafficUpdatesActivity
import com.mobile_computing.halifaxexplore.user_management.UserManagementActivity
import com.mobile_computing.halifaxexplore.weather.WeatherActivity

class MarketplaceActivity : AppCompatActivity() {
    private lateinit var toggle : ActionBarDrawerToggle
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView : NavigationView
    private lateinit var auth: FirebaseAuth
    private lateinit var recyclerView: RecyclerView
    private lateinit var marketplaceAdapter: MarketplaceAdapter
    private val marketplaceItems: MutableList<MarketplaceItem> = mutableListOf()
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_marketplace)

        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser
        drawerLayout = findViewById(R.id.drawer_layout)
        navView = findViewById(R.id.nav_view)
        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        toggle.syncState()
        drawerLayout.addDrawerListener(toggle)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        recyclerView = findViewById(R.id.recyclerViewMarketplace)
        recyclerView.layoutManager = LinearLayoutManager(this)
        marketplaceAdapter = MarketplaceAdapter(marketplaceItems)
        recyclerView.adapter = marketplaceAdapter

        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout)
        swipeRefreshLayout.setOnRefreshListener {
            // Perform the refresh action here (e.g., fetch new data)
            fetchMarketplaceData()
            // Stop the refresh animation when the data is updated
            swipeRefreshLayout.isRefreshing = false
        }

        fetchMarketplaceData()

        val postItemButton: Button = findViewById(R.id.postItemButton)
        postItemButton.setOnClickListener {

            val intent = Intent(this, PostItemActivity::class.java)
            if (user != null) {
                intent.putExtra("loggedInUser",user.email)
            }

            startActivity(intent)
        }

        navView.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.logout -> {
                    // Log out from Firebase
                    auth.signOut()

                    // Log out from Google Sign-In
                    val googleSignInClient = GoogleSignIn.getClient(this, GoogleSignInOptions.DEFAULT_SIGN_IN)
                    googleSignInClient.revokeAccess().addOnCompleteListener {
                        // Navigate to the login screen after successfully logging out
                        val intent = Intent(this, LoginActivity::class.java)
                        startActivity(intent)

                        // Show a toast indicating successful logout
                        Toast.makeText(
                            this,
                            "Logged Out",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
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
                    // Retrieve user details from the Firebase authentication
                    val user = auth.currentUser
                    val username = user?.displayName
                    val email = user?.email
                    val phoneNumber = user?.phoneNumber
                    val profileURL = user?.photoUrl

                    // Create an intent to refresh UserManagementActivity with updated details
                    val intent = Intent(this, UserManagementActivity::class.java)
                    intent.putExtra("name", username)
                    intent.putExtra("email", email)
                    intent.putExtra("phoneNumber", phoneNumber)
                    intent.putExtra("profileURL", profileURL)

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
    private fun fetchMarketplaceData() {

        marketplaceItems.clear()
        val db = Firebase.firestore
        val marketplaceCollection = db.collection("halifax-explore-marketplace")

        // Fetch items from the "marketplace" collection
        marketplaceCollection.get().addOnSuccessListener { documents ->
            for (document in documents) {
                val imageUrl = document.getString("imageUrl") ?: ""
                val itemName = document.getString("itemName") ?: ""
                val itemDescription = document.getString("itemDescription") ?: ""
                val itemPrice: Double? = document.getDouble("price")

                val sellerEmail = document.getString("sellerEmail") ?: ""

                val marketplaceItem = MarketplaceItem(
                    imageUrl,
                    itemName,
                    itemDescription,
                    itemPrice,
                    sellerEmail
                )

                marketplaceItems.add(marketplaceItem)
            }

            // Notify the adapter that the data has changed
            marketplaceAdapter.notifyDataSetChanged()

        }.addOnFailureListener { exception ->
            // Handle errors
            exception.printStackTrace()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
