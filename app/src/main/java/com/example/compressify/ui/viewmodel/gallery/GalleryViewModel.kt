package com.example.compressify.ui.viewmodel.gallery

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class GalleryViewModel: ViewModel() {
    private val _viewEffect = Channel<GalleryViewEffect>()
    val viewEffect = _viewEffect.receiveAsFlow()

    fun onEvent(event: GalleryViewEvent, context: Context) {
        when (event) {
            is GalleryViewEvent.ScreenComposed -> {
                handleGalleryButtonClick(context)
            }
            is GalleryViewEvent.PermissionResult -> {
                if (event.isGranted) {
                    viewModelScope.launch {
                        _viewEffect.send(GalleryViewEffect.LaunchGallery)
                    }
                } else {
                    viewModelScope.launch {
                        _viewEffect.send(GalleryViewEffect.ShowToast("Permission Denied to access gallery."))
                    }
                }
            }
            is GalleryViewEvent.ImageSelected -> {
                event.uri?.let { uri ->
                    val imageUriString = Uri.encode(uri.toString())
                    viewModelScope.launch {
                        _viewEffect.send(GalleryViewEffect.NavigateToEditScreen(imageUriString))
                    }
                } ?: run {
                    viewModelScope.launch {
                        _viewEffect.send(GalleryViewEffect.ShowToast("No image selected."))
                    }
                }
            }
        }
    }

    private fun handleGalleryButtonClick(context: Context) {
        viewModelScope.launch {
            _viewEffect.send(GalleryViewEffect.LaunchGallery)
        }
    }
}