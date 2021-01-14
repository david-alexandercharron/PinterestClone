package com.example.pinterest

import android.content.Context
import android.graphics.BitmapFactory
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException

class SavedAdapter(private val context: Context, private val files: Array<File>) :
    RecyclerView.Adapter<SavedAdapter.SavedViewHolder>(){

    class SavedViewHolder(val textView: View): RecyclerView.ViewHolder(textView)

    /** On Create View Holder **/
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_category_item, parent, false)


        return SavedViewHolder(view)
    }


    /** Get element of dataset at the position and update the content view **/
    override fun onBindViewHolder(holder: SavedViewHolder, position: Int) {

        // Bind the Image
        loadImageFromStorage(Environment.getExternalStorageDirectory().toString() + "/saved_images", files[position].name, holder)
        Log.v("asdf", "asdf123123")


    }

    /** Get number of urls **/
    override fun getItemCount() = files.size


    /** Load image from storage **/
    private fun loadImageFromStorage(path: String, file: String, holder: SavedViewHolder) {
        try {
            val file = File(path, file)
            val bitmap = BitmapFactory.decodeStream(FileInputStream(file))
            val image = holder.textView.findViewById<ImageView>(R.id.category_element_image)
            image.setImageBitmap(bitmap)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
    }


}