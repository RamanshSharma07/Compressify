package com.example.compressify.ui.view

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CompressionDetails(text:String, detail:String){
    HorizontalDivider(
        Modifier.padding(2.dp),
        DividerDefaults.
        Thickness,
        color = MaterialTheme.colorScheme.primary
    )
    Row {
        Text(
            text = text,
            modifier = Modifier
                .padding(16.dp, 0.dp),
            fontSize = 12.sp,
            color = Color.Gray
        )
        Text(
            text = detail,
            modifier = Modifier
                .padding(16.dp, 0.dp),
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.primary
        )
    }
}