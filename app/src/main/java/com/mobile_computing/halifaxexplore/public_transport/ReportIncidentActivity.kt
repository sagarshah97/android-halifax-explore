package com.mobile_computing.halifaxexplore.public_transport



import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FieldValue
import com.mobile_computing.halifaxexplore.R

class ReportIncidentActivity : AppCompatActivity() {

    private lateinit var titleEditText: EditText
    private lateinit var descriptionEditText: EditText
    private lateinit var submitButton: Button
    private val firestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report_incident)

        titleEditText = findViewById(R.id.titleEditText)
        descriptionEditText = findViewById(R.id.descriptionEditText)
        submitButton = findViewById(R.id.submitButton)

        submitButton.setOnClickListener {
            submitIncident()
        }
    }

    private fun submitIncident() {
        val title = titleEditText.text.toString().trim()
        val description = descriptionEditText.text.toString().trim()

        // Simple validation
        if (title.isEmpty() || description.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }

        // Create a new incident with a timestamp
        val incident = hashMapOf(
            "title" to title,
            "description" to description,
            "timestamp" to FieldValue.serverTimestamp() // Gets the current timestamp from the server
        )

        firestore.collection("incidents")
            .add(incident)
            .addOnSuccessListener {
                Toast.makeText(this, "Incident reported successfully", Toast.LENGTH_SHORT).show()
                finish() // Close the activity after successful submission
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error reporting incident: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
