package com.example.compressify.ui.viewmodel.camera

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CameraViewModel: ViewModel() {
    private val _capturedImageUri = MutableStateFlow<Uri?>(null)
    val capturedImageUri = _capturedImageUri.asStateFlow()

    private val _viewEffect = Channel<CameraViewEffect>()
    val viewEffect = _viewEffect.receiveAsFlow()

    // We'll need context for file creation and permission checks,
    // but it should be passed in carefully and not held long-term if possible.
    // For permission checks, it's better to delegate the actual check to the View.
    // For file creation, the context is necessary.

    fun onEvent(event: CameraViewEvent, context: Context) { // Pass context when needed
        when (event) {
            is CameraViewEvent.InitializeCameraLaunch -> {
                handleTakePictureClick(context)
            }

            is CameraViewEvent.PermissionResult -> {
                if (event.isGranted) {
                    // Permission was granted, now create file and launch camera
                    val newImageUri = createImageFile(context)
                    _capturedImageUri.value = newImageUri
                    viewModelScope.launch {
                        _viewEffect.send(CameraViewEffect.LaunchCamera(newImageUri))
                    }
                } else {
                    viewModelScope.launch {
                        _viewEffect.send(CameraViewEffect.ShowToast("Camera Permission Denied"))
                    }
                }
            }

            is CameraViewEvent.PictureTakenResult -> {
                if (event.success) {
                    event.uri?.let { uri ->
                        _capturedImageUri.value = uri // Ensure ViewModel's state is updated
                        val imageUriString = Uri.encode(uri.toString())
                        viewModelScope.launch {
                            _viewEffect.send(CameraViewEffect.NavigateToEditScreen(imageUriString))
                        }
                    } ?: run {
                        // This case should ideally not happen if success is true and URI was provided to TakePicture contract
                        _capturedImageUri.value = null
                        viewModelScope.launch {
                            _viewEffect.send(CameraViewEffect.ShowToast("Image capture succeeded but URI is null"))
                        }
                    }
                } else {
                    _capturedImageUri.value = null // Reset if capture failed
                    viewModelScope.launch {
                        _viewEffect.send(CameraViewEffect.ShowToast("Image capture failed or cancelled"))
                    }
                }
            }
        }
    }
    private fun handleTakePictureClick(context: Context) {
        val cameraPermission = Manifest.permission.CAMERA
        when (ContextCompat.checkSelfPermission(context, cameraPermission)) {
            PackageManager.PERMISSION_GRANTED -> {
                val newImageUri = createImageFile(context)
                _capturedImageUri.value = newImageUri // Store URI in ViewModel
                viewModelScope.launch {
                    _viewEffect.send(CameraViewEffect.LaunchCamera(newImageUri))
                }
            }
            else -> {
                // Permission not granted, request it via an effect
                viewModelScope.launch {
                    _viewEffect.send(CameraViewEffect.RequestPermission(cameraPermission))
                }
            }
        }
    }

    private fun createImageFile(context: Context): Uri {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val imageFileName = "JPEG_${timeStamp}_"
        val storageDir = context.getExternalFilesDir("Pictures") // Matches path in file_paths.xml
        val imageFile = File.createTempFile(
            imageFileName,  /* prefix */
            ".jpg",         /* suffix */
            storageDir      /* directory */
        )
        return FileProvider.getUriForFile(
            context,
            "${context.packageName}.provider", // Authority must match AndroidManifest.xml
            imageFile
        )
    }
}