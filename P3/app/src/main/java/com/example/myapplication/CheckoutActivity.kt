package com.example.myapplication

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CheckoutActivity : AppCompatActivity() {

    private val PREFS_NAME = "CartPrefs"
    private val CART_KEY = "cart_items"

    private lateinit var textViewSummary: TextView
    private lateinit var textViewTotal: TextView
    private lateinit var buttonConfirm: Button
    private lateinit var progressBar: ProgressBar

    private var cartItems = mutableListOf<CartProduct>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)

        textViewSummary = findViewById(R.id.textViewSummary)
        textViewTotal = findViewById(R.id.textViewTotal)
        buttonConfirm = findViewById(R.id.buttonConfirm)
        progressBar = findViewById(R.id.progressBar)

        loadCartItems()
        displayCartInfo()

        buttonConfirm.setOnClickListener {
            placeOrder()
        }
    }

    private fun loadCartItems() {
        val sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val cartJson = sharedPreferences.getString(CART_KEY, "[]")
        cartItems = Gson().fromJson(cartJson, object : TypeToken<MutableList<Product>>() {}.type)
    }

    private fun displayCartInfo() {
        if (cartItems.isEmpty()) {
            textViewSummary.text = "No hay productos en el carrito."
            textViewTotal.text = "Total: $0.00"
            buttonConfirm.isEnabled = false
        } else {
            // Muestra la lista de productos (puedes formatearlos como quieras)
            val summary = cartItems.joinToString("\n") { "${it.name} - $${it.price}" }
            textViewSummary.text = summary

            val totalPrice = cartItems.sumOf { it.price }
            textViewTotal.text = "Total: $${"%.2f".format(totalPrice)}"
            buttonConfirm.isEnabled = true
        }
    }

    private fun placeOrder() {
        if (cartItems.isEmpty()) {
            Toast.makeText(this, "El carrito está vacío", Toast.LENGTH_SHORT).show()
            return
        }

        // Crea el objeto OrderRequest
        val totalPrice = cartItems.sumOf { it.price }
        val orderRequest = OrderRequest(
            products = cartItems,
            totalAmount = totalPrice
        )

        showLoading(true)

        RetrofitClient.apiService.createOrder(orderRequest).enqueue(object : Callback<OrderResponse> {
            override fun onResponse(call: Call<OrderResponse>, response: Response<OrderResponse>) {
                showLoading(false)
                if (response.isSuccessful) {
                    val orderResponse = response.body()
                    if (orderResponse?.status == "true") {
                        // Orden creada con éxito
                        Toast.makeText(this@CheckoutActivity, "Compra realizada con éxito. Orden ID: ${orderResponse.id}", Toast.LENGTH_LONG).show()
                        clearCart()
                        finish() // o redirige a otra pantalla
                    } else {
                        // Hubo algún error lógico en la creación de la orden
                        Toast.makeText(this@CheckoutActivity, "Error al procesar la compra: ${orderResponse?.message ?: "Desconocido"}", Toast.LENGTH_LONG).show()
                    }
                } else {
                    // Error HTTP (404, 500, etc.)
                    Toast.makeText(this@CheckoutActivity, "Error del servidor: ${response.code()}", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<OrderResponse>, t: Throwable) {
                showLoading(false)
                Toast.makeText(this@CheckoutActivity, "Error de red: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun clearCart() {
        val sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(CART_KEY, "[]")
        editor.apply()
    }

    private fun showLoading(loading: Boolean) {
        if (loading) {
            progressBar.visibility = View.VISIBLE
            buttonConfirm.isEnabled = false
        } else {
            progressBar.visibility = View.GONE
            buttonConfirm.isEnabled = true
        }
    }
}