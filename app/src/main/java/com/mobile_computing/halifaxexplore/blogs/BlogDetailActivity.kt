package com.mobile_computing.halifaxexplore.blogs

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mobile_computing.halifaxexplore.databinding.ActivityBlogDetailBinding
import com.squareup.picasso.Picasso

class BlogDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBlogDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBlogDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Retrieve data passed from the previous activity
        val product = intent.getSerializableExtra(EXTRA_BLOG) as Blog?

        // Populate the product details
        product?.let {
            binding.textViewBlogTitle.text = it.title
            binding.textViewBlogDetails.text = it.detail
            binding.textViewBlogCreator.text = "By ${it.author}"
            binding.textViewBlogPublished.text = "On ${it.date_published}"
            binding.textViewBlogSource.text = "Source: The Coast Halifax"

            // Set the URL text and attach a click listener
            binding.textViewBlogUrl.apply {
                text = "Read more"
                setOnClickListener { view ->
                    val product = intent.getSerializableExtra(EXTRA_BLOG) as Blog?
                    product?.let {
                        openUrl(it.url)
                    }
                }
            }

            // Load the image using Picasso
            Picasso.get().load(it.image).into(binding.imageViewProductDetail)
        }
    }

    private fun openUrl(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }

    companion object {
        const val EXTRA_BLOG = "extra_blog"
    }
}
