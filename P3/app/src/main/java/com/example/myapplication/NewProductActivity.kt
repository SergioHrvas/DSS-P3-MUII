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
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.http.Part
import java.io.File
import java.net.URI

class NewProductActivity : ComponentActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var productAdapter: ProductAdapter

    private lateinit var imageViewProduct: ImageView
    private lateinit var btnSelectImage: Button
    private lateinit var btnNewProduct: Button

    private val IMAGE_PICK_CODE = 1000
    private val CAMERA_REQUEST_CODE = 1001

    private var selectedImageUri: URI? = null
    private var selectedImageFile: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_product)
        val headerTitle = findViewById<TextView>(R.id.headerTitle)
        headerTitle.text = "${headerTitle.text} - Crear producto"

        imageViewProduct = findViewById(R.id.imageViewProduct)
        btnSelectImage = findViewById(R.id.btnSelectImage)
        btnNewProduct = findViewById(R.id.btnNewProduct)

        btnSelectImage.setOnClickListener {
            openImageChooser()
        }

        val priceInputLayout = findViewById<TextInputLayout>(R.id.inputLayoutPrice)
        val nameInputLayout = findViewById<TextInputLayout>(R.id.inputLayoutName)

        btnNewProduct.setOnClickListener {
            val editTextPrice = priceInputLayout.editText
            val price = editTextPrice?.text.toString()
            val editTextName = nameInputLayout.editText
            val name = editTextName?.text.toString()

            if (selectedImageFile != null) {
                if (name.isNotEmpty()) {
                    if (price.isNotEmpty()) {
                        createProduct(name, price.toDouble(), selectedImageFile!!)
                        val intent = Intent()
                        setResult(RESULT_OK, intent)
                        finish()
                    } else {
                        Toast.makeText(this, "El precio no puede estar vacío", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "El nombre no puede estar vacío", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Por favor selecciona una imagen", Toast.LENGTH_SHORT).show()
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

        val editTextPrice: EditText = findViewById(R.id.editTextPrice)
        editTextPrice.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {
                if (charSequence?.isNotEmpty() == true) {
                    editTextPrice.hint = ""
                } else {
                    editTextPrice.hint = "Nombre"
                }
            }
            override fun afterTextChanged(editable: Editable?) {}
        })
    }

    private fun openImageChooser() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            data?.data?.let { uri ->
                selectedImageUri = URI(uri.toString())
                val filePath = getFilePathFromUri(uri)
                val selectedImageFile = File(filePath)
                val permanentFile = File(filesDir, "selected_image.png") // Asegúrate de usar la extensión correcta

                if (selectedImageFile.exists()) {
                    selectedImageFile.copyTo(permanentFile, overwrite = true)
                    Log.v("Image", "Imagen copiada: ${permanentFile.absolutePath}")

                    imageViewProduct.setImageURI(Uri.fromFile(permanentFile))

                    Toast.makeText(this, "Imagen seleccionada: ${permanentFile.name}", Toast.LENGTH_LONG).show()

                    this.selectedImageFile = permanentFile // Asignar el archivo con extensión correcta
                }
            }
        }
    }

    private fun getFilePathFromUri(uri: Uri): String {
        val cursor = contentResolver.query(uri, null, null, null, null)
        cursor?.moveToFirst()
        val index = cursor?.getColumnIndex(OpenableColumns.DISPLAY_NAME) ?: -1
        val fileName = if (index != -1) cursor?.getString(index) else "temp_file"
        cursor?.close()

        val inputStream = contentResolver.openInputStream(uri)
        val tempFile = File.createTempFile(fileName, null, cacheDir)
        inputStream?.use { input ->
            tempFile.outputStream().use { output ->
                input.copyTo(output)
            }
        }
        return tempFile.absolutePath
    }

    private fun createProduct(name: String, price: Double, image: File) {
        val product = Product(name, price, "")

        val gson = Gson()
        val productJson = gson.toJson(product)

        val productRequestBody = productJson.toRequestBody("application/json".toMediaTypeOrNull())
        val productPart = MultipartBody.Part.createFormData("product", null, productRequestBody)

        val imageRequestBody = image.asRequestBody("image/*".toMediaTypeOrNull())
        val imagePart = MultipartBody.Part.createFormData("file", image.name, imageRequestBody)

        RetrofitClient.apiService.createProduct(productPart, imagePart).enqueue(object : Callback<Product> {
            override fun onResponse(call: Call<Product>, response: Response<Product>) {
                Log.v("API_RESPONSE", "$response")
                if (response.isSuccessful) {
                    val data = response.body()
                    Log.v("API_RESPONSE", "${data}")
                    val intent = Intent()
                    intent.putExtra("product_saved", true)
                    setResult(RESULT_OK, intent)
                    finish()
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
