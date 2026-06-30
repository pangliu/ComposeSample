package com.example.newproject.ui.theme

import androidx.compose.ui.graphics.Color

data class AppColors(
    val primary: Color,
    val secondary: Color,
    val secondaryDark: Color,
    val background: Color,
    val onBackground: Color
)

val NeonColors = AppColors(
    primary = Color(0xFF2EFFF5),
    secondary = Color(0xFFDF4CFF),
    secondaryDark = Color(0xFF4E3E83),
    background = Color(0xFF0B1327),
    onBackground = Color(0xFFADAEB0)
)

val BlackGoldColors = AppColors(
    primary = Color(0xFFFFD700),
    secondary = Color(0xFFB8860B),
    secondaryDark = Color(0xFF3D2B00),
    background = Color(0xFF050505),
    onBackground = Color(0xFFC8B89A)
)
