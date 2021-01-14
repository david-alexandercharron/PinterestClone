package com.example.pinterest

import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.firebase.auth.FirebaseAuth
import java.io.File

class SavedActivity: AppCompatActivity() {

    /** Attributes **/
    private lateinit var firstCharacter: TextView
    private lateinit var fullName: TextView
    private lateinit var email: TextView
    private lateinit var followers: TextView

    private lateinit var savedCategoryItemsRecyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager


    /** On Create **/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_saved)

        // Initialize the saved category items Recycler View
        initializeSavedCategoryItemsRecyclerView()

        // Initialize the title bar
        initializeTitleBar()

        // Initialize UI
        initializeUI()

    }

    /** Inflate a simple menu to the application **/
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.saved_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    /** Change the default behaviour of the back arrow in the menu, to not create a new intent **/
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        // Settings
        if(item.itemId == R.id.saved_settings) {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
            return true
        }

        // Back
        finish()
        return true
    }



    /** Initialize the Title Bar **/
    private fun initializeTitleBar(){
        // Remove the shadowing and title from the title bar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.elevation = 0F
        supportActionBar?.title = ""

    }

    /** Initialize the UI (TextViews, etc) **/
    private fun initializeUI(){
        firstCharacter = findViewById(R.id.saved_first_character)
        fullName = findViewById(R.id.saved_full_name)
        email = findViewById(R.id.saved_email)
        followers = findViewById(R.id.saved_followers_and_following)

        // Sync the views with the user data
        var user = FirebaseAuth.getInstance().currentUser
        if(user != null){
            firstCharacter.text = user.displayName?.first().toString()
            fullName.text = user.displayName
            email.text = user.email
            followers.text = "0 followers - 0 following"

        }
    }

    /** Initialize the Saved Category Items Recycler View  **/
    private fun initializeSavedCategoryItemsRecyclerView(){

        // Initialize the Recycler View Adapter and the Recycler View Manager
        viewManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        (viewManager as StaggeredGridLayoutManager).gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS

        // Get the list of images saved in the external storage directory saved_images
        val directory = File(Environment.getExternalStorageDirectory().toString() + "/saved_images")

        if (directory.exists()) {
            viewAdapter = SavedAdapter(this, directory.listFiles())

            // Handle the Recycler View
            savedCategoryItemsRecyclerView = findViewById<RecyclerView>(R.id.saved_category_items).apply {

                // Set fields of the recycler view
                setHasFixedSize(true)
                layoutManager = viewManager
                itemAnimator = null
                adapter = viewAdapter

            }

        }
    }



}