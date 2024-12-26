package com.mobile_computing.halifaxexplore.newsfeed

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.mobile_computing.halifaxexplore.R

class NewsDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news_detail)

        // Retrieve the passed data
        val title = intent.getStringExtra("title")
        val imageUrl = intent.getStringExtra("imageUrl")
        val sourceName = intent.getStringExtra("sourceName")
        val description = intent.getStringExtra("description")
        var content = intent.getStringExtra("content")
        val publishedAt =
            intent.getStringExtra("publishedAt")?.substringBefore('T') // Formatting the date

        // Trim the content if it ends with "+[number] chars"
        content = content?.substringBefore(" [+")

        findViewById<TextView>(R.id.newsTitle).text = title
        findViewById<TextView>(R.id.newsSource).text = "Source: $sourceName"
        findViewById<TextView>(R.id.newsDescription).text = "Description: $description"
        findViewById<TextView>(R.id.newsContent).text = "Content: $content"
        findViewById<TextView>(R.id.newsPublishedAt).text = "Published: $publishedAt"

        // Load the image using Glide
        Glide.with(this).load(imageUrl).into(findViewById<ImageView>(R.id.newsImage))

        // Click listener to open the source link
        val sourceUrl = intent.getStringExtra("sourceUrl")
        findViewById<TextView>(R.id.newsUrl).apply {
            text = "For more, read here: $sourceUrl"
            setOnClickListener {
                // Open the URL in a web browser
                sourceUrl?.let { url ->
                    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    startActivity(browserIntent)
                }
            }
        }
    }
}

