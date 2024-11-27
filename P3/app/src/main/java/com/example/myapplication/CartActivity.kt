package com.example.myapplication
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.ui.theme.MyApplicationTheme
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.example.myapplication.ApiService;
import android.util.Log;
import android.widget.Button
import android.widget.TextView

class CartActivity : ComponentActivity() {
    private lateinit var recyclerView: RecyclerView
    //private lateinit var cartAdapter: CartAdapter
    private lateinit var productAdapter: ProductAdapter

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

        apiService.getCartProducts().enqueue(object : Callback<List<Product>> {
            override fun onResponse(
                call: Call<List<Product>>,
                response: Response<List<Product>>
            ) {
                if (response.isSuccessful) {
                    val data = response.body()
                    data?.let { productList ->
                        Log.v("API_RESPONSE", "${productList}")
                        // Actualizar el RecyclerView con los datos recibidos
                        productAdapter = ProductAdapter(productList)
                        recyclerView.adapter = productAdapter
                    }
                } else {
                    Log.e("API_RESPONSE", "Error: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<Product>>, t: Throwable) {
                Log.e("API_ERROR", "Failure: ${t.message}")
            }
        })
    }
}
