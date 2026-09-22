package com.qpay.xcash.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// ui/transfer/wallet/confirm/ ConfirmTransferScreen 專屬顏色

data class ConfirmTransferColors(
    val subtitleText: Color,      // 「Please confirm your transfer details」提示文字
    val cardBackground: Color,    // My Xcash / 收款人 帳戶卡片底色
    val cardBorder: Brush,        // 帳戶卡片邊框；Black Gold = 金色橫向漸層
    val iconBackground: Color,    // 卡片內 icon 方塊底色
    val nameText: Color,          // 帳戶名稱（My Xcash / 收款人姓名）
    val subText: Color,           // 遮罩帳號 / 遮罩電話
    val tagBackground: Color,     // Family 等標籤底色
    val tagText: Color,           // 標籤文字
    val sectionLabelText: Color,  // 「Pay」「To」標籤
    val amountText: Color,        // 金額文字
    val feeLabelText: Color,      // Fee / Total Payment 標籤
    val feeValueText: Color,      // Fee / Total Payment 數值
    val nextButtonFill: Brush,    // Next 按鈕填滿；Black Gold = 金色橫向漸層
    val nextButtonText: Color,    // Next 按鈕文字
)

val NeonConfirmTransferColors = ConfirmTransferColors(
    subtitleText = neonCyan,
    cardBackground = twilightNavy,
    cardBorder = Brush.horizontalGradient(colors = listOf(neonPurple, neonCyan, neonPurple)),
    iconBackground = deepMidnight,
    nameText = themeWhite,
    subText = silverGray,
    tagBackground = neonPurple.copy(alpha = 0.25f),
    tagText = neonPurple,
    sectionLabelText = themeWhite,
    amountText = neonCyan,
    feeLabelText = silverGray,
    feeValueText = themeWhite,
    nextButtonFill = Brush.horizontalGradient(colors = listOf(neonPurple, neonDarkBlue)),
    nextButtonText = themeWhite,
)

val BlackGoldConfirmTransferColors = ConfirmTransferColors(
    subtitleText = antiqueGold,
    cardBackground = charcoalBlack,
    // 由左至右：oldGold → amberGold → champagneGold → amberGold → oldGold（與 gradient.goldShimmer 一致）
    cardBorder = Brush.horizontalGradient(
        colors = listOf(oldGold, amberGold, champagneGold, amberGold, oldGold)
    ),
    iconBackground = midnightIndigo,
    nameText = themeWhite,
    subText = warmSand,
    tagBackground = antiqueGold.copy(alpha = 0.2f),
    tagText = antiqueGold,
    sectionLabelText = themeWhite,
    amountText = antiqueGold,
    feeLabelText = warmSand,
    feeValueText = themeWhite,
    nextButtonFill = Brush.horizontalGradient(
        colors = listOf(oldGold, amberGold, champagneGold, amberGold, oldGold)
    ),
    nextButtonText = themeBlack,
)
