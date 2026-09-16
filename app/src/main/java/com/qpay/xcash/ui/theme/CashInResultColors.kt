package com.qpay.xcash.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// ui/cashin/result/ CashInResultScreen 專屬顏色

data class CashInResultColors(
    val failIconTint: Color,              // 失敗圖示（X）
    val failIconBorder: Color,             // 失敗圓形外框
    val successIconTint: Color,           // 成功圖示（勾勾）
    val successIconBorder: Color,          // 成功圓形外框
    val titleText: Color,                 // Cash In Failed / Successful 標題
    val failAmountText: Color,            // 失敗時的金額文字
    val successAmountText: Color,         // 成功時的金額文字
    val subtitleText: Color,              // 日期時間文字
    val failureReasonText: Color,         // 失敗原因文字
    val cardRowBackground: Color,         // 銀行卡 / 錢包區塊底色
    val cardRowBorder: Color,             // 銀行卡 / 錢包區塊邊框
    val cardIconBackground: Color,        // 區塊內 icon 方塊底色
    val cardLabelText: Color,             // 銀行 / 錢包名稱文字
    val cardSubText: Color,               // 遮罩卡號 / 錢包副標文字
    val transferArrowTint: Color,         // 中間向下箭頭
    val balanceLabelText: Color,          // Balance After Transaction 標籤
    val balanceValueText: Color,          // Balance After Transaction 數值
    val transactionHistoryLinkText: Color, // Transaction History 連結文字
    val refNoText: Color,                 // Ref No. 文字
    val confirmButtonFill: Brush,         // Confirm 按鈕填滿；Black Gold = 金色橫向漸層
    val confirmButtonText: Color,         // Confirm 按鈕文字
)

val NeonCashInResultColors = CashInResultColors(
    failIconTint = neonRed,
    failIconBorder = neonRed,
    successIconTint = neonCyan,
    successIconBorder = neonCyan,
    titleText = themeWhite,
    failAmountText = neonRed,
    successAmountText = neonCyan,
    subtitleText = silverGray,
    failureReasonText = neonRed,
    cardRowBackground = twilightNavy,
    cardRowBorder = neonCyan.copy(alpha = 0.5f),
    cardIconBackground = deepMidnight,
    cardLabelText = themeWhite,
    cardSubText = silverGray,
    transferArrowTint = neonCyan,
    balanceLabelText = silverGray,
    balanceValueText = themeWhite,
    transactionHistoryLinkText = neonCyan,
    refNoText = silverGray,
    confirmButtonFill = Brush.horizontalGradient(colors = listOf(neonPurple, neonDarkBlue)),
    confirmButtonText = themeWhite,
)

val BlackGoldCashInResultColors = CashInResultColors(
    failIconTint = coralRed,
    failIconBorder = coralRed,
    successIconTint = antiqueGold,
    successIconBorder = antiqueGold,
    titleText = themeWhite,
    failAmountText = coralRed,
    successAmountText = antiqueGold,
    subtitleText = warmSand,
    failureReasonText = coralRed,
    cardRowBackground = charcoalBlack,
    cardRowBorder = antiqueGold.copy(alpha = 0.6f),
    cardIconBackground = themeWhite,
    cardLabelText = themeWhite,
    cardSubText = warmSand,
    transferArrowTint = antiqueGold,
    balanceLabelText = warmSand,
    balanceValueText = themeWhite,
    transactionHistoryLinkText = antiqueGold,
    refNoText = warmSand,
    // 由左至右：oldGold → amberGold → champagneGold → amberGold → oldGold（與 gradient.goldShimmer 一致）
    confirmButtonFill = Brush.horizontalGradient(
        colors = listOf(oldGold, amberGold, champagneGold, amberGold, oldGold)
    ),
    confirmButtonText = themeBlack,
)
