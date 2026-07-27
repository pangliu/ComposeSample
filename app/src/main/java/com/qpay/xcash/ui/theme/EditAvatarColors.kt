package com.qpay.xcash.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor

// ui/components/ EditAvatarScreen 專屬顏色

data class EditAvatarColors(
    val circleBorder: Color,          // 頭像裁切預覽圓框
    val hintText: Color,              // "Pinch to adjust..." 提示文字
    val sliderTrackBackground: Color, // Slider 底色（未填充部分）
    val sliderTrackFill: Brush,       // Slider 填充色；Black Gold = 金色橫向漸層，Neon = neonCyan 單色
    val sliderThumb: Color,           // Slider 拉桿圓點
    val cancelText: Color,            // Cancel 文字
    val chooseText: Brush,            // Choose 文字；Black Gold = 金色橫向漸層，Neon = neonCyan 單色
)

val NeonEditAvatarColors = EditAvatarColors(
    circleBorder = neonCyan,
    hintText = silverGray,
    sliderTrackBackground = silverGray.copy(alpha = 0.25f),
    sliderTrackFill = SolidColor(neonCyan),
    sliderThumb = Color.White,
    cancelText = Color.White,
    chooseText = SolidColor(neonCyan),
)

val BlackGoldEditAvatarColors = EditAvatarColors(
    circleBorder = antiqueGold,
    hintText = warmSand,
    sliderTrackBackground = warmSand.copy(alpha = 0.25f),
    // 由左至右：oldGold → amberGold → champagneGold → amberGold → oldGold（同 gradient.goldShimmer 色階）
    sliderTrackFill = Brush.horizontalGradient(
        listOf(oldGold, amberGold, champagneGold, amberGold, oldGold)
    ),
    sliderThumb = Color.White,
    cancelText = silverMist,
    // 由左至右：oldGold → amberGold → champagneGold
    chooseText = Brush.horizontalGradient(
        listOf(oldGold, amberGold, champagneGold)
    ),
)
