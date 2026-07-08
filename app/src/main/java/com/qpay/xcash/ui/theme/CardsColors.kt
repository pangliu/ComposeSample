package com.qpay.xcash.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor

// ui/cards/ 資料夾底下畫面專屬顏色

data class CardsColors(
    val emptyStateActionText: Color, // CardsEmptyState "Link Your First Card" CTA 文字顏色
//    val promoBannerGradient: Brush,  // PromoBannerCard 背景漸層
    val primaryCardBorder: Brush,    // CreditCardItem 邊框（isPrimary = true）
    val secondaryCardBorder: Brush,  // CreditCardItem 邊框（isPrimary = false）
)

val NeonCardsColors = CardsColors(
    emptyStateActionText = neonGreen,
//    promoBannerGradient = Brush.verticalGradient(
//        listOf(Color(0xFF1A1050), Color(0xFF0B1030))
//    ),
    primaryCardBorder = SolidColor(neonPurple),
    secondaryCardBorder = SolidColor(neonCyan),
)

val BlackGoldCardsColors = CardsColors(
    emptyStateActionText = amberGold,
//    promoBannerGradient = Brush.verticalGradient(
//        listOf(charcoalBlack, nearBlack)
//    ),
    // 由左至右：graphiteGray → steelGray → silverMist → steelGray → graphiteGray（同 gradient.silverShimmer 色階）
    primaryCardBorder = Brush.horizontalGradient(
        colors = listOf(graphiteGray, steelGray, silverMist, steelGray, graphiteGray)
    ),
    // 由左至右：oldGold → amberGold → champagneGold → amberGold → oldGold（同 gradient.goldShimmer 色階）
    secondaryCardBorder = Brush.horizontalGradient(
        colors = listOf(oldGold, amberGold, champagneGold, amberGold, oldGold)
    ),
)
