package com.qpay.xcash.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor

// ui/friend/addFriend/ AddFriendScreen 專屬顏色

data class AddFriendColors(
    val sectionDot: Color,            // Phone Number 標籤前方的小圓點
    val sectionLabelText: Color,      // Phone Number 標籤文字

    val countryCodeBackground: Color, // 國碼選擇框底色
    val countryCodeBorder: Brush,     // 國碼選擇框邊框
    val countryCodeText: Color,       // 國碼文字
    val countryCodeChevron: Color,    // 國碼下拉箭頭

    val inputBackground: Color,       // 電話號碼輸入框底色
    val inputBorder: Brush,           // 電話號碼輸入框邊框
    val inputPlaceholderText: Color,  // 電話號碼 placeholder 文字
    val inputText: Color,             // 電話號碼輸入文字

    val searchIconTint: Color,        // 右側搜尋 icon 顏色
)

val NeonAddFriendColors = AddFriendColors(
    sectionDot = neonCyan,
    sectionLabelText = themeWhite,
    countryCodeBackground = twilightNavy,
    countryCodeBorder = SolidColor(neonCyan),
    countryCodeText = themeWhite,
    countryCodeChevron = neonCyan,
    inputBackground = twilightNavy,
    inputBorder = SolidColor(neonCyan),
    inputPlaceholderText = silverGray,
    inputText = themeWhite,
    searchIconTint = neonCyan,
)

val BlackGoldAddFriendColors = AddFriendColors(
    sectionDot = antiqueGold,
    sectionLabelText = themeWhite,
    countryCodeBackground = charcoalBlack,
    countryCodeBorder = Brush.horizontalGradient(
        colors = listOf(oldGold, amberGold, champagneGold, amberGold, oldGold)
    ),
    countryCodeText = themeWhite,
    countryCodeChevron = antiqueGold,
    inputBackground = charcoalBlack,
    inputBorder = Brush.horizontalGradient(
        colors = listOf(oldGold, amberGold, champagneGold, amberGold, oldGold)
    ),
    inputPlaceholderText = warmSand,
    inputText = themeWhite,
    searchIconTint = antiqueGold,
)
