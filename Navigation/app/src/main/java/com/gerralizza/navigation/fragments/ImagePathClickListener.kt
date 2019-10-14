package com.gerralizza.navigation.fragments

import android.view.View
import android.widget.ImageView

interface ImagePathClickListener {
    fun onImageClick(path: String, root: View, image: ImageView)
}