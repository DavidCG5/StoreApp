package com.example.storeapp

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class AddProductActivity : AppCompatActivity() {

    private lateinit var productImageView: ImageView
    private lateinit var productNameEditText: EditText
    private lateinit var productListButton: Button

    companion object {
        private const val REQUEST_IMAGE_CAPTURE = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_product)

        productImageView = findViewById(R.id.productImageView)
        productNameEditText = findViewById(R.id.productNameEditText)
        productListButton = findViewById(R.id.productListButton)

        productImageView.setOnClickListener {
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (takePictureIntent.resolveActivity(packageManager) != null) {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }


        productListButton.setOnClickListener {
            val productName = productNameEditText.text.toString()
            if (productName.isNotEmpty()) {

                addProductToList(productName)
                Toast.makeText(this, "Producto agregado", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Por favor ingrese un nombre de producto", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            productImageView.setImageBitmap(imageBitmap)

        }
    }

    private fun addProductToList(productName: String) {

    }
}
