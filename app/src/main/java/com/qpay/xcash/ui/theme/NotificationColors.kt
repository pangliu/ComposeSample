package com.qpay.xcash.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor

// NotificationsScreen 專屬顏色
// （NotificationsScreen、NotificationCard、NotificationSettingsDialog）

data class NotificationColors(
    val tab: NotificationTabColors,
    val filter: NotificationFilterColors,
    val card: NotificationCardColors,
    val settingDialog: NotifySettingDialogColors,
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

val NeonNotificationColors = NotificationColors(
    tab = NeonNotificationTabColors,
    filter = NeonNotificationFilterColors,
    card = NeonNotificationCardColors,
    settingDialog = NeonNotifySettingDialogColors,
)

val BlackGoldNotificationColors = NotificationColors(
    tab = BlackGoldNotificationTabColors,
    filter = BlackGoldNotificationFilterColors,
    card = BlackGoldNotificationCardColors,
    settingDialog = BlackGoldNotifySettingDialogColors,
)
