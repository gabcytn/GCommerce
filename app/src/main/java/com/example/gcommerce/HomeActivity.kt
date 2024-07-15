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
    private lateinit var homeFragment : HomeFragment
    private lateinit var meFragment: MeFragment
    private lateinit var notifFragment: NotificationsFragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)

        val sharedPreferences = getSharedPreferences("user_session", Context.MODE_PRIVATE)
        val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)
        if (!isLoggedIn){
            startActivity(Intent(this, MainActivity::class.java))
        }

        val spUserCredential = getSharedPreferences("user_credentials", Context.MODE_PRIVATE)
        val spDisplayName = spUserCredential.getString("display_name", "")!!
        val spEmail = spUserCredential.getString("email", "")!!

        // PROFILE PIC OF GOOGLE USERS || NULL
        val spProfilePhoto = spUserCredential.getString("profile_pic", null)

        val displayName = intent.getStringExtra("display_name")
        val email = intent.getStringExtra("email")

        homeFragment = if (spDisplayName != "") {
            HomeFragment(spDisplayName, spEmail)
        } else {
            HomeFragment(displayName, email)
        }

        meFragment = if (spDisplayName != "") {
            MeFragment(this, spDisplayName, spProfilePhoto, spEmail)
        } else {
            MeFragment(this, displayName, spProfilePhoto, email)
        }

        notifFragment = if (spDisplayName != "") {
            NotificationsFragment(spDisplayName)
        } else {
            NotificationsFragment(displayName)
        }

        val messagesFragment = MessagesFragment()

        setFragment(homeFragment)

        bottomNav = findViewById(R.id.bottomNavigationView)
        bottomNav.setOnItemSelectedListener {
            when(it.itemId){
                R.id.miHome -> setFragment(homeFragment)
                R.id.miNotifications -> setFragment(notifFragment)
                R.id.miMessages -> setFragment(messagesFragment)
                R.id.miMe -> setFragment(meFragment)
            }
            true
        }
        bottomNav.getOrCreateBadge(R.id.miNotifications).apply {
            number = 10
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