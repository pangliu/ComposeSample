package com.qpay.xcash.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// ui/transfer/ WalletTransferScreen 專屬顏色

data class WalletTransferColors(
    val tabSelectedText: Color,          // 選中的分頁文字
    val tabUnselectedText: Color,        // 未選中的分頁文字
    val tabIndicator: Brush,             // 選中分頁底線；Black Gold = 金色橫向漸層
    val tabDivider: Color,               // 分頁列底部分隔線
    val balanceText: Color,              // Available Balance 文字
    val balanceIconTint: Color,          // 眼睛 icon
    val bonusText: Color,                // Transfer Bonus 小字
    val chipSelectedBackground: Color,   // 選中的模式標籤底色
    val chipSelectedBorder: Color,       // 選中的模式標籤邊框
    val chipSelectedText: Color,         // 選中的模式標籤文字
    val chipUnselectedBackground: Color, // 未選中的模式標籤底色
    val chipUnselectedBorder: Color,     // 未選中的模式標籤邊框
    val chipUnselectedText: Color,       // 未選中的模式標籤文字
    val fieldBackground: Color,          // 輸入欄位底色
    val fieldBorder: Color,              // 輸入欄位邊框
    val fieldSelectedText: Color,        // 帶入的帳戶類型文字（Xcash Wallet）
    val fieldText: Color,                // 使用者輸入文字
    val fieldPlaceholderText: Color,     // placeholder 文字
    val fieldIconTint: Color,            // 欄位右側 icon（掃描）
    val fieldSideText: Color,            // 欄位右側小字（Limit）
    val feeLabelText: Color,             // Fee 標籤
    val feeValueText: Color,             // Fee 數值
    val nextButtonEnabledFill: Brush,    // Next 按鈕啟用時填滿；Black Gold = 金色橫向漸層
    val nextButtonDisabledFill: Color,   // Next 按鈕未輸入金額時的底色
    val nextButtonEnabledText: Color,    // Next 按鈕啟用時文字
    val nextButtonDisabledText: Color,   // Next 按鈕未輸入金額時文字
)

val NeonWalletTransferColors = WalletTransferColors(
    tabSelectedText = neonCyan,
    tabUnselectedText = silverGray,
    tabIndicator = Brush.horizontalGradient(colors = listOf(neonPurple, neonCyan, neonPurple)),
    tabDivider = neonCyan.copy(alpha = 0.2f),
    balanceText = neonCyan,
    balanceIconTint = silverGray,
    bonusText = silverGray,
    chipSelectedBackground = twilightNavy,
    chipSelectedBorder = neonCyan,
    chipSelectedText = neonCyan,
    chipUnselectedBackground = twilightNavy,
    chipUnselectedBorder = neonCyan.copy(alpha = 0.2f),
    chipUnselectedText = themeWhite,
    fieldBackground = twilightNavy,
    fieldBorder = neonCyan.copy(alpha = 0.5f),
    fieldSelectedText = neonCyan,
    fieldText = themeWhite,
    fieldPlaceholderText = silverGray,
    fieldIconTint = neonCyan,
    fieldSideText = silverGray,
    feeLabelText = silverGray,
    feeValueText = neonCyan,
    nextButtonEnabledFill = Brush.horizontalGradient(colors = listOf(neonPurple, neonDarkBlue)),
    nextButtonDisabledFill = twilightNavy,
    nextButtonEnabledText = themeWhite,
    nextButtonDisabledText = silverGray,
)

val BlackGoldWalletTransferColors = WalletTransferColors(
    tabSelectedText = antiqueGold,
    tabUnselectedText = warmSand.copy(alpha = 0.6f),
    // 由左至右：oldGold → amberGold → champagneGold → amberGold → oldGold（與 gradient.goldShimmer 一致）
    tabIndicator = Brush.horizontalGradient(
        colors = listOf(oldGold, amberGold, champagneGold, amberGold, oldGold)
    ),
    tabDivider = warmSand.copy(alpha = 0.2f),
    balanceText = antiqueGold,
    balanceIconTint = warmSand,
    bonusText = warmSand.copy(alpha = 0.7f),
    chipSelectedBackground = charcoalBlack,
    chipSelectedBorder = antiqueGold,
    chipSelectedText = antiqueGold,
    chipUnselectedBackground = charcoalBlack,
    chipUnselectedBorder = warmSand.copy(alpha = 0.25f),
    chipUnselectedText = themeWhite,
    fieldBackground = charcoalBlack,
    fieldBorder = antiqueGold.copy(alpha = 0.6f),
    fieldSelectedText = sunGold,
    fieldText = themeWhite,
    fieldPlaceholderText = antiqueGold,
    fieldIconTint = antiqueGold,
    fieldSideText = warmSand.copy(alpha = 0.7f),
    feeLabelText = antiqueGold,
    feeValueText = antiqueGold,
    nextButtonEnabledFill = Brush.horizontalGradient(
        colors = listOf(oldGold, amberGold, champagneGold, amberGold, oldGold)
    ),
    nextButtonDisabledFill = charcoalGray,
    nextButtonEnabledText = themeBlack,
    nextButtonDisabledText = warmSand.copy(alpha = 0.5f),
)
