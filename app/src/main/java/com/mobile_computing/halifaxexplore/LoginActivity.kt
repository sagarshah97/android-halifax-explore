package com.mobile_computing.halifaxexplore

import android.app.Activity
import android.content.Intent // Import intent to do communication between different component classes here it is LoginActivity with SecondActivity
import android.os.Bundle // allow to use Bundle data structure
import android.widget.Button // import to use the Button
import android.widget.EditText // Import to use text field
import android.widget.ImageView
import android.widget.Toast // Import to use Toast Message
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity // Use AppCompatActivity instead of Activity to provide backwards compatibility
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInResult
import com.google.android.gms.common.api.ResultCallback
import com.google.android.gms.common.api.Status
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

import com.mobile_computing.halifaxexplore.user_management.UserManagementActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var auth : FirebaseAuth
    private lateinit var googleSignInClient : GoogleSignInClient
    //private val RC_SIGN_IN = 9001

    //override the default onCreate function from AppCompatActivity to create our own
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState) // Instantiate the state from the parent class before continuing with our own initialization
        setContentView(R.layout.activity_login) // get resources required to create this view from activity_login.xml

//        // get view components from xml by their ids to be used in the view
//        var usernameEditText = findViewById<EditText>(R.id.usernameEditText)
//        var passwordEditText = findViewById<EditText>(R.id.passwordEditText)
//        val loginButton = findViewById<Button>(R.id.loginButton)
//
//        // this block of code tells us what to do after the login button is pressed
//        loginButton.setOnClickListener {
//            //get string data from the input fields
//            var username = usernameEditText.text.toString()
//            var password = passwordEditText.text.toString()
//
//            // Check if username and password match predefined values
//            if (username == "aa" && password == "aa") { //check if the values are same as pre defined ones in code
//                Toast.makeText(
//                    this, // in which context the toast should show up
//                    "Logged In", //toast message content
//                    Toast.LENGTH_SHORT //stay on screen for a short duration only
//                ).show() // show the toast message
//            // If details are fine go to SecondActivity using Intent
//                val intent = Intent(this, MainActivity::class.java)
//                intent.putExtra("user", username)
//                startActivity(intent) // start the next activity using intent
//            }
//            // If details are not proper show a toast to the users to make them understand that the entered details are invalid
//            else {
//                Toast.makeText(
//                    this, // in which context the toast should show up
//                    "Invalid credentials. Please try again.", //toast message content
//                    Toast.LENGTH_SHORT //stay on screen for a short duration only
//                ).show() // show the toast message
//            }
//        }

        //val googleSignInButton = findViewById<ImageView>(R.id.googleSignInButton)


        auth = FirebaseAuth.getInstance()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this , gso)

        findViewById<ImageView>(R.id.googleSignInButton).setOnClickListener{
            signInGoogle()
        }

    }

    private fun signInGoogle(){
        val signInIntent = googleSignInClient.signInIntent
        launcher.launch(signInIntent)
    }

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result ->
        if (result.resultCode == Activity.RESULT_OK){

            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            handleResults(task)
        }
    }

    private fun handleResults(task: Task<GoogleSignInAccount>) {
        if (task.isSuccessful){
            val account : GoogleSignInAccount? = task.result
            if (account != null){
                updateUI(account)
            }
        }else{
            Toast.makeText(this, task.exception.toString() , Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateUI(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken , null)
        auth.signInWithCredential(credential).addOnCompleteListener {
            if (it.isSuccessful){
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("email" , account.email)
                intent.putExtra("name" , account.displayName)
                intent.putExtra("profileURL" , account.photoUrl)
                println("###########################################"+account.photoUrl)
                startActivity(intent)
            }else{
                Toast.makeText(this, it.exception.toString() , Toast.LENGTH_SHORT).show()

            }
        }
    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//
//        if (requestCode == RC_SIGN_IN) {
//            val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
//            handleGoogleSignInResult(result)
//        }
//    }

//    private fun handleGoogleSignInResult(result: GoogleSignInResult) {
//        if (result.isSuccess) {
//            val account = result.signInAccount
//            val username = account?.displayName ?: "Unknown User"
//            val email = account?.email ?: "Unknown Email"
//
//            // Pass user details as extras in the intent
//            val intent = Intent(this, UserManagementActivity::class.java)
//            intent.putExtra("user", username)
//            intent.putExtra("email", email)
//            startActivity(intent)
//        } else {
//            Toast.makeText(this, "Google Sign-In failed. Please try again.", Toast.LENGTH_SHORT).show()
//        }
//    }
}