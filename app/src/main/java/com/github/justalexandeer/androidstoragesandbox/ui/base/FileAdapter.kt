package com.github.justalexandeer.androidstoragesandbox.ui.base

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import java.io.File

class FileAdapter(private val clickHandler: (File) -> Unit) :
    ListAdapter<File, FileViewHolder>(CustomFileDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileViewHolder {
        return FileViewHolder.create(parent, clickHandler)
    }

    override fun onBindViewHolder(holder: FileViewHolder, position: Int) {
        val customFile = getItem(position)
        holder.bind(customFile)
    }
}

class CustomFileDiffUtil : DiffUtil.ItemCallback<File>() {
    override fun areItemsTheSame(oldItem: File, newItem: File): Boolean {
        return oldItem.path == newItem.path
    }

    override fun areContentsTheSame(oldItem: File, newItem: File): Boolean {
        return oldItem == newItem
    }
}