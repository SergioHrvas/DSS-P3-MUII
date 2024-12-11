package com.example.myapplication

import android.content.Context
import android.util.Log
import com.example.myapplication.RetrofitClient.cookieJar
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {

    private const val BASE_URL = Constants.SERVER_URL

    // Crear el Retrofit usando el OkHttpClient con el CookieJar
    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    // Crear servicio Retrofit
    fun <T> createService(service: Class<T>): T {
        return retrofit.create(service)
    }



}
