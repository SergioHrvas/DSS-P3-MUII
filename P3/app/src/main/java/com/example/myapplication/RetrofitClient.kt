package com.example.myapplication

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private var retrofit: Retrofit? = null

    fun getClient(baseUrl: String): Retrofit {
        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                .baseUrl(baseUrl) // URL base de la API
                .addConverterFactory(GsonConverterFactory.create()) // Conversor de Gson
                .build()
        }
        return retrofit!!
    }
}
