package com.mobile_computing.halifaxexplore.forums

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mobile_computing.halifaxexplore.R
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

class AnswersActivity : AppCompatActivity() {
    private lateinit var answerAdapter: AnswerAdapter
    private lateinit var answerList: MutableList<Answer>
    private lateinit var recyclerView: RecyclerView
    private lateinit var answerButton: Button
    private lateinit var firestore: FirebaseFirestore
    private lateinit var question: Question
    private var user: String = "Pratik"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_answers)
        firestore = FirebaseFirestore.getInstance()
        val questionRef = firestore.collection("answers")
        recyclerView = findViewById(R.id.answersRecyclerView)
        answerList = mutableListOf()
        answerAdapter = AnswerAdapter(answerList)
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@AnswersActivity)
            adapter = answerAdapter
        }
        question = intent.getParcelableExtra<Question>("questionExtra")!!
        if (question != null) {
            val questionTextView: TextView = findViewById(R.id.questionTextView)
            val descriptionTextView: TextView = findViewById(R.id.descriptionTextView)
            val signatureTextView: TextView = findViewById(R.id.signatureTextView)

            questionTextView.text = question.question
            descriptionTextView.text = question.description // If description is present
            descriptionTextView.visibility = if (question.description.isNotEmpty()) View.VISIBLE else View.GONE
            val signatureText = "by ${question.user} on ${question.date}"
            signatureTextView.text = signatureText
        }
        questionRef.whereEqualTo("questionId", question.id).get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val id = document.id
                    val data = document.data
                    val answerText = data["answer"] as? String ?: ""
                    val user = data["user"] as? String ?: ""
                    val date = data["date"] as? String ?: ""
                    val answer = Answer(answerText, user, date)
                    answerList.add(answer)
                }
                answerAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Log.w(ContentValues.TAG, "Error getting documents: ", exception)
            }
        answerButton = findViewById(R.id.answerButton)
        answerButton.setOnClickListener {
            showAddAnswerDialog()
        }
    }
    private fun showAddAnswerDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_answer, null)
        val dialogEditText: TextInputEditText = dialogView.findViewById(R.id.dialogEditText)
        val dialog = AlertDialog.Builder(this)
            .setTitle("Add an answer")
            .setView(dialogView)
            .setPositiveButton("Submit") { dialogInterface, _ ->
                val answerText = dialogEditText.text.toString().trim()
                if (answerText.isNotEmpty()) {
                    addQuestionToFirestore(answerText, question.id)
                } else {
                    Toast.makeText(
                        this, // in which context the toast should show up
                        "Question cannot be empty, please try again.", //toast message content
                        Toast.LENGTH_SHORT //stay on screen for a short duration only
                    ).show()
                }
                dialogInterface.dismiss()
            }
            .setNegativeButton("Cancel") { dialogInterface, _ ->
                dialogInterface.dismiss()
            }
            .create()
        dialog.show()
    }
    private fun addQuestionToFirestore(answer: String, questionId: String){
        val db = Firebase.firestore
        val currentDate = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("d MMMM, yyyy", Locale.ENGLISH)
        val formattedDate = currentDate.format(formatter)
        val answerData = hashMapOf(
            "answer" to answer,
            "user" to user,
            "date" to formattedDate,
            "questionId" to questionId
        )
        db.collection("answers")
            .add(answerData)
            .addOnSuccessListener { documentReference ->
                val answerAdd = Answer(answer, user, formattedDate)
                answerList.add(answerAdd)
                answerAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener { e ->
                Log.w(ContentValues.TAG, "Error adding document", e)
            }
    }
}