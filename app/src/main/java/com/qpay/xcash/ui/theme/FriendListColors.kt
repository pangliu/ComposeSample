package com.qpay.xcash.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor

// ui/friend/list/ FriendListScreen 專屬顏色

data class FriendListColors(
    // ── SearchBar ────────────────────────────────────
    val searchBarBackground: Color,     // 搜尋框底色
    val searchBarBorder: Color,         // 搜尋框邊框
    val searchPlaceholderText: Color,   // placeholder 文字

    // ── Tab Row（All / Favorites / Recent）────────────
    val tabSelectedText: Color,         // 選中 tab 文字
    val tabUnselectedText: Color,       // 未選中 tab 文字
    val tabIndicator: Brush,            // 選中 tab 底線

    // ── Category Chips ─────────────────────────────────
    val chipSelectedFill: Brush,        // 選中 chip 底色；Black Gold = 金色橫向漸層
    val chipSelectedText: Color,        // 選中 chip 文字
    val chipUnselectedBorder: Color,    // 未選中 chip 外框
    val chipUnselectedText: Color,      // 未選中 chip 文字

    // ── Results Header ─────────────────────────────────
    val resultsCountText: Brush,        // "Results · N" 文字
    val sortText: Color,                // 排序文字 + 箭頭 icon

    // ── Friend List Item ───────────────────────────────
    val tagBackground: Color,           // Family / Besties 標籤底色
    val tagText: Color,                 // 標籤文字
    val starInactive: Color,            // 未收藏星星顏色
    val listDivider: Color,             // 項目分隔線
)

val NeonFriendListColors = FriendListColors(
    searchBarBackground = twilightNavy,
    searchBarBorder = neonCyan,
    searchPlaceholderText = silverGray,
    tabSelectedText = neonCyan,
    tabUnselectedText = silverGray,
    tabIndicator = SolidColor(neonCyan),
    chipSelectedFill = SolidColor(neonCyan.copy(alpha = 0.15f)),
    chipSelectedText = neonCyan,
    chipUnselectedBorder = neonCyan.copy(alpha = 0.4f),
    chipUnselectedText = silverGray,
    resultsCountText = SolidColor(silverGray),
    sortText = neonCyanLight,
    tagBackground = neonDarkPurple.copy(alpha = 0.4f),
    tagText = neonPurpleLight,
    starInactive = silverGray.copy(alpha = 0.5f),
    listDivider = neonCyan.copy(alpha = 0.08f),
)

val BlackGoldFriendListColors = FriendListColors(
    searchBarBackground = charcoalBlack,
    searchBarBorder = antiqueGold,
    searchPlaceholderText = warmSand,
    tabSelectedText = antiqueGold,
    tabUnselectedText = warmSand,
    tabIndicator = Brush.horizontalGradient(
        colors = listOf(oldGold, amberGold, champagneGold, amberGold, oldGold)
    ),
    // 由左至右：oldGold → amberGold → champagneGold → amberGold → oldGold（與 gradient.goldShimmer 一致）
    chipSelectedFill = Brush.horizontalGradient(
        colors = listOf(oldGold, amberGold, champagneGold, amberGold, oldGold)
    ),
    chipSelectedText = themeBlack,
    chipUnselectedBorder = antiqueGold,
    chipUnselectedText = warmSand,
    resultsCountText = Brush.horizontalGradient(
        colors = listOf(oldGold, amberGold, champagneGold, amberGold, oldGold)
    ),
    sortText = antiqueGold,
    tagBackground = charcoalGray,
    tagText = amberGold,
    starInactive = coolGray,
    listDivider = antiqueGold.copy(alpha = 0.15f),
)
