package com.github.justalexandeer.androidstoragesandbox.data.model

import com.github.justalexandeer.androidstoragesandbox.util.FilesType
import java.io.File

data class CustomFile(
    val file: File,
    val type: FilesType
)
