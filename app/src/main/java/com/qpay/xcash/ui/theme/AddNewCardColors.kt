package com.qpay.xcash.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor

// ui/cards/add/ AddNewCardScreen 專屬顏色

data class AddNewCardColors(
    // CardFormField
    val fieldLabel: Color,             // 欄位標題文字
    val fieldText: Color,              // 輸入中文字
    val fieldPlaceholder: Color,       // placeholder 文字
    val fieldBackground: Color,        // 輸入框底色
    val fieldBorderFocused: Color,     // 輸入框邊框（focus）
    val fieldBorderUnfocused: Color,   // 輸入框邊框（未 focus）
    val fieldCursor: Color,            // 游標顏色
    // InputMethodButton（OCR / Gallery）
    val methodBackgroundSelected: Color,   // 選中底色
    val methodBackgroundUnselected: Color, // 未選中底色
    val methodBorderSelected: Color,       // 選中邊框
    val methodBorderUnselected: Color,     // 未選中邊框
    val methodContentSelected: Color,      // 選中 icon / 文字
    val methodContentUnselected: Color,    // 未選中 icon / 文字
    // Confirm button
    val confirmBackground: Brush,      // 按鈕背景；Black Gold = 金色橫向漸層
    val confirmContent: Color,         // 按鈕文字
)

val NeonAddNewCardColors = AddNewCardColors(
    fieldLabel = Color.White,
    fieldText = Color.White,
    fieldPlaceholder = silverGray,
    fieldBackground = inkNavy,
    fieldBorderFocused = neonCyan,
    fieldBorderUnfocused = neonCyan.copy(alpha = 0.4f),
    fieldCursor = neonCyan,
    methodBackgroundSelected = steelNavy,
    methodBackgroundUnselected = inkNavy,
    methodBorderSelected = neonCyan,
    methodBorderUnselected = neonCyan,
    methodContentSelected = neonCyan,
    methodContentUnselected = neonCyan.copy(alpha = 0.5f),
    confirmBackground = SolidColor(neonCyan),
    confirmContent = darkSlate,
)

val BlackGoldAddNewCardColors = AddNewCardColors(
    fieldLabel = Color.White,
    fieldText = Color.White,
    fieldPlaceholder = warmSand,
    fieldBackground = charcoalBlack,
    fieldBorderFocused = antiqueGold,
    fieldBorderUnfocused = antiqueGold.copy(alpha = 0.4f),
    fieldCursor = antiqueGold,
    methodBackgroundSelected = deepBronze,
    methodBackgroundUnselected = charcoalBlack,
    methodBorderSelected = antiqueGold,
    methodBorderUnselected = antiqueGold,
    methodContentSelected = antiqueGold,
    methodContentUnselected = antiqueGold.copy(alpha = 0.5f),
    // 由左至右：oldGold → amberGold → champagneGold → amberGold → oldGold（同 gradient.goldShimmer 色階）
    confirmBackground = Brush.horizontalGradient(
        colors = listOf(oldGold, amberGold, champagneGold, amberGold, oldGold)
    ),
    confirmContent = Color.Black,
)
