package com.gerralizza.navigation.fragments

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.content.Context
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.graphics.Rect
import android.os.Bundle
import android.provider.MediaStore
import android.provider.MediaStore.MediaColumns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gerralizza.navigation.R


class ImagesListFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ImagesAdapter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context !is ImagePathClickListener) {
            throw IllegalStateException("context should be instance of ImagePathClickListener")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        adapter = ImagesAdapter { path, root, image ->
            (context as ImagePathClickListener).onImageClick(path, root, image)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_images_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val context = view.context

        recyclerView = view.findViewById(R.id.recycler)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerView.addItemDecoration(ItemDecoration())

        if (ContextCompat.checkSelfPermission(context, READ_EXTERNAL_STORAGE) != PERMISSION_GRANTED) {
            requestPermissions(arrayOf(READ_EXTERNAL_STORAGE), REQUEST_CODE_REQAD_PERMISSION)
        } else {
            loadImagesToAdapter()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == REQUEST_CODE_REQAD_PERMISSION) {
            if (grantResults.getOrNull(0) == PERMISSION_GRANTED) {
                loadImagesToAdapter()
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    private fun loadImagesToAdapter() {
        val ctx = context ?: return

        val uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(MediaColumns.DATA, MediaStore.Images.Media.BUCKET_DISPLAY_NAME)
        val sortBy = "${MediaStore.Images.Media.DATE_TAKEN} DESC"

        ctx.contentResolver.query(
            uri,
            projection,
            null,
            null,
            sortBy
        )?.use { cursor ->
            val columnIndexData = cursor.getColumnIndexOrThrow(MediaColumns.DATA)
            val listOfAllImages = ArrayList<String>()

            while (cursor.moveToNext()) {
                val absolutePathOfImage = cursor.getString(columnIndexData)
                listOfAllImages.add(absolutePathOfImage)
            }

            adapter.setItems(listOfAllImages)
        }
    }

    private class ItemDecoration : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            val p = view.context.resources.getDimensionPixelSize(R.dimen.card_decoration_padding)
            outRect.set(p, p, p, p)
        }
    }

    companion object {
        private const val REQUEST_CODE_REQAD_PERMISSION = 23
    }
}