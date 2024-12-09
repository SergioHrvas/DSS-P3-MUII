import android.util.Log
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl

val cookieJar = object : CookieJar {
    private val cookieStore = mutableMapOf<String, MutableList<Cookie>>() // Cambié el tipo de clave a String

    // Guardar cookies de la respuesta
    override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
        val host = url.host
        // Asegúrate de que el mapa contiene la lista de cookies para este dominio
        if (cookieStore[host] == null) {
            cookieStore[host] = mutableListOf()
        }
        cookieStore[host]?.addAll(cookies) // Añade las cookies recibidas

        // Imprimir para verificar
        Log.d("bbbbbbbbb" , "Cookies guardadas para $host: $cookies")
    }

    // Cargar cookies para la solicitud
    override fun loadForRequest(url: HttpUrl): List<Cookie> {
        val host = url.host
        val cookies = cookieStore[host] ?: emptyList()

        // Imprimir para verificar
        Log.d("aaaaaaaa", "Cookies cargadas para $host: $cookies")
        return cookies
    }
}