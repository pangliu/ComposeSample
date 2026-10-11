package com.qpay.xcash.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor

// ui/transfer/confirm/ ConfirmTransferScreen 專屬顏色

data class ConfirmTransferColors(
    val titleBrush: Brush?,       // 標題文字漸層；Black Gold = null（沿用 gradient.goldShimmer）
    val subtitleText: Color,      // 「Please confirm your transfer details」提示文字
    val payCardBackground: Color, // 付款方帳戶卡片底色（使用者自己的預設帳戶 / WalletTransferScreen 帶入的帳戶）
    val payCardBorder: Brush,     // 付款方帳戶卡片邊框；Black Gold = 金色橫向漸層
    val toCardBackground: Color,  // 收款方帳戶卡片底色
    val toCardBorder: Brush,      // 收款方帳戶卡片邊框；Black Gold = 金色橫向漸層
    val iconBackground: Color,    // 卡片內 icon 方塊底色
    val nameText: Color,          // 帳戶名稱
    val subText: Color,           // 遮罩帳號
    val sectionLabelText: Color,  // 「Pay」「To」標籤
    val amountText: Color,        // 金額文字
    val feeLabelText: Color,      // Fee / Total Payment 標籤
    val feeValueText: Color,      // Fee / Total Payment 數值
    val nextButtonFill: Brush,    // Next 按鈕填滿；Black Gold = 金色橫向漸層
    val nextButtonText: Color,    // Next 按鈕文字
)

val NeonConfirmTransferColors = ConfirmTransferColors(
    // 由左至右：neonOrchid → neonSkyCyan → neonOrchid
    titleBrush = Brush.horizontalGradient(colors = listOf(neonOrchid, neonSkyCyan, neonOrchid)),
    subtitleText = neonAzure,
    payCardBackground = neonCardNavy,
    payCardBorder = SolidColor(neonAzure),
    toCardBackground = neonCardIndigo,
    toCardBorder = SolidColor(neonAmethyst),
    iconBackground = neonIconGray,
    nameText = themeWhite,
    subText = neonSlateText,
    sectionLabelText = themeWhite,
    amountText = neonAzure,
    feeLabelText = themeWhite,
    feeValueText = themeWhite,
    // 由左至右：neonMagenta → neonViolet（與 WalletTransferScreen Next 按鈕一致）
    nextButtonFill = Brush.horizontalGradient(colors = listOf(neonMagenta, neonViolet)),
    nextButtonText = themeWhite,
)

val BlackGoldConfirmTransferColors = ConfirmTransferColors(
    titleBrush = null,
    subtitleText = antiqueGold,
    payCardBackground = charcoalBlack,
    // 由左至右：oldGold → amberGold → champagneGold → amberGold → oldGold（與 gradient.goldShimmer 一致）
    payCardBorder = Brush.horizontalGradient(
        colors = listOf(oldGold, amberGold, champagneGold, amberGold, oldGold)
    ),
    toCardBackground = charcoalBlack,
    toCardBorder = Brush.horizontalGradient(
        colors = listOf(oldGold, amberGold, champagneGold, amberGold, oldGold)
    ),
    iconBackground = midnightIndigo,
    nameText = themeWhite,
    subText = warmSand,
    sectionLabelText = themeWhite,
    amountText = antiqueGold,
    feeLabelText = warmSand,
    feeValueText = themeWhite,
    nextButtonFill = Brush.horizontalGradient(
        colors = listOf(oldGold, amberGold, champagneGold, amberGold, oldGold)
    ),
    nextButtonText = themeBlack,
)
