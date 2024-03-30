package com.andrescarvajald.colorpalettegenerator.util

import androidx.compose.ui.graphics.Color

fun IsDarkColor(color: Long): Boolean {
    val red = Color(color).red
    val green = Color(color).green
    val blue = Color(color).blue
    val luminance = 0.2126 * red + 0.7152 * green + 0.0722 * blue
    return luminance < 0.5
}