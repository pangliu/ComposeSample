package com.qpay.xcash.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.Color

// ui/cashin/ CashInScreen 專屬顏色

data class CashInColors(
    val bankIconBackground: Color,        // 銀行 icon 方塊底色
    val bankIconTint: Color,              // 銀行 icon（目前為內建圖示占位）
    val bankLabelText: Color,             // 銀行帳號文字
    val bankChevronTint: Color,           // 銀行選擇下拉箭頭
    val transferArrowTint: Color,         // 銀行 → 錢包中間箭頭
    val walletIconBackground: Color,      // Xcash Wallet icon 方塊底色
    val walletIconTint: Color,            // Xcash Wallet icon（目前為內建圖示占位）
    val walletLabelText: Color,           // Xcash Wallet 文字
    val amountCurrencyText: Color,        // 金額前方幣別文字（PHP）
    val amountValueText: Color,           // 已輸入金額文字
    val amountPlaceholderText: Color,     // 金額為 0 時的淡化文字
    val currentBalanceLabelText: Color,   // Current Balance 標籤文字
    val currentBalanceValueText: Color,   // Current Balance 數值文字
    val quickAmountChipBackground: Color, // 快速加值膠囊底色
    val quickAmountChipBorder: Color,     // 快速加值膠囊邊框
    val quickAmountChipText: Color,       // 快速加值膠囊文字
    val keypadNumberText: Color,          // 數字鍵盤主要數字文字
    val keypadBackspaceIconTint: Color,   // 數字鍵盤退格 icon
    val topUpButtonEnabledFill: Brush,    // Top Up 按鈕啟用時填滿；Black Gold = 金色橫向漸層
    val topUpButtonDisabledFill: Color,   // Top Up 按鈕未輸入金額時的底色
    val topUpButtonEnabledText: Color,    // Top Up 按鈕啟用時文字
    val topUpButtonDisabledText: Color,   // Top Up 按鈕未輸入金額時文字
)

val NeonCashInColors = CashInColors(
    bankIconBackground = twilightNavy,
    bankIconTint = neonCyan,
    bankLabelText = themeWhite,
    bankChevronTint = neonCyan,
    transferArrowTint = neonCyan,
    walletIconBackground = twilightNavy,
    walletIconTint = neonPurple,
    walletLabelText = themeWhite,
    amountCurrencyText = neonCyan,
    amountValueText = themeWhite,
    amountPlaceholderText = silverGray.copy(alpha = 0.4f),
    currentBalanceLabelText = silverGray,
    currentBalanceValueText = themeWhite,
    quickAmountChipBackground = twilightNavy,
    quickAmountChipBorder = neonCyan,
    quickAmountChipText = neonCyan,
    keypadNumberText = themeWhite,
    keypadBackspaceIconTint = silverGray,
    topUpButtonEnabledFill = SolidColor(neonPurple),
    topUpButtonDisabledFill = twilightNavy,
    topUpButtonEnabledText = themeWhite,
    topUpButtonDisabledText = silverGray,
)

val BlackGoldCashInColors = CashInColors(
    bankIconBackground = themeWhite,
    bankIconTint = antiqueGold,
    bankLabelText = antiqueGold,
    bankChevronTint = antiqueGold,
    transferArrowTint = antiqueGold,
    walletIconBackground = charcoalGray,
    walletIconTint = antiqueGold,
    walletLabelText = antiqueGold,
    amountCurrencyText = antiqueGold,
    amountValueText = antiqueGold,
    amountPlaceholderText = warmSand.copy(alpha = 0.4f),
    currentBalanceLabelText = warmSand,
    currentBalanceValueText = warmSand,
    quickAmountChipBackground = charcoalBlack,
    quickAmountChipBorder = antiqueGold,
    quickAmountChipText = antiqueGold,
    keypadNumberText = themeWhite,
    keypadBackspaceIconTint = warmSand,
    // 由左至右：oldGold → amberGold → champagneGold → amberGold → oldGold
    topUpButtonEnabledFill = Brush.horizontalGradient(
        colors = listOf(oldGold, amberGold, champagneGold, amberGold, oldGold)
    ),
    topUpButtonDisabledFill = charcoalGray,
    topUpButtonEnabledText = themeBlack,
    topUpButtonDisabledText = warmSand.copy(alpha = 0.5f),
)
