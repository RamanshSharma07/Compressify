package com.example.compressify.ui.viewmodel.save_image

sealed class PermissionState {
    data object Undefined : PermissionState() // Initial state before checking
    data object Granted : PermissionState()
    data class Denied(val shouldShowRationale: Boolean) : PermissionState()
}
