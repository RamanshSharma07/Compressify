package com.example.compressify.ui.view

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions

@Composable
fun CropScreenAndroid(
    imageUri: Uri,
    onDismissed: () -> Unit,
    onImageCropped: (uri: Uri?) -> Unit
){
    // Launcher for the crop activity
    val cropImageLauncher = rememberLauncherForActivityResult(
        contract = CropImageContract()
    ) { result ->
        if (result.isSuccessful) {
            // Cropped image URI
            onImageCropped(result.uriContent)
        } else {
            // Handle crop error or cancellation
            val exception = result.error
            Log.e("CropScreenAndroid", "Crop failed", exception)
            onDismissed()
        }
    }

    LaunchedEffect(Unit) {
        val cropOptions = CropImageOptions()
        val cropImageContractOptions = CropImageContractOptions(
            uri = imageUri,
            cropImageOptions = cropOptions
        )
        cropImageLauncher.launch(cropImageContractOptions)
    }
}

