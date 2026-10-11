package com.qpay.xcash.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor

// ui/transfer/wallet/dialog/ SelectFriendDialog 專屬顏色

data class SelectFriendDialogColors(
    val sheetBackground: Color,        // 底部彈窗底色
    val sheetBorder: Brush,            // 底部彈窗外框；Black Gold = 金色橫向漸層
    val dragHandle: Color,             // 頂部拖曳把手
    val searchBackground: Color,       // 搜尋框底色
    val searchBorder: Color,           // 搜尋框邊框
    val searchText: Color,             // 搜尋輸入文字
    val searchHintText: Color,         // 搜尋框 placeholder
    val searchIcon: Color,             // 搜尋 icon
    val avatarBorder: Brush,           // 好友頭像外框；Black Gold = 金色橫向漸層
    val nameText: Color,               // 好友名稱
    val subText: Color,                // 好友副標（遮罩手機號碼 / Xcash Account）
    val tagFamilyBackground: Color,    // "Family" 標籤底色
    val tagFamilyText: Color,          // "Family" 標籤文字
    val tagBestiesBackground: Color,   // "Besties" 標籤底色（其他未知標籤也沿用此樣式）
    val tagBestiesText: Color,         // "Besties" 標籤文字
    val emptyText: Color,              // 無好友 / 搜尋無結果文字
    val scrollbar: Color,              // 列表捲軸
)

// Neon 設計稿尚未提供，暫沿用既有 Neon 色票維持風格一致，待設計稿確認後再替換
val NeonSelectFriendDialogColors = SelectFriendDialogColors(
    sheetBackground = deepMidnight,
    sheetBorder = Brush.horizontalGradient(colors = listOf(neonPurple, neonCyan, neonPurple)),
    dragHandle = silverGray.copy(alpha = 0.4f),
    searchBackground = twilightNavy,
    searchBorder = neonCyan,
    searchText = themeWhite,
    searchHintText = silverGray,
    searchIcon = neonCyan,
    avatarBorder = Brush.horizontalGradient(colors = listOf(neonPurple, neonCyan, neonPurple)),
    nameText = themeWhite,
    subText = neonSlateText,
    tagFamilyBackground = steelTeal.copy(alpha = 0.4f),
    tagFamilyText = neonCyan,
    tagBestiesBackground = neonDarkPurple.copy(alpha = 0.6f),
    tagBestiesText = neonPurpleLight,
    emptyText = silverGray,
    scrollbar = neonCyan.copy(alpha = 0.4f),
)

val BlackGoldSelectFriendDialogColors = SelectFriendDialogColors(
    sheetBackground = onyxBlack,
    // 由左至右：oldGold → amberGold → champagneGold → amberGold → oldGold（與 gradient.goldShimmer 一致）
    sheetBorder = Brush.horizontalGradient(
        colors = listOf(oldGold, amberGold, champagneGold, amberGold, oldGold)
    ),
    dragHandle = ashGray,
    searchBackground = midnightSlate,
    searchBorder = antiqueGold,
    searchText = themeWhite,
    searchHintText = warmSand,
    searchIcon = antiqueGold,
    avatarBorder = Brush.horizontalGradient(
        colors = listOf(oldGold, amberGold, champagneGold, amberGold, oldGold)
    ),
    nameText = themeWhite,
    subText = dimGray,
    tagFamilyBackground = taupeBlack,
    tagFamilyText = softChampagne,
    tagBestiesBackground = slateNavy,
    tagBestiesText = mistSteel,
    emptyText = warmSand,
    scrollbar = scrollbarTaupe,
)
