package com.qpay.xcash.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor

// ui/scanpay/ 資料夾底下畫面專屬顏色

data class ScanPayColors(
    val qrFrameBackground: Color,    // MyQrContent QR code 區塊底色
    val qrCodeColor: Color,          // MyQrContent QR code 本體顏色
    val qrNickNameText: Color,       // MyQrContent MY_QR 上方 @nickName 文字
    val qrUserNameText: Color,       // MyQrContent MY_QR 下方 userName 文字
    val balanceAmountText: Color,    // MyQrContent Balance 金額文字
    val balanceToggleIcon: Color,    // MyQrContent Balance 顯示/隱藏眼睛圖示
    val questCardBackground: Color,  // MyQrContent Daily Quest 卡片底色
    val questTitleText: Color,       // MyQrContent Daily Quest 標題文字
    val questProgressText: Color,    // MyQrContent Daily Quest 進度文字
    val questProgressIndicator: Color, // MyQrContent Daily Quest 進度條顏色
    val questProgressTrack: Color,   // MyQrContent Daily Quest 進度條 track
    val qrTabBorder: Brush,          // QrModeTabSelector 外框邊框；Black Gold = 金色橫向漸層
    val qrTabSelectedFill: Brush,    // QrModeTabSelector 選中 Tab 填滿；Black Gold = 金色橫向漸層
    val actionButtonIconTint: Color, // MyQrActionButton icon tint；Neon = Unspecified（保留 icon 原色）
    val actionButtonIconBorder: Color, // MyQrActionButton icon 圓形邊框
    val actionButtonText: Color,     // MyQrActionButton 文字
    val inputRecipientText: Color,   // InputAmountScreen 收款人 @nickName / 姓名文字
    val inputCardOuterBorder: Color, // InputAmountScreen 收款人卡片外層邊框
    val inputCardInnerBorder: Color?, // InputAmountScreen 收款人卡片內層邊框；Black Gold = null（不顯示）
    val inputBalanceLabelText: Color, // InputAmountScreen Balance 標籤文字
    val inputBalanceAmountText: Color, // InputAmountScreen Balance 金額文字
    val reviewButtonFill: Brush,     // InputAmountScreen Review Details 按鈕填滿；Black Gold = 金色橫向漸層
    val reviewButtonBorder: Color?,  // InputAmountScreen Review Details 按鈕邊框；Neon = null（無邊框）
)

val NeonScanPayColors = ScanPayColors(
    qrFrameBackground = slateGray,
    qrCodeColor = neonCyan,
    qrNickNameText = neonCyan,
    qrUserNameText = silverGray,
    balanceAmountText = themeWhite,
    balanceToggleIcon = Color.Gray,
    questCardBackground = themeBlack.copy(alpha = 0.5f),
    questTitleText = themeWhite,
    questProgressText = silverGray,
    questProgressIndicator = neonCyan,
    questProgressTrack = themeWhite.copy(alpha = 0.2f),
    qrTabBorder = Brush.linearGradient(
        colors = listOf(neonPurple.copy(alpha = 0.8f), neonCyan.copy(alpha = 0.4f))
    ),
    qrTabSelectedFill = SolidColor(neonCyan),
    actionButtonIconTint = Color.Unspecified,
    actionButtonIconBorder = neonCyan,
    actionButtonText = silverGray,
    inputRecipientText = neonPurpleLight,
    inputCardOuterBorder = neonCyan.copy(alpha = 0.8f),
    inputCardInnerBorder = neonPurple.copy(alpha = 0.6f),
    inputBalanceLabelText = silverGray,
    inputBalanceAmountText = themeWhite,
    reviewButtonFill = SolidColor(neonPurple),
    reviewButtonBorder = null,
)

val BlackGoldScanPayColors = ScanPayColors(
    qrFrameBackground = charcoalBlack,
    qrCodeColor = espressoBrown,
    qrNickNameText = espressoBrown,
    qrUserNameText = espressoBrown,
    balanceAmountText = antiqueGold,
    balanceToggleIcon = warmSand,
    questCardBackground = charcoalBlack,
    questTitleText = espressoBrown,
    questProgressText = espressoBrown,
    questProgressIndicator = espressoBrown,
    questProgressTrack = espressoBrown.copy(alpha = 0.55f),
    // 由左至右：caramelGold → apricotGold → caramelGold
    qrTabBorder = Brush.horizontalGradient(
        colors = listOf(caramelGold, apricotGold, caramelGold)
    ),
    qrTabSelectedFill = Brush.horizontalGradient(
        colors = listOf(caramelGold, apricotGold, caramelGold)
    ),
    actionButtonIconTint = espressoBrown,
    actionButtonIconBorder = espressoBrown,
    actionButtonText = espressoBrown,
    inputRecipientText = camelGold,
    inputCardOuterBorder = apricotGold,
    inputCardInnerBorder = null,
    inputBalanceLabelText = camelGold,
    inputBalanceAmountText = silverGray,
    // 由左至右：camelGold → apricotGold → camelGold
    reviewButtonFill = Brush.horizontalGradient(
        colors = listOf(camelGold, apricotGold, camelGold)
    ),
    reviewButtonBorder = apricotGold,
)
