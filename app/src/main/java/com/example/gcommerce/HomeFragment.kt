package com.example.gcommerce

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class HomeFragment (private val displayName: String?, val email: String?) : Fragment () {
    private lateinit var tvGreetingText : TextView
    private lateinit var cvAddToCart : CardView
    private lateinit var shopItemsList : ArrayList<ShopItem>
    private lateinit var recyclerView : RecyclerView
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        tvGreetingText = view.findViewById(R.id.tvGreetingText)
        cvAddToCart = view.findViewById(R.id.cvShoppingCart)

        tvGreetingText.text = "For ${displayName}"
        cvAddToCart.setOnClickListener {
            val intent = Intent(context, CartActivity::class.java)
            intent.putExtra("buyer_name", displayName)
            startActivity(intent)
        }

        recyclerView = view.findViewById(R.id.rvMainShop)
        recyclerView.layoutManager = GridLayoutManager(context, 2)

        shopItemsList = arrayListOf()
        getUserData()

        recyclerView.adapter = ShopRecyclerAdapter(shopItemsList, requireContext(), displayName!!)

        return view
    }

    private fun getUserData() {
        val itemNames = listOf("One piece", "Jujutsu Kaisen", "Naruto", "Baki", "Solo Leveling", "Jojo's", "Black Clover", "Hunter x Hunter", "Vinland Saga", "Death Note")
        val itemPrices = listOf(300, 320, 250, 280, 250, 200, 200, 300, 280, 250)
        val itemImages = listOf(R.drawable.item_1, R.drawable.item_2, R.drawable.item_3, R.drawable.item_4, R.drawable.item_5, R.drawable.item_6, R.drawable.item_7, R.drawable.item_8, R.drawable.item_9, R.drawable.item_10)

        for (i in itemImages.indices) {
            shopItemsList.add(ShopItem(itemImages[i], itemNames[i], itemPrices[i], false))
        }
    }

}