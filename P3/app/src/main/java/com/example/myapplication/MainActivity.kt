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

import com.example.myapplication.ApiService;
<<<<<<< Updated upstream
=======
import android.util.Log;
import android.widget.Button
import android.widget.TextView
>>>>>>> Stashed changes

class MainActivity : ComponentActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var productAdapter: ProductAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main) // Asegúrate de usar tu archivo XML

<<<<<<< Updated upstream
        // Set up RecyclerView
        recyclerView = findViewById(R.id.
        recyclerViewProducts)
        recyclerView.layoutManager = LinearLayoutManager(this)

        productAdapter = ProductAdapter(sampleProducts)
        recyclerView.adapter = productAdapter
=======
        // Configurar RecyclerView
        recyclerView = findViewById(R.id.recyclerViewProducts)
        //val headerTitle = findViewById<TextView>(R.id.headerTitle)
        //headerTitle.text = "${headerTitle.text} - Lista de productos"
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Botón para ir al carrito
        val buttonGoToCart = findViewById<Button>(R.id.buttonGoToCart)
        buttonGoToCart.setOnClickListener {
            val intent = Intent(this, CartActivity::class.java)
            startActivity(intent)
        }

        // Llamar al método para obtener los productos
        fetchProductsFromApi()
    }


    private fun fetchProductsFromApi() {
        val apiService = ApiClient.createService(ApiService::class.java)
>>>>>>> Stashed changes

        // Llamada a la API
        apiService.getAllProducts().enqueue(object : Callback<List<Product>> {
            override fun onResponse(call: Call<List<Product>>, response: Response<List<Product>>) {
                if (response.isSuccessful) {
                    val productList = response.body()
                    productList?.let {
                        // Actualizar el adaptador con los datos recibidos
                        productAdapter = ProductAdapter(it)
                        recyclerView.adapter = productAdapter
                    }
                } else {
                    Log.e("API_ERROR", "Response not successful: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<Product>>, t: Throwable) {
                // Manejar el error de la llamada
                Log.e("API_ERROR", "Failure: ${t.message}")
            }
        })
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyApplicationTheme {
        Greeting("Android")
    }
}