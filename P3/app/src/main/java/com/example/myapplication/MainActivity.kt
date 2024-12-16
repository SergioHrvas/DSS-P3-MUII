package com.example.myapplication

import android.app.ActionBar
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Window
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.Cookie
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var productAdapter: ProductAdapter
    private val ADD_PRODUCT_REQUEST_CODE = 1
    private lateinit var productList: MutableList<Product>

    private lateinit var sharedPrefs: SharedPreferences;
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var drawerToggle: ActionBarDrawerToggle

    fun verifySessionWithServerAsync(sessionId: String?, callback: (Boolean) -> Unit) {
        val apiService = RetrofitClient.apiService
        Log.v("API_RESPONSE", "$sessionId")

        if (sessionId.isNullOrEmpty()) {
            callback(false)
            return
        }

        val call = apiService.verifySession(sessionId)
        Log.v("API_RESPONSE", "AAAAA")
        call.enqueue(object : Callback<ApiService.SessionVerificationResponse> {
            override fun onResponse(
                call: Call<ApiService.SessionVerificationResponse>,
                response: Response<ApiService.SessionVerificationResponse>
            ) {
                Log.v("API_RESPONSE", "<< ${response.body()?.valid}")
                callback(response.body()?.valid ?: false)
            }

            override fun onFailure(call: Call<ApiService.SessionVerificationResponse>, t: Throwable) {
                Log.v("API_RESPONSE", "<< ERROR")
                callback(false)

            }
        })
    }

    fun checkSessionAsync(context: Context) {
        val sessionId = sharedPrefs.getString("JSESSIONID", null)

        // Llamamos a la función asincrónica para verificar la sesión
        verifySessionWithServerAsync(sessionId) { isValid ->
            val fab: FloatingActionButton = findViewById(R.id.fabAddProduct)
            val navigationView: NavigationView = findViewById(R.id.navigation_view)
            val menu = navigationView.menu
            val menu_login = menu.findItem(R.id.menu_login)
            val menu_logout = menu.findItem(R.id.menu_logout)

            // Este bloque de código se ejecuta cuando la verificación de la sesión termine
            if (isValid) {
                // Si la sesión es válida, podemos seguir mostrando las partes de la app para admin
                val role = sharedPrefs.getString("ROLE", null)
                menu_login.isVisible = false
                menu_logout.isVisible = true

                Log.v("MENU_LOGOUT2", "${menu_logout.isVisible}")
                Log.v("MENU_LOGIN2                    Log.v(\"MENU_LOGOUT1\", \"${menu_logout.isVisible}\")\n" +
                        "                    Log.v(\"MENU_LOGIN1\", \"${menu_login.isVisible}\")\n", "${menu_login.isVisible}")

                if(role == "ROLE_ADMIN"){

                    fab.show()
                    // FloatingActionButton para añadir productos
                    fab.setOnClickListener {
                        val intent = Intent(this, NewProductActivity::class.java)
                        //addProductLauncher.launch(intent)
                        startActivityForResult(intent, 1);
                    }
                }
                else{

                    fab.hide()
                }
            } else {
                // Si la sesión no es válida, eliminamos ROLE_ADMIN de SharedPreferences
                menu_login.isVisible = true
                menu_logout.isVisible = false
                Log.v("MENU_LOGOUT3", "${menu_logout.isVisible}")
                Log.v("MENU_LOGIN3", "${menu_login.isVisible}")

                sharedPrefs.edit().putString("ROLE", "").apply()



            }
        }
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ADD_PRODUCT_REQUEST_CODE && resultCode == RESULT_OK) {
            val productSaved = data?.getBooleanExtra("product_saved", false) ?: false
            Log.d("DEBUG_FLOW", "Product saved: $productSaved")
            if (productSaved) {
                Log.d("DEBUG_FLOW", "Fetching products from API...")
                fetchProductsFromApi()
            }
        }
    }
    var cookieManager = android.webkit.CookieManager.getInstance()

    // Función asincrónica que simula obtener el rol de SharedPreferences
    fun getRoleFromSharedPreferences(callback: (String?) -> Unit) {
        // Simula un retraso para simular asincronía
        Handler(Looper.getMainLooper()).postDelayed({
            val roleValue = sharedPrefs.getString("ROLE", null)
            callback(roleValue)
        }, 500) // Simula un retraso de 500ms
    }

    override fun onResume() {
        super.onResume()
        // Refrescar la lista de productos
        Handler(Looper.getMainLooper()).postDelayed({
            fetchProductsFromApi()
            checkSessionAsync(this)
        }, 1000) // Retraso de 1 segundo    }

        val roleValue = sharedPrefs.getString("ROLE", null)
        val fab: FloatingActionButton = findViewById(R.id.fabAddProduct)

        if(roleValue == "ROLE_ADMIN"){
            fab.show()
            // FloatingActionButton para añadir productos
            fab.setOnClickListener {
                val intent = Intent(this, NewProductActivity::class.java)
                //addProductLauncher.launch(intent)
                startActivityForResult(intent, 1);
            }
        }
        else{
            fab.hide()
        }

        invalidateOptionsMenu()  // Esto actualiza la vista del menú
    }

    fun loadCookieToRetrofit(context: Context) {
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


    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Establecer el layout
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)

        setContentView(R.layout.activity_main)
        sharedPrefs = getSharedPreferences("AppCookies", Context.MODE_PRIVATE)

        // Cargar cookie en Retrofit
        loadCookieToRetrofit(this)

        checkSessionAsync(this)

        // Configurar el DrawerLayout y la Toolbar
        drawerLayout = findViewById(R.id.drawer_layout)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        // Configurar el ActionBarDrawerToggle
        drawerToggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )

        // Sincronizar el estado del Drawer con el ActionBarDrawerToggle
        drawerLayout.addDrawerListener(drawerToggle)
        drawerToggle.syncState()


        // Configurar NavigationView
        val navigationView: NavigationView = findViewById(R.id.navigation_view)
        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_map -> {
                    // Navegar al mapa
                    val intent = Intent(this, MapActivity::class.java) // Cambia MapActivity por el nombre de tu actividad
                    startActivity(intent)
                }
                R.id.menu_cart -> {
                    // Navegar al carrito
                    val intent = Intent(this, CartActivity::class.java) // Cambia CartActivity por el nombre de tu actividad
                    startActivity(intent)
                }
                R.id.menu_login -> {
                    // Navegar al login
                    val intent = Intent(this, LoginActivity::class.java) // Cambia LoginActivity por el nombre de tu actividad
                    startActivity(intent)
                }
                R.id.menu_logout -> {
                    sharedPrefs.edit().putString("ROLE", "").apply()
                    sharedPrefs.edit().putString("JSESSIONID", "").apply()

                    // Obtener el NavigationView y su menú
                    val navigationViewS: NavigationView = findViewById(R.id.navigation_view)
                    val menuS = navigationViewS.menu
                    val menu_loginS = menuS.findItem(R.id.menu_login)
                    val menu_logoutS = menuS.findItem(R.id.menu_logout)

                    // Cambiar visibilidad
                    menu_logoutS.isVisible = false
                    menu_loginS.isVisible = true


                    val fab: FloatingActionButton = findViewById(R.id.fabAddProduct)
                    fab.hide()
                    finish();
                    startActivity(getIntent());
                }
            }
            // Cerrar el Drawer
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }



        // Configurar ActionBarDrawerToggle
        val toggle = androidx.appcompat.app.ActionBarDrawerToggle(
            this, drawerLayout, toolbar,
            R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        productList = mutableListOf()
        productAdapter = ProductAdapter(
            this,
            productList,
            onDeleteClick = { product -> deleteProductFromApi(product) },
            onAddToCartClick = { product -> addProductToCart(product) }
        )


        // Vincula el RecyclerView
        recyclerView = findViewById(R.id.recyclerViewProducts)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = productAdapter

        // Guardar la cookie en SharedPreferences
        val sharedPrefs = getSharedPreferences("AppCookies", Context.MODE_PRIVATE)
        val cookieValue = sharedPrefs.getString("JSESSIONID", null)

        val editor = sharedPrefs.edit()

        editor.putString("JSESSIONID", cookieValue)
        editor.apply()

        // Configurar RecyclerView
        recyclerView = findViewById(R.id.recyclerViewProducts)
        val headerTitle = findViewById<TextView>(R.id.headerTitle)

        recyclerView.layoutManager = LinearLayoutManager(this)


        val fab: FloatingActionButton = findViewById(R.id.fabAddProduct)
        val menu = navigationView.menu

        // Llama a la función que obtiene el rol de manera asincrónica
        getRoleFromSharedPreferences { roleValue ->
            if (roleValue == "ROLE_ADMIN") {
                fab.show()
                fab.setOnClickListener {
                    val intent = Intent(this, NewProductActivity::class.java)
                    startActivityForResult(intent, ADD_PRODUCT_REQUEST_CODE)

                }
            } else {
                fab.hide()
            }
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
        Log.d("API_RESPONSE", "FETCHING PRODUCTS FROM API")

        // Cargar productos (ejemplo)
        RetrofitClient.apiService.getAllProducts().enqueue(object : Callback<List<Product>> {
            override fun onResponse(call: Call<List<Product>>, response: Response<List<Product>>) {
                if (response.isSuccessful) {
                    response.body()?.let { fetchedProducts ->

                        if (!recyclerView.isComputingLayout) {
                            productList.clear()
                            productList.addAll(fetchedProducts)
                            productAdapter.notifyDataSetChanged()
                        } else {
                            recyclerView.post {
                                productList.clear()
                                productList.addAll(fetchedProducts)
                                productAdapter.notifyDataSetChanged()
                            }
                        }

                        Log.d("API_RESPONSE", "Productos cargados con éxito (${response.code()})")

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