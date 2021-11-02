package com.github.justalexandeer.androidstoragesandbox.ui.shared

import android.app.Activity
import android.content.pm.PackageManager
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.github.justalexandeer.androidstoragesandbox.R
import com.github.justalexandeer.androidstoragesandbox.databinding.FragmentInternalBinding
import com.github.justalexandeer.androidstoragesandbox.databinding.FragmentSharedBinding
import com.github.justalexandeer.androidstoragesandbox.util.READ_EXTERNAL_STORAGE_REQUEST
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import java.util.jar.Manifest

@AndroidEntryPoint
class SharedFragment : Fragment() {

    private lateinit var binding: FragmentSharedBinding
    private val viewModel: SharedFragmentViewModel by viewModels()
    private val adapter = ImageAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSharedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupObservers()
        setupUI()
    }

    private fun setupObservers() {
        lifecycleScope.launchWhenCreated {
            viewModel.currentListOfImages.collect {
                it?.let {
                    adapter.submitList(it)
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            READ_EXTERNAL_STORAGE_REQUEST -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PERMISSION_GRANTED) {
                    setupUI()
                } else {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.shared_fragment_permission_denied),
                        Toast.LENGTH_SHORT
                    ).show()
                    getPermissions()
                }
            }
        }
    }

    private fun setupUI() {
        if (!havePermissions()) {
            getPermissions()
        } else {
            viewModel.loadImage()
            binding.sharedFragmentRecyclerView.layoutManager =
                GridLayoutManager(requireContext(), 3)
            binding.sharedFragmentRecyclerView.adapter = adapter
        }
    }

    private fun havePermissions() = ContextCompat.checkSelfPermission(
        requireContext(),
        android.Manifest.permission.READ_EXTERNAL_STORAGE
    ) == PackageManager.PERMISSION_GRANTED

    private fun getPermissions() {
        requestPermissions(
            arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
            READ_EXTERNAL_STORAGE_REQUEST
        )
    }

    companion object {
        private const val TAG = "SharedFragment"
    }
}