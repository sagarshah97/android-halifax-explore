package com.mobile_computing.halifaxexplore.public_transport


import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.firebase.firestore.FirebaseFirestore
import com.mobile_computing.halifaxexplore.R

class IncidentListActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var incidentListAdapter: IncidentListAdapter
    private val incidentList: MutableList<Incident> = mutableListOf()
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_incident_list)

        recyclerView = findViewById<RecyclerView>(R.id.incidentRecyclerView).apply {
            layoutManager = LinearLayoutManager(this@IncidentListActivity)
            adapter = IncidentListAdapter(incidentList)
        }
        incidentListAdapter = recyclerView.adapter as IncidentListAdapter
        val reportIncidentButton = findViewById<Button>(R.id.reportIncidentButton)
        reportIncidentButton.setOnClickListener {
            // Start ReportIncidentActivity when the button is clicked
            val intent = Intent(this, ReportIncidentActivity::class.java)
            startActivity(intent)
        }
        fetchIncidents()
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout)
        swipeRefreshLayout.setOnRefreshListener {
            fetchIncidents()
            swipeRefreshLayout.isRefreshing  = false
        }
    }

    private fun fetchIncidents() {
        FirebaseFirestore.getInstance().collection("incidents")
            .get()
            .addOnSuccessListener { documents ->
                incidentList.clear()
                for (document in documents) {
                    val incident = document.toObject(Incident::class.java).apply {
                        id = document.id // Capture the Firestore document ID if needed
                    }
                    incidentList.add(incident)
                }
                incidentListAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error loading incidents: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
    }
}

