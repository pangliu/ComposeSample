package com.qpay.xcash.ui.theme

import androidx.compose.ui.graphics.Color

// ui/avatar/ CameraScreen 專屬顏色

data class CameraColors(
    val shutterFill: Color,                // 快門按鈕底色
    val shutterBorder: Color,              // 快門按鈕外框
    val cancelIconTint: Color,             // 取消 icon 顏色
    val bottomIconTint: Color,             // 底部左側裝飾 icon 顏色
    val bottomIconBorder: Color,           // 底部左側裝飾 icon 外框
    val thumbnailBorder: Color,            // 右側縮圖外框
    val thumbnailPlaceholderBackground: Color, // 尚未拍照時縮圖佔位底色
    val permissionText: Color,             // 無相機權限提示文字
)

val NeonCameraColors = CameraColors(
    shutterFill = Color.White,
    shutterBorder = neonCyan,
    cancelIconTint = Color.White,
    bottomIconTint = neonCyan,
    bottomIconBorder = neonCyan.copy(alpha = 0.7f),
    thumbnailBorder = neonCyan.copy(alpha = 0.7f),
    thumbnailPlaceholderBackground = silverGray.copy(alpha = 0.15f),
    permissionText = silverGray,
)

val BlackGoldCameraColors = CameraColors(
    shutterFill = Color.White,
    shutterBorder = Color.White,
    cancelIconTint = Color.White,
    bottomIconTint = antiqueGold,
    bottomIconBorder = antiqueGold.copy(alpha = 0.7f),
    thumbnailBorder = antiqueGold.copy(alpha = 0.7f),
    thumbnailPlaceholderBackground = warmSand.copy(alpha = 0.15f),
    permissionText = warmSand,
)
