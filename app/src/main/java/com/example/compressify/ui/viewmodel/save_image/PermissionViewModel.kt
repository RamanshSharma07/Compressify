package com.example.compressify.ui.viewmodel.save_image

import android.Manifest
import android.app.Application
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// Assuming PermissionState and PermissionEvent are defined as above

class PermissionViewModel(application: Application) : AndroidViewModel(application) {

    private val _permissionState = MutableStateFlow<PermissionState>(PermissionState.Undefined)
    val permissionState: StateFlow<PermissionState> = _permissionState.asStateFlow()

    // Specific permission to handle by this instance (can be made more generic)
    // For this example, hardcoding WRITE_EXTERNAL_STORAGE
    private val permissionToRequest = Manifest.permission.WRITE_EXTERNAL_STORAGE

    init {
        // Initial check when ViewModel is created
        checkPermissionStatus()
    }

    fun handleEvent(event: PermissionEvent) {
        when (event) {
            is PermissionEvent.RequestPermission -> {
                // This event would be triggered by UI, but the actual launch of system dialog
                // needs to happen from the Activity/Composable. The ViewModel prepares the state.
                // The UI will observe permissionState and decide if it needs to launch the system dialog.
                // Or, more directly, the UI calls a method that says "I'm about to request".
                // For simplicity here, let's assume the UI will trigger the system dialog
                // after checking the state provided by this ViewModel.
                // Alternatively, the ViewModel could expose a "triggerSystemPermissionRequest" event.
            }
            is PermissionEvent.PermissionResult -> {
                if (event.isGranted) {
                    _permissionState.update { PermissionState.Granted }
                } else {
                    // After a denial, re-check to see if rationale should be shown for next time.
                    // The actual `shouldShowRequestPermissionRationale` needs an Activity.
                    // This part is tricky to fully abstract into a ViewModel without passing Activity context
                    // directly or using a callback mechanism.
                    // For now, let's simplify and assume the UI handles rationale display
                    // based on the updated Denied state.
                    _permissionState.update { PermissionState.Denied(shouldShowRationale = false) } // Placeholder
                    // To properly set shouldShowRationale, the Activity/Fragment would need to call
                    // a method on the ViewModel like `updatePermissionDeniedStatus(shouldShowRationale: Boolean)`
                }
            }
        }
    }

    fun checkPermissionStatus() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P && permissionToRequest == Manifest.permission.WRITE_EXTERNAL_STORAGE) {
            // On Android 10+ WRITE_EXTERNAL_STORAGE is largely not applicable for typical saving.
            // Consider it "granted" for the purpose of this specific permission flow if the OS is newer.
            _permissionState.update { PermissionState.Granted }
            return
        }

        when (ContextCompat.checkSelfPermission(
            getApplication(),
            permissionToRequest
        )) {
            PackageManager.PERMISSION_GRANTED -> {
                _permissionState.update { PermissionState.Granted }
            }
            PackageManager.PERMISSION_DENIED -> {
                // Here, ideally, you'd check shouldShowRequestPermissionRationale.
                // This requires an Activity, which ViewModels shouldn't hold directly.
                // The UI (Activity/Composable) will be responsible for this check and can inform the ViewModel.
                _permissionState.update { PermissionState.Denied(shouldShowRationale = false) } // Initial denial
            }
        }
    }

    /**
     * Call this from your Composable/Activity after a permission is denied
     * to update the rationale status.
     */
    fun updateDeniedStateWithRationale(shouldShow: Boolean) {
        if (_permissionState.value is PermissionState.Denied || _permissionState.value is PermissionState.Undefined) {
            _permissionState.update { PermissionState.Denied(shouldShowRationale = shouldShow) }
        }
    }
}
