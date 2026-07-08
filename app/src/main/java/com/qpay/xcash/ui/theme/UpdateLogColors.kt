package com.qpay.xcash.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// UpdateLogScreen 專屬顏色

data class UpdateLogColors(
    val cardBackground: Color,     // UpdateLogCard 底色
    val cardBorder: Color,         // UpdateLogCard 邊框 + glow 顏色
    val titleText: Color,          // 日期/標題文字顏色（titleGradient 為 null 時的 fallback）
    val titleGradient: Brush?,     // 日期/標題文字漸層；Neon = null（維持單色 titleText）
    val messageText: Color,        // 內文文字顏色
    val divider: Color,            // 分隔線顏色
    val chevronIcon: Color,        // 右側箭頭 icon 顏色
)

val NeonUpdateLogColors = UpdateLogColors(
    cardBackground = twilightNavy,
    cardBorder = neonCyan,
    titleText = neonCyan,
    titleGradient = null,
    messageText = silverGray,
    divider = neonCyan,
    chevronIcon = silverGray,
)

val BlackGoldUpdateLogColors = UpdateLogColors(
    cardBackground = charcoalBlack,
    cardBorder = antiqueGold,
    titleText = antiqueGold,
    titleGradient = Brush.verticalGradient(
        listOf(oldGold, amberGold, champagneGold, amberGold, oldGold)
    ),
    messageText = silverGray,
    divider = antiqueGold,
    chevronIcon = warmSand,
)
