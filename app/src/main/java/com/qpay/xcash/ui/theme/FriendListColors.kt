package com.qpay.xcash.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor

// ui/friend/list/ FriendListScreen 專屬顏色

data class FriendListColors(
    // ── Friend Avatar ──────────────────────────────────
    val avatarBorder: Brush,            // 頭像外框；Neon = 紫→青→紫橫向漸層

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

    // ── Friend List Item · Tag ──────────────────────────
    val tagFamilyBackground: Color,     // "Family" 標籤底色
    val tagFamilyText: Color,           // "Family" 標籤文字
    val tagBestiesBackground: Color,    // "Besties" 標籤底色（其他未知標籤也沿用此樣式）
    val tagBestiesText: Color,          // "Besties" 標籤文字
    val starSelected: Brush,            // 已收藏星星顏色；橫向漸層
    val starInactive: Color,            // 未收藏星星顏色
    val listDivider: Color,             // 項目分隔線

    // ── Not Found Card（搜尋無結果時的邀請卡片）──────────
    val notFoundCardBorder: Brush,      // 卡片外框
    val notFoundIconBackground: Color,  // 禮物 icon 圓形底色
    val notFoundIconTint: Color,        // 禮物 icon 顏色
    val notFoundHintText: Color,        // "No records found" 文字
    val notFoundTitleText: Color,       // "Not found?" 文字
    val notFoundActionText: Color,      // "Invite them to xCash" 文字
    val notFoundArrowTint: Color,       // 右側箭頭

    val sortDialog: FriendSortDialogColors, // FriendSortDialog 專屬顏色
)

// FriendSortDialog 專屬顏色
data class FriendSortDialogColors(
    val dialogBackground: Color,         // Dialog 底色
    val dialogBorder: Color,             // Dialog 外框邊框
    val dragHandle: Color,               // 頂部拖曳把手
    val titleText: Color,                // SORT BY 標題文字
    val optionBackground: Color,         // 選項列底色
    val optionBorder: Color,             // 選項列邊框
    val optionText: Color,               // 選項文字
    val radioBorder: Color,              // 未選中 radio 外框
    val radioSelectedBorder: Brush,      // 選中 radio 外框；橫向漸層
    val radioSelectedDot: Brush,         // 選中 radio 內部圓點；Black Gold = 金色橫向漸層
    val cancelButtonBorder: Color,       // Cancel 按鈕邊框
    val cancelButtonText: Color,         // Cancel 按鈕文字
    val confirmButtonFill: Brush,        // Confirm 按鈕填滿；Black Gold = 金色橫向漸層
    val confirmButtonText: Color,        // Confirm 按鈕文字
)

val NeonFriendSortDialogColors = FriendSortDialogColors(
    dialogBackground = deepMidnight,
    dialogBorder = neonCyan.copy(alpha = 0.7f),
    dragHandle = silverGray.copy(alpha = 0.4f),
    titleText = neonCyan,
    optionBackground = Color.Transparent,
    optionBorder = neonCyan.copy(alpha = 0.35f),
    optionText = Color.White,
    radioBorder = silverGray.copy(alpha = 0.5f),
    radioSelectedBorder = Brush.horizontalGradient(
        colors = listOf(neonPurple, neonCyan, neonPurple)
    ),
    radioSelectedDot = SolidColor(neonCyan),
    cancelButtonBorder = neonCyan,
    cancelButtonText = neonCyan,
    confirmButtonFill = Brush.horizontalGradient(
        colors = listOf(neonPurple, neonDarkBlue)
    ),
    confirmButtonText = themeWhite,
)

val BlackGoldFriendSortDialogColors = FriendSortDialogColors(
    dialogBackground = charcoalBlack,
    dialogBorder = paleGold,
    dragHandle = warmSand.copy(alpha = 0.4f),
    titleText = antiqueGold,
    optionBackground = Color.Transparent,
    optionBorder = antiqueGold.copy(alpha = 0.4f),
    optionText = warmSand,
    radioBorder = warmSand.copy(alpha = 0.5f),
    // 由左至右：oldGold → amberGold → champagneGold → amberGold → oldGold（與 gradient.goldShimmer 一致）
    radioSelectedBorder = Brush.horizontalGradient(
        colors = listOf(oldGold, amberGold, champagneGold, amberGold, oldGold)
    ),
    // 由左至右：oldGold → amberGold → champagneGold → amberGold → oldGold（與 gradient.goldShimmer 一致）
    radioSelectedDot = Brush.horizontalGradient(
        colors = listOf(oldGold, amberGold, champagneGold, amberGold, oldGold)
    ),
    cancelButtonBorder = antiqueGold,
    cancelButtonText = antiqueGold,
    // 由左至右：oldGold → amberGold → champagneGold → amberGold → oldGold（與 gradient.goldShimmer 一致）
    confirmButtonFill = Brush.horizontalGradient(
        colors = listOf(oldGold, amberGold, champagneGold, amberGold, oldGold)
    ),
    confirmButtonText = themeBlack,
)

val NeonFriendListColors = FriendListColors(
    avatarBorder = Brush.horizontalGradient(
        colors = listOf(neonPurple, neonCyan, neonPurple)
    ),
    searchBarBackground = twilightNavy,
    searchBarBorder = neonCyan,
    searchPlaceholderText = silverGray,
    tabSelectedText = neonCyan,
    tabUnselectedText = silverGray,
    tabIndicator = SolidColor(neonCyan),
    chipSelectedFill = SolidColor(neonCyan.copy(alpha = 1f)),
    chipSelectedText = deepNavy,
    chipUnselectedBorder = neonCyan.copy(alpha = 0.4f),
    chipUnselectedText = neonCyan.copy(alpha = 0.7f),
    resultsCountText = SolidColor(silverGray),
    sortText = neonCyanLight,
    tagFamilyBackground = steelTeal.copy(alpha = 0.4f),
    tagFamilyText = neonCyan,
    tagBestiesBackground = neonDarkPurple.copy(alpha = 0.6f),
    tagBestiesText = neonPurpleLight,
    starSelected = Brush.horizontalGradient(
        colors = listOf(neonPurple, neonCyan)
    ),
    starInactive = silverGray.copy(alpha = 0.5f),
    listDivider = neonCyan.copy(alpha = 0.08f),
    notFoundCardBorder = Brush.horizontalGradient(
        colors = listOf(neonPurple, neonCyan, neonPurple)
    ),
    notFoundIconBackground = neonCyan.copy(alpha = 0.15f),
    notFoundIconTint = neonCyan,
    notFoundHintText = themeWhite.copy(0.8f),
    notFoundTitleText = Color.White,
    notFoundActionText = neonCyan,
    notFoundArrowTint = neonCyan,
    sortDialog = NeonFriendSortDialogColors,
)

val BlackGoldFriendListColors = FriendListColors(
    avatarBorder = Brush.horizontalGradient(
        colors = listOf(oldGold, amberGold, champagneGold, amberGold, oldGold)
    ),
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
    // Black Gold 設計稿尚未提供 Family / Besties 差異化樣式，暫沿用同一組顏色
    tagFamilyBackground = charcoalGray,
    tagFamilyText = amberGold,
    tagBestiesBackground = graphiteGray.copy(alpha = 0.6f),
    tagBestiesText = Color.White,
    // 由左至右：oldGold → amberGold → champagneGold → amberGold → oldGold（與 gradient.goldShimmer 一致）
    starSelected = Brush.horizontalGradient(
        colors = listOf(oldGold, amberGold, champagneGold, amberGold, oldGold)
    ),
    starInactive = coolGray,
    listDivider = antiqueGold.copy(alpha = 0.15f),
    // 由左至右：oldGold → amberGold → champagneGold → amberGold → oldGold（與 gradient.goldShimmer 一致）
    notFoundCardBorder = Brush.horizontalGradient(
        colors = listOf(oldGold, amberGold, champagneGold, amberGold, oldGold)
    ),
    notFoundIconBackground = antiqueGold.copy(alpha = 0.15f),
    notFoundIconTint = antiqueGold,
    notFoundHintText = themeWhite.copy(0.8f),
    notFoundTitleText = warmSand,
    notFoundActionText = antiqueGold,
    notFoundArrowTint = antiqueGold,
    sortDialog = BlackGoldFriendSortDialogColors,
)
