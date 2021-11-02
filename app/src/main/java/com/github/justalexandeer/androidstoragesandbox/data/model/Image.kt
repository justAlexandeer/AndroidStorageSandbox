package com.github.justalexandeer.androidstoragesandbox.data.model

import android.net.Uri
import java.util.*

data class Image(
    val id: Long,
    val displayName: String,
    val dateAdded: Date,
    val contentUri: Uri
)