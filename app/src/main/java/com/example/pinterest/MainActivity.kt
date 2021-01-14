package com.example.pinterest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import java.util.ArrayList

class MainActivity : AppCompatActivity() {

    /** Attributes **/
    private lateinit var providers: List<AuthUI.IdpConfig>
    private val REQUEST_CODE: Int = 8888

    /** On Create **/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initiate the providers
        providers = listOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.FacebookBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build()
        )

        // Display the User Authentication options
        if(FirebaseAuth.getInstance().currentUser == null)
            displayUserAuthentication()
        else
            redirectUser()

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