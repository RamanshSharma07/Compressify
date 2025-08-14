package com.example.compressify.ui.viewmodel.gallery

import android.net.Uri

sealed class GalleryViewEvent {
    data object ScreenComposed : GalleryViewEvent() // To trigger initial launch
    data class PermissionResult(val permission: String, val isGranted: Boolean) : GalleryViewEvent()
    data class ImageSelected(val uri: Uri?) : GalleryViewEvent()
}