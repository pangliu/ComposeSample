package com.qpay.xcash.ui.theme

import androidx.compose.ui.graphics.Color

// SettingScreen 專屬顏色

data class SettingSectionColors(
    val title: Color,   // SectionCard 標題文字顏色
    val border: Color,  // SectionCard 邊框顏色
)

data class SettingToggleColors(
    val icon: Color,    // ToggleRow 左側 icon 顏色
    val switch: Color,  // ToggleRow 右側 NeonSwitch 顏色
)

data class SettingDropdownColors(
    val icon: Color,       // DropdownRow 左側 icon 顏色
    val menuButton: Color, // DropdownRow 右側下拉選單按鈕（底色 / 邊框 / 文字 / 箭頭）顏色
)

data class SettingNavColors(
    val icon: Color,  // NavRow 左側 icon 顏色
)

data class SettingColors(
    // ── SectionCard 標題 + 邊框 ──────────────────────────────
    val appearanceSection: SettingSectionColors,
    val notificationsSection: SettingSectionColors,
    val appInfoSection: SettingSectionColors,
    val accountSection: SettingSectionColors,

    // ── ToggleRow icon + switch ──────────────────────────────
    val hideBalanceToggle: SettingToggleColors,
    val systemAlertsToggle: SettingToggleColors,
    val promoNotificationsToggle: SettingToggleColors,
    val transactionAlertsToggle: SettingToggleColors,

    // ── DropdownRow icon + 下拉選單按鈕 ───────────────────────
    val themeDropdown: SettingDropdownColors,
    val languageDropdown: SettingDropdownColors,

    // ── NavRow icon ──────────────────────────────────────────
    val updateLogNav: SettingNavColors,
    val clearCacheNav: SettingNavColors,
    val deleteAccountNav: SettingNavColors,
    val logoutNav: SettingNavColors,
)

val NeonSettingColors = SettingColors(
    appearanceSection = SettingSectionColors(title = neonCyan, border = neonCyan),
    notificationsSection = SettingSectionColors(title = neonDarkBlue, border = neonDarkBlue),
    appInfoSection = SettingSectionColors(title = neonCyan, border = neonCyan),
    accountSection = SettingSectionColors(title = neonRed, border = neonRed),

    hideBalanceToggle = SettingToggleColors(icon = neonPurpleLight, switch = neonPurpleLight),
    systemAlertsToggle = SettingToggleColors(icon = neonCyan, switch = neonCyan),
    promoNotificationsToggle = SettingToggleColors(icon = neonDarkBlue, switch = neonDarkBlue),
    transactionAlertsToggle = SettingToggleColors(icon = neonDarkBlue, switch = neonDarkBlue),

    themeDropdown = SettingDropdownColors(icon = neonCyan, menuButton = neonCyan),
    languageDropdown = SettingDropdownColors(icon = neonPurpleLight, menuButton = neonPurpleLight),

    updateLogNav = SettingNavColors(icon = neonCyan),
    clearCacheNav = SettingNavColors(icon = neonCyan),
    deleteAccountNav = SettingNavColors(icon = neonRed),
    logoutNav = SettingNavColors(icon = neonRed),
)

val BlackGoldSettingColors = SettingColors(
    appearanceSection = SettingSectionColors(title = antiqueGold, border = Color.Transparent),
    notificationsSection = SettingSectionColors(title = champagneGold, border = Color.Transparent),
    appInfoSection = SettingSectionColors(title = antiqueGold, border = Color.Transparent),
    accountSection = SettingSectionColors(title = neonRed, border = Color.Transparent),

    hideBalanceToggle = SettingToggleColors(icon = antiqueGold, switch = paleChampagne),
    systemAlertsToggle = SettingToggleColors(icon = antiqueGold, switch = paleChampagne),
    promoNotificationsToggle = SettingToggleColors(icon = antiqueGold, switch = paleChampagne),
    transactionAlertsToggle = SettingToggleColors(icon = antiqueGold, switch = paleChampagne),

    themeDropdown = SettingDropdownColors(icon = antiqueGold, menuButton = Color.White),
    languageDropdown = SettingDropdownColors(icon = antiqueGold, menuButton = Color.White),

    updateLogNav = SettingNavColors(icon = antiqueGold),
    clearCacheNav = SettingNavColors(icon = antiqueGold),
    deleteAccountNav = SettingNavColors(icon = antiqueGold),
    logoutNav = SettingNavColors(icon = antiqueGold),
)
