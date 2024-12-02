package com.example.myapplication
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
class CheckoutActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)

        // Obtener el precio total pasado como parámetro
        val totalPrice = intent.getDoubleExtra("totalPrice", 0.0)
        val textViewTotal = findViewById<TextView>(R.id.textViewCheckoutTotal)
        textViewTotal.text = "Total: $%.2f".format(totalPrice)

        // Botón para confirmar la compra
        val buttonConfirm = findViewById<Button>(R.id.buttonConfirmPurchase)
        buttonConfirm.setOnClickListener {
            processCheckout()
        }
    }

    private fun processCheckout() {
        val apiService = ApiClient.createService(ApiService::class.java)

        // Realizar la llamada al endpoint de checkout
        apiService.checkout().enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful) {
                    // Mensaje de éxito desde la API
                    val confirmationMessage = response.body() ?: "Compra realizada con éxito"
                    Toast.makeText(this@CheckoutActivity, confirmationMessage, Toast.LENGTH_LONG).show()

                    // Finalizar la actividad y regresar a la pantalla anterior
                    setResult(Activity.RESULT_OK)
                    finish()
                } else {
                    // Mostrar mensaje de error al usuario
                    val errorMessage = "Error: ${response.code()} - ${response.message()}"
                    Toast.makeText(this@CheckoutActivity, errorMessage, Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                // Manejo de errores de conexión
                Toast.makeText(
                    this@CheckoutActivity,
                    "Error de conexión: ${t.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        })
    }
}
