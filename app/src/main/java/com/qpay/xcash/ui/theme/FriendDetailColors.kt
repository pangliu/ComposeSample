package com.qpay.xcash.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor

// ui/friend/detail/ FriendDetailScreen 專屬顏色

data class FriendDetailColors(
    val avatarBorder: Color,          // 頭像邊框
    val nameText: Color,               // 姓名輸入文字
    val nameCursor: Color,             // 姓名輸入游標
    val nameResetIcon: Color,          // 取消編輯（X）icon
    val nameSaveIcon: Color,           // 儲存姓名（disk）icon
    val infoLabelText: Color,          // Tags / Xcash ID / Memo 標題文字
    val infoValueText: Color,          // 電話號碼 / Xcash ID 值文字
    val copyIconTint: Color,           // 複製 icon
    val tagSelectedFill: Brush,        // 選中 tag 底色；Black Gold = 金色橫向漸層
    val tagSelectedText: Color,        // 選中 tag 文字
    val tagUnselectedBorder: Color,    // 未選中 tag 外框
    val tagUnselectedText: Color,      // 未選中 tag 文字
    val divider: Brush,                // 分隔線
    val memoBackground: Color,         // Memo 輸入框底色
    val memoBorder: Color,             // Memo 輸入框邊框
    val memoText: Color,               // Memo 輸入文字
    val memoCursor: Color,             // Memo 輸入游標
    val saveButtonBackground: Brush,   // Save 按鈕底色；Black Gold = 金色橫向漸層
    val saveButtonText: Color,         // Save 按鈕文字；Black Gold = 黑色
)

val NeonFriendDetailColors = FriendDetailColors(
    avatarBorder = neonCyan,
    nameText = Color.White,
    nameCursor = neonCyanLight,
    nameResetIcon = silverGray,
    nameSaveIcon = neonCyanLight,
    infoLabelText = neonPurpleLight,
    infoValueText = silverGray,
    copyIconTint = neonCyanLight,
    tagSelectedFill = SolidColor(neonCyan.copy(alpha = 0.15f)),
    tagSelectedText = neonCyan,
    tagUnselectedBorder = neonCyan.copy(alpha = 0.4f),
    tagUnselectedText = silverGray,
    divider = SolidColor(neonCyan.copy(alpha = 0.08f)),
    memoBackground = twilightNavy,
    memoBorder = neonCyan.copy(alpha = 0.3f),
    memoText = Color.White,
    memoCursor = neonCyanLight,
    saveButtonBackground = SolidColor(neonCyan),
    saveButtonText = deepNavy,
)

val BlackGoldFriendDetailColors = FriendDetailColors(
    avatarBorder = antiqueGold,
    nameText = Color.White,
    nameCursor = antiqueGold,
    nameResetIcon = warmSand,
    nameSaveIcon = antiqueGold,
    infoLabelText = antiqueGold,
    infoValueText = warmSand,
    copyIconTint = antiqueGold,
    // 由左至右：oldGold → amberGold → champagneGold → amberGold → oldGold（與 gradient.goldShimmer 一致）
    tagSelectedFill = Brush.horizontalGradient(
        colors = listOf(oldGold, amberGold, champagneGold, amberGold, oldGold)
    ),
    tagSelectedText = themeBlack,
    tagUnselectedBorder = antiqueGold.copy(alpha = 0.4f),
    tagUnselectedText = warmSand,
    divider = Brush.horizontalGradient(
        colors = listOf(oldGold, amberGold, champagneGold, amberGold, oldGold)
    ),
    memoBackground = charcoalBlack,
    memoBorder = antiqueGold.copy(alpha = 0.25f),
    memoText = Color.White,
    memoCursor = antiqueGold,
    // 由左至右：oldGold → amberGold → champagneGold → amberGold → oldGold（與 gradient.goldShimmer 一致）
    saveButtonBackground = Brush.horizontalGradient(
        colors = listOf(oldGold, amberGold, champagneGold, amberGold, oldGold)
    ),
    saveButtonText = themeBlack,
)
