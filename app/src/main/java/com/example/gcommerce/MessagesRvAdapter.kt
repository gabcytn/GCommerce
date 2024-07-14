package com.example.gcommerce

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintSet.Layout
import androidx.recyclerview.widget.RecyclerView

class MessagesRvAdapter (private val messagesList: ArrayList<MessagesModel>, val rowItem: Int) : RecyclerView.Adapter<MessagesRvAdapter.MessagesRvViewHolder> () {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MessagesRvAdapter.MessagesRvViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(rowItem, parent, false)
        return MessagesRvViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MessagesRvAdapter.MessagesRvViewHolder, position: Int) {
        val currItem = messagesList[position]

        holder.img.setImageResource(currItem.msgImage)
        holder.sender.text = currItem.msgSender
        holder.content.text = currItem.msgContent
        holder.timeSent.text = currItem.msgTimeSent
    }

    override fun getItemCount(): Int {
        return messagesList.size
    }

    class MessagesRvViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val img : ImageView = itemView.findViewById(R.id.ivMessageProfile)
        val sender : TextView = itemView.findViewById(R.id.tvMessageFrom)
        val content: TextView = itemView.findViewById(R.id.tvMessageContent)
        val timeSent : TextView = itemView.findViewById(R.id.tvMessageTimeSent)
    }
}