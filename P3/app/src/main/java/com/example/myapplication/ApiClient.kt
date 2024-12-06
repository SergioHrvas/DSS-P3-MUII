package com.example.myapplication

import com.example.myapplication.Constants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {

    private const val BASE_URL = Constants.SERVER_URL

    val retrofit: Retrofit = Retrofit.Builder().baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create
            ())
        .build()

    fun <T> createService(service: Class<T>): T {
        return retrofit.create(service)
    }
}