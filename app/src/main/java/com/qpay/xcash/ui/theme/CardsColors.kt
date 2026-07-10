package com.qpay.xcash.ui.theme

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp

// ui/cards/ 資料夾底下畫面專屬顏色

data class CardsColors(
    val emptyStateActionText: Color, // CardsEmptyState "Link Your First Card" CTA 文字顏色
    val manageButtonBorder: Brush,   // CreditCardItem Manage 按鈕外框
    val manageButtonText: Brush,     // CreditCardItem Manage 按鈕文字
    val primaryBadgeBorder: Brush,   // CreditCardItem Primary 徽章外框
    val primaryBadgeText: Brush,     // CreditCardItem Primary 徽章文字
    val primaryBadgeBackground: Color, // CreditCardItem Primary 徽章底色；Black Gold = 透明
    val primaryBadgeShape: Shape,      // CreditCardItem Primary 徽章圓角；Neon = 膠囊，Black Gold = 5.dp
    val addNewCardBackground: Brush,   // AddNewCardButton 背景；Black Gold = 金色橫向漸層
    val addNewCardContent: Color,      // AddNewCardButton 文字與 icon 顏色
    val voucherAmountText: Brush,      // VoucherTicket 金額文字；Black Gold = 金色直向漸層
)

val NeonCardsColors = CardsColors(
    emptyStateActionText = neonGreen,
    manageButtonBorder = SolidColor(neonOrange),
    manageButtonText = SolidColor(neonOrange),
    primaryBadgeBorder = SolidColor(neonPurple),
    primaryBadgeText = SolidColor(Color.White),
    primaryBadgeBackground = neonPurple.copy(alpha = 0.2f),
    primaryBadgeShape = CircleShape,
    addNewCardBackground = SolidColor(deepMidnight),
    addNewCardContent = neonCyan,
    voucherAmountText = SolidColor(neonPurple),
)

val BlackGoldCardsColors = CardsColors(
    emptyStateActionText = amberGold,
    // 由上而下：graphiteGray → steelGray → silverMist → steelGray → graphiteGray（同 gradient.silverShimmer 色階）
    manageButtonBorder = Brush.verticalGradient(
        colors = listOf(graphiteGray, steelGray, silverMist, steelGray, graphiteGray)
    ),
    manageButtonText = Brush.verticalGradient(
        colors = listOf(graphiteGray, steelGray, silverMist, steelGray, graphiteGray)
    ),
    // 由上而下：oldGold → amberGold → champagneGold → amberGold → oldGold（同 gradient.goldShimmer 色階）
    primaryBadgeBorder = Brush.verticalGradient(
        colors = listOf(oldGold, amberGold, champagneGold, amberGold, oldGold)
    ),
    primaryBadgeText = Brush.verticalGradient(
        colors = listOf(oldGold, amberGold, champagneGold, amberGold, oldGold)
    ),
    primaryBadgeBackground = Color.Transparent,
    primaryBadgeShape = RoundedCornerShape(5.dp),
    // 由左至右：oldGold → amberGold → champagneGold → amberGold → oldGold（同 gradient.goldShimmer 色階）
    addNewCardBackground = Brush.horizontalGradient(
        colors = listOf(oldGold, amberGold, champagneGold, amberGold, oldGold)
    ),
    addNewCardContent = Color.Black,
    // 由上而下：oldGold → amberGold → champagneGold → amberGold → oldGold（同 gradient.goldShimmer 色階）
    voucherAmountText = Brush.verticalGradient(
        colors = listOf(oldGold, amberGold, champagneGold, amberGold, oldGold)
    ),
)
