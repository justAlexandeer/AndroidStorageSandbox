package com.github.justalexandeer.androidstoragesandbox.ui.base

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.github.justalexandeer.androidstoragesandbox.R
import com.github.justalexandeer.androidstoragesandbox.databinding.ViewHolderFileBinding
import java.io.File

class FileViewHolder(
    private val view: View,
    private val binding: ViewHolderFileBinding,
    private val clickHandler: (File) -> Unit
) : RecyclerView.ViewHolder(view) {

    fun bind(file: File) {
        binding.viewHolderFileTitle.text = file.name
        when {
            file.isFile -> {
                Glide.with(view)
                    .load(R.drawable.file_black_48dp)
                    .into(binding.viewHolderFileImage)
            }
            file.isDirectory -> {
                Glide.with(view)
                    .load(R.drawable.folder_black_48dp)
                    .into(binding.viewHolderFileImage)
            }
        }
        binding.viewHolderLinearLayout.setOnClickListener {
            clickHandler(file)
        }
    }

    companion object {
        fun create(parent: ViewGroup, clickHandler: (File) -> Unit): FileViewHolder {
            val binding = ViewHolderFileBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            return FileViewHolder(binding.root, binding, clickHandler)
        }
    }
}