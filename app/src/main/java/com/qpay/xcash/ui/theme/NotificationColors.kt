package com.qpay.xcash.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor

// NotificationsScreen 專屬顏色
// （NotificationsScreen、NotificationCard、NotificationSettingsDialog）

data class NotificationColors(
    val pageBackground: Color,                   // 頁面底色
    val titleBrush: Brush?,                      // 標題文字漸層；Black Gold = null（沿用 gradient.goldShimmer）
    val tab: NotificationTabColors,
    val categoryCard: NotificationCategoryCardColors,
    val banner: NotificationBannerColors,
    val card: NotificationCardColors,
    val settingDialog: NotifySettingDialogColors,
)

data class NotificationTabColors(
    val activeText: Color,
    val inactiveText: Color,
    val indicator: Brush,       // 選中分頁底線；Black Gold = 金色橫向漸層
)

val NeonNotificationTabColors = NotificationTabColors(
    activeText = neonSkyCyan,
    inactiveText = silverGray.copy(alpha = 0.7f),
    indicator = SolidColor(neonSkyCyan),
)

val BlackGoldNotificationTabColors = NotificationTabColors(
    activeText = paleChampagne,
    inactiveText = silverGray.copy(alpha = 0.7f),
    // 由左至右：bronzeGold → paleChampagne → bronzeGold
    indicator = Brush.horizontalGradient(colors = listOf(bronzeGold, paleChampagne, bronzeGold)),
)

// 四個類別卡片（System Notices / Transaction Alerts / Promotions / To-Do）
data class NotificationCategoryCardColors(
    val background: Brush,      // 卡片底色（直向漸層）
    val border: Color?,         // 卡片邊框；Black Gold = null（不描邊）
    val iconTint: Color,        // 類別 icon
    val labelText: Color,       // 類別名稱
    val badgeBackground: Color, // 右上角數量 badge 底色
    val badgeText: Color,       // 右上角數量 badge 文字
)

val NeonNotificationCategoryCardColors = NotificationCategoryCardColors(
    background = Brush.verticalGradient(colors = listOf(neonCardIndigoTop, neonCardIndigoBottom)),
    border = neonSkyCyan.copy(alpha = 0.35f),
    iconTint = neonSkyCyan,
    labelText = neonSkyCyan,
    badgeBackground = neonMagenta,
    badgeText = Color.White,
)

val BlackGoldNotificationCategoryCardColors = NotificationCategoryCardColors(
    background = Brush.verticalGradient(colors = listOf(goldCardGraphiteTop, goldCardGraphiteBottom)),
    border = null,
    iconTint = apricotGold,
    labelText = apricotGold,
    badgeBackground = goldBadgeRed,
    badgeText = Color.White,
)

// 底部廣告 Banner（Limited Offer / Ad Banner / Claim Free）
data class NotificationBannerColors(
    val background: Brush,          // Banner 底色；Neon = 紫→深藍橫向漸層
    val illustrationTint: Color,    // 左側卡片插圖（暫用 icon，待設計提供圖檔）
    val subText: Color,             // 「Limited Offer」小標
    val titleText: Color,           // 「Ad Banner」主標
    val buttonBackground: Color,    // Claim Free 按鈕底色
    val buttonBorder: Color,        // Claim Free 按鈕邊框
    val buttonText: Color,          // Claim Free 按鈕文字
)

val NeonNotificationBannerColors = NotificationBannerColors(
    background = Brush.horizontalGradient(colors = listOf(neonBannerPurple, neonBannerNavy)),
    illustrationTint = neonSkyCyan,
    subText = Color.White,
    titleText = Color.White,
    buttonBackground = neonOceanBlue,
    buttonBorder = neonSkyCyan,
    buttonText = Color.White,
)

val BlackGoldNotificationBannerColors = NotificationBannerColors(
    background = SolidColor(goldBannerBrown),
    illustrationTint = antiqueGold,
    subText = goldBannerSubText,
    titleText = goldBannerTitle,
    buttonBackground = Color.Transparent,
    buttonBorder = antiqueGold.copy(alpha = 0.75f),
    buttonText = antiqueGold,
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
    pageBackground = neonPageNavy,
    // 由左至右：neonOrchid → neonSkyCyan
    titleBrush = Brush.horizontalGradient(colors = listOf(neonOrchid, neonSkyCyan)),
    tab = NeonNotificationTabColors,
    categoryCard = NeonNotificationCategoryCardColors,
    banner = NeonNotificationBannerColors,
    card = NeonNotificationCardColors,
    settingDialog = NeonNotifySettingDialogColors,
)

val BlackGoldNotificationColors = NotificationColors(
    pageBackground = goldEspressoBlack,
    titleBrush = null,
    tab = BlackGoldNotificationTabColors,
    categoryCard = BlackGoldNotificationCategoryCardColors,
    banner = BlackGoldNotificationBannerColors,
    card = BlackGoldNotificationCardColors,
    settingDialog = BlackGoldNotifySettingDialogColors,
)
