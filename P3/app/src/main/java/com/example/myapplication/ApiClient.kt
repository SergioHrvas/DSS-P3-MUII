package com.example.myapplication

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
     //private const val BASE_URL = "http://10.0.2.2:8080/"
    // private const val BASE_URL = "http://192.168.1.136:8080/" //PORTATIL SERGIO
    // private const val BASE_URL = "http://192.168.1.143:8080/" //SOBREMESA SERGIO
    private const val BASE_URL = "http://192.168.81.232:8080/" //PORTATIL SERGIO UGR
    val retrofit: Retrofit = Retrofit.Builder().baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create
            ())
        .build()

    fun <T> createService(service: Class<T>): T {
        return retrofit.create(service)
    }
}