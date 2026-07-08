package com.qpay.xcash.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// TransactionDetailScreen 專屬顏色

data class TransactionDetailColors(
    val cardBackground: Color,        // InfoCard 底色
    val failedStatusText: Color,      // 失敗狀態文字顏色（成功用 colors.accent.primary）
    val amountText: Color,            // 大額金額文字顏色（GradientText 的 Neon fallback 色）
    val successIcon: Color,           // 成功狀態 icon 顏色
    val paymentInfoCardBorder: Color,     // 支付信息卡片邊框
    val amountCardBorder: Color,      // 金額明細卡片邊框
    val transactionCardBorder: Color,  // 交易信息卡片邊框
    val closeButton: Color,           // 底部關閉按鈕邊框 + icon 顏色
    val dividerBrush: Brush,         // PartyRow 分隔線橫向漸層；Neon = null（維持單色 accent.primary）
    // action icon
    val reloadIcon: Color,
    val favoriteIcon: Color,
    val shareIcon: Color,
    // ── PartyRow（Pay To / Pay From）──────────────────────────
    val payToLabel: Color,            // Pay To 標籤文字顏色
    val payToTitle: Color,            // Pay To 姓名（title）文字顏色
    val payFromLabel: Color,          // Pay From 標籤文字顏色
    val payFromTitle: Color,          // Pay From 姓名（title）文字顏色

    // ── AmountRow（Amount / Fee / Total）──────────────────────
    val amountRowLabel: Color,        // 標籤文字顏色
    val amountRowTitle: Color,        // 金額數值文字顏色

    // ── LabelValueRow（Reference No. / Date）──────────────────
    val labelValueRowLabel: Color,    // 標籤文字顏色
    val labelValueRowTitle: Color,    // 對應數值文字顏色
)

val NeonTransactionDetailColors = TransactionDetailColors(
    cardBackground = deepMidnight,
    failedStatusText = neonPink,
    amountText = neonPurpleLight,
    successIcon = Color.Unspecified,
    paymentInfoCardBorder = neonCyan,
    amountCardBorder = cyberPurple,
    transactionCardBorder = neonCyan,
    closeButton = neonPurpleLight,
    dividerBrush = Brush.horizontalGradient(
        colors = listOf(neonCyan, neonCyan)
    ),
    reloadIcon = Color.Unspecified,
    favoriteIcon = Color.Unspecified,
    shareIcon = Color.Unspecified,
    payToLabel = neonMint,
    payToTitle = neonMint,
    payFromLabel = neonBlushPink,
    payFromTitle = neonBlushPink,

    amountRowLabel = Color.White,
    amountRowTitle = cyberPurple,

    labelValueRowLabel = silverGray,
    labelValueRowTitle = neonCyan,
)

val BlackGoldTransactionDetailColors = TransactionDetailColors(
    cardBackground = Color.Transparent,
    failedStatusText = neonRed,
    amountText = antiqueGold,
    successIcon = antiqueGold,
    paymentInfoCardBorder = Color.Transparent,
    amountCardBorder = Color.Transparent,
    transactionCardBorder = Color.Transparent,
    closeButton = Color.White,
    dividerBrush = Brush.horizontalGradient(
        colors = listOf(graphiteGray, steelGray, silverMist, steelGray, graphiteGray)
    ),
    reloadIcon = amberGold,
    favoriteIcon = champagneGold,
    shareIcon = paleGold,
    payToLabel = antiqueGold,
    payToTitle = coolGray,
    payFromLabel = antiqueGold,
    payFromTitle = coolGray,

    amountRowLabel = antiqueGold,
    amountRowTitle = coolGray,

    labelValueRowLabel = coolGray,
    labelValueRowTitle = coolGray,
)
