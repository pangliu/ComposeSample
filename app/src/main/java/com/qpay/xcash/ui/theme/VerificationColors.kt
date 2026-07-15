package com.qpay.xcash.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor

// ui/profile/verification/ VerificationStatusScreen 專屬顏色

data class VerificationColors(
    val sectionHeaderText: Color,         // 區塊標題文字（Identity / Verification Checklist）
    val cardBackground: Color,            // IdentityCard / ChecklistCard 底色
    val identityCardBorder: Brush,        // IdentityCard 外框；Neon = cyan→purple 漸層
    val userNameText: Color,              // IdentityCard Hi, name 文字
    val xcashIdText: Color,               // IdentityCard xcash ID 文字
    val checklistCardBorder: Brush,       // ChecklistCard 外框；Neon = cyan→purple 漸層
    val checklistDivider: Color,          // Checklist 項目間分隔線
    val verifiedItem: Color,              // 已驗證項目（icon / 進度條填色 / 狀態文字）
    val pendingItem: Color,               // 審核中項目（icon / 進度條填色 / 狀態文字）
    val verifiedBarBorder: Brush,         // 已驗證項目進度條外框；Black Gold = 金色橫向漸層
    val pendingBarBorder: Brush,          // 審核中項目進度條外框；Black Gold = 粉色橫向漸層
    val verifiedBarFirstFill: Brush,      // 已驗證項目進度條第一格填色；Black Gold = 金色橫向漸層
    val pendingBarFirstFill: Brush,       // 審核中項目進度條第一格填色；Black Gold = 粉色橫向漸層
    val actionButtonFill: Brush,          // ACTION REQUIRED 按鈕填滿；Neon = 透明
    val actionButtonBorder: Color,        // ACTION REQUIRED 按鈕邊框
    val actionButtonText: Color,          // ACTION REQUIRED 按鈕文字；Black Gold = 黑色
)

val NeonVerificationColors = VerificationColors(
    sectionHeaderText = themeWhite,
    cardBackground = midnightNavy,
    identityCardBorder = Brush.linearGradient(
        colors = listOf(neonCyan.copy(alpha = 0.4f), neonPurple.copy(alpha = 0.8f))
    ),
    userNameText = themeWhite,
    xcashIdText = silverGray,
    checklistCardBorder = Brush.linearGradient(
        colors = listOf(neonCyan.copy(alpha = 0.3f), neonPurple.copy(alpha = 0.4f))
    ),
    checklistDivider = neonCyan.copy(alpha = 0.08f),
    verifiedItem = neonCyan,
    pendingItem = neonMellowPeach,
    verifiedBarBorder = SolidColor(neonCyan.copy(alpha = 0.8f)),
    pendingBarBorder = SolidColor(neonMellowPeach.copy(alpha = 0.8f)),
    verifiedBarFirstFill = SolidColor(neonCyan),
    pendingBarFirstFill = SolidColor(neonMellowPeach),
    actionButtonFill = SolidColor(Color.Transparent),
    actionButtonBorder = neonMellowPeach,
    actionButtonText = neonMellowPeach,
)

val BlackGoldVerificationColors = VerificationColors(
    sectionHeaderText = antiqueGold,
    cardBackground = darkObsidian,
    identityCardBorder = SolidColor(paleGold),
    userNameText = coolGray,
    xcashIdText = coolGray,
    checklistCardBorder = SolidColor(paleGold),
    checklistDivider = antiqueGold.copy(alpha = 0.2f),
    verifiedItem = antiqueGold,
    pendingItem = neonMellowPeach,
    // 由左至右：bronzeGold → paleChampagne
    verifiedBarBorder = Brush.horizontalGradient(
        colors = listOf(bronzeGold, paleChampagne)
    ),
    // 由左至右：rosewoodPink → neonMellowPeach
    pendingBarBorder = Brush.horizontalGradient(
        colors = listOf(rosewoodPink, neonMellowPeach)
    ),
    // 由左至右：bronzeGold → paleChampagne（與外框同漸層）
    verifiedBarFirstFill = Brush.horizontalGradient(
        colors = listOf(bronzeGold, paleChampagne)
    ),
    // 由左至右：rosewoodPink → neonMellowPeach（與外框同漸層）
    pendingBarFirstFill = Brush.horizontalGradient(
        colors = listOf(rosewoodPink, neonMellowPeach)
    ),
    // 由左至右：deepMaroon → dustyRose → blushPink → dustyRose → deepMaroon（與 UnlinkCardDialog Unlink 按鈕一致）
    actionButtonFill = Brush.horizontalGradient(
        colors = listOf(deepMaroon, dustyRose, blushPink, dustyRose, deepMaroon)
    ),
    actionButtonBorder = wineRed,
    actionButtonText = neonMellowPeach,
)
