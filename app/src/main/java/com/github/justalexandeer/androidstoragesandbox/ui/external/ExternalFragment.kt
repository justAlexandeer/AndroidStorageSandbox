package com.github.justalexandeer.androidstoragesandbox.ui.external

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.justalexandeer.androidstoragesandbox.databinding.FragmentExternalBinding
import com.github.justalexandeer.androidstoragesandbox.databinding.FragmentInternalBinding

class ExternalFragment: Fragment() {

    private lateinit var binding: FragmentExternalBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentExternalBinding.inflate(inflater, container, false)
        return binding.root
    }

}