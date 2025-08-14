package com.example.compressify.ui.viewmodel.gallery

sealed class GalleryViewEffect {
    object LaunchGallery : GalleryViewEffect()
    data class NavigateToEditScreen(val imageUri: String) : GalleryViewEffect()
    data class ShowToast(val message: String) : GalleryViewEffect()
    object ClosePickerScreen
}