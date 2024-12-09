package com.example.myapplication

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class CartActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var cartAdapter: CartAdapter
    private lateinit var textViewTotalPrice: TextView
    private lateinit var buttonBack: MaterialButton
    private lateinit var buttonCheckout: MaterialButton

    private var cartItems = mutableListOf<Product>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        // Enlazar vistas
        recyclerView = findViewById(R.id.recyclerViewCart)
        textViewTotalPrice = findViewById(R.id.textViewTotalPrice)
        buttonBack = findViewById(R.id.buttonBack)
        buttonCheckout = findViewById(R.id.buttonCheckout)

        // Configurar RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        cartAdapter = CartAdapter(cartItems, this::removeFromCart)
        recyclerView.adapter = cartAdapter

        // Cargar elementos del carrito
        loadCartItems()

        // Botón para regresar
        buttonBack.setOnClickListener {
            finish()
        }

        // Botón para proceder al pago
        buttonCheckout.setOnClickListener {
            // Aquí puedes implementar la lógica para procesar el pago
            clearCart()
        }
    }

    private fun loadCartItems() {
        val sharedPreferences = getSharedPreferences("CartPrefs", MODE_PRIVATE)
        val cartJson = sharedPreferences.getString("cart_items", "[]")
        cartItems = Gson().fromJson(cartJson, object : TypeToken<MutableList<Product>>() {}.type)
        cartAdapter.updateCart(cartItems)
        updateTotalPrice()
    }

    private fun saveCartItems() {
        val sharedPreferences = getSharedPreferences("CartPrefs", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val cartJson = Gson().toJson(cartItems)
        editor.putString("cart_items", cartJson)
        editor.apply()
    }

    private fun removeFromCart(product: Product) {
        cartItems.remove(product)
        saveCartItems()
        cartAdapter.updateCart(cartItems)
        updateTotalPrice()
    }

    private fun clearCart() {
        cartItems.clear()
        saveCartItems()
        cartAdapter.updateCart(cartItems)
        updateTotalPrice()
    }

    private fun updateTotalPrice() {
        val totalPrice = cartItems.sumOf { it.price }
        textViewTotalPrice.text = "Precio final : $${"%.2f".format(totalPrice)}"
    }
}
