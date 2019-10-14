package com.gerralizza.navigation.fragments

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.gerralizza.navigation.R
import java.io.File

class FullScreenImageFragment : Fragment() {
    private lateinit var imagePath: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        imagePath = arguments?.getString(KEY_IMAGE_PATH) ?: ""
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val imageView = ImageView(inflater.context)
        imageView.isClickable = true
        imageView.layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT)
        imageView.scaleType = ImageView.ScaleType.FIT_CENTER
        imageView.transitionName = getString(R.string.image_transition)
        return imageView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (view as ImageView).setImageURI(Uri.fromFile(File(imagePath)))
    }

    companion object {
        private const val KEY_IMAGE_PATH = "imagePath"

        fun create(imagePath: String): FullScreenImageFragment {
            return FullScreenImageFragment().apply {
                arguments = Bundle(1).apply {
                    putString(KEY_IMAGE_PATH, imagePath)
                }
            }
        }
    }
}