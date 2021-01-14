package com.example.pinterest

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView

class CategoryAdapter(var context: Context, var categories: List<Category>, var nextButton: Button) : BaseAdapter() {

    /** Get an item at a specific position **/
    override fun getItem(position: Int): Any {
        return categories[position]
    }

    /** Get the item's id at a specific position **/
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    /** Get number of categories **/
    override fun getCount(): Int {
        return categories.size
    }

    /** Get View **/
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        // Get the view of the category
        val categoryView: View = View.inflate(context, R.layout.category_item, null)

        // Get the category
        val category: Category = categories[position]

        // Category Image
        val categoryImage = categoryView.findViewById<ImageView>(R.id.category_image)
        categoryImage.background = context.getDrawable(category.imageId)

        // Category Name
        val categoryName = categoryView.findViewById<TextView>(R.id.category_name)
        categoryName.text = context.getString(category.nameRes)

        // Category Selected
        val categorySelected = categoryView.findViewById<TextView>(R.id.category_selected)

        // Deselect or select the category on item click
        categoryView.setOnClickListener {

            // Toggle select
            if(categorySelected.text == "") {
                category.isSelected = true
                categoryImage.alpha = 0.4f
                categorySelected.text = "✓"

            } else {
                category.isSelected = false
                categoryImage.alpha = 0.6f
                categorySelected.text = ""
            }

            // Enable the  next button if the number of selected categories is larger than 4
            nextButton.isEnabled = categories.filter{ it.isSelected }.size > 4

        }

        return categoryView
    }

}