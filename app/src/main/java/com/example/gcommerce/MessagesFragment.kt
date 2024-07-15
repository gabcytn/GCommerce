package com.example.gcommerce

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MessagesFragment : Fragment () {
    private lateinit var rvMessages : RecyclerView
    private lateinit var messagesList : ArrayList<MessagesModel>
    private lateinit var messagesAdapter: MessagesRvAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_messages, container, false)

        rvMessages = view.findViewById(R.id.rvMessages)
        rvMessages.layoutManager = LinearLayoutManager(requireContext())
        rvMessages.setHasFixedSize(true)

        messagesList = arrayListOf()
        getMessagesData()

        messagesAdapter = MessagesRvAdapter(messagesList, R.layout.message_row)
        rvMessages.adapter = messagesAdapter

        return view
    }

    private fun getMessagesData() {
        val msgProfiles = listOf(R.drawable.msg_1, R.drawable.msg_2, R.drawable.msg_3, R.drawable.msg_4, R.drawable.msg_5, R.drawable.msg_6, R.drawable.msg_7, R.drawable.msg_8, R.drawable.msg_9, R.drawable.msg_10)
        val msgSenders = listOf("Uniqlo", "Nike", "H&M", "Zara", "infinitee", "Penshoppe", "Bench", "Oxygn", "SM Store", "Cotton On")
        val msgTimeSent = listOf("3m", "56m", "2hr", "10hr", "16hr", "23h", "1d", "3d", "6d", "1w")
        val msgContents = listOf("Reacted ❤ to your message.",
            "Nike sale is coming!",
            "Have you seen the latest drops?",
            "Get a discount",
            "Received na sir?",
            "Sent a photo.",
            "Click our profile for more!",
            "Sent an attachment.",
            "Reacted ❤ to your message.",
            "Thanks for shopping!")
        for (i in msgProfiles.indices) {
            messagesList.add(MessagesModel(msgProfiles[i], msgSenders[i], msgContents[i], msgTimeSent[i]))
        }
    }


}