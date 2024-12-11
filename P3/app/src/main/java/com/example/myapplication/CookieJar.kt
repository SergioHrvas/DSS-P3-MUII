package com.example.myapplication

import android.util.Log
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl

class PersistentCookieJar : CookieJar {
    private val cookieStore: MutableMap<String, List<Cookie>> = mutableMapOf()

    override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {

        // Almacenar las cookies asociadas al dominio
        cookieStore[Constants.SERVER_URL] = cookies.toMutableList()
        cookies.forEach { cookie ->
            Log.v("COOKIE_DEBUG", "Cookie guardada: ${cookie.name}=${cookie.value}, dominio: ${cookie.domain}")
        }
    }

    override fun loadForRequest(url: HttpUrl): List<Cookie> {
        // Recuperar las cookies para el dominio
        return cookieStore[Constants.SERVER_URL] ?: emptyList()
    }

    fun getCookie(name: String): Cookie? {
        return cookieStore.values.flatten().find { it.name == name }
    }
}
