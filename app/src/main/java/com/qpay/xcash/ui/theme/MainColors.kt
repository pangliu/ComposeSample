package com.qpay.xcash.ui.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

// ui/main/ 資料夾底下所有畫面專屬顏色
// 目前僅 CustomBottomNavigation 有 Black Gold 對照色；其餘（ScanAndPayTab 等）
// 黑金設計圖尚未提供，暫維持 Neon-only hardcode。

data class BottomNavColors(
    val activeIconTint: Color,   // BottomNavItem 選中狀態 icon 顏色（Color.Unspecified = 沿用圖片原色）
    val activeText: Color,       // BottomNavItem 選中狀態文字顏色
    val scanPayIconTint: Color,  // ScanAndPayTab icon 顏色（Color.Unspecified = 沿用圖片原色）
    val scanPayIconSize: Dp
)

val NeonBottomNavColors = BottomNavColors(
    activeIconTint = Color.Unspecified,
    activeText = mediumCyan,
    scanPayIconTint = Color.Unspecified,
    scanPayIconSize = 24.dp
)

val BlackGoldBottomNavColors = BottomNavColors(
    activeIconTint = antiqueGold,
    activeText = antiqueGold,
    scanPayIconTint = Color.Black,
    scanPayIconSize = 35.dp
)
