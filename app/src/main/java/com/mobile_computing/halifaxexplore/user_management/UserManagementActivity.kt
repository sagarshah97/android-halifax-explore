package com.mobile_computing.halifaxexplore.user_management

import com.mobile_computing.halifaxexplore.LoginActivity
import com.mobile_computing.halifaxexplore.R

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.bumptech.glide.Glide
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.mobile_computing.halifaxexplore.MainActivity
import com.mobile_computing.halifaxexplore.alerts.AlertsActivity
import com.mobile_computing.halifaxexplore.blogs.BlogsActivity
import com.mobile_computing.halifaxexplore.business_directory.BusinessDirectoryActivity
import com.mobile_computing.halifaxexplore.event_calendar.EventCalendarActivity
import com.mobile_computing.halifaxexplore.forums.ForumsActivity
import com.mobile_computing.halifaxexplore.marketplace.MarketplaceActivity
import com.mobile_computing.halifaxexplore.newsfeed.NewsFeedActivity
import com.mobile_computing.halifaxexplore.public_transport.PublicTransportActivity
import com.mobile_computing.halifaxexplore.service_finder.ServiceFinderActivity
import com.mobile_computing.halifaxexplore.traffic_updates.TrafficUpdatesActivity
import com.mobile_computing.halifaxexplore.weather.WeatherActivity

class UserManagementActivity : AppCompatActivity() {
    private lateinit var toggle : ActionBarDrawerToggle
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView : NavigationView
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_management)

        auth = FirebaseAuth.getInstance()

        // Access the TextView by its ID
        //var myTextView = findViewById<TextView>(R.id.myTextView)

        // Retrieve the current user
        val user = auth.currentUser


        // Check if the user is authenticated
        if (user != null) {
            // Get user details
            val username = user.displayName

            // Update the TextView with user details
            //myTextView.text = "Welcome to Halifax Explore $username! Please choose one of the options from the navigation menu to make the most of the app!"
        }

        // Retrieve user details from the intent extras
        val username = intent.getStringExtra("name")
        val email = intent.getStringExtra("email")
        val profileURL = intent.getStringExtra("profileURL")
        println("**************************************************************"+profileURL)

        // Access the TextViews by their IDs
        val profileImageView = findViewById<de.hdodenhof.circleimageview.CircleImageView>(R.id.profileImageView)
        var usernameTextView = findViewById<TextView>(R.id.usernameTextView)
        var emailTextView = findViewById<TextView>(R.id.emailTextView)

        // Load the user's profile image using Glide
        Glide.with(this)
            .load(profileURL)
            .placeholder(R.drawable.default_profile_image) // You can set a placeholder image
            .error(R.drawable.default_profile_image) // You can set an error image
            .into(profileImageView)

        // Update the TextViews with user details
        usernameTextView.text = username
        emailTextView.text = email

        val backButton = findViewById<Button>(R.id.backButton)
        backButton.setOnClickListener {
            // Create an intent to navigate to MainActivity
            val intent = Intent(this, MainActivity::class.java)

            // Add any extra data you want to pass to MainActivity
            // intent.putExtra("key", "value")

            // Start the MainActivity
            startActivity(intent)
        }

        // Update the TextViews with user details
        usernameTextView.text = username
        emailTextView.text =  email

        drawerLayout = findViewById(R.id.drawer_layout)
        navView = findViewById(R.id.nav_view)

        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        toggle.syncState()
        drawerLayout.addDrawerListener(toggle)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
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
                    // Retrieve user details from Firebase authentication
                    val user = auth.currentUser
                    val username = user?.displayName
                    val email = user?.email
                    val phoneNumber = user?.phoneNumber
                    val profileURL = user?.photoUrl?.toString()

                    println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"+profileURL)

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
        auth = FirebaseAuth.getInstance()


    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}