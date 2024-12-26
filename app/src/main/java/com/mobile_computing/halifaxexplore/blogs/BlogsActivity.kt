package com.mobile_computing.halifaxexplore.blogs

import com.mobile_computing.halifaxexplore.LoginActivity
import com.mobile_computing.halifaxexplore.R

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.mobile_computing.halifaxexplore.MainActivity
import com.mobile_computing.halifaxexplore.alerts.AlertsActivity
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

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.RequestBody
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import com.mobile_computing.halifaxexplore.databinding.ActivityBlogBinding
import com.mobile_computing.halifaxexplore.databinding.ItemBlogBinding
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import androidx.lifecycle.lifecycleScope

class BlogsActivity : AppCompatActivity(), CoroutineScope by MainScope() {

    private lateinit var binding: ActivityBlogBinding
    private lateinit var productAdapter: ProductAdapter
    private lateinit var spinner: Spinner

    private lateinit var toggle : ActionBarDrawerToggle
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView : NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBlogBinding.inflate(layoutInflater)
        setContentView(binding.root)
        drawerLayout = findViewById(R.id.drawer_layout)
        navView = findViewById(R.id.nav_view)
        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        toggle.syncState()
        drawerLayout.addDrawerListener(toggle)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
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

        // Initialize the Spinner
        spinner = findViewById(R.id.spinnerCategories)
        val categories = listOf(
            "Food and Drinks",
            "Arts and Culture",
            "City",
            "Opinion and Letters",
            "Music",
            "Education",
            "Cultural Festivals",
            "General"
        )
        val categoryIds =
            listOf("957796", "25939607", "4435354", "25873723", "3196454", "4435383", "4427120", "")
        setupSpinner(categories, categoryIds)

        binding.recyclerViewProducts.layoutManager = LinearLayoutManager(this)
        productAdapter = ProductAdapter()
        binding.recyclerViewProducts.adapter = productAdapter

        // Make the API call in a background thread
        launch {
            val result = withContext(Dispatchers.IO) {
                fetchData(categoryIds[0])
            }
            productAdapter.setProducts(result)
        }

        binding.buttonRefresh.setOnClickListener {
            spinner.setSelection(0)
            // Refresh the data if needed
            launch {
                val result = withContext(Dispatchers.IO) {
                    fetchData(categoryIds[0])
                }
                productAdapter.setProducts(result)
            }
        }
    }

    private fun setupSpinner(categories: List<String>, categoryIds: List<String>) {
        val adapter = ArrayAdapter(this, R.layout.spinner_item_layout, categories)
        adapter.setDropDownViewResource(R.layout.spinner_item_layout)
        spinner.adapter = adapter

        // Set a default selection (e.g., the first category)
        spinner.setSelection(0)

        // Handle spinner item selection
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>?,
                selectedItemView: View?,
                position: Int,
                id: Long
            ) {
                // Handle the selected category
                val selectedCategoryId = categoryIds[position]

                lifecycleScope.launch {
                    val result = withContext(Dispatchers.IO) {
                        fetchData(selectedCategoryId)
                    }
                    productAdapter.setProducts(result)
                }
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
                // Do nothing
            }
        }

    }

    private suspend fun fetchData(code: String): List<Blog> = withContext(Dispatchers.IO) {

        val client = OkHttpClient()
        val requestBody = RequestBody.create(
            "application/json".toMediaTypeOrNull(),
            "{\"filter_code\":\"$code\"}"
        )
        val request = Request.Builder()
            .url("https://qtxz7rjvme.execute-api.us-east-1.amazonaws.com/dev/get-blog-details")
            .post(requestBody)
            .build()

        try {
            val response: Response = client.newCall(request).execute()
            val jsonString = response.body?.string() ?: ""
            val blogResponse = Gson().fromJson(jsonString, BlogResponse::class.java)

            blogResponse.blogs
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    private inner class ProductAdapter : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {
        private var blogs: List<Blog> = emptyList()

        fun setProducts(blogs: List<Blog>) {
            this.blogs = blogs
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
            val binding =
                ItemBlogBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return ProductViewHolder(binding)
        }

        override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
            val blog = blogs[position]

            Picasso.get().load(blog.image).into(holder.binding.imageViewThumbnail)

            holder.binding.textViewTitle.text = blog.title
            holder.binding.textViewDescription.text = blog.author
        }

        override fun getItemCount(): Int = blogs.size

        inner class ProductViewHolder(val binding: ItemBlogBinding) :
            RecyclerView.ViewHolder(binding.root),
            View.OnClickListener {

            init {
                itemView.setOnClickListener(this)
            }

            override fun onClick(view: View) {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val blog = blogs[position]

                    // Create an intent to launch the ProductDetailActivity
                    val intent = Intent(view.context, BlogDetailActivity::class.java).apply {
                        putExtra(BlogDetailActivity.EXTRA_BLOG, blog)
                    }

                    // Start the ProductDetailActivity
                    view.context.startActivity(intent)
                }
            }
        }
    }

    override fun onDestroy() {
        cancel()
        super.onDestroy()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
