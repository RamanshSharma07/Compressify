package com.example.compressify.ui.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.compressify.R

@Composable
fun HomeScreen(modifier: Modifier, navController: NavHostController) {
    var cameraLauncherScreenVariable by remember {
        mutableStateOf(false)
    }

    var galleryLauncherScreenVariable by remember {
        mutableStateOf(false)
    }


    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ){

            CustomTopAppBar()

            Spacer(modifier = Modifier.height(32.dp))

            Column(
                modifier = modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Button(
                    onClick = {
                        cameraLauncherScreenVariable = true
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
                    Text("Open Camera", fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        galleryLauncherScreenVariable = true
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
                    Text("Open Gallery", fontWeight = FontWeight.Bold)
                }
            }
            Spacer(modifier = Modifier.height(48.dp))

//            Text(
//                text = "Advertisement",
//                modifier = Modifier.align(Alignment.CenterHorizontally),
//                fontSize = 12.sp,
//                color = Color.Gray
//            )
//
//            Spacer(modifier = Modifier.height(16.dp))
//
//            Box(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .weight(1f)
//                    .background(Color(0xFFE0F2F1)),
//                contentAlignment = Alignment.Center
//            ) {
//                Image(
//                    painter = painterResource(R.drawable.demo_add),
//                    contentDescription = "Framed photo",
//                    modifier = Modifier.size(200.dp)
//                )
//            }
        }

        if (cameraLauncherScreenVariable){
            CameraLauncherScreen(navController)
        }
        if(galleryLauncherScreenVariable){
            GalleryImagePicker(navController){
                galleryLauncherScreenVariable = false
            }
        }
    }
}
