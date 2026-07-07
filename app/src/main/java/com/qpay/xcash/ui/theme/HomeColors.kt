package com.qpay.xcash.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.geometry.Offset
import com.qpay.xcash.ui.home.notifications.dialog.NotificationSettingsDialog

// ui/home/ 資料夾底下畫面專屬顏色
// 目前僅 BalanceCard、XEssentialsCard、QuestCard、RecentActivity 有 Black Gold 對照色；ui/home 其餘元件
// （EditEssentialsDialog、DeleteAccountDialog、LogoutDialog、
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
    switchBorder = platinumSilver.copy(0.5f),
    switchIcon = platinumSilver,
    switchText = silverMist,
)

data class EssentialsCardColors(
    val itemBackground: Brush, // EssentialItemView 圖示方塊背景
    val itemBorder: Brush,     // EssentialItemView 邊框漸層
    val itemIcon: Color,       // EssentialItemView icon 顏色
    val itemLabel: Color,      // EssentialItemView icon 下方標籤文字顏色
    val editText: Color,       // Edit 按鈕文字顏色
    val editTextBg: Color,     // Edit 按鈕背景顏色
    val editTextBorder: Color, // Edit 按鈕邊框顏色
)

val NeonEssentialsCardColors = EssentialsCardColors(
    itemBackground = Brush.linearGradient(
        colors = listOf(navyDark, indigoDark)
    ),
    itemBorder = Brush.linearGradient(
        colors = listOf(
            neonCyan.copy(alpha = 0.4f),
            neonPurple.copy(alpha = 0.8f)
        )
    ),
    itemIcon = neonCyan,
    itemLabel = silverGray,
    editText = silverGray,
    editTextBg = slateCharcoal,
    editTextBorder = duskIndigo
)

val BlackGoldEssentialsCardColors = EssentialsCardColors(
    itemBackground = SolidColor(richBlack),
    // 左下至右上：taupeGold → paleChampagne → taupeGold
    itemBorder = Brush.linearGradient(
        colors = listOf(caramelBrown, taupeGold, paleChampagne, taupeGold, caramelBrown),
        start = Offset(0f, Float.POSITIVE_INFINITY),
        end = Offset(Float.POSITIVE_INFINITY, 0f)
    ),
    itemIcon = paleChampagne,
    itemLabel = coolGray,
    editText = coolGray,
    editTextBg = charcoalLightBlack,
    editTextBorder = platinumSilver.copy(0.5f),
)

data class QuestCardColors(
    val border: Brush,               // 卡片邊框（Neon: 純色；Black Gold: 金色漸層，同 itemBorder）
    val stackPointsText: Color,      // "Stack your points now" 文字顏色
    val breathingEffectText: Color,  // "with breathing cyan light effect" 文字顏色
    val linkText: Color,             // "Claim your loot here" / "Start your grid & earn" 文字顏色
)

val NeonQuestCardColors = QuestCardColors(
    border = SolidColor(neonPurple),
    stackPointsText = Color.White,
    breathingEffectText = Color.White,
    linkText = neonDivider,
)

val BlackGoldQuestCardColors = QuestCardColors(
    // 左下至右上：taupeGold → paleChampagne → taupeGold（同 EssentialsCard itemBorder）
    border = Brush.linearGradient(
        colors = listOf(caramelBrown, taupeGold, paleChampagne, taupeGold, caramelBrown),
        start = Offset(0f, Float.POSITIVE_INFINITY),
        end = Offset(Float.POSITIVE_INFINITY, 0f)
    ),
    stackPointsText = coolGray,
    breathingEffectText = coolGray,
    linkText = mutedGold,
)

data class RecentActivityColors(
    val border: Brush,   // 卡片邊框（Neon: 純色；Black Gold: 銀色漸層，由左至右，同 silverShimmer）
    val cashInIconTint: Color,
    val cashOutIconTint: Color,
)

val NeonRecentActivityColors = RecentActivityColors(
    border = SolidColor(neonCyanLight),
    cashInIconTint = limeGreen,
    cashOutIconTint = vibrantPink
)

val BlackGoldRecentActivityColors = RecentActivityColors(
    // 由左至右：graphiteGray → steelGray → silverMist → steelGray → graphiteGray（同 gradient.silverShimmer 色階）
    border = Brush.horizontalGradient(
        colors = listOf(graphiteGray, steelGray, silverMist, steelGray, graphiteGray)
    ),
    cashInIconTint = antiqueGold,
    cashOutIconTint = platinumSilver
)

data class NotificationTabColors(
    val activeText: Color,
    val inactiveText: Color,
)

val NeonNotificationTabColors = NotificationTabColors(
    activeText = Color.White,
    inactiveText = silverGray,
)

val BlackGoldNotificationTabColors = NotificationTabColors(
    activeText = paleGold,
    inactiveText = paleGold.copy(alpha = 0.5f),
)

data class NotificationFilterColors(
    val selectedBackground: Color,  // Neon: 填滿 accent 色；Black Gold: Transparent（不填底色，只描邊）
    val selectedBorder: Color,
    val unselectedBorder: Color,
    val selectedText: Color,
    val unselectedText: Color,
)

val NeonNotificationFilterColors = NotificationFilterColors(
    selectedBackground = neonCyan,
    selectedBorder = neonCyan.copy(alpha = 0.6f),
    unselectedBorder = neonCyan.copy(alpha = 0.6f),
    selectedText = deepMidnight,
    unselectedText = silverGray,
)

val BlackGoldNotificationFilterColors = NotificationFilterColors(
    selectedBackground = Color.Transparent,
    selectedBorder = antiqueGold,
    unselectedBorder = antiqueGold.copy(alpha = 0.5f),
    selectedText = antiqueGold,
    unselectedText = antiqueGold.copy(alpha = 0.5f),
)

data class NotificationCardColors(
    val promoBorder: Color,
    val systemBorder: Color,
    val activityBorder: Color,
    val promoBackground: Color,
    val systemBackground: Color,
    val activityBackground: Color,
    val titleText: Color,
    val contentText: Color
)

val NeonNotificationCardColors = NotificationCardColors(
    promoBorder = neonPink.copy(alpha = 0.4f),
    systemBorder = neonPurple.copy(alpha = 0.4f),
    activityBorder = neonCyan.copy(alpha = 0.4f),
    promoBackground = notifyCardBg,
    systemBackground = notifyCardBg,
    activityBackground = notifyCardBg,
    titleText = Color.White,
    contentText = silverGray
)

val BlackGoldNotificationCardColors = NotificationCardColors(
    promoBorder = terracottaGold,
    systemBorder = cornflowerBlue,
    activityBorder = paleGold.copy(alpha = 0.8f),
    promoBackground = terracottaGold.copy(alpha = 0.1f),
    systemBackground = cornflowerBlue.copy(alpha = 0.1f),
    activityBackground = paleGold.copy(alpha = 0.1f),
    titleText = Color.White,
    contentText = silverGray
)

data class NotifySettingDialogColors(
    val notifyContentText: Color,
    val notifySysIconTint: Color,
    val notifySysSwitch: Color,
    val notifyPromoIconTint: Color,
    val notifyPromoSwitch: Color,
    val notifyTransactionIconTint: Color,
    val notifyTransactionSwitch: Color,
    val contentBorder: Brush,       // 內容卡片邊框（Neon: 純色；Black Gold: 金色漸層，由左至右，同 goldShimmer 色階）
    val cancelButtonBorder: Color,  // 取消按鈕邊框
    val cancelButtonIcon: Color,    // 取消按鈕 icon 顏色
)

val NeonNotifySettingDialogColors = NotifySettingDialogColors(
    notifyContentText = Color.White,
    notifySysIconTint = neonCyan,
    notifySysSwitch = neonCyan,
    notifyPromoIconTint = neonPurpleLight,
    notifyPromoSwitch = neonDarkBlue,
    notifyTransactionIconTint = neonPurpleLight,
    notifyTransactionSwitch = neonDarkBlue,
    contentBorder = SolidColor(neonCyan.copy(alpha = 0.6f)),
    cancelButtonBorder = neonPurpleLight.copy(alpha = 0.6f),
    cancelButtonIcon = neonPurpleLight,
)

val BlackGoldNotifySettingDialogColors = NotifySettingDialogColors(
    notifyContentText = Color.White,
    notifySysIconTint = paleChampagne,
    notifySysSwitch = paleChampagne,
    notifyPromoIconTint = paleChampagne,
    notifyPromoSwitch = paleChampagne,
    notifyTransactionIconTint = paleChampagne,
    notifyTransactionSwitch = paleChampagne,
    // 由左至右：oldGold → amberGold → champagneGold → amberGold → oldGold（同 gradient.goldShimmer 色階）
    contentBorder = Brush.horizontalGradient(
        colors = listOf(oldGold, amberGold, champagneGold, amberGold, oldGold)
    ),
    cancelButtonBorder = Color.White,
    cancelButtonIcon = Color.White,
)