package com.example.compressify.ui.view

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.compressify.ui.navigation.AppDestinations
import com.example.compressify.ui.theme.MyAppTheme
import com.example.compressify.ui.viewmodel.ImageCompressor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun ShowScreen(
    modifier: Modifier,
    navController: NavHostController,
    originalImageUri: Uri,
    compressionQuality: Int,
    sizeOfImage: Int
){
    var compressedImageData by remember {
        mutableStateOf<ByteArray?>(null)
    }

    val context = LocalContext.current
    val imageCompressor = ImageCompressor(context)
    var isLoading by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }
    var imageBitmap by remember { mutableStateOf<ImageBitmap?>(null) }
    val imageBitmapGetter = DisplayImageFromByteArray()
    var saveImage by remember { mutableStateOf(false) }
    var shareImage by remember { mutableStateOf(false) }
    var newSizeOfImage by remember { mutableStateOf<Long?>(null) }
    var reductionPercentage by remember {
        mutableStateOf<Long?>(null)
    }


    LaunchedEffect(originalImageUri, compressionQuality) {
        isLoading = true
        val result: ByteArray? = withContext(Dispatchers.IO) {
            imageCompressor.compressImageToThreshold(
                imageUri = originalImageUri,
                thresholdSizeBytes = (compressionQuality.toLong() * 1024)
            )
        }
        compressedImageData = result
        newSizeOfImage = compressedImageData?.size?.div(1024L)// Update the state variable
        reductionPercentage = (sizeOfImage-newSizeOfImage!!)*100/sizeOfImage
        isLoading = false
    }

    LaunchedEffect(compressedImageData){
        isLoading = imageBitmapGetter.fromByteArrayToBimap(
            compressedImageData,
            onError = {
                error = it
            },
            onSuccess = {
                imageBitmap = it
            }
        )
    }

    LaunchedEffect(shareImage){
        if(shareImage && imageBitmap != null){
//            Toast.makeText(context, "starting share", Toast.LENGTH_SHORT).show()
            shareImage(context, imageBitmap)
//            Toast.makeText(context, "share ended", Toast.LENGTH_SHORT).show()
        }
    }



    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ){
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp, 0.dp)
        ){
            CustomTopAppBar()

            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ){

                when{
//                    isLoading -> {
//                        CircularProgressIndicator(modifier = Modifier.fillMaxSize())
//                    }
                    imageBitmap != null -> {
                        Image(
                            bitmap = imageBitmap!!,
                            contentDescription = "Image from ByteArray",
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
//                    error != null -> {
//                        Text("Error: $error")
//                    }
                    else -> {
//                        Text("Image data is being processed or is not available.")
                        CircularProgressIndicator(modifier = Modifier.fillMaxSize())
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                CompressionDetails("Original Size", "${sizeOfImage}KB")
                if(newSizeOfImage!=null){

                    CompressionDetails("Size After Reduction", "${newSizeOfImage}KB")
                }
                CompressionDetails("Reduction Percentage", "${reductionPercentage}%")

                Spacer(modifier = Modifier.height(16.dp))


                Button(
                    onClick = {
                        saveImage = true
                    },
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .height(48.dp)
                        .shadow(4.dp, RoundedCornerShape(50)),
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary,
                        contentColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text("Download Image", fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        shareImage = !shareImage
                    },
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .height(48.dp)
                        .shadow(4.dp, RoundedCornerShape(50)),
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.secondary
                    )
                ) {
                    Text("Share Image", fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        navController.navigate(
                            AppDestinations.SCREEN_HOME_ROUTE
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .height(48.dp)
                        .shadow(4.dp, RoundedCornerShape(50)),
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.secondary
                    )
                ) {
                    Text("Go Back to Home Page", fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Advertisement",
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    fontSize = 12.sp,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(16.dp))

            }
        }
    }
    if(saveImage){
        SaveImageLauncher(
            imageBitmap = imageBitmap!!,
            onImageSaved = {
                saveImage = it
            }
        )
    }
}


suspend fun shareImage(context: Context, imageBitmap: ImageBitmap?) {
    imageBitmap ?: return // Do nothing if imageBitmap is null

    // Determine the desired file name and extension for sharing
    val fileNameForSharing = "shared_image.png" // Or make it dynamic, e.g., based on current time

    val imageUri = withContext(Dispatchers.IO) {
        // Pass the desired fileName to your imageBitmapToUri function
        imageBitmapToUri(context, imageBitmap, fileNameForSharing)
    }

    imageUri?.let { uri ->
        val intent = Intent(Intent.ACTION_SEND)

        // *** THIS IS WHERE YOU SET THE INTENT TYPE BASED ON THE URI/FILENAME ***
        val type = if (fileNameForSharing.endsWith(".png", ignoreCase = true)) {
            "image/png"
        } else if (fileNameForSharing.endsWith(".jpg", ignoreCase = true) || fileNameForSharing.endsWith(".jpeg", ignoreCase = true)) {
            "image/jpeg"
        } else {
            // A generic image MIME type if the extension is different or unknown
            // Or you could get it from the ContentResolver if needed for more complex Uris
            Log.w("shareImage", "Unknown image type for $fileNameForSharing, using image/*.")
            "image/*"
        }
        intent.type = type

        intent.putExtra(Intent.EXTRA_STREAM, uri)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        // Optional: Add a subject for email apps, etc.
        // intent.putExtra(Intent.EXTRA_SUBJECT, "Check out this image!")
        // Optional: Add some text to accompany the image
        // intent.putExtra(Intent.EXTRA_TEXT, "Image shared from My App")

        context.startActivity(Intent.createChooser(intent, "Share Image"))
    } ?: run {
        // Handle error: e.g., show a toast message
        Log.e("ShareImage", "Failed to get URI for sharing")
        // You might want to update an error state variable to show in the UI
        // errorState.value = "Could not save image for sharing."
    }
}


@Preview
@Composable
fun PreviewShowScreen() {
    MyAppTheme {
//        ShowScreen(Modifier, NavHostController(LocalContext.current), Uri.parse(""), 50,)
    }
}