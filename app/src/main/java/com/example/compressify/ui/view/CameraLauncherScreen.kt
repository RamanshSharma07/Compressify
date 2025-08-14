package com.example.compressify.ui.view

import android.Manifest
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.compressify.ui.navigation.AppDestinations
import com.example.compressify.ui.viewmodel.camera.CameraViewEffect
import com.example.compressify.ui.viewmodel.camera.CameraViewEvent
import com.example.compressify.ui.viewmodel.camera.CameraViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun CameraLauncherScreen(
    navController: NavHostController,
    viewModel: CameraViewModel = viewModel()
){
    val context = LocalContext.current
//    val capturedImageUriFromViewModel by viewModel.capturedImageUri.collectAsState()

    // --- Permission Launchers ---
    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        viewModel.onEvent(
            CameraViewEvent.PermissionResult(Manifest.permission.CAMERA, isGranted),
            context
        )
    }

    val takePictureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success: Boolean ->
        val currentUriForPicture = viewModel.capturedImageUri.value
        viewModel.onEvent(
            CameraViewEvent.PictureTakenResult(success, currentUriForPicture),
            context
        )
    }

    // --- Effect Collection ---
    LaunchedEffect(key1 = viewModel.viewEffect) {
        viewModel.viewEffect.collectLatest { effect ->
            when (effect) {
                is CameraViewEffect.LaunchCamera -> {
                    takePictureLauncher.launch(effect.imageUri)
                }
                is CameraViewEffect.RequestPermission -> {
                    cameraPermissionLauncher.launch(effect.permission)
                }
                is CameraViewEffect.NavigateToEditScreen -> {
                    // Important: Ensure this navigation doesn't cause re-composition
                    // of CameraLauncherScreen in a loop if it's still in the backstack
                    // and visible/active.
                    navController.navigate("${AppDestinations.SCREEN_EDIT_ROUTE}/${effect.imageUri}") {
                        // Example: popUpTo(AppDestinations.HOME_SCREEN_ROUTE) { inclusive = false }
                        // to prevent coming back here immediately after navigation.
                        // Adjust based on your navigation graph.
                    }
                }
                is CameraViewEffect.ShowToast -> {
                    Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    LaunchedEffect(Unit) {
        viewModel.onEvent(CameraViewEvent.InitializeCameraLaunch, context)
    }

}
