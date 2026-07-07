package com.qpay.xcash.ui.theme

import androidx.compose.ui.graphics.Color

// TransactionDetailScreen 專屬顏色

data class TransactionDetailColors(
    val cardBackground: Color,        // InfoCard 底色
    val failedStatusText: Color,      // 失敗狀態文字顏色（成功用 colors.accent.primary）
    val amountText: Color,            // 大額金額文字顏色（GradientText 的 Neon fallback 色）
    val amountCardBorder: Color,      // 金額明細卡片邊框
    val closeButton: Color,           // 底部關閉按鈕邊框 + icon 顏色

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
    cardBackground = Color(0xFF0A1628),
    failedStatusText = neonPink,
    amountText = neonPurpleLight,
    amountCardBorder = cyberPurple,
    closeButton = neonPurpleLight,

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
    amountCardBorder = paleGold,
    closeButton = antiqueGold,

    payToLabel = antiqueGold,
    payToTitle = coolGray,
    payFromLabel = antiqueGold,
    payFromTitle = coolGray,

    amountRowLabel = antiqueGold,
    amountRowTitle = coolGray,

    labelValueRowLabel = coolGray,
    labelValueRowTitle = coolGray,
)
