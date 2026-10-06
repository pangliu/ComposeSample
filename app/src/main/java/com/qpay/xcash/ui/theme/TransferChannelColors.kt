package com.qpay.xcash.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// ui/transfer/transferChannel/ TransferChannelScreen 專屬顏色

data class TransferChannelColors(
    val pageBackground: Color?,         // 頁面底色；Black Gold = null（沿用 bg.page）
    val titleBrush: Brush?,             // 標題文字漸層；Black Gold = null（沿用 gradient.goldShimmer）
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
    pageBackground = neonInkBlack,
    // 由左至右：neonOrchid → neonSkyCyan（與 WalletTransferScreen 標題一致）
    titleBrush = Brush.horizontalGradient(colors = listOf(neonOrchid, neonSkyCyan)),
    sectionTitleText = neonSkyCyan,
    tilePlaceholderBackground = themeWhite,
    tilePlaceholderText = deepMidnight,
    // 設計稿未提供選中狀態，沿用 WalletTransferScreen 分頁底線配色
    tileSelectedBorder = Brush.horizontalGradient(colors = listOf(neonOrchid, neonSkyCyan, neonOrchid)),
    tileFailedBackground = charcoalLightBlack,
    tileFailedText = steelGray,
    // 設計稿未包含 Next 按鈕，沿用 WalletTransferScreen Next 按鈕配色
    nextButtonEnabledFill = Brush.horizontalGradient(colors = listOf(neonMagenta, neonViolet)),
    nextButtonDisabledFill = twilightNavy,
    nextButtonEnabledText = themeWhite,
    nextButtonDisabledText = silverGray,
)

val BlackGoldTransferChannelColors = TransferChannelColors(
    pageBackground = null,
    titleBrush = null,
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
