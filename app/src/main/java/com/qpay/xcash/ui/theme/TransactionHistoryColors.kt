package com.qpay.xcash.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor

// ui/profile/transaction/ TransactionHistoryScreen 專屬顏色

data class TransactionHistoryColors(
    // ── BalanceCard ─────────────────────────────────────────
    val cardBackground: Color,            // BalanceCard / TransactionListCard 底色
    val balanceCardBorder: Brush,         // BalanceCard 外框；Neon = cyan→purple 漸層
    val balanceLabelText: Color,          // BALANCE 標籤文字 + 眼睛 icon
    val balanceValueText: Color,          // 金額文字
    val tokenCircleBackground: Brush,     // 右側 Token 圓圈底色
    val tokenCircleBorder: Color,         // Token 圓圈邊框
    val tokenValueText: Color,            // Token 數字
    val tokenLabelText: Color,            // X POINTS 標籤

    // ── TimeFilterRow ───────────────────────────────────────
    val timeFilterBorder: Brush,          // 時間篩選外框；Neon = cyan→purple 漸層
    val timeFilterSelectedBackground: Color, // 選中項底色
    val timeFilterSelectedText: Color,    // 選中項文字
    val timeFilterText: Color,            // 未選中項文字

    // ── CategoryFilterRow ───────────────────────────────────
    val categoryFilterBorder: Color,      // 分類篩選外框
    val categorySelectedBackground: Color, // 選中 segment 底色
    val categoryText: Color,              // 分類文字

    // ── TransactionListCard ─────────────────────────────────
    val listCardBorder: Brush,            // 清單卡片外框；Neon = cyan→purple 漸層
    val listDivider: Color,               // 項目間分隔線
    val emptyText: Color,                 // 無資料文字

    // ── TransactionItem ─────────────────────────────────────
    val itemIconGradient: Brush?,         // 項目 icon 漸層；Neon = null（原樣顯示）
    val itemTitleText: Color,             // 交易名稱
    val itemSubtitleText: Color,          // 帳號 · 日期
    val amountIncomingText: Color,        // 入帳金額（+）
    val amountOutgoingText: Color,        // 出帳金額（-）
)

val NeonTransactionHistoryColors = TransactionHistoryColors(
    cardBackground = notifyCardBg,
    balanceCardBorder = Brush.linearGradient(
        colors = listOf(neonCyan.copy(alpha = 0.5f), neonPurple.copy(alpha = 0.7f))
    ),
    balanceLabelText = silverGray,
    balanceValueText = themeWhite,
    tokenCircleBackground = Brush.radialGradient(
        colors = listOf(neonPurple.copy(alpha = 0.35f), neonCyan.copy(alpha = 0.15f))
    ),
    tokenCircleBorder = neonCyan.copy(alpha = 0.5f),
    tokenValueText = themeWhite,
    tokenLabelText = silverGray,
    timeFilterBorder = Brush.linearGradient(
        colors = listOf(neonCyan.copy(alpha = 0.5f), neonPurple.copy(alpha = 0.7f))
    ),
    timeFilterSelectedBackground = neonCyan.copy(alpha = 0.12f),
    timeFilterSelectedText = neonCyan,
    timeFilterText = silverGray,
    categoryFilterBorder = steelTeal,
    categorySelectedBackground = steelTeal,
    categoryText = themeWhite,
    listCardBorder = Brush.linearGradient(
        colors = listOf(neonCyan.copy(alpha = 0.25f), neonPurple.copy(alpha = 0.3f))
    ),
    listDivider = neonCyan.copy(alpha = 0.08f),
    emptyText = silverGray,
    itemIconGradient = null,
    itemTitleText = themeWhite,
    itemSubtitleText = silverGray,
    amountIncomingText = neonCyan,
    amountOutgoingText = neonPurpleLight,
)

val BlackGoldTransactionHistoryColors = TransactionHistoryColors(
    cardBackground = darkObsidian,
    balanceCardBorder = SolidColor(paleGold),
    balanceLabelText = antiqueGold,
    balanceValueText = coolGray,
    tokenCircleBackground = Brush.radialGradient(
        colors = listOf(antiqueGold.copy(alpha = 0.25f), antiqueGold.copy(alpha = 0.08f))
    ),
    tokenCircleBorder = paleGold,
    tokenValueText = coolGray,
    tokenLabelText = warmSand,
    timeFilterBorder = SolidColor(paleGold),
    timeFilterSelectedBackground = antiqueGold.copy(alpha = 0.12f),
    timeFilterSelectedText = antiqueGold,
    timeFilterText = warmSand,
    categoryFilterBorder = paleGold,
    categorySelectedBackground = caramelBrown,
    categoryText = themeWhite,
    listCardBorder = SolidColor(paleGold),
    listDivider = antiqueGold.copy(alpha = 0.2f),
    emptyText = warmSand,
    // 由左至右：bronzeGold → paleChampagne → bronzeGold
    itemIconGradient = Brush.horizontalGradient(
        colors = listOf(bronzeGold, paleChampagne, bronzeGold)
    ),
    itemTitleText = coolGray,
    itemSubtitleText = warmSand,
    amountIncomingText = amberGold,
    amountOutgoingText = platinumSilver,
)
