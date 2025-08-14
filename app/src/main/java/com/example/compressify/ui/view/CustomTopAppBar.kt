package com.example.compressify.ui.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.compressify.R


@Composable
fun CustomTopAppBar(){

    var openSettings by remember { mutableStateOf(false) }

    if(openSettings){
        Settings {
            openSettings = !openSettings
        }
    } else {
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Compressify",
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center),
                color = MaterialTheme.colorScheme.primary,
                fontSize = 20.sp,
                fontFamily = MaterialTheme.typography.titleMedium.fontFamily,
                fontWeight = FontWeight.Bold
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(onClick = {
                    openSettings = !openSettings
                }) {
                    Icon(
                        painter = painterResource(R.drawable.setting),
                        contentDescription = "hvbjv",
                        modifier = Modifier.size(24.dp),
                        tint = MaterialTheme.colorScheme.primary,
                    )
                }
            }
        }
    }
}
