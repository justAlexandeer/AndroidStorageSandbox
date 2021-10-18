package com.github.justalexandeer.androidstoragesandbox.ui.internal

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.justalexandeer.androidstoragesandbox.databinding.FragmentInternalBinding
import java.io.File
import java.io.FileOutputStream

class InternalFragment: Fragment() {

    private lateinit var binding: FragmentInternalBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentInternalBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.internalFragmentButtonStart.setOnClickListener {
            startSomeThing()
        }
    }

    fun startSomeThing() {
        val path = context?.filesDir
        path?.let {
            val directory = path.absolutePath.plus("/test")
            val pathDirectory = File(directory)
            pathDirectory.mkdir()
            val newFile = File(directory, "Test")
            val fileOutputStream = FileOutputStream(newFile)
            fileOutputStream.use {
                it.write("firstList".toByteArray())
            }
        }

    }

    companion object {
        private const val TAG = "InternalFragment"
    }
}