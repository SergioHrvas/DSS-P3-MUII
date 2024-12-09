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
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.floatingactionbutton.FloatingActionButton
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import org.osmdroid.config.Configuration

class MainActivity : ComponentActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var productAdapter: ProductAdapter
    private val ADD_PRODUCT_REQUEST_CODE = 1

    private val addProductLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            // Si el resultado es OK, actualizar la lista de productos
            fetchProductsFromApi() // Actualizar la lista de productos
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Configuration.getInstance().load(applicationContext, PreferenceManager.getDefaultSharedPreferences(applicationContext))


        // Configurar RecyclerView
        recyclerView = findViewById(R.id.recyclerViewProducts)
        val headerTitle = findViewById<TextView>(R.id.headerTitle)
        headerTitle.text = "${headerTitle.text} - Lista de productos"
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Configurar adaptador con datos de muestra o datos de la API
        // productAdapter = ProductAdapter(sampleProducts)
        // recyclerView.adapter = productAdapter

        // Botón para ir al carrito
        val buttonGoToCart = findViewById<Button>(R.id.buttonGoToCart)
        buttonGoToCart.setOnClickListener {
            val intent = Intent(this, CartActivity::class.java)
            startActivity(intent)
        }

        // Botón para ir al mapa
        val buttonGoToMap = findViewById<Button>(R.id.buttonGoToMap)
        buttonGoToMap.setOnClickListener {
            val intent = Intent(this, MapActivity::class.java)
            startActivity(intent)
        }

        val buttonLogin = findViewById<Button>(R.id.buttonLogin)
        buttonLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }


        // FloatingActionButton para añadir productos
        val fab: FloatingActionButton = findViewById(R.id.fabAddProduct)
        fab.setOnClickListener {
            val intent = Intent(this, NewProductActivity::class.java)
            addProductLauncher.launch(intent)
        }

        fetchProductsFromApi()

    }


// Llamar al método para obtener los productos
private fun fetchProductsFromApi() {
        val apiService = ApiClient.createService(ApiService::class.java)

        apiService.getAllProducts().enqueue(object : Callback<List<Product>> {
            override fun onResponse(call: Call<List<Product>>, response: Response<List<Product>>) {
                if (response.isSuccessful) {
                    response.body()?.let { productList ->
                        Log.v("IMAGENES", "$productList")

                        // Configuramos el adaptador con la lista de productos y el callback
                        productAdapter = ProductAdapter(this@MainActivity, productList.toMutableList()) { product ->
                            // Aquí pasamos el producto recibido al método deleteProductFromApi
                            deleteProductFromApi(product)
                        }
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

    private fun deleteProductFromApi(product: Product) {
        val apiService = ApiClient.createService(ApiService::class.java)
        apiService.deleteProduct(product.id).enqueue(object : Callback<Boolean> {
            override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                if (response.isSuccessful) {
                    productAdapter.removeProduct(product) // Eliminamos el producto del adaptador
                    Log.d("API_DELETE", "${product.name} eliminado con éxito")
                } else {
                    Log.e("API_DELETE", "Error al eliminar: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<Boolean>, t: Throwable) {
                Log.e("API_DELETE", "Error de red: ${t.message}")
            }
        })
    }

}