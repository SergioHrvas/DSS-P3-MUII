package com.example.myapplication

import android.content.Context
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
import androidx.drawerlayout.widget.DrawerLayout
import androidx.preference.PreferenceManager
import okhttp3.Cookie
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import org.osmdroid.config.Configuration
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

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

    var cookieManager = android.webkit.CookieManager.getInstance()

    override fun onStart() {
        // Cargar cookie en Retrofit
        loadCookieToRetrofit(this)

        fetchProductsFromApi()
        super.onStart()

    }

    fun loadCookieToRetrofit(context: Context) {
        val sharedPrefs = context.getSharedPreferences("AppCookies", Context.MODE_PRIVATE)
        val cookieValue = sharedPrefs.getString("JSESSIONID", null)

        if (cookieValue != null) {
            val cookie = Cookie.Builder()
                .name("JSESSIONID")
                .value(cookieValue)
                .domain("192.168.1.143")
                .path("/")
                .build()
            RetrofitClient.cookieJar.saveFromResponse(
                Constants.SERVER_URL.toHttpUrlOrNull()!!,
                listOf(cookie)
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Establecer el layout
        setContentView(R.layout.activity_main)
        // Cargar cookie en Retrofit
        loadCookieToRetrofit(this)

        // Guardar la cookie en SharedPreferences
        val sharedPrefs = getSharedPreferences("AppCookies", Context.MODE_PRIVATE)
        val cookieValue = sharedPrefs.getString("JSESSIONID", null)

        val editor = sharedPrefs.edit()

        editor.putString("JSESSIONID", cookieValue)
        editor.apply()

        if (cookieValue.isNullOrEmpty()) {
            // Redirigir al Login si no hay cookie
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()  // Finaliza la actividad actual
            return
        } else {
            // Proceder con la actividad principal
            println("Cookie encontrada: $cookieValue")
        }


        // Configurar RecyclerView
        recyclerView = findViewById(R.id.recyclerViewProducts)
        val headerTitle = findViewById<TextView>(R.id.headerTitle)
        headerTitle.text = "${headerTitle.text} - Lista de productos"  // Asegúrate de que headerTitle existe en el layout

        recyclerView.layoutManager = LinearLayoutManager(this)

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

        fetchProductsFromApi() // Llama a la API después de la configuración
    }

    // Definimos las constantes de las preferencias y claves
    private val PREFS_NAME = "CartPrefs"
    private val CART_KEY = "cart_items"

    // Dentro de fetchProductsFromApi(), al crear el adapter, agregamos el callback onAddToCartClick
    private fun fetchProductsFromApi() {
        // Este code snippet asume que ya obtuviste la lista productList de la API o de donde quieras.
        // Por simplicidad asume que ya la tienes.

        // Cargar productos (ejemplo)
        RetrofitClient.apiService.getAllProducts().enqueue(object : Callback<List<Product>> {
            override fun onResponse(call: Call<List<Product>>, response: Response<List<Product>>) {
                if (response.isSuccessful) {
                    response.body()?.let { productList ->

                        // Ahora el adapter incluye el callback onAddToCartClick
                        productAdapter = ProductAdapter(
                            this@MainActivity,
                            productList.toMutableList(),
                            onDeleteClick = { product ->
                                deleteProductFromApi(product)
                            },
                            onAddToCartClick = { product ->
                                addProductToCart(product)
                            }
                        )
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

    private fun addProductToCart(product: Product) {
        val sharedPrefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPrefs.getString(CART_KEY, null)

        val type = object : TypeToken<MutableList<Product>>() {}.type
        val currentCart: MutableList<Product> = if (json != null) {
            gson.fromJson(json, type)
        } else {
            mutableListOf()
        }

        currentCart.add(product)

        val editor = sharedPrefs.edit()
        editor.putString(CART_KEY, gson.toJson(currentCart))
        editor.apply()

        Log.d("CART", "Producto ${product.name} agregado al carrito.")
    }

    private fun deleteProductFromApi(product: Product) {
        RetrofitClient.apiService.deleteProduct(product.id).enqueue(object : Callback<Boolean> {
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