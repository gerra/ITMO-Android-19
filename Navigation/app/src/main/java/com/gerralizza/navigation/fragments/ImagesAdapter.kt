package com.gerralizza.navigation.fragments

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView

class ImagesAdapter(private val clickListener: (String, View, ImageView) -> Unit) : RecyclerView.Adapter<ItemViewHolder>() {
    private val imagePaths = arrayListOf<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ItemViewHolder(parent, clickListener)
    override fun getItemCount() = imagePaths.size
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) = holder.bind(imagePaths[position])
    override fun onViewRecycled(holder: ItemViewHolder) = holder.onRecycle()

    fun setItems(imagePaths: List<String>) {
        this.imagePaths.clear()
        this.imagePaths.addAll(imagePaths)
        notifyDataSetChanged()
    }
}