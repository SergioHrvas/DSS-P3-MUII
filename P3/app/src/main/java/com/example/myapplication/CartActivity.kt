package com.example.myapplication

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CartActivity : ComponentActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var cartAdapter: CartAdapter // Usamos CartAdapter en lugar de ProductAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        // Configurar RecyclerView
        recyclerView = findViewById(R.id.recyclerViewCart)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Llamar al método para obtener los productos del carrito
        fetchCartFromApi()
    }

    private fun fetchCartFromApi() {
        val apiService = ApiClient.createService(ApiService::class.java)

        apiService.getCartProducts().enqueue(object : Callback<List<CartProduct>> {
            override fun onResponse(
                call: Call<List<CartProduct>>,
                response: Response<List<CartProduct>>
            ) {
                if (response.isSuccessful) {
                    val data = response.body()
                    data?.let { cartProductList ->
                        Log.v("API_RESPONSE", "$cartProductList")
                        // Configurar el adaptador con los datos recibidos
                        cartAdapter = CartAdapter(cartProductList)
                        recyclerView.adapter = cartAdapter
                    }
                } else {
                    Log.e("API_RESPONSE", "Error: ${response.code()} - ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<CartProduct>>, t: Throwable) {
                Log.e("API_ERROR", "Failure: ${t.message}")
            }
        })
    }
}
