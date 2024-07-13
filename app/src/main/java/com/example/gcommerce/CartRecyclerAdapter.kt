package com.example.gcommerce

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

class CartRecyclerAdapter(private var cartItemsList: ArrayList<CartItem>, val context: Context, val buyer: String, val activity: CartActivity) : RecyclerView.Adapter<CartRecyclerAdapter.CartViewHolder> () {
    class CartViewHolder (itemView: View, context: Context, cartItems: ArrayList<CartItem>, buyer: String) : RecyclerView.ViewHolder(itemView){
        val itemImage: ImageView = itemView.findViewById(R.id.ivCartItem)
        val itemName: TextView = itemView.findViewById(R.id.tvCartItemName)
        val itemPrice: TextView = itemView.findViewById(R.id.tvCartItemPrice)
        val itemQty: TextView = itemView.findViewById(R.id.tvQuantity)

        private val btnInc: CardView = itemView.findViewById(R.id.cvPlusQty)
        private val btnDec: CardView = itemView.findViewById(R.id.cvMinusQty)
        val btnRemove: ImageView = itemView.findViewById(R.id.ivDeleteItem)

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
            Log.i("MainActivity", "Current item to delete: $currItem")
            db.deleteCartItem(currItem.cartItemName)

            cartItemsList.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, cartItemsList.size)

            if (cartItemsList.isEmpty()) {
                activity.updateUIonDelete()
            }

        }
    }

    override fun getItemCount(): Int {
        return cartItemsList.size
    }

}