package com.example.myapplication

import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {

    private const val BASE_URL = Constants.SERVER_URL

    // Almacén de cookies
    private val cookieStore = mutableMapOf<String, MutableList<Cookie>>()

    // Crear el CookieJar
    private val cookieJar = object : CookieJar {
        override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
            val domain = url.host
            if (cookieStore[domain] == null) {
                cookieStore[domain] = mutableListOf()
            }
            cookieStore[domain]?.addAll(cookies)
            println("Cookies guardadas para $domain: $cookies")
        }

        override fun loadForRequest(url: HttpUrl): List<Cookie> {
            val cookies = cookieStore[url.host] ?: emptyList()
            println("Cookies cargadas para ${url.host}: $cookies")
            return cookies
        }
    }

    // Crear un cliente OkHttp con el CookieJar
    private val okHttpClient = OkHttpClient.Builder()
        .cookieJar(cookieJar)  // Usar el CookieJar
        .build()

    // Crear el Retrofit usando el OkHttpClient con el CookieJar
    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient) // Configura Retrofit con el OkHttpClient
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    // Crear servicio Retrofit
    fun <T> createService(service: Class<T>): T {
        return retrofit.create(service)
    }

    fun printStoredCookies() {
        cookieStore.values.flatten().forEach { cookie ->
            println("Cookie almacenada: ${cookie.name} = ${cookie.value}")
        }
    }
}
