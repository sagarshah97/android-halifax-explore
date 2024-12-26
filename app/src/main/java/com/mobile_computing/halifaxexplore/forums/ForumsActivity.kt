package com.mobile_computing.halifaxexplore.forums

import android.content.ContentValues.TAG
import com.mobile_computing.halifaxexplore.LoginActivity
import com.mobile_computing.halifaxexplore.R
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mobile_computing.halifaxexplore.MainActivity
import com.mobile_computing.halifaxexplore.alerts.AlertsActivity
import com.mobile_computing.halifaxexplore.blogs.BlogsActivity
import com.mobile_computing.halifaxexplore.business_directory.BusinessDirectoryActivity
import com.mobile_computing.halifaxexplore.event_calendar.EventCalendarActivity
import com.mobile_computing.halifaxexplore.marketplace.MarketplaceActivity
import com.mobile_computing.halifaxexplore.newsfeed.NewsFeedActivity
import com.mobile_computing.halifaxexplore.public_transport.PublicTransportActivity
import com.mobile_computing.halifaxexplore.service_finder.ServiceFinderActivity
import com.mobile_computing.halifaxexplore.traffic_updates.TrafficUpdatesActivity
import com.mobile_computing.halifaxexplore.user_management.UserManagementActivity
import com.mobile_computing.halifaxexplore.weather.WeatherActivity
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

class ForumsActivity : AppCompatActivity() {
    private lateinit var toggle : ActionBarDrawerToggle
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView : NavigationView
    private lateinit var questionAdapter: QuestionAdapter
    private lateinit var filteredQuestionList: MutableList<Question>
    private lateinit var questionList: MutableList<Question>
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchEditText: EditText
    private lateinit var askQuestionButton: Button
    private lateinit var firestore: FirebaseFirestore
    private var user: String = "Pratik"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forums)
        firestore = FirebaseFirestore.getInstance()
        val questionRef = firestore.collection("questions")
        recyclerView = findViewById(R.id.recyclerView)
        searchEditText = findViewById(R.id.searchEditText)
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val searchText = s.toString().trim().lowercase()
                filterQuestions(searchText)
            }
            override fun afterTextChanged(s: Editable?) {}
        })
        questionList = mutableListOf()
        filteredQuestionList = mutableListOf()
        questionAdapter = QuestionAdapter(filteredQuestionList)
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@ForumsActivity)
            adapter = questionAdapter
        }
        drawerLayout = findViewById(R.id.drawer_layout)
        navView = findViewById(R.id.nav_view)
        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        toggle.syncState()
        drawerLayout.addDrawerListener(toggle)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        navView.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.logout -> {
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    Toast.makeText(
                        this, // in which context the toast should show up
                        "Logged Out", //toast message content
                        Toast.LENGTH_SHORT //stay on screen for a short duration only
                    ).show()
                }
                R.id.home -> {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }
                R.id.alerts -> {
                    val intent = Intent(this, AlertsActivity::class.java)
                    startActivity(intent)
                }
                R.id.blogs -> {
                    val intent = Intent(this, BlogsActivity::class.java)
                    startActivity(intent)
                }
                R.id.businesses -> {
                    val intent = Intent(this, BusinessDirectoryActivity::class.java)
                    startActivity(intent)
                }
                R.id.events -> {
                    val intent = Intent(this, EventCalendarActivity::class.java)
                    startActivity(intent)
                }
                R.id.forums -> {
                    val intent = Intent(this, ForumsActivity::class.java)
                    startActivity(intent)
                }
                R.id.marketplace -> {
                    val intent = Intent(this, MarketplaceActivity::class.java)
                    startActivity(intent)
                }
                R.id.newsfeed -> {
                    val intent = Intent(this, NewsFeedActivity::class.java)
                    startActivity(intent)
                }
                R.id.transport -> {
                    val intent = Intent(this, PublicTransportActivity::class.java)
                    startActivity(intent)
                }
                R.id.services -> {
                    val intent = Intent(this, ServiceFinderActivity::class.java)
                    startActivity(intent)
                }
                R.id.traffic -> {
                    val intent = Intent(this, TrafficUpdatesActivity::class.java)
                    startActivity(intent)
                }
                R.id.profile -> {
                    val intent = Intent(this, UserManagementActivity::class.java)
                    startActivity(intent)
                }
                R.id.weather -> {
                    val intent = Intent(this, WeatherActivity::class.java)
                    startActivity(intent)
                }
            }
            true
        }
        questionRef.get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val id = document.id
                    val data = document.data
                    val questionText = data["question"] as? String ?: ""
                    val user = data["user"] as? String ?: ""
                    val date = data["date"] as? String ?: ""
                    val description = data["description"] as? String ?: ""
                    val question = Question(id, questionText, description, user, date)
                    questionList.add(question)
                }
                filteredQuestionList.addAll(questionList)
                questionAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents: ", exception)
            }
        askQuestionButton = findViewById(R.id.askQuestionButton)
        askQuestionButton.setOnClickListener {
            showAskQuestionDialog()
        }
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }
    private fun filterQuestions(query: String) {
        filteredQuestionList.clear()
        if (query.isEmpty()) {
            filteredQuestionList.addAll(questionList)
        } else {
            for (question in questionList) {
                if (question.question.lowercase().contains(query)) {
                    filteredQuestionList.add(question)
                }
            }
        }
        questionAdapter.notifyDataSetChanged()
    }
    private fun showAskQuestionDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_ask_question, null)
        val dialogEditText: TextInputEditText = dialogView.findViewById(R.id.dialogEditText)
        val descriptionEditText: TextInputEditText = dialogView.findViewById(R.id.descriptionEditText)

        val dialog = AlertDialog.Builder(this)
            .setTitle("Ask a Question")
            .setView(dialogView)
            .setPositiveButton("Submit") { dialogInterface, _ ->
                val questionText = dialogEditText.text.toString().trim()
                val descriptionText = descriptionEditText.text.toString().trim()
                if (questionText.isNotEmpty()) {
                    addQuestionToFirestore(questionText, descriptionText)
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
    private fun addQuestionToFirestore(question: String, description: String){
        val db = Firebase.firestore
        val currentDate = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("d MMMM, yyyy", Locale.ENGLISH)
        val formattedDate = currentDate.format(formatter)
        val questionData = hashMapOf(
            "question" to question,
            "description" to description,
            "user" to user,
            "date" to formattedDate
        )
        db.collection("questions")
            .add(questionData)
            .addOnSuccessListener { documentReference ->
                val questionAdd = Question(documentReference.id, question, description, user, formattedDate)
                questionList.add(questionAdd)
                filteredQuestionList.add(questionAdd)
                questionAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
            }
    }
}