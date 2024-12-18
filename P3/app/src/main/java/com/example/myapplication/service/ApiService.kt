package com.example.myapplication.service

import com.example.myapplication.model.Product
import com.example.myapplication.model.User
import okhttp3.MultipartBody
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.Part

interface ApiService {
    // Get all products
    // Devolvemos en GSON para poder usar RecyclerView //TODO GET POST PATH DELETE ADD

    @GET("/api/products")
    fun getAllProducts(): Call<List<Product>>

    // Add a product (POST request example)
    @Multipart
    @POST("/api/admin/save_product")
    fun createProduct(
        @Part product: MultipartBody.Part, @Part file: MultipartBody.Part
    ): Call<Product>

    @DELETE("api/admin/delete_product/{id}")
    fun deleteProduct(
        @Path("id") id: Long
    ): Call<Boolean>

    @POST("/api/orders")
    fun createOrder(@Body orderRequest: OrderRequest): Call<OrderResponse>

    @POST("/api/login")
    fun loginUser(
        @Body request: User
    ): Call<LoginResponse>

    @GET("verify-session")
    fun verifySession(@Header("Cookie") sessionId: String): Call<SessionVerificationResponse>


}