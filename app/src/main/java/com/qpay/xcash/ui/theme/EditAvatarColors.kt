package com.qpay.xcash.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor

// ui/avatar/ EditAvatarScreen 專屬顏色

data class EditAvatarColors(
    val circleBorder: Color,          // 頭像裁切預覽圓框
    val hintText: Color,              // "Pinch to adjust..." 提示文字
    val sliderTrackBackground: Color, // Slider 底色（未填充部分）
    val sliderTrackFill: Brush,       // Slider 填充色；Black Gold = 金色橫向漸層，Neon = neonCyan 單色
    val sliderThumb: Color,           // Slider 拉桿圓點
    val cancelText: Color,            // Cancel 文字
    val chooseText: Brush,            // Choose 文字；Black Gold = 金色橫向漸層，Neon = neonCyan 單色
    // UploadAvatarErrorDialog
    val errorDialogBackground: Color,      // Dialog 底色
    val errorDialogBorder: Color,          // Dialog 邊框
    val errorDialogIconRing: Color,        // 警示 icon 外圈圓框
    val errorDialogIconTint: Color,        // 警示 icon tint
    val errorDialogTitleText: Color,       // 標題文字
    val errorDialogMessageText: Color,     // 說明文字
    val errorDialogCancelBorder: Color,    // Cancel 按鈕邊框
    val errorDialogCancelText: Brush,      // Cancel 按鈕文字
    val errorDialogTryAgainFill: Brush,    // Try Again 按鈕背景；Black Gold = 金色橫向漸層，Neon = neonPurple 單色
    val errorDialogTryAgainText: Color,    // Try Again 按鈕文字；Black Gold = 黑色
)

val NeonEditAvatarColors = EditAvatarColors(
    circleBorder = neonCyan,
    hintText = silverGray,
    sliderTrackBackground = silverGray.copy(alpha = 0.25f),
    sliderTrackFill = SolidColor(neonCyan),
    sliderThumb = Color.White,
    cancelText = Color.White,
    chooseText = SolidColor(neonCyan),
    errorDialogBackground = twilightNavy,
    errorDialogBorder = neonCyan.copy(alpha = 0.4f),
    errorDialogIconRing = neonCyan,
    errorDialogIconTint = neonCyan,
    errorDialogTitleText = Color.White,
    errorDialogMessageText = silverGray.copy(alpha = 0.85f),
    errorDialogCancelBorder = neonCyan,
    errorDialogCancelText = SolidColor(Color.White),
    errorDialogTryAgainFill = SolidColor(neonPurple),
    errorDialogTryAgainText = Color.White,
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
    errorDialogBackground = charcoalBlack,
    errorDialogBorder = antiqueGold.copy(alpha = 0.5f),
    errorDialogIconRing = antiqueGold,
    errorDialogIconTint = antiqueGold,
    errorDialogTitleText = Color.White,
    errorDialogMessageText = silverGray.copy(alpha = 0.85f),
    errorDialogCancelBorder = antiqueGold,
    errorDialogCancelText = Brush.horizontalGradient(
        colors = listOf(oldGold, amberGold, champagneGold, amberGold, oldGold)
    ),
    // 由左至右：oldGold → amberGold → champagneGold → amberGold → oldGold（同 gradient.goldShimmer 色階）
    errorDialogTryAgainFill = Brush.horizontalGradient(
        colors = listOf(oldGold, amberGold, champagneGold, amberGold, oldGold)
    ),
    errorDialogTryAgainText = Color.Black,
)

// ui/components/AvatarDialog 專屬顏色

data class AvatarDialogColors(
    val cardBackground: Color,   // 「拍照 / 從相簿選擇」卡片底色
    val cardBorder: Color,       // 「拍照 / 從相簿選擇」卡片邊框
    val actionText: Color,       // 選項文字（拍照 / 從相簿選擇）
    val actionIcon: Color,       // 選項 icon（拍照 / 從相簿選擇）
    val divider: Color,          // 選項間分隔線
    val cancelBackground: Color, // Cancel 按鈕底色
    val cancelBorder: Color,     // Cancel 按鈕邊框
    val cancelText: Color,       // Cancel 按鈕文字
)

val NeonAvatarDialogColors = AvatarDialogColors(
    cardBackground = twilightNavy,
    cardBorder = neonCyan,
    actionText = neonCyan,
    actionIcon = neonCyan,
    divider = slateGray.copy(alpha = 0.8f),
    cancelBackground = twilightNavy,
    cancelBorder = neonCyan,
    cancelText = neonCyan,
)

val BlackGoldAvatarDialogColors = AvatarDialogColors(
    cardBackground = charcoalGray,
    cardBorder = antiqueGold,
    actionText = neonBlue,
    actionIcon = neonBlue,
    divider = slateGray.copy(alpha = 0.8f),
    cancelBackground = charcoalGray,
    cancelBorder = antiqueGold,
    cancelText = neonBlue,
)
