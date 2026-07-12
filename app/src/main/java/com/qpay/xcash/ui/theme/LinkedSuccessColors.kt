package com.qpay.xcash.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor

// ui/cards/linked_success/ LinkedSuccessScreen 專屬顏色

data class LinkedSuccessColors(
    // Checkmark circle
    val checkCircleBorder: Color,   // 打勾圓圈邊框
    val checkIcon: Color,           // 打勾圖示
    // Title
    val titleText: Brush,           // "Card Linked Successfully!" 標題；Black Gold = 金色直向漸層
    // Info card
    val cardBackground: Color,      // Info card 底色
    val cardBorder: Color,          // Info card 邊框
    val congratsText: Color,        // Congratulations 文字
    val divider: Color,             // Info card 內分隔線
    // ActionButton
    val setupPrimaryBackground: Brush, // Setup Primary Card 按鈕背景；Neon = 透明，Black Gold = goldShimmer 橫向漸層
    val setupPrimaryBorder: Brush,     // Setup Primary Card 按鈕邊框；Black Gold = champagneGold 單色
    val setupPrimaryText: Brush,       // Setup Primary Card 按鈕文字；Black Gold = 黑色
    val notNowBackground: Brush,       // Not Now 按鈕背景；Neon = 透明，Black Gold = silverShimmer 橫向漸層
    val notNowBorder: Brush,           // Not Now 按鈕邊框；Black Gold = silverMist 單色
    val notNowText: Brush,             // Not Now 按鈕文字；Black Gold = 黑色
)

val NeonLinkedSuccessColors = LinkedSuccessColors(
    checkCircleBorder = neonCyan,
    checkIcon = neonCyan,
    titleText = SolidColor(neonCyan),
    cardBackground = oceanNavy,
    cardBorder = neonCyan.copy(alpha = 0.5f),
    congratsText = Color.White,
    divider = neonCyan.copy(alpha = 0.15f),
    setupPrimaryBackground = SolidColor(Color.Transparent),
    setupPrimaryBorder = SolidColor(neonCyan.copy(alpha = 0.7f)),
    setupPrimaryText = SolidColor(neonCyan),
    notNowBackground = SolidColor(Color.Transparent),
    notNowBorder = SolidColor(neonCyan.copy(alpha = 0.7f)),
    notNowText = SolidColor(neonCyan),
)

val BlackGoldLinkedSuccessColors = LinkedSuccessColors(
    checkCircleBorder = antiqueGold,
    checkIcon = antiqueGold,
    // 由上而下：oldGold → amberGold → champagneGold → amberGold → oldGold（同 gradient.goldShimmer 色階）
    titleText = Brush.verticalGradient(
        colors = listOf(oldGold, amberGold, champagneGold, amberGold, oldGold)
    ),
    cardBackground = charcoalBlack,
    cardBorder = antiqueGold.copy(alpha = 0.5f),
    congratsText = Color.White,
    divider = antiqueGold.copy(alpha = 0.15f),
    // 由左至右：oldGold → amberGold → champagneGold → amberGold → oldGold（同 gradient.goldShimmer 色階）
    setupPrimaryBackground = Brush.horizontalGradient(
        colors = listOf(oldGold, amberGold, champagneGold, amberGold, oldGold)
    ),
    setupPrimaryBorder = SolidColor(champagneGold),
    setupPrimaryText = SolidColor(Color.Black),
    // 由左至右：graphiteGray → steelGray → silverMist → steelGray → graphiteGray（同 gradient.silverShimmer 色階）
    notNowBackground = Brush.horizontalGradient(
        colors = listOf(graphiteGray, steelGray, silverMist, steelGray, graphiteGray)
    ),
    notNowBorder = SolidColor(silverMist),
    notNowText = SolidColor(Color.Black),
)
