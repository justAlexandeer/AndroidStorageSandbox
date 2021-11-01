package com.github.justalexandeer.androidstoragesandbox.ui.internal

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.PopupMenu
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.github.justalexandeer.androidstoragesandbox.databinding.FragmentInternalBinding
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.github.justalexandeer.androidstoragesandbox.R
import com.github.justalexandeer.androidstoragesandbox.ui.base.FileAdapter
import com.github.justalexandeer.androidstoragesandbox.util.TYPE_FILE
import com.github.justalexandeer.androidstoragesandbox.util.TYPE_FOLDER
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import java.io.File

@AndroidEntryPoint
class InternalFragment : Fragment() {

    private lateinit var binding: FragmentInternalBinding
    private val viewModel: InternalFragmentViewModel by viewModels()
    private val adapter = FileAdapter { file ->
        when {
            file.isFile -> {
                Toast.makeText(requireContext(), "Не умею окрывать файлы", Toast.LENGTH_SHORT)
                    .show()
            }
            file.isDirectory -> {
                viewModel.updateCurrentPath(file)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

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

        setupViewModel()
        setupUI()
        setupObservers()
    }

    private fun setupObservers() {
        lifecycleScope.launchWhenCreated {
            viewModel.currentPath.collect {
                it?.let { currentPath ->
                    binding.internalFragmentPathTextView.text = currentPath.absolutePath
                    val listOfFiles = mutableListOf<File>()
                    currentPath.list()?.let { list ->
                        list.forEach { listElement ->
                            val file = File(currentPath.absolutePath, listElement)
                            listOfFiles.add(file)
                        }
                    }
                    adapter.submitList(listOfFiles)
                }
            }
        }
        val navBackStackEntry = findNavController().getBackStackEntry(R.id.internal_fragment)
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                if (navBackStackEntry.savedStateHandle.contains(TYPE_FILE)) {
                    val result = navBackStackEntry.savedStateHandle.get<String>(TYPE_FILE)
                    result?.let {
                        createFile(result)
                        navBackStackEntry.savedStateHandle.set(TYPE_FILE, null)
                    }
                }
                if (navBackStackEntry.savedStateHandle.contains(TYPE_FOLDER)) {
                    val result = navBackStackEntry.savedStateHandle.get<String>(TYPE_FOLDER)
                    result?.let {
                        createFolder(result)
                        navBackStackEntry.savedStateHandle.set(TYPE_FOLDER, null)
                    }
                }
            }
        }
        navBackStackEntry.lifecycle.addObserver(observer)
        viewLifecycleOwner.lifecycle.addObserver(LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_DESTROY) {
                navBackStackEntry.lifecycle.removeObserver(observer)
            }
        })
    }

    private fun setupViewModel() {
        if (viewModel.currentPath.value == null) {
            viewModel.currentPath.value = requireContext().filesDir
        }
    }

    private fun setupUI() {
        binding.internalFragmentRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.internalFragmentRecyclerView.adapter = adapter
        binding.internalFragmentButtonBack.setOnClickListener {
            onBackButtonClick()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.internal_fragment_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.internal_fragment_menu_create -> {
                createAndShowPopupMenu(activity?.findViewById(R.id.internal_fragment_menu_create))
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }

    }

    private fun createAndShowPopupMenu(view: View?) {
        view?.let {
            val popupMenu = PopupMenu(requireContext(), view)
            popupMenu.inflate(R.menu.internal_fragment_menu_create)

            popupMenu.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.internal_fragment_menu_create_file -> {
                        val action =
                            InternalFragmentDirections.actionInternalFragmentToFileContentDialogFragment(
                                "file"
                            )
                        findNavController().navigate(action)
                        true
                    }
                    R.id.internal_fragment_menu_create_folder -> {
                        val action =
                            InternalFragmentDirections.actionInternalFragmentToFileContentDialogFragment(
                                "folder"
                            )
                        findNavController().navigate(action)
                        true
                    }
                    else -> super.onOptionsItemSelected(it)
                }
            }
            popupMenu.show()
        }
    }

    private fun createFile(name: String) {
        when (name.isEmpty()) {
            false -> {
                val file = File(viewModel.currentPath.value, name)
                if (file.createNewFile()) {
                    Toast.makeText(requireContext(), "Файл создался", Toast.LENGTH_SHORT).show()
                    refreshList()
                } else {
                    Toast.makeText(requireContext(), "Файл не создался", Toast.LENGTH_SHORT).show()
                }
            }
            true -> {
                Toast.makeText(
                    requireContext(),
                    requireContext().getString(R.string.internal_fragment_type_file_name),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun createFolder(name: String) {
        when (name.isEmpty()) {
            false -> {
                val file = File(viewModel.currentPath.value, name)
                if (file.mkdir()) {
                    Toast.makeText(requireContext(), "Папка создалась", Toast.LENGTH_SHORT).show()
                    refreshList()
                } else {
                    Toast.makeText(requireContext(), "Папка не создалась", Toast.LENGTH_SHORT)
                        .show()
                }
            }
            true -> {
                Toast.makeText(
                    requireContext(),
                    requireContext().getString(R.string.internal_fragment_type_folder_name),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun onBackButtonClick() {
        viewModel.currentPath.value?.let {
            if (it != requireContext().filesDir) {
                viewModel.currentPath.value = File(it.parent!!)
            } else {
                Toast.makeText(
                    requireContext(),
                    requireContext().getText(R.string.internal_fragment_back_folder_warning),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun refreshList() {
        val listOfFiles = mutableListOf<File>()
        val currentPath = viewModel.currentPath.value
        currentPath?.list()?.let { list ->
            list.forEach { listElement ->
                val file = File(currentPath.absolutePath, listElement)
                listOfFiles.add(file)
            }
        }
        adapter.submitList(listOfFiles)
    }

    companion object {
        private const val TAG = "InternalFragment"
    }
}