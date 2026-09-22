package com.qpay.xcash.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// ui/transfer/ GeneralTransferScreen 專屬顏色

data class GeneralTransferColors(
    val recipientCardBackground: Color,     // 收款人卡片底色
    val recipientCardBorder: Brush,         // 收款人卡片邊框；Black Gold = 金色橫向漸層
    val avatarBackground: Color,            // 「To」圓形頭像底色
    val avatarBorder: Color,                // 「To」圓形頭像邊框
    val avatarText: Color,                  // 「To」文字
    val recipientNameText: Color,           // 收款人姓名
    val recipientPhoneText: Color,          // 收款人遮罩電話
    val tagBackground: Color,               // Family 等標籤底色
    val tagText: Color,                     // 標籤文字
    val hintText: Color,                    // 「Please select an account...」提示文字
    val accountRowBackground: Color,        // 帳戶列底色
    val accountRowBorder: Color,            // 未選中帳戶列邊框
    val accountRowSelectedBackground: Color, // 選中帳戶列底色
    val accountRowSelectedBorder: Brush,    // 選中帳戶列邊框
    val accountIconBackground: Color,       // 帳戶 icon 方塊底色（目前為內建圖示占位）
    val accountIconTint: Color,             // 帳戶 icon 顏色
    val accountNameText: Color,             // 銀行 / 錢包名稱
    val accountNumberText: Color,           // 遮罩卡號
    val feeText: Color,                     // Fee / No Fee 文字
    val expiredText: Color,                 // Card expired. 提示文字
    val manualInputBackground: Color,       // 「Can't find the account?」區塊底色
    val manualInputBorder: Color,           // Manual Input 按鈕邊框
    val manualInputText: Color,             // 提示與按鈕文字
    val manualInputIconBackground: Color,   // 左側問號 icon 底色
    val nextButtonFill: Brush,              // Next 按鈕填滿；Black Gold = 金色橫向漸層
    val nextButtonText: Color,              // Next 按鈕文字
)

val NeonGeneralTransferColors = GeneralTransferColors(
    recipientCardBackground = twilightNavy,
    recipientCardBorder = Brush.horizontalGradient(colors = listOf(neonPurple, neonCyan, neonPurple)),
    avatarBackground = deepMidnight,
    avatarBorder = neonCyan,
    avatarText = neonCyan,
    recipientNameText = themeWhite,
    recipientPhoneText = silverGray,
    tagBackground = neonPurple.copy(alpha = 0.25f),
    tagText = neonPurple,
    hintText = neonCyan,
    accountRowBackground = twilightNavy,
    accountRowBorder = neonCyan.copy(alpha = 0.2f),
    accountRowSelectedBackground = steelNavy,
    accountRowSelectedBorder = Brush.horizontalGradient(colors = listOf(neonPurple, neonCyan, neonPurple)),
    accountIconBackground = deepMidnight,
    accountIconTint = neonCyan,
    accountNameText = themeWhite,
    accountNumberText = silverGray,
    feeText = silverGray,
    expiredText = neonRed,
    manualInputBackground = twilightNavy,
    manualInputBorder = neonCyan.copy(alpha = 0.5f),
    manualInputText = neonCyan,
    manualInputIconBackground = deepMidnight,
    nextButtonFill = Brush.horizontalGradient(colors = listOf(neonPurple, neonDarkBlue)),
    nextButtonText = themeWhite,
)

val BlackGoldGeneralTransferColors = GeneralTransferColors(
    recipientCardBackground = charcoalBlack,
    // 由左至右：oldGold → amberGold → champagneGold → amberGold → oldGold（與 gradient.goldShimmer 一致）
    recipientCardBorder = Brush.horizontalGradient(
        colors = listOf(oldGold, amberGold, champagneGold, amberGold, oldGold)
    ),
    avatarBackground = midnightIndigo,
    avatarBorder = antiqueGold,
    avatarText = antiqueGold,
    recipientNameText = themeWhite,
    recipientPhoneText = warmSand,
    tagBackground = antiqueGold.copy(alpha = 0.2f),
    tagText = antiqueGold,
    hintText = antiqueGold,
    accountRowBackground = charcoalBlack,
    accountRowBorder = warmSand.copy(alpha = 0.25f),
    accountRowSelectedBackground = midnightIndigo,
    accountRowSelectedBorder = Brush.horizontalGradient(
        colors = listOf(oldGold, amberGold, champagneGold, amberGold, oldGold)
    ),
    accountIconBackground = themeWhite,
    accountIconTint = antiqueGold,
    accountNameText = themeWhite,
    accountNumberText = warmSand,
    feeText = warmSand,
    expiredText = coralRed,
    manualInputBackground = deepBronze,
    manualInputBorder = antiqueGold.copy(alpha = 0.4f),
    manualInputText = antiqueGold,
    manualInputIconBackground = charcoalBlack,
    nextButtonFill = Brush.horizontalGradient(
        colors = listOf(oldGold, amberGold, champagneGold, amberGold, oldGold)
    ),
    nextButtonText = themeBlack,
)
