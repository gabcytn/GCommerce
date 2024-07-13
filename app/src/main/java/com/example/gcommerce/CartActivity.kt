package com.example.gcommerce

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CartActivity : AppCompatActivity() {
    private lateinit var recyclerView : RecyclerView
    private lateinit var cartItemsList : ArrayList<CartItem>
    private lateinit var tvEmptyCart : TextView
    private lateinit var btnCheckout : Button
    private lateinit var cartAdapter : CartRecyclerAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        tvEmptyCart = findViewById(R.id.tvEmptyCart)
        btnCheckout = findViewById(R.id.btnCheckout)
        val buyer = intent.getStringExtra("buyer_name")!!
        findViewById<TextView>(R.id.tvCartTitle).text = "${buyer}'s cart"
        findViewById<ImageView>(R.id.ivArrowLeft).setOnClickListener { finish() }

        recyclerView = findViewById(R.id.rvCart)
        recyclerView.layoutManager = LinearLayoutManager(this)

        cartItemsList = arrayListOf()
        getCartData(buyer)

        cartAdapter = CartRecyclerAdapter(cartItemsList, this, buyer, this)
        recyclerView.adapter = cartAdapter

    }

    private fun getCartData(buyer: String) {
        val db = DBHandler(this)
        val result = db.getCartItems(buyer)
        if (result.isEmpty()) {
            tvEmptyCart.text = "Your cart is empty!"
            btnCheckout.visibility = View.GONE
            return
        }
        btnCheckout.visibility = View.VISIBLE
        for (i in result.indices) {
            val img = result[i].itemImage
            val name = result[i].itemName
            val price = result[i].itemPrice
            val item = CartItem(img, name, price, 1)

            cartItemsList.add(item)
        }
    }

    fun updateUIonDelete() {
        tvEmptyCart.text = "Your cart is empty!"
        btnCheckout.visibility = View.GONE
    }

}