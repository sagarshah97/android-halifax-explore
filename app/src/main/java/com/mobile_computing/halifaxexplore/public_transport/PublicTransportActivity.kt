package com.mobile_computing.halifaxexplore.public_transport

import android.content.Intent
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.mobile_computing.halifaxexplore.R

class PublicTransportActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_public_transport)

        val busButton = findViewById<FrameLayout>(R.id.busCard)
        busButton.setOnClickListener {
            // Launch Bus Activity
            // Replace BusActivity::class.java with the actual bus activity
            startActivity(Intent(this, BusActivity::class.java))
        }

        val ferryButton = findViewById<FrameLayout>(R.id.ferryCard)
        ferryButton.setOnClickListener {
            // Launch Ferry Activity
            // Replace FerryActivity::class.java with the actual ferry activity
            startActivity(Intent(this, FerryActivity::class.java))
        }
        val icidentButton = findViewById<FrameLayout>(R.id.incidentCard)
        icidentButton.setOnClickListener {
            // Launch Ferry Activity
            // Replace FerryActivity::class.java with the actual ferry activity
            startActivity(Intent(this, IncidentListActivity::class.java))
        }

    }
}
