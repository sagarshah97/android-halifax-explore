package com.mobile_computing.halifaxexplore.business_directory

import com.mobile_computing.halifaxexplore.R
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class BusinessAdapter(
    private val businesses: List<LocalBusiness>,
    private val onItemClick: (LocalBusiness) -> Unit
) : RecyclerView.Adapter<BusinessViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BusinessViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_business, parent, false)
        return BusinessViewHolder(view)
    }

    override fun onBindViewHolder(holder: BusinessViewHolder, position: Int) {
        val business = businesses[position]

        holder.businessName.text = business.name
        holder.businessAddress.text = business.address
        holder.ratingStars.rating = business.rating.toFloat()
        holder.openingHours.text = "${business.opening_hour} - ${business.closing_hour}"
        Picasso.get().load(business.image).into(holder.businessImage)

        holder.itemView.setOnClickListener { onItemClick(business) }
    }

    override fun getItemCount(): Int {
        return businesses.size
    }
}
