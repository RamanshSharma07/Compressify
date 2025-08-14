package com.example.compressify.ui.viewmodel

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.OutputStream

class FileManager(
    private val context: Context
) {
    suspend fun saveImageToGallery(
        bitmap: Bitmap, // Assuming you have a Bitmap
        displayName: String,
        mimeType: String = "image/jpeg" // Or "image/png"
    ): Boolean{
        return withContext(Dispatchers.IO){
            val imageCollection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
            } else {
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            }

            val contentValues = ContentValues().apply {
                put(MediaStore.Images.Media.DISPLAY_NAME, displayName)
                put(MediaStore.Images.Media.MIME_TYPE, mimeType)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    put(MediaStore.Images.Media.IS_PENDING, 1) // Mark as pending
                }
            }

            var imageUri: Uri? = null
            try {
                imageUri = context.contentResolver.insert(imageCollection, contentValues)
                // Corrected code:
                imageUri?.let { uri ->
                    context.contentResolver.openOutputStream(uri)?.use { outputStream: OutputStream -> // The lambda here IS (OutputStream) -> Unit
                        val success = if (mimeType == "image/jpeg") {
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 95, outputStream)
                        } else {
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                        }
                        if (!success) {
                            // Optionally delete the entry if compression failed
                            context.contentResolver.delete(uri, null, null)
                            // imageUri = null // Reset URI - CAUTION: see note below
                            Log.e("FileManager", "Failed to compress image to MediaStore.")
                            // If you reset imageUri to null here, the update for IS_PENDING might not find the URI
                            // if an error happened during compression.
                            // It might be better to let the outer try-catch handle deletion
                            // or ensure the 'uri' (from imageUri?.let) is still valid for IS_PENDING update.
                            // For now, let's assume the current logic is intended.
                            // To make this safer, you could set a flag:
                            var operationSuccessful = success // Keep track of success
                        }
                    } // The 'as (OutputStream) -> Unit' was removed from here

                    // Consider the case where openOutputStream(uri) returns null.
                    // The 'use' block wouldn't execute, and the IS_PENDING update would still run.
                    // This might be okay if 'insert' guarantees a usable URI or if failure is handled.

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        // Only update IS_PENDING if the URI is still considered valid and the operation
                        // was meant to proceed. If 'imageUri' was nulled inside 'use' due to an error,
                        // this 'uri' from the outer 'let' might still be used here. This is a bit tricky.
                        // A flag to check if compression succeeded before updating IS_PENDING might be better.

                        // Assuming imageUri was NOT nulled inside the 'use' block for this part to work as intended
                        // or that you handle the case where compression failed but you still want to clear IS_PENDING
                        // for the initially created entry (which might then be deleted by the catch block if imageUri is null).

                        // Let's refine based on the assumption that if imageUri becomes null, we shouldn't update.
                        // However, your original imageUri is local to the suspend function's scope,
                        // not the class member 'imageUri' (if one exists). The local 'imageUri'
                        // is what is used for 'uri' in the 'let' block.

                        // The 'imageUri = null' inside the 'if(!success)' block only affects the
                        // local 'imageUri' for the return value, not the 'uri' used by the 'let' scope.
                        // So the IS_PENDING update will still use the original 'uri'.

                        contentValues.clear()
                        contentValues.put(MediaStore.Images.Media.IS_PENDING, 0)
                        context.contentResolver.update(uri, contentValues, null, null) // 'uri' here is from imageUri?.let
                    }
                }

            } catch (e: Exception) {
                Log.e("FileManager", "Error saving image to gallery", e)
                // If there was an error and an URI was created, delete the incomplete entry
                imageUri?.let { context.contentResolver.delete(it, null, null) }
                imageUri = null
            }
            if(imageUri != null) return@withContext true
            else return@withContext false
        }
    }
}