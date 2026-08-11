package com.qpay.xcash.ui.theme

import androidx.compose.ui.graphics.Color

// ui/friend/components/ FoundFriendItem 專屬顏色

data class FoundFriendColors(
    val addButtonBackground: Color,   // 左側「+」圓形按鈕底色
    val addButtonBorder: Color,       // 左側「+」圓形按鈕邊框
    val addButtonIcon: Color,         // 左側「+」icon 顏色
    val avatarBorder: Color,          // 頭像外框
    val nameText: Color,              // 姓名文字（Neon = 單色；Black Gold 由 gradient.goldShimmer 覆蓋）
    val usernameText: Color,          // @username 文字
    val dismissIconTint: Color,       // 右側 X 關閉 icon 顏色
)

val NeonFoundFriendColors = FoundFriendColors(
    addButtonBackground = neonCyan,
    addButtonBorder = neonCyan,
    addButtonIcon = themeBlack,
    avatarBorder = neonCyan,
    nameText = themeWhite,
    usernameText = silverGray,
    dismissIconTint = silverGray,
)

val BlackGoldFoundFriendColors = FoundFriendColors(
    addButtonBackground = antiqueGold,
    addButtonBorder = antiqueGold,
    addButtonIcon = themeBlack,
    avatarBorder = antiqueGold,
    nameText = themeWhite,
    usernameText = warmSand,
    dismissIconTint = warmSand,
)
