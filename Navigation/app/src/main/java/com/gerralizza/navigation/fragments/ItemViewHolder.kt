package com.gerralizza.navigation.fragments

import android.graphics.Bitmap
import android.os.AsyncTask
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.Px
import androidx.recyclerview.widget.RecyclerView
import com.gerralizza.navigation.R

class ItemViewHolder(parent: ViewGroup, private val clickListener: (String, View, ImageView) -> Unit) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.images_list_item, parent, false)
) {
    val image = itemView.findViewById<ImageView>(R.id.image)
    private val title = itemView.findViewById<TextView>(R.id.title)
    private val progress = itemView.findViewById<ProgressBar>(R.id.progress)

    private var imageLoader: ImageLoader? = null

    private val width = itemView.context.resources.getDimensionPixelSize(R.dimen.image_preview_width)
    private val height = itemView.context.resources.getDimensionPixelSize(R.dimen.image_preview_height)

    private var curPath: String = ""

    private var last: Pair<String, Bitmap>? = null

    init {
        itemView.setOnClickListener {
            clickListener(curPath, itemView, image)
        }
    }

    fun bind(imagePath: String) {
        image.transitionName = imagePath

        curPath = imagePath

//        image.visibility = View.INVISIBLE
//        progress.visibility = View.VISIBLE

        title.text = imagePath

        imageLoader?.cancel(true)

        if (imagePath == last?.first) {
            onLoad(last!!.second)
            imageLoader?.listener = null
            imageLoader = null
        } else {
            imageLoader = ImageLoader(imagePath, width, height).apply {
                listener = {
                    last = Pair(imagePath, it)
                    onLoad(it)
                }
                execute()
            }
        }

        onLoad(BitmapHelper.getOptimalBitmap(imagePath, width, height))
    }

    fun onRecycle() {
        imageLoader?.listener = null
        imageLoader?.cancel(true)
        imageLoader = null
        last = null
    }

    private fun onLoad(bitmap: Bitmap) {
        image.setImageBitmap(bitmap)

        image.visibility = View.VISIBLE
        progress.visibility = View.INVISIBLE
    }

    private class ImageLoader(
        val path: String,
        @Px val width: Int,
        @Px val height: Int
    ) : AsyncTask<Unit, Unit, Bitmap>() {
        var listener: ((Bitmap) -> Unit)? = null

        override fun doInBackground(vararg params: Unit?): Bitmap? {
            return BitmapHelper.getOptimalBitmap(path, width, height)
        }

        override fun onPostExecute(result: Bitmap?) {
            result?.let {
                listener?.invoke(it)
            }
        }
    }
}