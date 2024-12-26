package com.mobile_computing.halifaxexplore.newsfeed

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mobile_computing.halifaxexplore.R

class NewsAdapter(private val context: Context, private val articles: List<Article>) : RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    class NewsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titleTextView: TextView = view.findViewById(R.id.titleTextView)
        val imageView: ImageView = view.findViewById(R.id.imageView) // ImageView for news image
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.news_item, parent, false)
        return NewsViewHolder(view)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val article = articles[position]
        holder.titleTextView.text = article.title

        // Load image using Glide
        if (!article.urlToImage.isNullOrEmpty()) {
            Glide.with(context)
                .load(article.urlToImage)
                .into(holder.imageView)
        }

        holder.itemView.setOnClickListener {
            val intent = Intent(context, NewsDetailActivity::class.java).apply {
                putExtra("title", article.title)
                putExtra("description", article.description)
                putExtra("publishedAt", article.publishedAt)
                putExtra("content", article.content)
                putExtra("imageUrl", article.urlToImage)
                putExtra("sourceName", article.source.name) // Pass the source name
                putExtra("sourceUrl", article.url) // Pass the source URL if available
            }
            context.startActivity(intent)
        }
    }


    override fun getItemCount() = articles.size
}