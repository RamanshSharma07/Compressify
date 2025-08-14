package com.example.compressify.ui.view

import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DisplayImageFromByteArray() {
    suspend fun fromByteArrayToBimap(imageData: ByteArray?, onError: (String) -> Unit, onSuccess: (ImageBitmap) -> Unit): Boolean{
        var imageBitmap: ImageBitmap?
        var isLoading = false
        var error:String? = null


        if (imageData == null || imageData.isEmpty()) {
            imageBitmap = null
            error = "No image data provided."
            isLoading = false
            onError(error)
            return isLoading
        }
        isLoading = true
        error = null
        try {
            // Decode ByteArray to Android Bitmap on a background thread
            val androidBitmap = withContext(Dispatchers.Default) {
                BitmapFactory.decodeByteArray(imageData, 0, imageData.size)
            }

            if (androidBitmap != null) {
                // Convert Android Bitmap to Compose ImageBitmap on the main thread
                // (asImageBitmap() is not computationally heavy itself)
                imageBitmap = androidBitmap.asImageBitmap()
                onSuccess(imageBitmap)
                isLoading = false
                return isLoading
            } else {
                error = "Failed to decode image data."
                onError(error)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            error = "Error decoding image: ${e.localizedMessage}"
            onError(error)
        } finally {
            isLoading = false
        }
        return isLoading
    }

}

