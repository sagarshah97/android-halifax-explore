package com.mobile_computing.halifaxexplore.public_transport



import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Timestamp
import com.mobile_computing.halifaxexplore.R
import com.mobile_computing.halifaxexplore.public_transport.Incident
import java.text.SimpleDateFormat
import java.util.*

class IncidentListAdapter(private val incidentList: MutableList<Incident>) :
    RecyclerView.Adapter<IncidentListAdapter.IncidentViewHolder>() {

    inner class IncidentViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val iconImageView: ImageView = view.findViewById(R.id.iconImageView)
        private val titleTextView: TextView = view.findViewById(R.id.titleTextView)
        private val descriptionTextView: TextView = view.findViewById(R.id.descriptionTextView)
        private val timestampTextView: TextView = view.findViewById(R.id.timestampTextView)
        private val chevronImageView: ImageView = view.findViewById(R.id.chevronImageView)
        private val detailLayout: View = view.findViewById(R.id.detailLayout)

        init {
            // Toggle the visibility of the details when the item is clicked
            view.setOnClickListener {
                if (detailLayout.visibility == View.VISIBLE) {
                    detailLayout.visibility = View.GONE
                    chevronImageView.setImageResource(R.drawable.chevron_down)
                } else {
                    detailLayout.visibility = View.VISIBLE
                    chevronImageView.setImageResource(R.drawable.chevron_down)
                }
            }
        }

        fun bind(incident: Incident) {
            titleTextView.text = incident.title
            descriptionTextView.text = incident.description
            // Format the timestamp to a readable date
            timestampTextView.text = incident.timestamp?.toDate()?.let { date ->
                SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(date)
            } ?: "Unknown"
            // Set the icon for the incident item
            iconImageView.setImageResource(R.drawable.mark)
            // Initially, details are hidden
            detailLayout.visibility = View.GONE
            chevronImageView.setImageResource(R.drawable.chevron_down)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IncidentViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_incident, parent, false)
        return IncidentViewHolder(view)
    }

    override fun onBindViewHolder(holder: IncidentViewHolder, position: Int) {
        holder.bind(incidentList[position])
    }

    override fun getItemCount(): Int = incidentList.size
}
