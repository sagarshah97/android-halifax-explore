package com.mobile_computing.halifaxexplore.event_calendar

import android.os.Bundle

import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.mobile_computing.halifaxexplore.R

class SavedEventsActivity : AppCompatActivity() {

    private lateinit var eventListView: ListView
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_saved_events)

        eventListView = findViewById(R.id.eventListView)
        db = FirebaseFirestore.getInstance()

        // Fetch saved events from Firestore
        fetchSavedEvents()
    }

    private fun fetchSavedEvents() {
        // Assuming you have a Firestore collection named "events"
        db.collection("events")
            .get()
            .addOnSuccessListener { result ->
                val eventList = ArrayList<String>()
                for (document in result) {
                    // Assuming your Firestore documents have a "title" field
                    val title = document.getString("title")
                    title?.let {
                        eventList.add(it)
                    }
                }
                // Create an ArrayAdapter to display the event titles
                val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, eventList)
                eventListView.adapter = adapter
            }
            .addOnFailureListener { exception ->
                // Handle errors
            }
    }
}
