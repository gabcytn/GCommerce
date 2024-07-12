package com.example.gcommerce

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment

class HomeFragment (private val displayName: String?, val email: String?) : Fragment () {
    private lateinit var tvGreetingText : TextView
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        tvGreetingText = view.findViewById(R.id.tvGreetingText)

        tvGreetingText.text = "For Mr. ${displayName}"

        return view
    }

}