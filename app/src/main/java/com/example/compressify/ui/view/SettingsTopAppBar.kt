package com.example.compressify.ui.view

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.compressify.R

@Composable
fun SettingsTopAppBar(
    onBack: () -> Unit,
    title: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 0.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Back Icon
        IconButton (
            onClick = onBack
        ) {
            Icon(
                painter = painterResource(R.drawable.arrow_back),
                contentDescription = "Back btn",
                modifier = Modifier.size(24.dp),
                tint = MaterialTheme.colorScheme.primary,
            )
        }

        Text(
            text = title,
            color = MaterialTheme.colorScheme.primary,
            fontSize = 20.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp),
            fontFamily = MaterialTheme.typography.titleMedium.fontFamily,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.width(48.dp)) // Approximate width of an IconButton (24dp icon + padding)
    }
}
