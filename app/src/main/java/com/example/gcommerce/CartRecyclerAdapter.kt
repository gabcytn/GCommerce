package com.example.gcommerce

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

class CartRecyclerAdapter(private var cartItemsList: ArrayList<CartItem>, val context: Context, val buyer: String, val activity: CartActivity) : RecyclerView.Adapter<CartRecyclerAdapter.CartViewHolder> () {
    class CartViewHolder (itemView: View, context: Context, cartItems: ArrayList<CartItem>, buyer: String) : RecyclerView.ViewHolder(itemView){
        val itemImage: ImageView = itemView.findViewById(R.id.ivCartItem)
        val itemName: TextView = itemView.findViewById(R.id.tvCartItemName)
        val itemPrice: TextView = itemView.findViewById(R.id.tvCartItemPrice)
        val itemQty: TextView = itemView.findViewById(R.id.tvQuantity)

        val btnInc: CardView = itemView.findViewById(R.id.cvPlusQty)
        val btnDec: CardView = itemView.findViewById(R.id.cvMinusQty)
        val btnRemove: ImageView = itemView.findViewById(R.id.ivDeleteItem)

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CartRecyclerAdapter.CartViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.cart_item, parent, false)
        return CartViewHolder(itemView, context, cartItemsList, buyer)
    }

    override fun onBindViewHolder(holder: CartRecyclerAdapter.CartViewHolder, position: Int) {
        val currentItem = cartItemsList[position]
        holder.itemImage.setImageResource(currentItem.cartItemImage)
        holder.itemName.text = currentItem.cartItemName
        holder.itemPrice.text = "P${currentItem.cartItemPrice}.00"
        holder.itemQty.text = currentItem.cartItemQty.toString()

        holder.btnRemove.setOnClickListener {
            val db = DBHandler(context)
            val currItem = cartItemsList[position]
            db.deleteCartItem(currItem.cartItemName)

            cartItemsList.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, cartItemsList.size)

            if (cartItemsList.isEmpty()) {
                activity.updateUIonDelete()
            }

            Toast.makeText(context, "${currItem.cartItemName} removed", Toast.LENGTH_SHORT).show()

            val amt = db.getTotalAmount(buyer)
            activity.updateTotalAmount(amt)

        }

        val db = DBHandler(context)
        holder.btnDec.setOnClickListener {
            if (holder.itemQty.text.toString() == "1") {
                return@setOnClickListener
            }
            val newCount = holder.itemQty.text.toString().toInt() - 1
            holder.itemQty.text = newCount.toString()

            currentItem.cartItemQty -= 1

            // Update database
            db.updateCartItemQuantity(newCount, currentItem.cartItemName)
            val amt = db.getTotalAmount(buyer)
            activity.updateTotalAmount(amt)
        }

        holder.btnInc.setOnClickListener {
            val newCount = holder.itemQty.text.toString().toInt() + 1
            holder.itemQty.text = newCount.toString()

            currentItem.cartItemQty += 1

            // Update database
            db.updateCartItemQuantity(newCount, currentItem.cartItemName)
            val amt = db.getTotalAmount(buyer)
            activity.updateTotalAmount(amt)
        }

    }

    override fun getItemCount(): Int {
        return cartItemsList.size
    }

    fun onCheckout() {
        cartItemsList.clear()
        notifyDataSetChanged()
    }

}