package com.example.compressify.ui.viewmodel.save_image

sealed class PermissionEvent {
    data object RequestPermission : PermissionEvent()
    data class PermissionResult(val isGranted: Boolean) : PermissionEvent()
    // You could add an event if you want the ViewModel to trigger showing a rationale dialog
    // data object RationaleShown : PermissionEvent()
}
