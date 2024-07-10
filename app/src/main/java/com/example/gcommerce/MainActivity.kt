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

class MainActivity : AppCompatActivity() {
    private lateinit var tvCreateAccount : TextView
    private lateinit var etLoginUsername : EditText
    private lateinit var etLoginPassword : EditText
    private lateinit var btnSignIn : Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        tvCreateAccount = findViewById(R.id.tvCreateAccount)
        btnSignIn = findViewById(R.id.btnSignIn)

        val sharedPreferences = getSharedPreferences("user_session", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)
        if (isLoggedIn){
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }

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
                editor.putBoolean("isLoggedIn", true)
                editor.apply()

                Intent(this, HomeActivity::class.java).also {
                    startActivity(it)
                }
                etLoginUsername.text.clear()
                etLoginPassword.text.clear()
                finish()
            } else if (isLoggedIn == false) {
                Toast.makeText(this, "Invalid credentials", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun onLogin(): Boolean? {
        etLoginUsername = findViewById(R.id.etLoginUsername)
        etLoginPassword = findViewById(R.id.etLoginPassword)

        if (etLoginUsername.text.isEmpty() || etLoginPassword.text.isEmpty()){
            Toast.makeText(this, "Incomplete fields!", Toast.LENGTH_SHORT).show()
            return null
        }

        val dbHandler = DBHandler(this)
        val isLoggedIn = dbHandler.onLogin(etLoginUsername.text.toString(), etLoginPassword.text.toString())

        return isLoggedIn
    }
}