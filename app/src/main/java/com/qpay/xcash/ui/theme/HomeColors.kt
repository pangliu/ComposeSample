package com.qpay.xcash.ui.theme

import androidx.compose.ui.graphics.Color

// ui/home/ 資料夾底下畫面專屬顏色
// 目前僅 BalanceCard 有 Black Gold 對照色；ui/home 其餘元件
// （XEssentialsCard、EditEssentialsDialog、DeleteAccountDialog、LogoutDialog、
//   SettingScreen、NotificationCard、TransactionDetailScreen、UpdateLogScreen 的
//   private val CardBg 等）黑金設計圖尚未提供，暫維持 Neon-only hardcode，
//   待設計圖到位後再依 docs/theme_system.md 的 SOP 抽出。

data class BalanceCardColors(
    val cashInBackground: Color,        // Cash In 按鈕背景（Neon 無邊框用 Transparent）
    val cashInBorder: Color,             // Cash In 按鈕邊框
    val cashInText: Color,               // Cash In 按鈕文字 + icon
    val sendBackground: Color,          // Send 按鈕背景（Black Gold 無填色用 Transparent）
    val sendBorder: Color,              // Send 按鈕邊框
    val sendText: Color,                // Send 按鈕文字 + icon
    val switchBackground: Color,        // Balance Switch 按鈕背景
    val switchBorder: Color,            // Balance Switch 按鈕邊框
    val switchIcon: Color,              // Balance Switch 圖示顏色
    val switchText: Color,              // Balance Switch 文字顏色
)


val NeonBalanceCardColors = BalanceCardColors(
    cashInBackground = limeGreen,
    cashInBorder = Color.Transparent,
    cashInText = Color.Black,
    sendBackground = vibrantPink,
    sendBorder = Color.Transparent,
    sendText = Color.Black,
    switchBackground = slateCharcoal,
    switchBorder = duskIndigo,
    switchIcon = silverGray,
    switchText = silverGray,
)

val BlackGoldBalanceCardColors = BalanceCardColors(
    cashInBackground = Color.Transparent,
    cashInBorder = amberGold,
    cashInText = amberGold,
    sendBackground = Color.Transparent,
    sendBorder = platinumSilver,
    sendText = platinumSilver,
    switchBackground = charcoalGray,
    switchBorder = platinumSilver,
    switchIcon = platinumSilver,
    switchText = silverMist,
)
