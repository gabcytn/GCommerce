package com.example.gcommerce

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeActivity : AppCompatActivity() {
    private lateinit var bottomNav : BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val sharedPreferences = getSharedPreferences("user_session", Context.MODE_PRIVATE)
        val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)
        if (!isLoggedIn){
            startActivity(Intent(this, MainActivity::class.java))
        }

        val homeFragment = HomeFragment()
        val notificationsFragment = NotificationsFragment()
        val messagesFragment = MessagesFragment()
        val meFragment = MeFragment(this)

        setFragment(homeFragment)

        bottomNav = findViewById(R.id.bottomNavigationView)
        bottomNav.setOnItemSelectedListener {
            when(it.itemId){
                R.id.miHome -> setFragment(homeFragment)
                R.id.miNotifications -> setFragment(notificationsFragment)
                R.id.miMessages -> setFragment(messagesFragment)
                R.id.miMe -> setFragment(meFragment)
            }
            true
        }
        bottomNav.getOrCreateBadge(R.id.miMessages).apply {
            number = 8
            isVisible = true
        }
    }

    private fun setFragment(f: Fragment){
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFrameLayout, f)
            commit()
        }
    }
}