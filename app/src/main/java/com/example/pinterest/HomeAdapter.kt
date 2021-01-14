package com.example.pinterest

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class HomeAdapter(private val context: Context, private val categoryItems: List<CategoryItem>) :
    RecyclerView.Adapter<HomeAdapter.HomeViewHolder>(){

    class HomeViewHolder(val textView: View): RecyclerView.ViewHolder(textView)

    /** On Create View Holder **/
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_category_item, parent, false)

        return HomeViewHolder(view)
    }


    /** Get element of dataset at the position and update the content view **/
    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {

        // Items will remain the same on scroll, this caused problems with the image
        //holder.setIsRecyclable(true)

        // Bind the Image
        val image = holder.textView.findViewById(R.id.category_element_image) as ImageView
        Glide.with(context).load(categoryItems[position].url).placeholder(R.drawable.category_memes).into(image)

        // When we click on the image, start a new activity displaying the details about the image
        image.setOnClickListener {

            // Get the Contact and create the intent
            val intent = Intent(context, CategoryItemActivity::class.java)

            // Pass data about the Contact to the intent
            intent.putExtra("url", categoryItems[position].url)
            intent.putExtra("description", categoryItems[position].title)

            // Start the activity
            context.startActivity(intent)
        }

        image.requestLayout()

    }

    /** Get number of urls **/
    override fun getItemCount() = categoryItems.size


}