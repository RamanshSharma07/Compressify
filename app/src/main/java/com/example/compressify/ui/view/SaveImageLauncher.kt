package com.example.compressify.ui.view

import android.Manifest
import android.app.Activity
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.compressify.ui.viewmodel.FileManager
import com.example.compressify.ui.viewmodel.save_image.PermissionEvent
import com.example.compressify.ui.viewmodel.save_image.PermissionState
import com.example.compressify.ui.viewmodel.save_image.PermissionViewModel

@Composable
fun SaveImageLauncher(
    permissionViewModel: PermissionViewModel = viewModel(),
    imageBitmap: ImageBitmap,
    onImageSaved: (Boolean) -> Unit
){
    val context = LocalContext.current
    val activity = context as? Activity // Needed for shouldShowRequestPermissionRationale
    var saveImage by remember { mutableStateOf(false) }

    val permissionState by permissionViewModel.permissionState.collectAsState()

    // Launcher for the permission request
    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        permissionViewModel.handleEvent(PermissionEvent.PermissionResult(isGranted))
    }
    val fileManager = FileManager(context)
    var isImageSaved by remember { mutableStateOf(false) }

    // Effect to update rationale status if permission is denied
    LaunchedEffect(permissionState) {
        if (permissionState is PermissionState.Denied && activity != null) {
            val shouldShow = ActivityCompat.shouldShowRequestPermissionRationale(
                activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE // Or get from ViewModel if made generic
            )
            // Only update if the current state in VM doesn't match
            if ((permissionState as PermissionState.Denied).shouldShowRationale != shouldShow) {
                permissionViewModel.updateDeniedStateWithRationale(shouldShow)
            }
        }
    }

    LaunchedEffect(saveImage) {
        if(permissionState == PermissionState.Granted){
            Toast.makeText(context, "Your image is being saved", Toast.LENGTH_SHORT).show()
            isImageSaved = fileManager.saveImageToGallery(
                imageBitmap.asAndroidBitmap(),
                "compressed_image"
            )
            if(isImageSaved){
                Toast.makeText(context, "The image has been saved", Toast.LENGTH_SHORT).show()
                onImageSaved(false)
            }
        }
    }

    Column {
        when (val state = permissionState) {
            is PermissionState.Undefined -> {
                Text("Checking permission...")
                // You might trigger an initial check here if not done in init of VM
            }
            is PermissionState.Granted -> {
                saveImage = true
            }
            is PermissionState.Denied -> {
                Text("Permission Denied.")
                if (state.shouldShowRationale) {
                    Text("We need this permission to save files. Please grant it.")
                    // Show a more prominent rationale UI if desired
                }
                Button(onClick = {
                    if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) { // Only request if applicable
                        requestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    } else {
                        // For Android 10+, this specific permission isn't requested for general saving.
                        // You might update the ViewModel state to Granted or an "NotApplicable" state.
                        // For this example, let's assume if it's Q+, it means we don't proceed with THIS request.
                        permissionViewModel.checkPermissionStatus() // Re-evaluate for Q+
                    }
                }) {
                    Text("Request Write Permission")
                }
            }
        }
    }
}


