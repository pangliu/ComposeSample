package com.qpay.xcash.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor

// ui/friend/qrcode/ QRCodeScreen（Friends → QR Code 頁面）專屬顏色

data class QrCodeColors(
    // ── SCAN QR / MY QR Tab（QRCodeScreen 專屬漸層，不影響其他頁面共用的 QrModeTabSelector）──
    val tabBorder: Brush,            // QrModeTabSelector 外框漸層
    val tabSelectedFill: Brush,      // QrModeTabSelector 選中 Tab 底色漸層

    // ── QR Frame（掃描 / 顯示 QR 用的方形取景框）──
    val frameBackground: Color,      // 取景框底色
    val frameCorner: Brush,          // 取景框四角 L 型裝飾線

    // ── Camera Permission Placeholder（SCAN QR 無相機權限時顯示）──
    val permissionText: Color,       // 「需要相機權限」提示文字
    val goToSettingBackground: Brush,// 「Go To Setting」按鈕底色
    val goToSettingText: Color,      // 「Go To Setting」按鈕文字
    val permissionHintText: Color,   // 框外「Please check your setting」提示文字

    // ── My QR（MY QR Tab 顯示的 QR code 卡片）──
    val myQrCardBackground: Brush,   // 外層卡片底色（Black Gold = 金屬金色漸層）
    val myQrPhoneText: Color,        // 卡片下方電話號碼文字顏色
    val qrCodeBackground: Color,     // QR code 模組後方的淺色底（維持掃描辨識度）
    val qrCodeColor: Color,          // QR code 模組顏色

    // ── Share Profile Button ──
    val shareProfileBackground: Color, // 「Share Profile」按鈕底色
    val shareProfileIconTint: Color,   // 「Share Profile」icon 顏色
    val shareProfileText: Color,       // 「Share Profile」文字顏色
)

val NeonQrCodeColors = QrCodeColors(
    tabBorder = Brush.horizontalGradient(
//        colors = listOf(oldGold, amberGold, champagneGold, amberGold, oldGold
        colors = listOf(neonDarkPurple, neonPurple)
    ),
    tabSelectedFill = SolidColor(neonCyan),
//        Brush.horizontalGradient(
//        colors = listOf(oldGold, amberGold, champagneGold, amberGold, oldGold)
//    ),
    frameBackground = twilightNavy,
    frameCorner = SolidColor(neonCyan),
    permissionText = themeWhite,
    goToSettingBackground = SolidColor(neonPurple),
    goToSettingText = Color.White,
    permissionHintText = silverGray,
    myQrCardBackground = Brush.linearGradient(
        colors = listOf(neonDarkPurple, neonPurple)
    ),
    myQrPhoneText = themeWhite,
    qrCodeBackground = Color.White,
    qrCodeColor = deepMidnight,
    shareProfileBackground = twilightNavy,
    shareProfileIconTint = neonCyan,
    shareProfileText = themeWhite,
)

val BlackGoldQrCodeColors = QrCodeColors(
    tabBorder = Brush.horizontalGradient(
        colors = listOf(oldGold, amberGold, champagneGold, amberGold, oldGold)
    ),
    tabSelectedFill = Brush.horizontalGradient(
        colors = listOf(oldGold, amberGold, champagneGold, amberGold, oldGold)
    ),
    frameBackground = charcoalBlack,
    frameCorner = Brush.horizontalGradient(
        colors = listOf(oldGold, amberGold, champagneGold, amberGold, oldGold)
    ),
    permissionText = themeWhite,
    goToSettingBackground = Brush.horizontalGradient(
        colors = listOf(oldGold, amberGold, champagneGold, amberGold, oldGold)
    ),
    goToSettingText = Color.Black,
    permissionHintText = silverGray,
    myQrCardBackground = Brush.horizontalGradient(
        colors = listOf(oldGold, amberGold, champagneGold, amberGold, oldGold)
    ),
    myQrPhoneText = Color.Black,
    qrCodeBackground = Color.White,
    qrCodeColor = charcoalBlack,
    shareProfileBackground = charcoalLightBlack,
    shareProfileIconTint = champagneGold,
    shareProfileText = themeWhite,
)
