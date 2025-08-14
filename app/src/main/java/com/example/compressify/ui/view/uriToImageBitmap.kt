package com.example.compressify.ui.view

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import java.io.IOException

/**
 * Converts a Uri to an ImageBitmap.
 *
 * This function opens an input stream from the content resolver for the given Uri,
 * decodes it into a Android Bitmap, and then converts it to a Jetpack Compose ImageBitmap.
 *
 * @param context The Context used to access the content resolver.
 * @param uri The Uri of the image to convert.
 * @return An ImageBitmap if successful, or null if an error occurs (e.g., file not found, decoding error).
 */
fun uriToImageBitmap(context: Context, uri: Uri): ImageBitmap? {
    return try {
        val inputStream = context.contentResolver.openInputStream(uri)
        inputStream?.use { // Use 'use' to ensure the stream is closed automatically
            BitmapFactory.decodeStream(it)?.asImageBitmap()
        }
    } catch (e: IOException) {
        // Log the error or handle it as needed
        e.printStackTrace()
        null
    } catch (e: SecurityException) {
        // Handle cases where you don't have permission to read the URI
        e.printStackTrace()
        null
    }
}
