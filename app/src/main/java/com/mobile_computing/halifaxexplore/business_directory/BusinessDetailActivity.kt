package com.mobile_computing.halifaxexplore.business_directory

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mobile_computing.halifaxexplore.R
import com.mobile_computing.halifaxexplore.databinding.ActivityBusinessDetailBinding
import com.squareup.picasso.Picasso
import android.widget.ProgressBar

class BusinessDetailActivity : AppCompatActivity() {

    private lateinit var progressBar: ProgressBar
    var selectedBusinessReviews = mutableListOf<Review>()
    private lateinit var binding: ActivityBusinessDetailBinding
    private val TAG = "MainActivity"
    var documentId: String = ""

//    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBusinessDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        documentId = intent.getStringExtra(EXTRA_BUSINESS_ID).toString()
        progressBar = findViewById(R.id.progressBar)
        progressBar.visibility = View.VISIBLE
        getReviewsFromFirestore()
//        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout)
//        swipeRefreshLayout.setOnRefreshListener {
//            // Perform the refresh action here (e.g., fetch new data)
//            getReviewsFromFirestore()
//            // Stop the refresh animation when the data is updated
//            swipeRefreshLayout.isRefreshing = false
//        }
    }

    private fun getReviewsFromFirestore(){
        selectedBusinessReviews.clear()
        val db = Firebase.firestore
        db.collection("local_businesses/${documentId}/reviews")
            .get()
            .addOnSuccessListener { reviewsResult ->
                val reviews = mutableListOf<Review>()

                for (reviewDocument in reviewsResult) {
                    selectedBusinessReviews.clear()
                    val review = reviewDocument.toObject(Review::class.java)
                    //selectedBusinessReviews.clear()
                    reviews.add(review)
                    println(">>>> REVIEW IN DETAIL:: ${review}")
                }

                selectedBusinessReviews = reviews
                displayDataOnUI()
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting reviews for ${documentId}.", exception)
//                selectedBusinessReviews = intent.getSerializableExtra(EXTRA_BUSINESS_REVIEWS) as Array<Review>?
                println(">>>> REVIEW IN DETAIL CATCH:: ${selectedBusinessReviews}")
                displayDataOnUI()
            }
    }

    private fun displayDataOnUI(){
        // Receive the selected business details from the intent
        val selectedBusinessName = intent.getStringExtra(EXTRA_BUSINESS_NAME)
        val selectedBusinessAddress = intent.getStringExtra(EXTRA_BUSINESS_ADDRESS)
        val selectedBusinessRating = intent.getFloatExtra(EXTRA_BUSINESS_RATING, 0.0f)
        val selectedBusinessPhone = intent.getStringExtra(EXTRA_BUSINESS_PHONE)
        val selectedBusinessEmail = intent.getStringExtra(EXTRA_BUSINESS_EMAIL)
        val selectedBusinessDescription = intent.getStringExtra(EXTRA_BUSINESS_DESCRIPTION)
        val selectedBusinessImage = intent.getStringExtra(EXTRA_BUSINESS_IMAGE)

        // Display the details in the UI
        binding.businessNameTextView.text = selectedBusinessName
        binding.businessAddressTextView.text = selectedBusinessAddress
        binding.businessPhoneTextView.text = selectedBusinessPhone
        binding.businessEmailTextView.text = selectedBusinessEmail
        binding.businessDescriptionTextView.text = selectedBusinessDescription
        Picasso.get().load(selectedBusinessImage).into(binding.businessImageView)

        val reviewsContainer: LinearLayout = findViewById(R.id.reviewsContainer)
        reviewsContainer.removeAllViews()

        if (selectedBusinessReviews != null) {
            for (review in selectedBusinessReviews!!) {
                val reviewView = layoutInflater.inflate(R.layout.item_review, null)

                val userNameTextView: TextView = reviewView.findViewById(R.id.userNameTextView)
                userNameTextView.text = review.user

                val commentTextView: TextView = reviewView.findViewById(R.id.commentTextView)
                commentTextView.text = review.comment

                val ratingBar: RatingBar = reviewView.findViewById(R.id.ratingBar)
                ratingBar.rating = review.rating.toFloat()

                reviewsContainer.addView(reviewView)
            }
        }
        progressBar.visibility = View.GONE

        // Set click listeners
        binding.businessPhoneTextView.setOnClickListener {
            if (selectedBusinessPhone != null) {
                dialPhoneNumber(selectedBusinessPhone)
            }
        }
        binding.businessEmailTextView.setOnClickListener {
            if (selectedBusinessEmail != null) {
                sendEmail(selectedBusinessEmail)
            }
        }
        binding.businessAddressTextView.setOnClickListener {
            if (selectedBusinessAddress != null) {
                openMaps(selectedBusinessAddress)
            }
        }

        binding.addReviewButton.setOnClickListener {
            showAddReviewDialog()
        }
    }

    private fun showAddReviewDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_review, null)

        val commentEditText = dialogView.findViewById<EditText>(R.id.reviewCommentEditText)
        val ratingBar = dialogView.findViewById<RatingBar>(R.id.reviewRatingBar)

        val dialog = AlertDialog.Builder(this)
            .setTitle("Add Review")
            .setView(dialogView)
            .setPositiveButton("Submit") { _, _ ->
                val user = "Pratik"
                val comment = commentEditText.text.toString()
                val rating = ratingBar.rating

                // Save the review to Firestore
                saveReviewToFirestore(user, comment, rating)
            }
            .setNegativeButton("Cancel", null)
            .create()

        dialog.show()
    }

    private fun saveReviewToFirestore(user: String, comment: String, rating: Float) {
        val db = Firebase.firestore
        val reviewsCollection = db.collection("local_businesses").document(documentId).collection("reviews")

        val newReview = Review(user, comment, rating.toDouble())

        reviewsCollection.add(newReview)
            .addOnSuccessListener {
                selectedBusinessReviews.clear()
                getReviewsFromFirestore()
                Toast.makeText(this, "Review added successfully", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Error adding review", e)
                Toast.makeText(this, "Failed to add review", Toast.LENGTH_SHORT).show()
            }
    }

    private fun openMaps(address: String) {
        val gmmIntentUri: Uri = Uri.parse("geo:0,0?q=${Uri.encode(address)}")
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapIntent.setPackage("com.google.android.apps.maps")
        startActivity(mapIntent)
    }

    private fun sendEmail(email: String) {
        val emailIntent = Intent(Intent.ACTION_SENDTO)
        emailIntent.data = Uri.parse("mailto:$email")
        startActivity(emailIntent)
    }

    private fun dialPhoneNumber(phoneNumber: String) {
        val dialIntent = Intent(Intent.ACTION_DIAL)
        dialIntent.data = Uri.parse("tel:$phoneNumber")
        startActivity(dialIntent)
    }

    companion object {
        const val EXTRA_BUSINESS_ID = "extra_business_id"
        const val EXTRA_BUSINESS_NAME = "extra_business_name"
        const val EXTRA_BUSINESS_ADDRESS = "extra_business_address"
        const val EXTRA_BUSINESS_RATING = "extra_business_rating"
        const val EXTRA_BUSINESS_PHONE = "extra_business_phone"
        const val EXTRA_BUSINESS_EMAIL = "extra_business_email"
        const val EXTRA_BUSINESS_DESCRIPTION = "extra_business_description"
        const val EXTRA_BUSINESS_IMAGE = "extra_business_image"
        const val EXTRA_BUSINESS_REVIEWS = "extra_business_reviews"

    }
}
