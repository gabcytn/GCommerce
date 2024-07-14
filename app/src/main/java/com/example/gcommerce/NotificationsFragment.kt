package com.example.gcommerce

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class NotificationsFragment : Fragment () {
    private lateinit var rvNotifications : RecyclerView
    private lateinit var notificationsList : ArrayList<MessagesModel>
    private lateinit var rvNotificationsAdapter : MessagesRvAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_notifications, container, false)

        rvNotifications = view.findViewById(R.id.rvNotifications)
        rvNotifications.layoutManager = LinearLayoutManager(requireContext())
        rvNotifications.setHasFixedSize(true)

        notificationsList = arrayListOf()
        getNotificationsData()

        rvNotificationsAdapter = MessagesRvAdapter(notificationsList, R.layout.notification_row)
        rvNotifications.adapter = rvNotificationsAdapter

        return view
    }

    private fun getNotificationsData() {
        val notifTitles = listOf("Welcome to GCommerce!",
            "SALE ALERT",
            "Hurry, your voucher is expiring!",
            "Update your App NOW!",
            "FREE Shipping for YOU!",
            "Save up to P250.00 OFF",
            "20% OFF?",
            "New Login Alert!",
            "Verify your account",
            "Exclusive Sale!")
        val msgTimeSent = listOf("Just now", "25m", "5hr", "17hr", "21hr", "24h", "1d", "6d", "1w", "2w")
        for (i in notifTitles.indices) {
            notificationsList.add(MessagesModel(R.drawable.ic_clearmain, notifTitles[i], "Lorem ipsum", msgTimeSent[i]))
        }
    }
}