package com.example.pinterest

import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import java.io.File
import java.util.ArrayList

class SettingsActivity: AppCompatActivity() {

    /** Attributes **/
    private lateinit var clearSavesButton: Button
    private lateinit var clearCategoriesButton: Button
    private lateinit var logOutButton: Button
    private lateinit var providers: List<AuthUI.IdpConfig>
    private val REQUEST_CODE: Int = 8888

    /** On Create **/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        // Initiate the providers
        providers = listOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.FacebookBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build()
        )

        // Initialize the buttons
        initializeButtons()

        // Initialize Support Action Bar
        initializeSupportActionBar()

    }

    /** On Activity Result **/
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // If the user presses back instead of logging in, we want to leave the application
        if(FirebaseAuth.getInstance().currentUser == null)
            finish()

        // Redirect the user
        redirectUser()

    }

    /** Initialize Buttons**/
    private fun initializeButtons(){

        // Initialize buttons
        clearSavesButton = findViewById(R.id.button_clear_saves)
        clearCategoriesButton = findViewById(R.id.button_clear_categories)
        logOutButton = findViewById(R.id.button_log_out)

        clearSavesButton.setOnClickListener { clearSaves() }
        clearCategoriesButton.setOnClickListener { clearCategories() }
        logOutButton.setOnClickListener { logOut() }

    }

    /** Initialize Support Action Bar **/
    private fun initializeSupportActionBar(){

        // Remove the shadowing and change the title from the title bar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.elevation = 0F
        supportActionBar?.title = getString(R.string.settings_title)
    }

    /** Clear Saves **/
    private fun clearSaves(){
        // Get the list of images saved in the external storage directory saved_images
        val directory = File(Environment.getExternalStorageDirectory().toString() + "/saved_images")
        if (directory.exists()) {
            for (file in directory.listFiles()){
                file.delete()
            }
        }

        Toast.makeText(this, "Successfully cleared saves", Toast.LENGTH_SHORT).show()
    }

    /** Clear Categories **/
    private fun clearCategories(){

        // Get the user UID
        val uid = FirebaseAuth.getInstance().currentUser?.uid

        if(uid != null){

            FirestoreManager.clearCategories(this, uid)
            Toast.makeText(this, "Successfully cleared saves", Toast.LENGTH_SHORT).show()

            // Show the intent to chose categories
            val intent = Intent(this, CategoryActivity::class.java)
            startActivity(intent)

        }else{
            Toast.makeText(this, "An error occurred while attempting to clear the categories", Toast.LENGTH_SHORT).show()
        }

    }

    /** Log out **/
    private fun logOut(){

        // Log out
        AuthUI.getInstance().signOut(this@SettingsActivity)
            .addOnCompleteListener{
                displayUserAuthentication()
            }
            .addOnFailureListener{e ->
                Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()

            }
    }

    /** Redirect the user to the home page if he already chose his categories, or else redirect him to choose his categories **/
    private fun redirectUser() {

        // Check if user connected has already chosen his categories
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        if(uid != null){

            FirestoreManager.getCategoryDocumentRefByUserUid(uid)
                .addOnSuccessListener { documents ->

                    if(documents.size() > 0 ) {

                        // Get categories to pass it to the HomeActivity intent
                        var categories = arrayListOf<String>()
                        for (document in documents) {
                            categories.add(document.data["categoryNameId"].toString())
                        }

                        // If the user has already chosen his categories, start the HomeActivity
                        val intent = Intent(this, HomeActivity::class.java)
                        intent.putStringArrayListExtra("categories", categories as ArrayList<String>)
                        startActivity(intent)

                    } else {

                        // If the user hasn't chosen his categories, start the CategoryActivity
                        val intent = Intent(this, CategoryActivity::class.java)
                        startActivity(intent)
                    }

                }
                .addOnFailureListener { e ->
                    Log.d("asdf", "get failed with", e)
                }
        }
    }

    /** Display User Authentication **/
    private fun displayUserAuthentication(){
        startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .setTheme(R.style.AppTheme)
            .setLogo(R.drawable.pinterest_logo)
            .build(), REQUEST_CODE)
    }


}