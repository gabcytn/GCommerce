package com.example.gcommerce

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class MeFragment(private val context: Context, private val buyer: String?, val profilePhoto: String?, private val email: String?) : Fragment () {
    private lateinit var auth : FirebaseAuth
    private lateinit var googleSignInClient : GoogleSignInClient
    private lateinit var btnPurchaseHistory : TextView
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

        btnPurchaseHistory = view.findViewById(R.id.btnPurchaseHistory)
        btnPurchaseHistory.setOnClickListener {
            val intent = Intent(requireContext(), HistoryActivity::class.java)
            intent.putExtra("buyer", buyer)

            startActivity(intent)
        }

        val profileImageView = view.findViewById<ImageView>(R.id.ivProfilePhoto)
        val profileName = view.findViewById<TextView>(R.id.tvProfileName)
        if (profilePhoto != null) {
            Glide.with(requireContext())
                .load(profilePhoto)
                .placeholder(R.drawable.mock_profile)
                .error(R.drawable.img_not_found)
                .into(profileImageView)
        } else {
            profileImageView.setImageResource(R.drawable.mock_profile)
        }

        profileName.text = buyer
        view.findViewById<TextView>(R.id.tvProfileBio).text = "Hi, I'm $buyer, and I love to eat, travel, and shop at GCommerce App!"
        view.findViewById<TextView>(R.id.tvProfileEmail).text = email
        return view
    }
}