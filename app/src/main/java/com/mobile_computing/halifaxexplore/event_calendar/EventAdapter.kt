package com.mobile_computing.halifaxexplore.event_calendar

import Event
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mobile_computing.halifaxexplore.R

class EventAdapter(private val events: List<Event>, private val context: Context) : RecyclerView.Adapter<EventAdapter.EventViewHolder>() {

    class EventViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titleTextView: TextView = view.findViewById(R.id.titleTextView)
        val headerNumberTextView: TextView = view.findViewById(R.id.headerNumberTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.event_item, parent, false)
        return EventViewHolder(view)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = events[position]
        holder.titleTextView.text = event.title
        holder.headerNumberTextView.text = "${position + 1}."

        holder.itemView.setOnClickListener {
            val intent = Intent(context, EventDetailActivity::class.java)
            intent.putExtra("title", event.title)
            intent.putExtra("description", event.description)
            intent.putExtra("category", event.category)
            val venueName = event.entities.firstOrNull()?.name
            intent.putExtra("venue", venueName)

            context.startActivity(intent)
        }
    }

    override fun getItemCount() = events.size
}
