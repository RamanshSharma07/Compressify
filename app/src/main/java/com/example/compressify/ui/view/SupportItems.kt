package com.example.compressify.ui.view

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.compressify.ui.theme.MyAppTheme
import com.example.compressify.R

@Composable
fun SupportItems(text: String, icon: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = null,
            modifier = Modifier.size(24.dp),
            tint = MaterialTheme.colorScheme.primary,
        )
        Text(text = text, modifier = Modifier.padding(start = 8.dp))
        if (text == "Remove adds") {
            Spacer(modifier = Modifier.weight(1f))
            Text(text = "$2.99")
        }
    }
}

@Preview
@Composable
fun PreviewSupportItems () {
    MyAppTheme {
        SupportItems("Remove adds", R.drawable.watch_adds)
    }
}