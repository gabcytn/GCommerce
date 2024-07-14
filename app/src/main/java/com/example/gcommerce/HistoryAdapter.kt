package com.example.gcommerce

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class HistoryAdapter(private val historyItems: ArrayList<HistoryModel>) : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> () {
    class HistoryViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView){
        val historyImage : ImageView = itemView.findViewById(R.id.ivHistoryCartItem)
        val historyName : TextView = itemView.findViewById(R.id.tvHistoryItemName)
        val historyPrice : TextView = itemView.findViewById(R.id.tvHistoryItemPrice)
        val historyQty : TextView = itemView.findViewById(R.id.tvHistoryQty)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.history_item, parent, false)
        return HistoryViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return historyItems.size
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val currItem = historyItems[position]

        holder.historyImage.setImageResource(currItem.image)
        holder.historyName.text = currItem.name
        holder.historyPrice.text = "P${currItem.price}.00"
        holder.historyQty.text = "Qty: ${currItem.qty}"
    }
}