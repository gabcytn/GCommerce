package com.example.gcommerce

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

class CartRecyclerAdapter(private val cartItemsList: ArrayList<CartItem>) : RecyclerView.Adapter<CartRecyclerAdapter.CartViewHolder> () {
    class CartViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView){
        val itemImage: ImageView = itemView.findViewById(R.id.ivCartItem)
        val itemName: TextView = itemView.findViewById(R.id.tvCartItemName)
        val itemPrice: TextView = itemView.findViewById(R.id.tvCartItemPrice)
        val itemQty: TextView = itemView.findViewById(R.id.tvQuantity)

        val btnInc = itemView.findViewById<CardView>(R.id.cvPlusQty)
        val btnDec = itemView.findViewById<CardView>(R.id.cvMinusQty)

        init {
            btnDec.setOnClickListener {
                if (itemQty.text.toString() == "1") {
                    return@setOnClickListener
                }
                itemQty.text = (itemQty.text.toString().toInt() - 1).toString()
            }
            btnInc.setOnClickListener {
                itemQty.text = (itemQty.text.toString().toInt() + 1).toString()
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CartRecyclerAdapter.CartViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.cart_item, parent, false)
        return CartViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CartRecyclerAdapter.CartViewHolder, position: Int) {
        val currentItem = cartItemsList[position]
        holder.itemImage.setImageResource(currentItem.cartItemImage)
        holder.itemName.text = currentItem.cartItemName
        holder.itemPrice.text = "P${currentItem.cartItemPrice}.00"
        holder.itemQty.text = currentItem.cartItemQty.toString()
    }

    override fun getItemCount(): Int {
        return cartItemsList.size
    }
}