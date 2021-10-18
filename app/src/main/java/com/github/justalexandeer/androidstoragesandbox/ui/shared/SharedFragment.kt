package com.github.justalexandeer.androidstoragesandbox.ui.shared

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.justalexandeer.androidstoragesandbox.databinding.FragmentInternalBinding
import com.github.justalexandeer.androidstoragesandbox.databinding.FragmentSharedBinding

class SharedFragment: Fragment() {

    private lateinit var binding: FragmentSharedBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSharedBinding.inflate(inflater, container, false)
        return binding.root
    }

}