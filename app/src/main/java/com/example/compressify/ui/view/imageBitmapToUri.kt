package com.example.compressify.ui.view

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

fun imageBitmapToUri(
    context: Context,
    imageBitmap: ImageBitmap,
    fileName: String = "cropped_image.png"
): Uri? {
    // 1. Convert ImageBitmap to Android Bitmap
    val androidBitmap = imageBitmap.asAndroidBitmap()

    // 2. Define the cache file
    val cachePath = File(context.cacheDir, "images")
    cachePath.mkdirs() // Create the directory if it doesn't exist
    val file = File(cachePath, fileName)

    try {
        // 3. Save the bitmap to the file
        val fileOutputStream = FileOutputStream(file)
        val format = if (fileName.endsWith(".png", ignoreCase = true)) {
            Bitmap.CompressFormat.PNG
        } else {
            Bitmap.CompressFormat.JPEG
        }
        androidBitmap.compress(format, 100, fileOutputStream) // Adjust quality as needed
        fileOutputStream.close()

        // 4. Get a content URI using FileProvider
        val authority = "${context.packageName}.provider"
        return FileProvider.getUriForFile(context, authority, file)

    } catch (e: IOException) {
        e.printStackTrace()
        // Handle the error appropriately, e.g., show a toast or log
        return null
    }
}
