package com.qpay.xcash.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor

// ui/cards/detail/ CardDetailScreen 專屬顏色

data class CardDetailColors(
    // NicknameField
    val nicknameBorder: Color,   // 暱稱輸入框邊框
    val nicknameText: Color,     // 暱稱輸入文字
    val nicknameCursor: Color,   // 暱稱輸入游標
    val nicknameEditIcon: Color, // 暱稱編輯圖示
    // Unlink Card 按鈕（頁面）
    val unlinkBackground: Brush, // 按鈕背景；Black Gold = silverShimmer 橫向漸層
    val unlinkBorder: Color,     // 按鈕邊框；Black Gold = silverMist
    val unlinkText: Color,       // 按鈕文字；Black Gold = 黑色
    // UnlinkCardDialog
    val dialogBackground: Color, // Dialog 底色
    val dialogBorder: Color,     // Dialog 邊框
)

val NeonCardDetailColors = CardDetailColors(
    nicknameBorder = neonCyanLight,
    nicknameText = Color.White,
    nicknameCursor = neonCyanLight,
    nicknameEditIcon = neonCyanLight,
    unlinkBackground = SolidColor(Color.Transparent),
    unlinkBorder = neonPurpleLight,
    unlinkText = neonPurpleLight,
    dialogBackground = duskNavy,
    dialogBorder = indigoLine,
)

val BlackGoldCardDetailColors = CardDetailColors(
    nicknameBorder = antiqueGold,
    nicknameText = Color.White,
    nicknameCursor = antiqueGold,
    nicknameEditIcon = antiqueGold,
    // 由左至右：graphiteGray → steelGray → silverMist → steelGray → graphiteGray（同 gradient.silverShimmer 色階）
    unlinkBackground = Brush.horizontalGradient(
        colors = listOf(graphiteGray, steelGray, silverMist, steelGray, graphiteGray)
    ),
    unlinkBorder = silverMist,
    unlinkText = Color.Black,
    dialogBackground = charcoalBlack,
    dialogBorder = paleGold,
)
