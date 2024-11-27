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
import android.widget.Button
import android.util.Log;
import android.widget.TextView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import androidx.appcompat.app.AppCompatActivity


class MainActivity : ComponentActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var productAdapter: ProductAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



// Configurar RecyclerView
recyclerView = findViewById(R.id.recyclerViewProducts)
val headerTitle = findViewById<TextView>(R.id.headerTitle)
headerTitle.text = "${headerTitle.text} - Lista de productos"
recyclerView.layoutManager = LinearLayoutManager(this)

// Configurar adaptador con datos de muestra o datos de la API
productAdapter = ProductAdapter(sampleProducts)
recyclerView.adapter = productAdapter

// Botón para ir al carrito
val buttonGoToCart = findViewById<Button>(R.id.buttonGoToCart)
buttonGoToCart.setOnClickListener {
    val intent = Intent(this, CartActivity::class.java)
    startActivity(intent)
}

// FloatingActionButton para añadir productos
val fab: FloatingActionButton = findViewById(R.id.fabAddProduct)
fab.setOnClickListener {
    val intent = Intent(this, NewProductActivity::class.java)
    startActivity(intent)
}

// Llamar al método para obtener los productos
private fun fetchProductsFromApi() {
    val apiService = ApiClient.createService(ApiService::class.java)

    apiService.getAllProducts().enqueue(object : Callback<List<Product>> {
        override fun onResponse(
            call: Call<List<Product>>,
            response: Response<List<Product>>
        ) {
            Log.v("API_RESPONSE", "$response")

            if (response.isSuccessful) {
                val data = response.body()
                Log.v("API_RESPONSE", "${data}")

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