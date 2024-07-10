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

class MeFragment(private val context: Context) : Fragment () {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_me, container, false)

        view.findViewById<Button>(R.id.btnLogout).setOnClickListener{
            val sharedPreferences = requireActivity().getSharedPreferences("user_session", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()

            AlertDialog.Builder(context)
                .setTitle("Logout")
                .setMessage("Do you want to logout?")
                .setPositiveButton("Yes"){_, _ ->
                    editor.putBoolean("isLoggedIn", false)
                    editor.apply()
                    
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