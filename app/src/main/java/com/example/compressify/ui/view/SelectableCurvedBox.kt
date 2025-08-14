package com.example.compressify.ui.view

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.compressify.data.SelectableOption

@Composable
fun SelectableCurvedBox(
    modifier: Modifier = Modifier,
    option: SelectableOption,
    isSelected: Boolean,
    onSelected: (Int) -> Unit,
    cornerRadius: Dp,
    boxHeight: Dp
) {
    val backgroundColor = if (isSelected) option.selectedColor else option.defaultColor
    val contentColor = if (isSelected) option.selectedContentColor else option.defaultContentColor
    val borderModifier = if (isSelected) {
        Modifier.border(
            width = 2.dp,
            color = MaterialTheme.colorScheme.outline, // Or a more prominent color
            shape = RoundedCornerShape(cornerRadius)
        )
    } else {
        Modifier
    }

    Surface(
        modifier = modifier
            .height(boxHeight)
            .clip(androidx.compose.foundation.shape.RoundedCornerShape(cornerRadius)) // Clip for the background
            .then(borderModifier) // Apply border over the background
            .selectable(
                selected = isSelected,
                onClick = { onSelected(option.id) },
                role = Role.RadioButton // Or Role.Tab, depending on semantic meaning
            ),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(cornerRadius), // Shape for elevation and surface properties
        color = backgroundColor,
        contentColor = contentColor,
        tonalElevation = if (isSelected) 4.dp else 1.dp // More elevation when selected
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = option.label,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                fontSize = 14.sp // Adjust as needed
            )
        }
    }
}