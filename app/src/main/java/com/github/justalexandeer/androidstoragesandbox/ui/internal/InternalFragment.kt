package com.github.justalexandeer.androidstoragesandbox.ui.internal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.justalexandeer.androidstoragesandbox.databinding.FragmentInternalBinding

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

}