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
    val dialogBackground: Color,       // Dialog 底色
    val dialogBorder: Brush,           // Dialog 邊框；Black Gold = silverShimmer 橫向漸層
    val dialogWarningTint: Color,      // 警示 icon tint；Neon = 不套色（維持原圖），Black Gold = coralRed
    val dialogTitleText: Color,        // 標題文字；Black Gold = coralRed
    val dialogCardNumberText: Color,   // 卡號後四碼文字；Black Gold = coralRed
    val dialogDescText: Color,         // 描述文字；Black Gold = coralRed
    val dialogUnlinkBackground: Brush, // Unlink 按鈕背景；Black Gold = 紅色橫向漸層
    val dialogUnlinkBorder: Color,     // Unlink 按鈕邊框；Black Gold = blushPink
    val dialogUnlinkText: Color,       // Unlink 按鈕文字；Black Gold = 黑色
    val dialogCancelBackground: Brush, // Cancel 按鈕背景；Black Gold = silverShimmer 橫向漸層
    val dialogCancelBorder: Color,     // Cancel 按鈕邊框；Black Gold = silverMist
    val dialogCancelText: Color,       // Cancel 按鈕文字；Black Gold = 黑色
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
    dialogBorder = SolidColor(indigoLine),
    dialogWarningTint = Color.Unspecified,
    dialogTitleText = Color.White,
    dialogCardNumberText = Color.White,
    dialogDescText = Color.White.copy(alpha = 0.6f),
    dialogUnlinkBackground = SolidColor(bloodRed.copy(alpha = 0.5f)),
    dialogUnlinkBorder = dustyCrimson.copy(alpha = 0.6f),
    dialogUnlinkText = dustyCrimson,
    dialogCancelBackground = SolidColor(duskNavy),
    dialogCancelBorder = vibrantPink,
    dialogCancelText = vibrantPink,
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
    // 由左至右：graphiteGray → steelGray → silverMist → steelGray → graphiteGray（同 gradient.silverShimmer 色階）
    dialogBorder = Brush.horizontalGradient(
        colors = listOf(graphiteGray, steelGray, silverMist, steelGray, graphiteGray)
    ),
    dialogWarningTint = coralRed,
    dialogTitleText = coralRed,
    dialogCardNumberText = coralRed,
    dialogDescText = coralRed,
    // 由左至右：deepMaroon → dustyRose → blushPink → dustyRose → deepMaroon
    dialogUnlinkBackground = Brush.horizontalGradient(
        colors = listOf(deepMaroon, dustyRose, blushPink, dustyRose, deepMaroon)
    ),
    dialogUnlinkBorder = blushPink,
    dialogUnlinkText = Color.Black,
    // 由左至右：graphiteGray → steelGray → silverMist → steelGray → graphiteGray（同 gradient.silverShimmer 色階）
    dialogCancelBackground = Brush.horizontalGradient(
        colors = listOf(graphiteGray, steelGray, silverMist, steelGray, graphiteGray)
    ),
    dialogCancelBorder = silverMist,
    dialogCancelText = Color.Black,
)
