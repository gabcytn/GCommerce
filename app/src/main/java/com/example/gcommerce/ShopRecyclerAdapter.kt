package com.example.gcommerce

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class ShopRecyclerAdapter(val shopItems : ArrayList<ShopItem>, val context: Context, val buyer: String) : RecyclerView.Adapter<ShopRecyclerAdapter.ShopRecyclerViewHolder> () {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopRecyclerViewHolder {
        val itemView =LayoutInflater.from(parent.context).inflate(R.layout.main_shop_item, parent, false)
        return ShopRecyclerViewHolder(itemView, shopItems, context, buyer)
    }

    override fun getItemCount(): Int {
        return shopItems.size
    }

    override fun onBindViewHolder(holder: ShopRecyclerViewHolder, position: Int) {
        val currentItem = shopItems[position]

        holder.itemImage.setImageResource(currentItem.itemImage)
        holder.itemName.text = currentItem.itemName
        holder.itemPrice.text = "P${currentItem.itemPrice}.00"

    }

    class ShopRecyclerViewHolder(itemView: View, shopItems: ArrayList<ShopItem>, context: Context, buyer: String) : RecyclerView.ViewHolder(itemView) {
        val itemImage : ImageView = itemView.findViewById(R.id.ivItemImage)
        val itemName : TextView = itemView.findViewById(R.id.tvItemName)
        val itemPrice : TextView = itemView.findViewById(R.id.tvItemPrice)
        val itemBtn = itemView.findViewById<Button>(R.id.btnAddToCart)

        init {
            itemBtn.setOnClickListener {
//                this.isAddedToCart = true
                val currItem = shopItems[adapterPosition]
                val dbHandler = DBHandler(context)
                val isAddedToCart = dbHandler.isAddedToCart(currItem.itemName, buyer)

                if (isAddedToCart) {
                    return@setOnClickListener
                }

                val currItemName = currItem.itemName
                val currItemPrice = currItem.itemPrice
                val currItemImage = currItem.itemImage

                val feedback = dbHandler.insertCartItems(currItemName, currItemPrice, buyer, currItemImage)
                if (feedback == "Success") {
                    Toast.makeText(context, "Added to cart", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }
}