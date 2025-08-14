package com.example.compressify.ui.view

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.compressify.ui.navigation.AppDestinations
import com.example.compressify.ui.theme.MyAppTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException

@Composable
fun EditScreen(
    modifier: Modifier,
    navController: NavHostController,
    dataFromA: String?
){

    var showCropScreen by remember {
        mutableStateOf(false)
    }

    var imageUri = remember(dataFromA) {
        dataFromA?.let { encodedUriString ->
            try {
                Uri.decode(encodedUriString).toUri()
            } catch (e: Exception) {
                null // Parsing failed
            }
        }
    }

    val context = LocalContext.current

    var sliderVisible by remember {
        mutableStateOf(false)
    }
    var currentSliderValue by remember {
        mutableFloatStateOf(0f)
    }

    var sizeOfImage by remember { mutableStateOf<Long?>(null) }
    LaunchedEffect(imageUri) {
        if (imageUri != null) {
            // Switch to a background thread for the file operation
            val size = withContext(Dispatchers.IO) {
                getImageSizeInKB(context, imageUri!!)
            }
            sizeOfImage = size
        } else {
            sizeOfImage = null // Reset if imageUri is null
        }
    }

    Surface(
        modifier = modifier
            .fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ){
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp, 0.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            CustomTopAppBar()

            Spacer(modifier = Modifier.height(8.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                when {
                    imageUri != null -> {
                        AsyncImage(
                            model = imageUri,
                            contentDescription = "Received Image",
                            modifier = Modifier
                                .padding(16.dp, 18.dp)
                                .fillMaxWidth()
                        )
                    }
                    else -> {
                        CircularProgressIndicator(modifier = Modifier.fillMaxSize())
                    }
                }

                if (sliderVisible) {
                    Text(text = "Select the size of compressed image:")
                    Slider(
                        value = currentSliderValue,
                        onValueChange = { newValue ->
                            currentSliderValue = newValue
                            // Value is updated live here, but we only "capture" it on button click
                        },
                        valueRange = 0f..95f
                    )
                    Text(text = "Estimated size after compression : ${(sizeOfImage!!-((currentSliderValue.toInt())*sizeOfImage!!/100).toInt())}KB")
                } else {
                    Spacer(modifier = Modifier.weight(1f))
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Button(
                        onClick = {
//                            Toast.makeText(context, "Button clicked", Toast.LENGTH_SHORT).show()
                            showCropScreen = true
                        },
                        modifier = Modifier
                            .height(48.dp)
                            .shadow(4.dp, RoundedCornerShape(50)),
                        shape = RoundedCornerShape(50),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.secondary
                        )
                    ) {
                        Text("Crop", fontWeight = FontWeight.Bold)
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    Button(
                        onClick = {
                            sliderVisible = !sliderVisible
                        },
                        modifier = Modifier
                            .height(48.dp)
                            .shadow(4.dp, RoundedCornerShape(50)),
                        shape = RoundedCornerShape(50),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.secondary
                        )
                    ) {
                        Text("Compress", fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        imageUri?.let { uriToPass ->
                            val encodedUri = Uri.encode(uriToPass.toString())
                            // Pass the currentSliderValue directly
                            navController.navigate(
                                "${AppDestinations.SCREEN_SHOW_ROUTE}/$encodedUri/${(sizeOfImage!!-((currentSliderValue.toInt())*sizeOfImage!!/100).toInt())}/$sizeOfImage"
//                                "${AppDestinations.SCREEN_SHOW_ROUTE}/$encodedUri/${(100-currentSliderValue)}"

                            )
                        } ?: run {
                            // Handle case where imageUri is null (e.g., show a Toast)
//                            Toast.makeText(LocalContext.current, "Please select an image first", Toast.LENGTH_SHORT).show()
                        }
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
                    Text("Next", fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        if(showCropScreen){
            Toast.makeText(context, "crop button is pressed", Toast.LENGTH_SHORT).show()
            CropScreenAndroid(
                imageUri = imageUri!!,
                onDismissed = {
                    showCropScreen = false
                    Toast.makeText(context, "Dismissed", Toast.LENGTH_SHORT).show()
                },
                onImageCropped = {
                    imageUri = it
                    showCropScreen = false
                    Toast.makeText(context, "Succeeded", Toast.LENGTH_SHORT).show()
                }
            )
        }
    }
}


fun getImageSizeInKB(context: Context, uri: Uri): Long? {
    return try {
        context.contentResolver.openInputStream(uri)?.use { inputStream ->
            val bytes = inputStream.readBytes()
            bytes.size / 1024L // Convert bytes to KB
        }
    } catch (e: IOException) {
        e.printStackTrace()
        null // Handle error, e.g., file not found or couldn't be read
    } catch (e: SecurityException) {
        e.printStackTrace()
        null // Handle error, e.g., permission denial
    }
}

@Preview
@Composable
fun PreviewEditScreen() {
    MyAppTheme {
        EditScreen(Modifier, NavHostController(LocalContext.current), null)
    }
}