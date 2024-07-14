package com.example.gcommerce

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.ArrayList

class HistoryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_history)

        val llNoItems = findViewById<LinearLayout>(R.id.llNoItems)
        val nsvHistory = findViewById<NestedScrollView>(R.id.nsvHistory)

        val buyer = intent.getStringExtra("buyer")!!

        val db = DBHandler(this)
        val items = db.getHistoryItems(buyer)

        llNoItems.visibility = if (items.isEmpty()) {
            View.VISIBLE
        } else {
            View.GONE
        }

        nsvHistory.visibility = if (items.isEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }

        val rvHistory = findViewById<RecyclerView>(R.id.rvPurchaseHistory)
        rvHistory.layoutManager = LinearLayoutManager(this)
        rvHistory.setHasFixedSize(true)

        rvHistory.adapter = HistoryAdapter(items)

        findViewById<Button>(R.id.btnNoHistory).setOnClickListener {
            finish()
        }
    }


}