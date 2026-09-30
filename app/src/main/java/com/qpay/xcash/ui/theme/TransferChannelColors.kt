package com.qpay.xcash.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// ui/transfer/transferChannel/ TransferChannelScreen 專屬顏色

data class TransferChannelColors(
    val sectionTitleText: Color,        // E-Wallet / Bank 分類標題
    val tilePlaceholderBackground: Color, // 渠道圖片未提供 / 載入中的預設白框底色
    val tilePlaceholderText: Color,     // 預設白框內的渠道名稱文字
    val tileSelectedBorder: Brush,      // 選中渠道外框；Black Gold = 金色橫向漸層
    val tileFailedBackground: Color,    // 圖片載入失敗的底色
    val tileFailedText: Color,          // 圖片載入失敗的「•••」與 loading failed 文字
    val nextButtonEnabledFill: Brush,   // Next 按鈕已選渠道時填滿；Black Gold = 金色橫向漸層
    val nextButtonDisabledFill: Color,  // Next 按鈕未選渠道時的底色
    val nextButtonEnabledText: Color,   // Next 按鈕已選渠道時文字
    val nextButtonDisabledText: Color,  // Next 按鈕未選渠道時文字
)

val NeonTransferChannelColors = TransferChannelColors(
    sectionTitleText = neonCyan,
    tilePlaceholderBackground = themeWhite,
    tilePlaceholderText = deepMidnight,
    tileSelectedBorder = Brush.horizontalGradient(colors = listOf(neonPurple, neonCyan, neonPurple)),
    tileFailedBackground = twilightNavy,
    tileFailedText = silverGray,
    nextButtonEnabledFill = Brush.horizontalGradient(colors = listOf(neonPurple, neonDarkBlue)),
    nextButtonDisabledFill = twilightNavy,
    nextButtonEnabledText = themeWhite,
    nextButtonDisabledText = silverGray,
)

val BlackGoldTransferChannelColors = TransferChannelColors(
    sectionTitleText = champagneGold,
    tilePlaceholderBackground = themeWhite,
    tilePlaceholderText = themeBlack,
    // 由左至右：oldGold → amberGold → champagneGold → amberGold → oldGold（與 gradient.goldShimmer 一致）
    tileSelectedBorder = Brush.horizontalGradient(
        colors = listOf(oldGold, amberGold, champagneGold, amberGold, oldGold)
    ),
    tileFailedBackground = charcoalLightBlack,
    tileFailedText = steelGray,
    nextButtonEnabledFill = Brush.horizontalGradient(
        colors = listOf(oldGold, amberGold, champagneGold, amberGold, oldGold)
    ),
    nextButtonDisabledFill = charcoalLightBlack,
    nextButtonEnabledText = themeBlack,
    nextButtonDisabledText = steelGray,
)
