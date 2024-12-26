package com.mobile_computing.halifaxexplore.event_calendar

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import android.widget.Button
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import com.mobile_computing.halifaxexplore.R

class EventDetailActivity : AppCompatActivity() {

    private lateinit var title: String
    private lateinit var description: String
    private lateinit var category: String
    private lateinit var venue: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_detail)

        // Retrieve data passed through the intent
        title = intent.getStringExtra("title") ?: ""
        description = intent.getStringExtra("description") ?: ""
        category = intent.getStringExtra("category") ?: ""
        venue = intent.getStringExtra("venue") ?: ""

        findViewById<TextView>(R.id.eventTitle).text = title
        findViewById<TextView>(R.id.eventDescription).text = description
        findViewById<TextView>(R.id.eventCategory).text = category
        findViewById<TextView>(R.id.eventVenue).text = venue

        val saveButton = findViewById<Button>(R.id.saveEventButton)
        saveButton.setOnClickListener {
            saveEventToFirestore()
        }

        // Add the "View Saved Events" button and its click listener
        val viewSavedEventsButton = findViewById<Button>(R.id.viewSavedEventsButton)
        viewSavedEventsButton.setOnClickListener {
            // Navigate to SavedEventsActivity
            val intent = Intent(this, SavedEventsActivity::class.java)
            startActivity(intent)
        }
    }

    private fun saveEventToFirestore() {
        val db = FirebaseFirestore.getInstance()

        // Create a new event with a map
        val event = hashMapOf(
            "title" to title,
            "description" to description,
            "category" to category,
            "venue" to venue
        )

        // Add a new document with a generated ID
        db.collection("events")
            .add(event)
            .addOnSuccessListener { documentReference ->
                Toast.makeText(this, "Event saved successfully", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error saving event", Toast.LENGTH_SHORT).show()
            }
    }
}
