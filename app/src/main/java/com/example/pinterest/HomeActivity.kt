package com.example.pinterest

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import org.jsoup.Jsoup

class HomeActivity: AppCompatActivity() {

    /** Attributes **/
    private lateinit var categoryItemsRecyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var homeMenu: BottomNavigationView

    private lateinit var todayText: TextView
    private lateinit var followingText: TextView


    private var categories: MutableList<String> = arrayListOf()
    private var urls: MutableList<CategoryItem> = mutableListOf()

    /** On Create **/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // Get categories
        categories = intent.getStringArrayListExtra("categories")

        // Initialize UI
        initializeUI()


        // Initialize Home Menu
        initializeHomeMenu()

        // Initialize Category Items List View
        initializeCategoryItemsRecyclerView(this)

        // Sync images
        syncImages(this)

    }

    /** On Resume set the selected item on the home menu to be the home menu item **/
    override fun onResume() {
        super.onResume()
        homeMenu.selectedItemId = R.id.home_menu_item
    }

    /** Initialize UI **/
    private fun initializeUI(){
        todayText = findViewById(R.id.home_today)
        followingText = findViewById(R.id.home_following)

        todayText.setOnClickListener {
            Toast.makeText(this, "The today functionality is currently unavailable.", Toast.LENGTH_SHORT).show()
        }

        followingText.setOnClickListener {
            Toast.makeText(this, "The following functionality is currently unavailable.", Toast.LENGTH_SHORT).show()
        }
    }


    /** Initialize the Home Menu **/
    private fun initializeHomeMenu(){

        // Find the view
        homeMenu = findViewById(R.id.home_menu)
        homeMenu.setOnNavigationItemSelectedListener {
            when (it.itemId) {

                // Home
                R.id.home_menu_item -> {
                    true
                }

                // Search
                R.id.search_menu_item -> {
                    Toast.makeText(this, "The search functionality is currently unavailable.", Toast.LENGTH_SHORT).show()
                    false
                }

                // Notifications
                R.id.notifications_menu_item -> {
                    Toast.makeText(this, "The notifications functionality is currently unavailable.", Toast.LENGTH_SHORT).show()
                    false
                }

                // Saved
                R.id.saved_menu_item -> {
                    // Create a new intent and launch it
                    val intent = Intent(this, SavedActivity::class.java)
                    startActivity(intent)
                    true
                }

                else -> false
            }
        }
    }

    /** Initialize the Category Items Recycler View  **/
    private fun initializeCategoryItemsRecyclerView(context: Context){

        // Initialize the Recycler View Adapter and the Recycler View Manager
        viewManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        (viewManager as StaggeredGridLayoutManager).gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS

        viewAdapter = HomeAdapter(context, urls)

        // Handle the Recycler View
        categoryItemsRecyclerView = findViewById<RecyclerView>(R.id.category_items_list_view).apply {

            // Set fields of the recycler view
            setHasFixedSize(true)
            layoutManager = viewManager
            itemAnimator = null

        }
    }

    /** Sync Images fetched with UI using Coroutines **/
    private fun syncImages (applicationContext: HomeActivity)  = GlobalScope.launch(IO) {

        // Network actions
        for (category in categories){
            fetchImagesFromUrl("https://images.google.com/images?q=$category")
        }


        // Update the UI
        withContext(Main){

            // Notify the adapter that the data set has changed after fetching image urls
            urls.shuffle()
            categoryItemsRecyclerView.adapter = HomeAdapter(applicationContext, urls.take(25))
            viewAdapter.notifyDataSetChanged()
            findViewById<ProgressBar>(R.id.category_loading).visibility = View.GONE
        }

    }


    /** Fetch images from url **/
    private fun fetchImagesFromUrl(url: String) {

        // Variables for Jsoup
        var doc = Jsoup.connect(url).get()
        var images = doc.select("img")

        // Get images from url and add the image's url to the url list
        for (image in images){

            if(image.attr("data-src").isNotEmpty() && image.attr("alt").isNotEmpty()){
                urls.add(CategoryItem(image.attr("data-src"), image.attr("alt")))
                //Log.v("asdf", image.attr("data-src") + " : " + image.attr("alt"))
            }
        }

    }



}