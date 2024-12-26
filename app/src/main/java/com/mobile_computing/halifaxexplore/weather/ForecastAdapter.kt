package com.mobile_computing.halifaxexplore.weather

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mobile_computing.halifaxexplore.R
import kotlin.math.roundToInt
import java.text.SimpleDateFormat
import java.util.*

class ForecastAdapter(private var forecastList: List<ForecastDay>) :
    RecyclerView.Adapter<ForecastAdapter.ForecastViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecastViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_forecast_card, parent, false)
        return ForecastViewHolder(view)
    }

    override fun onBindViewHolder(holder: ForecastViewHolder, position: Int) {
        holder.bind(forecastList[position], position)
    }

    override fun getItemCount(): Int = forecastList.size

    fun updateData(newData: List<ForecastDay>) {
        forecastList = newData
        notifyDataSetChanged()
    }

    class ForecastViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvDay: TextView = itemView.findViewById(R.id.tvDay)
        private val tvMaxTemperature: TextView = itemView.findViewById(R.id.tvMaxTemperature)
        private val tvMinTemperature: TextView = itemView.findViewById(R.id.tvMinTemperature)
        private val ivWeatherIcon: ImageView = itemView.findViewById(R.id.ivWeatherIcon)

        fun bind(forecast: ForecastDay, position: Int) {
//            val context = itemView.context

            if (position == 0) {
                tvDay.text = "Today"
                itemView.background = null
            } else {
                // Format the date
                val originalFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val targetFormat = SimpleDateFormat("E dd", Locale.getDefault())
                val date = originalFormat.parse(forecast.date)
                date?.let {
                    tvDay.text = targetFormat.format(date)
                }
                itemView.setBackgroundResource(R.drawable.forecast_border_left)
            }
            tvMaxTemperature.text = "${forecast.day.maxtemp_c.roundToInt()}°"
            tvMinTemperature.text = "${forecast.day.mintemp_c.roundToInt()}°"
            Glide.with(itemView.context)
                .load("https://${forecast.day.condition.icon}")
                .into(ivWeatherIcon)
        }
    }
}
