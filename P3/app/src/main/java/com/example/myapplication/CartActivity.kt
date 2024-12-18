package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
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
    private lateinit var buttonBack2: MaterialButton
    private lateinit var buttonCheckout: MaterialButton
    private lateinit var buttonClearCart: MaterialButton

    private var cartItems = mutableListOf<Product>()

    private val PREFS_NAME = "CartPrefs"
    private val CART_KEY = "cart_items"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        // Enlazar vistas
        recyclerView = findViewById(R.id.recyclerViewCart)
        textViewTotalPrice = findViewById(R.id.textViewTotalPrice)
        buttonBack2 = findViewById(R.id.buttonBack2)
        buttonCheckout = findViewById(R.id.buttonCheckout)
        buttonClearCart = findViewById(R.id.buttonClearCart)

        // Encontrar el botón de retroceso
        val buttonBack: ImageButton = findViewById(R.id.buttonBack)

        // Hacer visible el botón de retroceso
        buttonBack.visibility = View.VISIBLE

        // Configurar la acción de retroceso
        buttonBack.setOnClickListener {
            onBackPressed()  // Llamada para volver a la actividad anterior
        }

        // Configurar RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        cartAdapter = CartAdapter(cartItems, this::removeFromCart)
        recyclerView.adapter = cartAdapter

        // Cargar elementos del carrito la primera vez
        loadCartItems()

        // Botón para regresar
        buttonBack2.setOnClickListener {
            finish()
        }
        buttonClearCart.setOnClickListener {
            clearCart()
        }

        // Botón para proceder al pago
        buttonCheckout.setOnClickListener {
            val intent = Intent(this, CheckoutActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        // Al volver a esta actividad, recargamos el carrito por si se vació en CheckoutActivity
        loadCartItems()
    }

    private fun loadCartItems() {
        val sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
        val cartJson = sharedPreferences.getString(CART_KEY, "[]")
        cartItems = Gson().fromJson(cartJson, object : TypeToken<MutableList<Product>>() {}.type)
        cartAdapter.updateCart(cartItems)
        updateTotalPrice()
    }

    private fun saveCartItems() {
        val sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val cartJson = Gson().toJson(cartItems)
        editor.putString(CART_KEY, cartJson)
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
