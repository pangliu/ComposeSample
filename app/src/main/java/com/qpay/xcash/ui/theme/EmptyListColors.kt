package com.qpay.xcash.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor

// ui/friend/emptylist/ EmptyListScreen 專屬顏色

data class EmptyListColors(
    val iconCircleBorder: Color,     // 圖示外框圓圈
    val iconTint: Color,             // 圖示顏色
    val titleText: Color,            // 標題文字（例："No Friends Yet"）
    val subtitleText: Color,         // 說明文字
    val actionButtonFill: Brush,     // CTA 按鈕底色；Black Gold = 金色橫向漸層
    val actionButtonText: Color,     // CTA 按鈕文字
)

// Neon 設計稿尚未提供，暫沿用 Neon 主題強調色維持風格一致，待設計稿確認後再替換
val NeonEmptyListColors = EmptyListColors(
    iconCircleBorder = neonCyan,
    iconTint = neonCyan,
    titleText = Color.White,
    subtitleText = silverGray,
    actionButtonFill = SolidColor(neonCyan),
    actionButtonText = deepNavy,
)

val BlackGoldEmptyListColors = EmptyListColors(
    iconCircleBorder = antiqueGold,
    iconTint = antiqueGold,
    titleText = Color.White,
    subtitleText = warmSand,
    // 由左至右：oldGold → amberGold → champagneGold → amberGold → oldGold（與 gradient.goldShimmer 一致）
    actionButtonFill = Brush.horizontalGradient(
        colors = listOf(oldGold, amberGold, champagneGold, amberGold, oldGold)
    ),
    actionButtonText = themeBlack,
)
