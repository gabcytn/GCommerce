package com.example.gcommerce

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class MainActivity : AppCompatActivity() {
    private lateinit var tvCreateAccount : TextView
    private lateinit var etLoginUsername : EditText
    private lateinit var etLoginPassword : EditText
    private lateinit var btnSignIn : Button
    private lateinit var cvGoogle : CardView

    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient : GoogleSignInClient

    private lateinit var checkAuth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        etLoginUsername = findViewById(R.id.etLoginUsername)
        etLoginPassword = findViewById(R.id.etLoginPassword)

        val sharedPreferences = getSharedPreferences("user_session", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)


        checkAuth = FirebaseAuth.getInstance()
        val currUser = checkAuth.currentUser
        if (currUser != null || isLoggedIn ) {
            val spUserCredentials = getSharedPreferences("user_credentials", Context.MODE_PRIVATE)
            val spUserEditor = spUserCredentials.edit()
            spUserEditor.remove("display_name").apply()
            spUserEditor.remove("email").apply()
            if (currUser != null) {
                val currentUser = checkAuth.currentUser
                val displayName = currentUser?.displayName.toString()
                val email = currentUser?.email.toString()

                val intent = Intent(this, HomeActivity::class.java)
//                spUserEditor.clear().apply()
                spUserEditor.putString("display_name", displayName)
                spUserEditor.putString("email", email)
                spUserEditor.apply()

                startActivity(intent)
            } else if (isLoggedIn) {
                val spCustomCredential = getSharedPreferences("custom_credentials", Context.MODE_PRIVATE)

                val displayName = spCustomCredential.getString("display_name", "")
                val email = spCustomCredential.getString("email", "")

                val intent = Intent(this, HomeActivity::class.java)

//                spUserEditor.clear().apply()
                spUserEditor.putString("display_name", displayName)
                spUserEditor.putString("email", email)
                spUserEditor.apply()

                startActivity(intent)
            }

            editor.putBoolean("isLoggedIn", true)
            editor.apply()

            finish()

        }

        auth = FirebaseAuth.getInstance()
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        tvCreateAccount = findViewById(R.id.tvCreateAccount)
        btnSignIn = findViewById(R.id.btnSignIn)
        cvGoogle = findViewById(R.id.cvGoogle)
        etLoginUsername = findViewById(R.id.etLoginUsername)
        etLoginPassword = findViewById(R.id.etLoginPassword)

        tvCreateAccount.setOnClickListener {
            Intent(this, RegisterActivity::class.java).also {
                startActivity(it)
            }
            findViewById<EditText>(R.id.etLoginUsername).text.clear()
            findViewById<EditText>(R.id.etLoginPassword).text.clear()
        }

        btnSignIn.setOnClickListener {
            val isLoggedIn = onLogin()
            if (isLoggedIn == true){
                val dbHandler = DBHandler(this)
                val displayName = etLoginUsername.text.toString()
                val email = dbHandler.getEmail(etLoginUsername.text.toString())

                val spCustomCredential = getSharedPreferences("custom_credentials", Context.MODE_PRIVATE)
                val spCustomEditor = spCustomCredential.edit()

                spCustomEditor.putString("display_name", displayName)
                spCustomEditor.putString("email", email)
                spCustomEditor.apply()

                editor.putBoolean("isLoggedIn", true)
                editor.apply()

                val intent = Intent(this, HomeActivity::class.java)
                intent.putExtra("display_name", displayName)
                intent.putExtra("email", email)
                startActivity(intent)

                etLoginUsername.text.clear()
                etLoginPassword.text.clear()
                finish()
            } else if (isLoggedIn == false) {
                Toast.makeText(this, "Invalid credentials", Toast.LENGTH_SHORT).show()
            }
        }
        cvGoogle.setOnClickListener{
            signInWithGoogle()
        }
    }


    private fun signInWithGoogle() {
        // Configure Google Sign-In client
        val intent = googleSignInClient.signInIntent
        startActivityForResult(intent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Handle sign in failure
                Toast.makeText(this, "Google sign in failed: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, navigate to HomeActivity
                    val currentUser = checkAuth.currentUser
                    val displayName = currentUser?.displayName.toString()
                    val email = currentUser?.email.toString()

                    val sharedPreferences = getSharedPreferences("user_session", Context.MODE_PRIVATE)
                    val editor = sharedPreferences.edit()
                    editor.putBoolean("isLoggedIn", true)
                    editor.apply()

                    val intent = Intent(this, HomeActivity::class.java)
                    intent.putExtra("display_name", displayName)
                    intent.putExtra("email", email)
                    startActivity(intent)

                    etLoginUsername.text.clear()
                    etLoginPassword.text.clear()
                    finish()

                } else {
                    // Sign in failure
                    Toast.makeText(this, "Authentication failed.", Toast.LENGTH_SHORT).show()
                }
            }
    }


    private fun onLogin(): Boolean? {

        if (etLoginUsername.text.isEmpty() || etLoginPassword.text.isEmpty()){
            Toast.makeText(this, "Incomplete fields!", Toast.LENGTH_SHORT).show()
            return null
        }

        val dbHandler = DBHandler(this)
        val isLoggedIn = dbHandler.onLogin(etLoginUsername.text.toString(), etLoginPassword.text.toString())

        return isLoggedIn
    }

    companion object {
        private const val RC_SIGN_IN = 9001
    }
}