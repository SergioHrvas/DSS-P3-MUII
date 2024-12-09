package com.example.myapplication

import android.app.Activity
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
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.myapplication.ApiClient.printStoredCookies
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)
        val headerTitle = findViewById<TextView>(R.id.headerTitle)
        headerTitle.text = "${headerTitle.text} - Iniciar sesión"

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
                    val intent = Intent()
                    setResult(RESULT_OK, intent)
                    finish()
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




    private fun login(name: String, password: String, ) {
        val apiService = ApiClient.createService(ApiService::class.java)


        val loginRequest = User(username = name, password = password)

        Log.v("LOGINREQUEST", "$loginRequest")
        apiService.loginUser(loginRequest).enqueue(object : Callback<ApiService.LoginResponse> {
            override fun onResponse(call: Call<ApiService.LoginResponse>, response: Response<ApiService.LoginResponse>) {

                Log.v("API_RESPONSE", "$response")
                if (response.isSuccessful) {
                    val data = response.body()
                    Log.v("API_RESPONSE", "${data}")
                    printStoredCookies()
                } else {
                    Log.e("API_RESPONSE", "Error: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<ApiService.LoginResponse>, t: Throwable) {
                Log.e("API_ERROR", "Failure: ${t.message}")
            }
        })
    }
}
