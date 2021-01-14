package com.example.pinterest

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

/**
 * Category :
 *    A category is a classification of interests.
 **/
data class Category(
    val id: Int,
    @DrawableRes val imageId: Int,
    @StringRes val nameRes: Int,
    var isSelected: Boolean = false
) {
    // data will automatically define equals(), hashCode(), toString()

    var name: String = ""

}