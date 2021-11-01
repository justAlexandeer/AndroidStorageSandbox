package com.github.justalexandeer.androidstoragesandbox.ui.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.github.justalexandeer.androidstoragesandbox.R
import com.github.justalexandeer.androidstoragesandbox.databinding.FragmentDialogContentFileBinding
import com.github.justalexandeer.androidstoragesandbox.util.TYPE_FILE
import com.github.justalexandeer.androidstoragesandbox.util.TYPE_FOLDER

class FileContentDialogFragment : DialogFragment() {

    private lateinit var binding: FragmentDialogContentFileBinding
    private val args: FileContentDialogFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDialogContentFileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val dialogFragmentTitle =
            requireContext().resources.getString(R.string.fragment_dialog_content_file_title) +
                    " " + args.typeFile
        binding.fragmentDialogContentFileTitle.text = dialogFragmentTitle

        binding.fragmentDialogContentFilePositiveButton.setOnClickListener {
            when (args.typeFile) {
                TYPE_FILE -> {
                    with(findNavController()) {
                        previousBackStackEntry?.savedStateHandle?.set(
                            TYPE_FILE,
                            binding.fragmentDialogContentFileEditText.text.toString()
                        )
                        navigateUp()
                    }
                }
                TYPE_FOLDER -> {
                    with(findNavController()) {
                        previousBackStackEntry?.savedStateHandle?.set(
                            TYPE_FOLDER,
                            binding.fragmentDialogContentFileEditText.text.toString()
                        )
                        navigateUp()
                    }
                }
                else -> findNavController().navigateUp()
            }
        }
        binding.fragmentDialogContentFileNegativeButton.setOnClickListener {
            findNavController().navigateUp()
        }
    }
}