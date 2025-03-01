package com.example.myapplication
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import com.example.myapplication.Product
import retrofit2.Call

interface ApiService {
    // Get all products
    // Devolvemos en GSON para poder usar RecyclerView //TODO GET POST PATH DELETE ADD

    @GET("/api/products")
    fun getAllProducts(): Call<List<Product>>

    //He planteado que no hace falta un objeto cart puesto que en el fondo
    // es el mismo concepto que products solo que la disposición futura en la UI
    //será distinta
    @GET("/api/cart")
    fun getCartProducts(): Call<List<Product>>

    // Add a product (POST request example)
    /*@POST("/products/add")
    fun addProduct(
        @Query("name") name: String,
        @Query("price") price: Double
    ): Call<Void>
     */

}