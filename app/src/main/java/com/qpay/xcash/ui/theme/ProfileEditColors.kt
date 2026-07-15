package com.qpay.xcash.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor

// ui/profile/edit/ ProfileEditScreen 專屬顏色

data class ProfileEditColors(
    val bannerTitleText: Color,     // PromoBanner 第一行標題文字
    val bannerSubtitleText: Color,  // PromoBanner 第二行副標題文字
    val cardBackground: Color,      // SectionCard 卡片底色
    val cardBorder: Brush,          // SectionCard 卡片外框
    val cardGlow: Color,            // SectionCard 卡片外框 glow（僅 Neon 顯示）
    val chipBorder: Color,          // 區塊標題 chip 外框
    val chipGlow: Color,            // 區塊標題 chip 外框 glow（僅 Neon 顯示）
    val chipTitleText: Color,       // 區塊標題 chip 文字
    val accentGradient: Brush?,     // 區塊標題 chip 文字 / Row icon 漸層（由上至下）；Neon = null（維持單色）
    val fieldLabelText: Color,      // 欄位標籤文字
    val fieldValueText: Color,      // 欄位值文字（右側資料 / Dropdown 標籤）
)

val NeonProfileEditColors = ProfileEditColors(
    bannerTitleText = lemonYellow,
    bannerSubtitleText = themeWhite,
    cardBackground = midnightNavy,
    cardBorder = SolidColor(neonBlueLight.copy(alpha = 0.5f)),
    cardGlow = neonBlueLight,
    chipBorder = neonDarkPurple,
    chipGlow = neonDarkPurple,
    chipTitleText = themeWhite,
    accentGradient = null,
    fieldLabelText = silverGray,
    fieldValueText = themeWhite,
)

val BlackGoldProfileEditColors = ProfileEditColors(
    bannerTitleText = paleChampagne,
    bannerSubtitleText = coolGray,
    cardBackground = darkObsidian,
    cardBorder = SolidColor(paleGold),
    cardGlow = antiqueGold,
    chipBorder = paleGold,
    chipGlow = antiqueGold,
    chipTitleText = antiqueGold,
    // 由上至下：tawnyGold → paleChampagne → tawnyGold（與 InviteFriendsDialog accentGradient 一致）
    accentGradient = Brush.verticalGradient(
        colors = listOf(tawnyGold, paleChampagne, tawnyGold)
    ),
    fieldLabelText = themeWhite,
    fieldValueText = themeWhite,
)
