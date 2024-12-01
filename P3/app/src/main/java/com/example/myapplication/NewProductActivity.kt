package com.example.myapplication

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.ui.theme.MyApplicationTheme
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.example.myapplication.ApiService;
import android.util.Log;
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputLayout

class NewProductActivity : ComponentActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var productAdapter: ProductAdapter

    private lateinit var imageViewProduct: ImageView
    private lateinit var btnSelectImage: Button
    private lateinit var btnNewProduct: Button

    // Código para seleccionar una imagen de la galería
    private val IMAGE_PICK_CODE = 1000
    private val CAMERA_REQUEST_CODE = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_product)



        // Configurar RecyclerView
        //recyclerView = findViewById(R.id.recylerAddProduct)
        val headerTitle = findViewById<TextView>(R.id.headerTitle)
        headerTitle.text = "${headerTitle.text} - Crear producto"



        imageViewProduct = findViewById(R.id.imageViewProduct)
        btnSelectImage = findViewById(R.id.btnSelectImage)
        btnNewProduct = findViewById(R.id.btnNewProduct)

        // Configurar el botón para seleccionar una imagen
        btnSelectImage.setOnClickListener {
            // Llamar al método para abrir la galería o cámara
            openImageChooser()
        }

        val priceInputLayout = findViewById<TextInputLayout>(R.id.inputLayoutPrice)


        val nameInputLayout = findViewById<TextInputLayout>(R.id.inputLayoutName)

        // Configurar el botón para crear producto
        btnNewProduct.setOnClickListener {

            val editTextPrice = priceInputLayout.editText  // This gives you the EditText inside the TextInputLayout

            // Get the text from the EditText
            val price = editTextPrice?.text.toString()

            val editTextName = nameInputLayout.editText  // This gives you the EditText inside the TextInputLayout

            // Get the text from the EditText
            val name = editTextName?.text.toString()

            // Llamar al método para crear producto
            if(price.isNotEmpty()) {
                createProduct(name, price.toDouble())
                // Una vez agregado el producto, volvemos atrás
                val intent = Intent()
                setResult(
                    RESULT_OK,
                    intent
                ) // Esto asegura que la actividad principal sabrá que se ha agregado un producto
                finish()  // Volver atrás
            }
            else{

            }
        }


        val editTextName: EditText = findViewById(R.id.editTextName)

        editTextName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) {
                // No es necesario hacer nada aquí
            }

            override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {
                if (charSequence?.isNotEmpty() == true) {
                    // El hint se oculta cuando el usuario escribe
                    editTextName.hint = ""
                } else {
                    // Restaurar el hint si el campo está vacío
                    editTextName.hint = "Nombre"
                }
            }

            override fun afterTextChanged(editable: Editable?) {
                // No es necesario hacer nada aquí
            }
        })

        val editTextPrice: EditText = findViewById(R.id.editTextPrice)

        editTextPrice.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) {
                // No es necesario hacer nada aquí
            }

            override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {
                if (charSequence?.isNotEmpty() == true) {
                    // El hint se oculta cuando el usuario escribe
                    editTextPrice.hint = ""
                } else {
                    // Restaurar el hint si el campo está vacío
                    editTextPrice.hint = "Nombre"
                }
            }

            override fun afterTextChanged(editable: Editable?) {
                // No es necesario hacer nada aquí
            }
        })


    }

    // Método para abrir la galería o cámara
    private fun openImageChooser() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    // Manejar el resultado de la selección de la imagen
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Verificar si la imagen fue seleccionada correctamente
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            // Obtener la URI de la imagen seleccionada
            val imageUri: Uri? = data?.data
            imageViewProduct.setImageURI(imageUri) // Mostrar la imagen seleccionada
        }
    }



    // Llamar al método para obtener los productos
    private fun createProduct(name: String, price: Double) {
        val apiService = ApiClient.createService(ApiService::class.java)

        val product = Product(name, price)



        apiService.createProduct(product).enqueue(object : Callback<Product> {
            override fun onResponse(
                call: Call<Product>,
                response: Response<Product>
            ) {
                Log.v("API_RESPONSE", "$response")

                if (response.isSuccessful) {
                    val data = response.body()
                    Log.v("API_RESPONSE", "${data}")


                } else {
                    Log.e("API_RESPONSE", "Error: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<Product>, t: Throwable) {
                Log.e("API_ERROR", "Failure: ${t.message}")
            }
        })
    }
}