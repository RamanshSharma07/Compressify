package com.example.compressify.data

import androidx.compose.ui.graphics.Color


/**
 * This is the data class for theme selection boxes
 * */
data class SelectableOption(
    val id: Int,
    val label: String,
    val defaultColor: Color,
    val selectedColor: Color,
    val defaultContentColor: Color,
    val selectedContentColor: Color
)