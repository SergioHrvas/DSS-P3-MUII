package com.example.myapplication

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.logging.HttpLoggingInterceptor

object ApiClient {
    private var retrofit: Retrofit? = null

    private const val BASE_URL = Constants.SERVER_URL

    val cookieJar = PersistentCookieJar()

    private val okHttpClient = OkHttpClient.Builder().cookieJar(cookieJar).addInterceptor { chain ->
            val request = chain.request()

            // Recuperar la cookie que se ha almacenado en tu CookieJar
            val cookieValue = getCookie("JSESSIONID")

            // Imprimir la cookie para verificar su valor
            println("Cookie que se va a enviar: JSESSIONID=$cookieValue")

            // Si hay cookie, agregarla al encabezado
            val modifiedRequest = if (cookieValue != null) {
                request.newBuilder()
                    .header("Cookie", "JSESSIONID=$cookieValue")  // Añadir cookie al encabezado
                    .build()
            } else {
                request  // Si no hay cookie, enviar la solicitud sin ella
            }

            // Continuar con la solicitud
            chain.proceed(modifiedRequest)
        }.addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY  // Habilitar logs de cuerpo de la solicitud
        }).build()

    fun getClient(baseUrl: String): Retrofit {
        if (retrofit == null) {
            retrofit = Retrofit.Builder().baseUrl(baseUrl) // URL base de la API
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create()) // Conversor de Gson
                .build()
        }
        return retrofit!!
    }

    val apiService: ApiService by lazy {
        getClient(BASE_URL).create(ApiService::class.java) // Crea una instancia de ApiService
    }

    fun getCookie(name: String): String? {
        return cookieJar.getCookie(name)?.value
    }

}
