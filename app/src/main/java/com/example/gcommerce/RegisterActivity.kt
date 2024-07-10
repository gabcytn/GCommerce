package com.example.gcommerce

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class RegisterActivity : AppCompatActivity() {
    private lateinit var etRegisterFirstName : EditText
    private lateinit var etRegisterLastName : EditText
    private lateinit var etRegisterEmail : EditText
    private lateinit var etRegisterUsername : EditText
    private lateinit var etRegisterPassword : EditText
    private lateinit var etRegisterConfirmPassword : EditText
    private lateinit var tvSignIn : TextView
    private lateinit var btnRegister : Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)

        btnRegister = findViewById(R.id.btnRegister)
        tvSignIn = findViewById(R.id.tvSignIn)

        tvSignIn.setOnClickListener {
            finish()
        }
        btnRegister.setOnClickListener {
            onRegister()
        }
    }

    private fun onRegister() {
        etRegisterFirstName = findViewById(R.id.etRegisterFirstName)
        etRegisterLastName = findViewById(R.id.etRegisterLastName)
        etRegisterEmail = findViewById(R.id.etRegisterEmail)
        etRegisterUsername = findViewById(R.id.etRegisterUsername)
        etRegisterPassword = findViewById(R.id.etRegisterPassword)
        etRegisterConfirmPassword = findViewById(R.id.etRegisterConfirmPassword)
        btnRegister = findViewById(R.id.btnRegister)

        val fields = listOf(etRegisterFirstName, etRegisterLastName, etRegisterEmail, etRegisterUsername, etRegisterPassword, etRegisterConfirmPassword)
        for (i in fields){
            if (i.text.isEmpty()){
                createToast("Please fill in all fields")
                return
            }
        }

        if (etRegisterPassword.text.toString() != etRegisterConfirmPassword.text.toString()){
            createToast("Passwords do not match!")
            return
        }

        val user = User(etRegisterFirstName.text.toString(),
            etRegisterLastName.text.toString(),
            etRegisterEmail.text.toString(),
            etRegisterUsername.text.toString(),
            etRegisterPassword.text.toString())

        val dbHandler = DBHandler(this)
        val feedback = dbHandler.insertData(user)

        when (feedback){
            "Success" -> {
                createToast("Success")
                finish()
            }
            "Failed" -> {
                createToast("Failed")
            }
        }
    }

    private fun createToast(s: String) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show()
    }
}