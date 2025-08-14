package com.example.compressify.ui.viewmodel.camera

import android.net.Uri

/**
 * Events the Composable can send to the ViewModel
 * */
sealed class CameraViewEvent {

    data object InitializeCameraLaunch : CameraViewEvent()
//    data object TakePictureClicked : CameraViewEvent()
    data class PermissionResult(val permission: String, val isGranted: Boolean) : CameraViewEvent()
    data class PictureTakenResult(val success: Boolean, val uri: Uri?) : CameraViewEvent()
}