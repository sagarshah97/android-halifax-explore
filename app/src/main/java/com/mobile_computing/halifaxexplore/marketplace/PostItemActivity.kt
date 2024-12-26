package com.mobile_computing.halifaxexplore.marketplace

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.mobile_computing.halifaxexplore.databinding.ActivityPostItemBinding

class PostItemActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPostItemBinding
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostItemBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.postButton.setOnClickListener {
            // Get values from your form elements
            val itemName = binding.yourItemNameEditText.text.toString()
            val itemDescription = binding.yourItemDescriptionEditText.text.toString()
            val itemPrice = binding.yourItemPriceEditText.text.toString().toDouble()
            val imageUrl = binding.yourItemImageURLEditText.text.toString()
            val sellerEmail = intent.getStringExtra("loggedInUser")

            // Call a function to store the item in Firestore
            if (sellerEmail != null) {
                postItem(itemName, itemDescription, itemPrice, sellerEmail,imageUrl)
            }
        }

        binding.goBackButton.setOnClickListener {
            // Navigate back to the MarketplaceActivity
            val intent = Intent(this, MarketplaceActivity::class.java)
            startActivity(intent)
        }
    }

    private fun postItem(itemName: String, itemDescription: String, itemPrice: Double, sellerEmail: String,imageUrl: String) {
        // Create a new item in Firestore
        val newItem = hashMapOf(
            "itemName" to itemName,
            "itemDescription" to itemDescription,
            "price" to itemPrice,
            "sellerEmail" to sellerEmail,
            "imageUrl" to imageUrl
            // Add more fields as needed
        )

        db.collection("halifax-explore-marketplace")
            .add(newItem)
            .addOnSuccessListener {
                // Handle successful posting
                finish() // Close the activity after posting
            }
            .addOnFailureListener {
                // Handle failure
            }
    }
}
