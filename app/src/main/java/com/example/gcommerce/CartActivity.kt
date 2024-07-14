package com.example.gcommerce

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toDrawable
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CartActivity : AppCompatActivity() {
    private lateinit var recyclerView : RecyclerView
    private lateinit var cartItemsList : ArrayList<CartItem>
    private lateinit var tvEmptyCart : TextView
    private lateinit var tvTotal : TextView
    private lateinit var tvTotalAmount : TextView
    private lateinit var btnCheckout : Button
    private lateinit var cartAdapter : CartRecyclerAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        tvEmptyCart = findViewById(R.id.tvEmptyCart)
        btnCheckout = findViewById(R.id.btnCheckout)
        tvTotal = findViewById(R.id.tvTotal)
        tvTotalAmount = findViewById(R.id.tvTotalAmount)


        val buyer = intent.getStringExtra("buyer_name")!!
        findViewById<TextView>(R.id.tvCartTitle).text = "${buyer}'s cart"
        findViewById<ImageView>(R.id.ivArrowLeft).setOnClickListener { finish() }
        btnCheckout.setOnClickListener {
            val db = DBHandler(this)

            if (db.getCartItems(buyer).size != 0) {
                Toast.makeText(this, "Checkout complete!", Toast.LENGTH_SHORT).show()
            }
            // Insert into purchase history table
            db.onCheckout(buyer)
            db.deleteAll(buyer)
            cartAdapter.onCheckout()
            updateUIonDelete()
        }

        val totalAmount = DBHandler(this).getTotalAmount(buyer)
        tvTotalAmount.text = "P${totalAmount}.00"

        // RecyclerView initializations
        recyclerView = findViewById(R.id.rvCart)
        recyclerView.layoutManager = LinearLayoutManager(this)

        cartItemsList = arrayListOf()
        getCartData(buyer)

        cartAdapter = CartRecyclerAdapter(cartItemsList, this, buyer, this)
        recyclerView.adapter = cartAdapter

    }

    fun updateTotalAmount(amt: Int) {
        if (amt == 0) {
            tvTotalAmount.text = ""
            return
        }
        tvTotalAmount.text = "P${amt}.00"
    }

    private fun getCartData(buyer: String) {
        val db = DBHandler(this)
        val result = db.getCartItems(buyer)
        if (result.isEmpty()) {
            tvEmptyCart.text = "Your cart is empty!"
            btnCheckout.setBackgroundResource(R.drawable.btn_disabled)
            tvTotal.text = ""
            tvTotalAmount.text = ""
            return
        }
        btnCheckout.foreground = android.R.attr.selectable.toDrawable()
        btnCheckout.setBackgroundResource(R.drawable.btn_signin_bg)
        for (i in result.indices) {
            val img = result[i].itemImage
            val name = result[i].itemName
            val price = result[i].itemPrice
            val qty = db.getQuantity(buyer, name)

            val item = CartItem(img, name, price, qty!!)

            cartItemsList.add(item)
        }
    }

    fun updateUIonDelete() {
        tvEmptyCart.text = "Your cart is empty!"
        btnCheckout.setBackgroundResource(R.drawable.btn_disabled)
        btnCheckout.foreground = null
        tvTotal.text = ""
        tvTotalAmount.text = ""
    }

}