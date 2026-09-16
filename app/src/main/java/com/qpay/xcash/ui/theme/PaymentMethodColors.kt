package com.qpay.xcash.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor

// ui/cashin/paymentmethod/ PaymentMethodScreen 專屬顏色

data class PaymentMethodColors(
    val cardRowBackground: Color,          // 卡片列底色
    val cardRowBorderSelected: Brush,      // 選中卡片列邊框；Black Gold = 金色橫向漸層
    val cardRowBorderUnselected: Color,    // 未選中卡片列邊框
    val radioBorder: Color,                // 未選中 radio 外框
    val radioSelectedBorder: Brush,        // 選中 radio 外框
    val radioSelectedDot: Brush,           // 選中 radio 內部圓點
    val badgeBackground: Brush,            // 卡片網路小圖示背景（目前為內建色塊占位）
    val badgeChip: Color,                  // 小圖示上的晶片方塊
    val badgeText: Color,                  // 小圖示上的卡別文字（VISA/MASTERCARD）
    val premiumTagBackground: Color,       // PREMIUM 標籤底色
    val premiumTagText: Color,             // PREMIUM 標籤文字
    val bankNameText: Color,               // 銀行名稱文字
    val cardNumberText: Color,             // 卡號遮罩文字
    val expiredText: Color,                // Card expired. 提示文字
    val linkCardBackground: Color,         // 「Can't find your card?」區塊底色
    val linkCardBorder: Color,             // Link Card Now 按鈕邊框
    val linkCardText: Color,               // Link Card Now 按鈕文字
    val linkCardIconBackground: Color,     // 左側問號 icon 底色
    val confirmButtonFill: Brush,          // Confirm 按鈕填滿；Black Gold = 金色橫向漸層
    val confirmButtonText: Color,          // Confirm 按鈕文字
)

val NeonPaymentMethodColors = PaymentMethodColors(
    cardRowBackground = twilightNavy,
    cardRowBorderSelected = Brush.horizontalGradient(
        colors = listOf(neonPurple, neonCyan, neonPurple)
    ),
    cardRowBorderUnselected = neonCyan.copy(alpha = 0.2f),
    radioBorder = silverGray.copy(alpha = 0.5f),
    radioSelectedBorder = Brush.horizontalGradient(
        colors = listOf(neonPurple, neonCyan, neonPurple)
    ),
    radioSelectedDot = SolidColor(neonCyan),
    badgeBackground = Brush.linearGradient(colors = listOf(neonBlue, neonDarkBlue)),
    badgeChip = silverGray.copy(alpha = 0.6f),
    badgeText = themeWhite,
    premiumTagBackground = neonPurple.copy(alpha = 0.25f),
    premiumTagText = neonPurple,
    bankNameText = themeWhite,
    cardNumberText = silverGray,
    expiredText = neonRed,
    linkCardBackground = twilightNavy,
    linkCardBorder = neonCyan.copy(alpha = 0.5f),
    linkCardText = neonCyan,
    linkCardIconBackground = deepMidnight,
    confirmButtonFill = Brush.horizontalGradient(
        colors = listOf(neonPurple, neonDarkBlue)
    ),
    confirmButtonText = themeWhite,
)

val BlackGoldPaymentMethodColors = PaymentMethodColors(
    cardRowBackground = charcoalBlack,
    // 由左至右：oldGold → amberGold → champagneGold → amberGold → oldGold（與 gradient.goldShimmer 一致）
    cardRowBorderSelected = Brush.horizontalGradient(
        colors = listOf(oldGold, amberGold, champagneGold, amberGold, oldGold)
    ),
    cardRowBorderUnselected = warmSand.copy(alpha = 0.15f),
    radioBorder = warmSand.copy(alpha = 0.5f),
    radioSelectedBorder = Brush.horizontalGradient(
        colors = listOf(oldGold, amberGold, champagneGold, amberGold, oldGold)
    ),
    radioSelectedDot = Brush.horizontalGradient(
        colors = listOf(oldGold, amberGold, champagneGold, amberGold, oldGold)
    ),
    badgeBackground = Brush.linearGradient(colors = listOf(navyDark, indigoDark)),
    badgeChip = platinumSilver.copy(alpha = 0.7f),
    badgeText = themeWhite,
    premiumTagBackground = antiqueGold.copy(alpha = 0.2f),
    premiumTagText = antiqueGold,
    bankNameText = themeWhite,
    cardNumberText = warmSand,
    expiredText = coralRed,
    linkCardBackground = deepBronze,
    linkCardBorder = antiqueGold.copy(alpha = 0.4f),
    linkCardText = antiqueGold,
    linkCardIconBackground = charcoalBlack,
    // 由左至右：oldGold → amberGold → champagneGold → amberGold → oldGold（與 gradient.goldShimmer 一致）
    confirmButtonFill = Brush.horizontalGradient(
        colors = listOf(oldGold, amberGold, champagneGold, amberGold, oldGold)
    ),
    confirmButtonText = themeBlack,
)
