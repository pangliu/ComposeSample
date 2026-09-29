package com.qpay.xcash.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor

// ui/transfer/result/ TransferResultScreen 專屬顏色

data class TransferResultColors(
    val successIconTint: Color,           // 成功圖示（勾勾）
    val successIconBorder: Color,         // 成功圓形外框
    val failIconTint: Color,              // 失敗圖示（X）
    val failIconBorder: Color,            // 失敗圓形外框
    val titleText: Color,                 // Transfer Successful / Failed 標題
    val successAmountText: Color,         // 成功時的金額文字
    val failAmountText: Color,            // 失敗時的金額文字
    val subtitleText: Color,              // 日期時間文字
    val failureReasonText: Color,         // 失敗原因文字
    val cardBackground: Color,            // 付款方 / 收款方卡片底色
    val cardBorder: Brush,                // 付款方 / 收款方卡片邊框；Black Gold = 金色橫向漸層
    val cardIconBackground: Color,        // 卡片內 icon 方塊底色
    val cardNameText: Color,              // 帳戶名稱文字
    val cardSubText: Color,               // 遮罩帳號文字
    val transferArrowTint: Color,         // 中間向下箭頭
    val balanceLabelText: Color,          // Balance After Transaction 標籤
    val balanceValueText: Color,          // Balance After Transaction 數值
    val transactionHistoryLinkText: Color, // Transaction History 連結文字
    val refNoText: Color,                 // Ref No. 文字
    val addFriendButtonFill: Color,       // Add to Friends List 按鈕底色
    val addFriendButtonBorder: Color,     // Add to Friends List 按鈕邊框
    val addFriendButtonText: Color,       // Add to Friends List 按鈕文字 / icon
    val doneButtonFill: Brush,            // Done 按鈕填滿；Black Gold = 深藍紫橫向漸層
    val doneButtonBorder: Color,          // Done 按鈕邊框
    val doneButtonText: Color,            // Done 按鈕文字
    val shareButtonFill: Brush,           // Share Transfer Details 按鈕填滿；Neon = 透明
    val shareButtonBorder: Color,         // Share Transfer Details 按鈕邊框
    val shareButtonText: Color,           // Share Transfer Details 按鈕文字
)

val NeonTransferResultColors = TransferResultColors(
    successIconTint = neonCyan,
    successIconBorder = neonCyan,
    failIconTint = neonRed,
    failIconBorder = neonRed,
    titleText = themeWhite,
    successAmountText = neonCyan,
    failAmountText = neonRed,
    subtitleText = silverGray,
    failureReasonText = neonRed,
    cardBackground = twilightNavy,
    cardBorder = Brush.horizontalGradient(colors = listOf(neonPurple, neonCyan, neonPurple)),
    cardIconBackground = deepMidnight,
    cardNameText = themeWhite,
    cardSubText = silverGray,
    transferArrowTint = neonCyan,
    balanceLabelText = silverGray,
    balanceValueText = themeWhite,
    transactionHistoryLinkText = neonCyan,
    refNoText = silverGray,
    addFriendButtonFill = twilightNavy,
    addFriendButtonBorder = neonCyan.copy(alpha = 0.5f),
    addFriendButtonText = themeWhite,
    doneButtonFill = SolidColor(neonCyan),
    doneButtonBorder = neonCyan,
    doneButtonText = themeBlack,
    shareButtonFill = SolidColor(Color.Transparent),
    shareButtonBorder = neonPurple,
    shareButtonText = neonPurpleLight,
)

val BlackGoldTransferResultColors = TransferResultColors(
    successIconTint = antiqueGold,
    successIconBorder = antiqueGold,
    failIconTint = coralRed,
    failIconBorder = coralRed,
    titleText = themeWhite,
    successAmountText = amberGold,
    failAmountText = coralRed,
    subtitleText = warmSand,
    failureReasonText = coralRed,
    cardBackground = charcoalBlack,
    // 由左至右：oldGold → amberGold → champagneGold → amberGold → oldGold（與 ConfirmTransferScreen 卡片一致）
    cardBorder = Brush.horizontalGradient(
        colors = listOf(oldGold, amberGold, champagneGold, amberGold, oldGold)
    ),
    cardIconBackground = midnightIndigo,
    cardNameText = themeWhite,
    cardSubText = warmSand,
    transferArrowTint = antiqueGold,
    balanceLabelText = themeWhite,
    balanceValueText = themeWhite,
    transactionHistoryLinkText = antiqueGold,
    refNoText = themeWhite,
    addFriendButtonFill = charcoalLightBlack,
    addFriendButtonBorder = charcoalGray,
    addFriendButtonText = themeWhite,
    // 由左至右：midnightIndigo → slateViolet → midnightIndigo（與 TransactionSuccessfulScreen Done 按鈕一致）
    doneButtonFill = Brush.horizontalGradient(
        colors = listOf(midnightIndigo, slateViolet, midnightIndigo)
    ),
    doneButtonBorder = apricotGold,
    doneButtonText = apricotGold,
    // 由左至右：deepViolet → amethystPurple → deepViolet（與 TransactionSuccessfulScreen Share 按鈕一致）
    shareButtonFill = Brush.horizontalGradient(
        colors = listOf(deepViolet, amethystPurple, deepViolet)
    ),
    shareButtonBorder = apricotGold,
    shareButtonText = apricotGold,
)
