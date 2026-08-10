package com.qpay.xcash.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor

// ui/friend/ FriendScreen（Friends 入口頁）專屬顏色

data class FriendColors(
    // ── Action Circle Buttons（Invite / QR Code / Search）──
    val actionCircleBackground: Color,   // 圓形按鈕底色
    val actionCircleBorder: Brush,       // 圓形按鈕邊框
    val actionIconTint: Brush,           // 圓形按鈕 icon 顏色
    val actionLabelText: Color,          // 圓形按鈕下方文字

    // ── Menu Card（Add via Third Party / My Friend List）──
    val menuIconBackground: Color,       // 選單項目左側 icon 圓形底色
    val menuIconBorder: Color,           // 選單項目左側 icon 圓形邊框
    val menuIconTint: Brush,             // 選單項目 icon 顏色
    val menuTitleText: Color,            // 選單項目標題文字
    val menuSubtitleText: Color,         // 選單項目說明文字
    val menuChevron: Color,              // 選單項目右側箭頭
    val menuDivider: Color,              // 選單項目間分隔線
)

val NeonFriendColors = FriendColors(
    actionCircleBackground = twilightNavy,
    actionCircleBorder = SolidColor(neonCyan),
    actionIconTint = SolidColor(neonCyan),
    actionLabelText = silverGray,
    menuIconBackground = twilightNavy,
    menuIconBorder = neonCyan,
    menuIconTint = SolidColor(neonCyan),
    menuTitleText = themeWhite,
    menuSubtitleText = silverGray,
    menuChevron = silverGray,
    menuDivider = neonCyan,
)

val BlackGoldFriendColors = FriendColors(
    actionCircleBackground = charcoalBlack,
    actionCircleBorder = Brush.horizontalGradient(
        colors = listOf(oldGold, amberGold, champagneGold, amberGold, oldGold)
    ),
    actionIconTint = Brush.horizontalGradient(
        colors = listOf(oldGold, amberGold, champagneGold, amberGold, oldGold)
    ),
    actionLabelText = Color.White,
    menuIconBackground = charcoalBlack,
    menuIconBorder = antiqueGold,
    menuIconTint = Brush.horizontalGradient(
        colors = listOf(oldGold, amberGold, champagneGold, amberGold, oldGold)
    ),
    menuTitleText = themeWhite,
    menuSubtitleText = silverGray,
    menuChevron = coolGray,
    menuDivider = antiqueGold,
)
