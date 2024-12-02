package com.example.myapplication
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import com.example.myapplication.Product
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE

interface ApiService {
    // Get all products
    // Devolvemos en GSON para poder usar RecyclerView //TODO GET POST PATH DELETE ADD

    @GET("/api/products")
    fun getAllProducts(): Call<List<Product>>

    //He planteado que no hace falta un objeto cart puesto que en el fondo
    // es el mismo concepto que products solo que la disposición futura en la UI
    //será distinta
    @GET("/api/cart")
    fun getCartProducts(): Call<List<CartProduct>>

    // Add a product (POST request example)
    @POST("/api/admin/save_product")
    fun createProduct(
        @Body product: Product
    ): Call<Product>

    @DELETE("api/admin/delete_product/{id}")
    fun deleteProduct(
        @Path("id") id: Long
    ): Call<Boolean>

    @POST("api/admin/cart/pay/")
    fun checkout(): Call<String>
}