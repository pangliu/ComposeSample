package com.qpay.xcash.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor

// ui/friend/detail/ FriendDetailScreen 專屬顏色

data class FriendDetailColors(
    val avatarBorder: Color,          // 頭像邊框
    val nameText: Color,               // 姓名輸入文字
    val nameCursor: Color,             // 姓名輸入游標
    val nameEditIcon: Color,           // 編輯姓名（pencil）icon
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
    // FriendRemoveConfirmDialog（ui/friend/components/）
    val removeDialogBackground: Color,     // Dialog 底色
    val removeDialogBorder: Color,         // Dialog 邊框
    val removeDialogTitleText: Color,      // 標題文字
    val removeDialogMessageText: Color,    // 說明文字
    val removeDialogCancelBorder: Color,   // Cancel 按鈕邊框
    val removeDialogCancelText: Color,     // Cancel 按鈕文字
    val removeDialogConfirmFill: Brush,    // Confirm 按鈕底色；Black Gold = 金色橫向漸層
    val removeDialogConfirmText: Color,    // Confirm 按鈕文字；Black Gold = 黑色
)

val NeonFriendDetailColors = FriendDetailColors(
    avatarBorder = neonCyan,
    nameText = Color.White,
    nameCursor = neonCyanLight,
    nameEditIcon = neonCyanLight,
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
    // Neon 版設計稿尚未提供，暫沿用既有 Neon 色票，維持與 remove 按鈕一致的警示色（neonRed）
    removeDialogBackground = twilightNavy,
    removeDialogBorder = neonCyan.copy(alpha = 0.4f),
    removeDialogTitleText = Color.White,
    removeDialogMessageText = silverGray.copy(alpha = 0.85f),
    removeDialogCancelBorder = neonCyan,
    removeDialogCancelText = neonCyan,
    removeDialogConfirmFill = Brush.horizontalGradient(
        colors = listOf(neonPurple, cyberPurple)
    ),
    removeDialogConfirmText = Color.White,
)

val BlackGoldFriendDetailColors = FriendDetailColors(
    avatarBorder = antiqueGold,
    nameText = Color.White,
    nameCursor = antiqueGold,
    nameEditIcon = antiqueGold,
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
    removeDialogBackground = charcoalBlack,
    removeDialogBorder = antiqueGold.copy(alpha = 0.4f),
    removeDialogTitleText = Color.White,
    removeDialogMessageText = silverGray.copy(alpha = 0.8f),
    removeDialogCancelBorder = antiqueGold,
    removeDialogCancelText = antiqueGold,
    // 由左至右：oldGold → amberGold → champagneGold → amberGold → oldGold（與 gradient.goldShimmer 一致）
    removeDialogConfirmFill = Brush.horizontalGradient(
        colors = listOf(oldGold, amberGold, champagneGold, amberGold, oldGold)
    ),
    removeDialogConfirmText = themeBlack,
)
