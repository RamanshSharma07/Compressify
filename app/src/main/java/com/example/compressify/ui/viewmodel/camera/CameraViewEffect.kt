package com.example.compressify.ui.viewmodel.camera

import android.net.Uri

/**
 * One-time actions the ViewModel tells the Composable to perform
 * */
sealed class CameraViewEffect {
    data class LaunchCamera(val imageUri: Uri) : CameraViewEffect()
    data class RequestPermission(val permission: String) : CameraViewEffect()
    data class NavigateToEditScreen(val imageUri: String) : CameraViewEffect()
    data class ShowToast(val message: String) : CameraViewEffect()
}