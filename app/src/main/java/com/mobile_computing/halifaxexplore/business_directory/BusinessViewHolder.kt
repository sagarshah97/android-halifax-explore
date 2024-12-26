package com.mobile_computing.halifaxexplore.business_directory

import android.view.View
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mobile_computing.halifaxexplore.R

class BusinessViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val businessImage: ImageView = itemView.findViewById(R.id.businessImage)
    val businessName: TextView = itemView.findViewById(R.id.businessName)
    val openingHours: TextView = itemView.findViewById(R.id.openingHours)
    val ratingStars: RatingBar = itemView.findViewById(R.id.ratingStars)
    val businessAddress: TextView = itemView.findViewById(R.id.businessAddress)
}
