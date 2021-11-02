package com.github.justalexandeer.androidstoragesandbox.ui.shared

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.github.justalexandeer.androidstoragesandbox.R
import com.github.justalexandeer.androidstoragesandbox.data.model.Image

class ImageViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    fun bind(image: Image) {
        val imageView = view.findViewById<ImageView>(R.id.image_view_holder_image)
        Glide.with(view)
            .load(image.contentUri)
            .into(imageView)
    }

    companion object {
        fun create(viewGroup: ViewGroup): ImageViewHolder {
            return ImageViewHolder(
                LayoutInflater.from(viewGroup.context)
                    .inflate(R.layout.image_view_holder, viewGroup, false)
            )
        }
    }
}