package com.github.justalexandeer.androidstoragesandbox.ui.internal

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import java.io.File
import java.util.concurrent.Flow
import javax.inject.Inject

class InternalFragmentViewModel: ViewModel() {

    var currentPath: MutableStateFlow<File?> = MutableStateFlow(null)

    fun updateCurrentPath(file: File) {
        currentPath.value = file
    }

}