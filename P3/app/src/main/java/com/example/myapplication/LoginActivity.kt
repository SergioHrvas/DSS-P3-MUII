package com.example.myapplication

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.text.Editable
import android.text.TextWatcher
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.http.Part
import java.io.File
import java.net.URI

class LoginActivity : ComponentActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var productAdapter: ProductAdapter

    private lateinit var btnLogin: Button
    var cookieManager = android.webkit.CookieManager.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        cookieManager = android.webkit.CookieManager.getInstance()

        val cookieManager = android.webkit.CookieManager.getInstance()
        val cookies = cookieManager.getCookie(Constants.SERVER_URL) // Cambia por tu dominio.

        setContentView(R.layout.login)
        val headerTitle = findViewById<TextView>(R.id.headerTitle)
        headerTitle.text = "Iniciar sesión"

        // Encontrar el botón de retroceso
        val buttonBack: ImageButton = findViewById(R.id.buttonBack)

        // Hacer visible el botón de retroceso
        buttonBack.visibility = View.VISIBLE

        // Configurar la acción de retroceso
        buttonBack.setOnClickListener {
            onBackPressed()  // Llamada para volver a la actividad anterior
        }


        btnLogin = findViewById(R.id.btnLogin)


        val passwordInputLayout = findViewById<TextInputLayout>(R.id.inputLayoutPassword)
        val nameInputLayout = findViewById<TextInputLayout>(R.id.inputLayoutName)

        btnLogin.setOnClickListener {
            val editTextPassword = passwordInputLayout.editText
            val password = editTextPassword?.text.toString()
            val editTextName = nameInputLayout.editText
            val name = editTextName?.text.toString()

            if (name.isNotEmpty()) {
                if (password.isNotEmpty()) {
                    login(name, password)

                    } else {
                        Toast.makeText(this, "La contraseña no puede estar vacía", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "El nombre no puede estar vacío", Toast.LENGTH_SHORT).show()
                }
            }

        val editTextName: EditText = findViewById(R.id.editTextName)
        editTextName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {
                if (charSequence?.isNotEmpty() == true) {
                    editTextName.hint = ""
                } else {
                    editTextName.hint = "Nombre"
                }
            }
            override fun afterTextChanged(editable: Editable?) {}
        })

        val editTextPassword: EditText = findViewById(R.id.editTextPassword)
        editTextPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {
                if (charSequence?.isNotEmpty() == true) {
                    editTextPassword.hint = ""
                } else {
                    editTextPassword.hint = "Nombre"
                }
            }
            override fun afterTextChanged(editable: Editable?) {}
        })
    }

    fun saveCookieLocally(context: Context, role: String) {
        val sessionCookie = RetrofitClient.getCookie("JSESSIONID")
        val sharedPrefs = context.getSharedPreferences("AppCookies", Context.MODE_PRIVATE)
        sharedPrefs.edit().putString("JSESSIONID", sessionCookie).apply()
        sharedPrefs.edit().putString("ROLE", role).apply()
    }

    private fun login(name: String, password: String) {
        val apiService = ApiClient.createService(ApiService::class.java)

        val loginRequest = User(username = name, password = password)

        RetrofitClient.apiService.loginUser(loginRequest).enqueue(object : Callback<ApiService.LoginResponse> {
            override fun onResponse(call: Call<ApiService.LoginResponse>, response: Response<ApiService.LoginResponse>) {
                Log.v("API_RESPONSE", "Response: $response")

                if (response.isSuccessful) {
                    val data = response.body()
                    Log.v("API_RESPONSE", "Login Data: $data")

                    val sessionCookie = data?.sessionId
                    if (sessionCookie != null) {
                        saveCookieLocally(applicationContext, data.role)
                        Log.v("API_RESPONSE", "Cookie guardada: $sessionCookie")

                        // Redirigir al MainActivity
                        val intent = Intent()
                        setResult(RESULT_OK, intent)
                        finish()
                    } else {
                        Log.e("API_RESPONSE", "Cookie no encontrada en la respuesta.")
                        // Mostrar mensaje de error
                        Toast.makeText(applicationContext, "Error: Cookie no encontrada.", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Log.e("API_RESPONSE", "Error: ${response.code()}")
                    // Mostrar mensaje de error al usuario
                    Toast.makeText(applicationContext, "Error al iniciar sesión: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ApiService.LoginResponse>, t: Throwable) {
                Log.e("API_RESPONSE", "Error en la petición: ${t.message}")
                // Mostrar mensaje de error al usuario
                Toast.makeText(applicationContext, "Error de red: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
