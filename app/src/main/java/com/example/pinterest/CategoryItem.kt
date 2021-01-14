package com.example.pinterest

/**
 * CategoryItem :
 *    A category item is an item found from a certain category.
 **/
data class CategoryItem(val url: String, val title: String) {
    // data will automatically define equals(), hashCode(), toString()
}