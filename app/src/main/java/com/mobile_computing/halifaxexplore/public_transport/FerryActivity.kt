package com.mobile_computing.halifaxexplore.public_transport

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.mobile_computing.halifaxexplore.R

class FerryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ferry)

        val cardHalifaxAlderney = findViewById<CardView>(R.id.cardHalifaxAlderney)
        val halifaxAlderneyHeader = findViewById<LinearLayout>(R.id.halifaxAlderneyHeader)
        val halifaxAlderneyDetails = findViewById<LinearLayout>(R.id.halifaxAlderneyDetails)
        val chevronAlderney = findViewById<ImageView>(R.id.chevronAlderney)

        val cardHalifaxWoodside = findViewById<CardView>(R.id.cardHalifaxWoodside)
        val halifaxWoodsideHeader = findViewById<LinearLayout>(R.id.halifaxWoodsideHeader)
        val halifaxWoodsideDetails = findViewById<LinearLayout>(R.id.halifaxWoodsideDetails)
        val chevronWoodside = findViewById<ImageView>(R.id.chevronWoodside)


        halifaxAlderneyHeader.setOnClickListener {
            toggleDetailsVisibility(halifaxAlderneyDetails, chevronAlderney)
        }

        halifaxWoodsideHeader.setOnClickListener {
            toggleDetailsVisibility(halifaxWoodsideDetails, chevronWoodside)
        }
        // Repeat the setup for the Halifax - Woodside card

    }

    private fun toggleDetailsVisibility(details: LinearLayout, chevron: ImageView) {
        val isVisible = details.visibility == View.VISIBLE
        details.visibility = if (isVisible) View.GONE else View.VISIBLE
        chevron.animate().rotation(if (isVisible) 0f else 180f).setDuration(200).start()
    }
}
