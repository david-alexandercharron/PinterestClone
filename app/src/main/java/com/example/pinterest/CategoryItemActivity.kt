package com.example.pinterest

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.drawToBitmap
import com.bumptech.glide.Glide
import java.io.*


class CategoryItemActivity : AppCompatActivity() {

    /** Attributes **/
    private lateinit var backButton: ImageButton
    private lateinit var image: ImageView
    private lateinit var descriptionText: TextView
    private lateinit var saveButton: Button
    private lateinit var visitButton: Button


    /** On Create **/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_item)

        // Initialize the Home image
        initializeHomeImage()

        // Initialize the Home Description
        initializeHomeDescription()

        // Initialize the Back Button
        initializeButtons()




    }

    /** Initialize the Home Image **/
    private fun initializeHomeImage(){

        // Find the view
        image = findViewById(R.id.home_item_image)

        // Get the image by url
        val url = intent.getStringExtra("url")
        Glide.with(this).load(url).placeholder(R.drawable.category_memes).into(image)
    }

    /** Initialize the Home Description **/
    private fun initializeHomeDescription(){

        // Find the view
        descriptionText = findViewById(R.id.home_item_description)

        // Get the image by url
        val description = intent.getStringExtra("description")
        descriptionText.text = description
    }

    /** Initialize Buttons **/
    private fun initializeButtons(){

        // Find the view
        backButton = findViewById(R.id.back)

        // When we click on it, let's finish the activity and return to the home page
        backButton.setOnClickListener {
            finish()
        }

        // Save buttons
        saveButton = findViewById(R.id.save_button)
        saveButton.setOnClickListener { saveImageBitmap(image.drawToBitmap()) }

        // Visit buttons
        visitButton = findViewById(R.id.visit_button)
        visitButton.setOnClickListener {
            Toast.makeText(this, "The visit functionality is currently unavailable.", Toast.LENGTH_SHORT).show()
        }
    }



    /** Check for Write External Storage permissions **/
    private fun checkWriteExternalStoragePermissions(): Boolean {
        val TAG = "Storage Permission"
        return if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED
            ) {
                Log.v(TAG, "Permission is granted")
                true
            } else {
                Log.v(TAG, "Permission is revoked")
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    1
                )
                false
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG, "Permission is granted")
            true
        }
    }

    /** Save Image to Extranl Storage **/
    fun saveImageBitmap(image_bitmap: Bitmap) {

        // Get the external storage directory
        val root = Environment.getExternalStorageDirectory().toString()

        // Ask for permissions to write to external storage
        if (checkWriteExternalStoragePermissions()) { // check or ask permission

            // We will save the images in the /saved_images directory
            val directory = File(root, "/saved_images")

            // If the directory does not exist (1st time) we create it
            if (!directory.exists()) {
                directory.mkdirs()
            }

            // the file will have a name of image-timestamp.jpg
            val fileName = "image-${System.currentTimeMillis()}.jpg"
            val file = File(directory, fileName)

            // If the file exists, we will delete it (safety)
            if (file.exists()) {
                file.delete()
            }

            // We will now attempt to save the image to external storage
            try {
                file.createNewFile()
                val output = FileOutputStream(file)
                image_bitmap.compress(Bitmap.CompressFormat.JPEG, 90, output)
                output.flush()
                output.close()
                Toast.makeText(this, "Image was successfully saved.", Toast.LENGTH_LONG).show()
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }

        }
    }




}