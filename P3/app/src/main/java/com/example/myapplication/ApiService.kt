package com.example.myapplication
import android.view.PixelCopy.Request
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Multipart
import retrofit2.http.Part

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
    @Multipart
    @POST("/api/admin/save_product")
    fun createProduct(
        @Part product: MultipartBody.Part, // No debe tener el nombre aquí
        @Part file: MultipartBody.Part
    ): Call<Product>

    @DELETE("api/admin/delete_product/{id}")
    fun deleteProduct(
        @Path("id") id: Long
    ): Call<Boolean>

    @POST("api/admin/cart/pay/")
    fun checkout(): Call<String>

    data class LoginResponse(
        val sessionId: String,
        val role: String
    )

    @POST("/api/login")
    fun loginUser(
        @Body request: User
    ): Call<LoginResponse>

}