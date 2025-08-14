package com.example.compressify.ui.view

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.compressify.ui.navigation.AppDestinations
import com.example.compressify.ui.viewmodel.gallery.GalleryViewEffect
import com.example.compressify.ui.viewmodel.gallery.GalleryViewEvent
import com.example.compressify.ui.viewmodel.gallery.GalleryViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun GalleryImagePicker(
    navController: NavController,
    viewModel: GalleryViewModel = viewModel(),
    onPickerDismissed: () -> Unit
){
    val context = LocalContext.current
    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        viewModel.onEvent(GalleryViewEvent.ImageSelected(uri), context)
    }

    // Collect effects from the ViewModel
    LaunchedEffect(key1 = viewModel.viewEffect) {
        viewModel.viewEffect.collectLatest { effect ->
            when (effect) {
//                is GalleryViewEffect.RequestPermission -> {
//                    galleryPermissionLauncher.launch(effect.permission)
//                }
                GalleryViewEffect.LaunchGallery -> {
                    pickImageLauncher.launch("image/*")
                }
                is GalleryViewEffect.NavigateToEditScreen -> {
                    // Navigate and then ensure this picker is dismissed so it doesn't
                    // immediately re-trigger if the user navigates back to the calling screen.
                    navController.navigate("${AppDestinations.SCREEN_EDIT_ROUTE}/${effect.imageUri}")
                    onPickerDismissed() // Notify to hide this picker
                }
                is GalleryViewEffect.ShowToast -> {
                    Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
                }
                GalleryViewEffect.ClosePickerScreen -> {
                    onPickerDismissed() // Notify to hide this picker
                }
            }
        }
    }

    // Trigger the gallery launch request when this Composable enters the composition
    LaunchedEffect(Unit) {
        viewModel.onEvent(GalleryViewEvent.ScreenComposed, context)
    }
}
