package com.example.pinterest

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.GridView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth


class CategoryActivity : AppCompatActivity() {

    /** Attributes **/
    private lateinit var gridView: GridView
    private lateinit var nextButton: Button
    private val categories = listOf(
        Category(0, R.drawable.category_mens_health_fitness, R.string.category_mens_health_fitness),
        Category(1, R.drawable.category_memes, R.string.category_memes),
        Category(2, R.drawable.category_diy_and_home_improvement, R.string.category_diy_and_home_improvement),
        Category(3, R.drawable.category_home_decor, R.string.category_home_decor),
        Category(4, R.drawable.category_mens_style, R.string.category_mens_style),
        Category(5, R.drawable.category_food_and_drink, R.string.category_food_and_drink),
        Category(6, R.drawable.category_woodworking, R.string.category_woodworking),
        Category(7, R.drawable.category_gadgets, R.string.category_gadgets),
        Category(8, R.drawable.category_backyards, R.string.category_backyards),
        Category(9, R.drawable.category_mens_hairstyle, R.string.category_mens_hairstyle),
        Category(10, R.drawable.category_tattoos_and_body_art, R.string.category_tattoos_and_body_art),
        Category(11, R.drawable.category_survival_tips, R.string.category_survival_tips)
    )

    /** On Create **/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)


        // Initialize Category Names
        initializeCategoryNames()

        // Initialize the next button
        nextButton = findViewById(R.id.next_button)
        nextButton.isEnabled = false

        // When the user is done selecting his categories
        nextButton.setOnClickListener {

            // Save the categories to the user
            var uid = FirebaseAuth.getInstance().currentUser?.uid
            var saveCategories = categories.filter{ it.isSelected }.toTypedArray()
            FirestoreManager.saveCategories(uid, saveCategories)

            // Start the HomeActivity Intent
            val intent = Intent(this, HomeActivity::class.java)
            val selectedCategories = arrayListOf<String>()
            selectedCategories.addAll(categories.filter{ it.isSelected }.map { it.name })
            intent.putStringArrayListExtra("categories", selectedCategories)
            startActivity(intent)

        }

        // Initialize the grid view of categories
        gridView = findViewById(R.id.categories_grid_view)
        gridView.adapter = CategoryAdapter(applicationContext, categories, nextButton)


    }


    private fun initializeCategoryNames() {
        for (category in categories){
            category.name = getString(category.nameRes)
        }
    }

}