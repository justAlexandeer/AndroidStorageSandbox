package com.github.justalexandeer.androidstoragesandbox.ui.shared

import android.app.Application
import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Message
import android.provider.MediaStore
import android.provider.MediaStore.Images
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.justalexandeer.androidstoragesandbox.data.model.Image
import dagger.hilt.android.internal.Contexts.getApplication
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URI
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class SharedFragmentViewModel @Inject constructor(
    @ApplicationContext val context: Context
) : ViewModel() {

    var currentListOfImages: MutableStateFlow<List<Image>?> = MutableStateFlow(null)

    fun loadImage() {
        if (currentListOfImages != null) {
            viewModelScope.launch {
                currentListOfImages.value = queryImages()
            }
        }
    }

    private suspend fun queryImages(): List<Image> {
        val images = mutableListOf<Image>()
        withContext(Dispatchers.IO) {
            var collection: Uri = Images.Media.EXTERNAL_CONTENT_URI
            val projection = arrayOf(
                Images.Media._ID,
                Images.Media.DISPLAY_NAME,
                Images.Media.DATE_ADDED
            )
            val sortOrder = "${Images.Media.DATE_ADDED} DESC"
            context.contentResolver.query(
                collection,
                projection,
                null,
                null,
                sortOrder
            )?.use { cursor ->
                val idColumn = cursor.getColumnIndexOrThrow(Images.Media._ID)
                val dateModifiedColumn =
                    cursor.getColumnIndexOrThrow(Images.Media.DATE_ADDED)
                val displayNameColumn =
                    cursor.getColumnIndexOrThrow(Images.Media.DISPLAY_NAME)
                while (cursor.moveToNext()) {
                    val id = cursor.getLong(idColumn)
                    val dateModified =
                        Date(TimeUnit.SECONDS.toMillis(cursor.getLong(dateModifiedColumn)))
                    val displayName = cursor.getString(displayNameColumn)
                    val contentUri = ContentUris.withAppendedId(
                        Images.Media.EXTERNAL_CONTENT_URI,
                        id
                    )
                    val image = Image(id, displayName, dateModified, contentUri)
                    images += image
                }
            }
        }
        return images
    }

    companion object {
        private const val TAG = "SharedFragmentViewModel"
    }
}