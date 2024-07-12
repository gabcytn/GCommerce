package com.example.gcommerce

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class MeFragment(private val context: Context) : Fragment () {
    private lateinit var auth : FirebaseAuth
    private lateinit var googleSignInClient : GoogleSignInClient
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_me, container, false)

        auth = FirebaseAuth.getInstance()
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)

        view.findViewById<Button>(R.id.btnLogout).setOnClickListener{
            val sharedPreferences = requireActivity().getSharedPreferences("user_session", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()

            val spUserCredential = requireActivity().getSharedPreferences("user_credentials", Context.MODE_PRIVATE)
            val spUserEditor = spUserCredential.edit()

            AlertDialog.Builder(context)
                .setTitle("Logout")
                .setMessage("Do you want to logout?")
                .setPositiveButton("Yes"){_, _ ->
//                    auth.signOut()
                    Firebase.auth.signOut()
                    googleSignInClient.signOut()

                    editor.putBoolean("isLoggedIn", false)
                    editor.apply()

                    spUserEditor.remove("display_name").apply()
                    spUserEditor.remove("email").apply()
                    spUserEditor.clear().apply()

                    startActivity(Intent(context, MainActivity::class.java))
                    requireActivity().finish()

                }.setNegativeButton("No"){w, _ ->
                    w.cancel()
                }.create()
                .show()

        }

        return view
    }
}