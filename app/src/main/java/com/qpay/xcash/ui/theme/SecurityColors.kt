package com.qpay.xcash.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor

// ui/profile/security/ SecurityCenterScreen 專屬顏色

data class SecurityColors(
    val sectionHeaderText: Color,        // 區塊標題文字（Identity & Status）
    val cardBackground: Color,           // IdentityCard / ScoreCard / ChecklistCard 底色
    val identityCardBorder: Brush,       // IdentityCard 外框；Neon = cyan→purple 漸層
    val userNameText: Color,             // IdentityCard Hi, name 文字
    val xcashIdText: Color,              // IdentityCard xcash ID 文字
    val scoreCardBorder: Color,          // SecurityScoreCard 外框
    val scoreIconGradient: Brush?,       // ic_security icon 漸層；Neon = null（原樣顯示）
    val scoreLabelText: Color,           // SECURITY SCORE 標籤文字
    val scoreLabelGradient: Brush?,      // SECURITY SCORE 標籤文字漸層；Neon = null（維持單色文字）
    val scoreValueText: Color,           // 分數文字（Rating: 95/100）
    val scoreTrailingIconTint: Color,    // ic_gold_coin / ic_gift icon tint；Neon = Unspecified（原樣顯示）
    val checklistCardBorder: Color,      // ChecklistCard 外框
    val checklistTitleText: Color,       // SECURITY CHECKLIST 標題文字
    val checklistTitleGradient: Brush?,  // SECURITY CHECKLIST 標題文字漸層；Neon = null（維持單色文字）
    val checklistTitleBackground: Color, // SECURITY CHECKLIST 標題列底色
    val checklistHeaderDivider: Color,   // 標題列下方分隔線
    val checklistDivider: Color,         // Checklist 項目間分隔線
    val itemIconGradient: Brush?,        // Checklist 項目 icon 漸層；Neon = null（原樣顯示）
    val itemTitleText: Color,            // Checklist 項目標題
    val itemSubtitleText: Color,         // Checklist 項目副標題
    val statusStepText: Color,           // 狀態步驟一般文字（Set > Confirmed）
    val statusStepActiveText: Color,     // 狀態步驟最後一格（active）文字
    val chevron: Color,                  // 項目右側箭頭
    val pinDialog: SecurityPinDialogColors,  // SecurityPinDialog 專屬顏色
)

// SecurityPinDialog 專屬顏色
data class SecurityPinDialogColors(
    val background: Color,               // Dialog 底色
    val border: Color,                   // Dialog 外框邊框
    val titleText: Color,                // 標題文字
    val titleGradient: Brush?,           // 標題文字漸層；Neon = null（維持單色文字）
    val titleDivider: Color,             // 標題下方分隔線
    val bannerBackground: Color,         // Promo banner 底色
    val bannerBorder: Color,             // Promo banner 邊框
    val bannerText: Color,               // Promo banner 文字
    val bannerIconTint: Color,           // ic_trophy / ic_gift icon tint；Neon = Unspecified（原樣顯示）
    val pinLabelText: Color,             // Enter/Confirm PIN 標籤文字
    val pinBoxFilledBackground: Color,   // 已輸入 PIN 格底色
    val pinBoxFilledBorder: Color,       // 已輸入 PIN 格邊框
    val pinBoxEmptyBorder: Color,        // 未輸入 PIN 格邊框
    val pinDot: Color,                   // PIN 格內圓點
    val confirmButtonBorder: Color,      // 確認按鈕圓形邊框
    val confirmButtonIcon: Color,        // 確認按鈕 Save icon
    val closeButtonBorder: Color,        // 關閉按鈕圓形邊框
    val closeButtonIcon: Color,          // 關閉按鈕 X icon
)

val NeonSecurityColors = SecurityColors(
    sectionHeaderText = themeWhite,
    cardBackground = midnightNavy,
    identityCardBorder = Brush.linearGradient(
        colors = listOf(neonCyan.copy(alpha = 0.4f), neonPurple.copy(alpha = 0.8f))
    ),
    userNameText = themeWhite,
    xcashIdText = silverGray,
    scoreCardBorder = neonPurple.copy(alpha = 0.7f),
    scoreIconGradient = null,
    scoreLabelText = neonPurple,
    scoreLabelGradient = null,
    scoreValueText = themeWhite,
    scoreTrailingIconTint = Color.Unspecified,
    checklistCardBorder = neonBlueLight.copy(alpha = 0.6f),
    checklistTitleText = themeWhite,
    checklistTitleGradient = null,
    checklistTitleBackground = neonCyan.copy(alpha = 0.08f),
    checklistHeaderDivider = neonCyan.copy(alpha = 0.2f),
    checklistDivider = neonCyan.copy(alpha = 0.1f),
    itemIconGradient = null,
    itemTitleText = themeWhite,
    itemSubtitleText = silverGray,
    statusStepText = silverGray,
    statusStepActiveText = neonBlueLight,
    chevron = themeWhite.copy(alpha = 0.4f),
    pinDialog = SecurityPinDialogColors(
        background = twilightNavy,
        border = neonCyan.copy(alpha = 0.7f),
        titleText = neonCyan,
        titleGradient = null,
        titleDivider = neonCyan,
        bannerBackground = neonPurple.copy(alpha = 0.08f),
        bannerBorder = neonPurple.copy(alpha = 0.7f),
        bannerText = neonPurple,
        bannerIconTint = Color.Unspecified,
        pinLabelText = themeWhite,
        pinBoxFilledBackground = neonCyan.copy(alpha = 0.15f),
        pinBoxFilledBorder = neonCyan,
        pinBoxEmptyBorder = neonCyan.copy(alpha = 0.4f),
        pinDot = neonCyan,
        confirmButtonBorder = neonCyan,
        confirmButtonIcon = neonCyan,
        closeButtonBorder = neonPurpleLight,
        closeButtonIcon = neonPurpleLight,
    ),
)

val BlackGoldSecurityColors = SecurityColors(
    sectionHeaderText = antiqueGold,
    cardBackground = darkObsidian,
    identityCardBorder = SolidColor(paleGold),
    userNameText = coolGray,
    xcashIdText = coolGray,
    scoreCardBorder = paleGold,
    // 由左至右：bronzeGold → paleChampagne → bronzeGold
    scoreIconGradient = Brush.horizontalGradient(
        colors = listOf(bronzeGold, paleChampagne, bronzeGold)
    ),
    scoreLabelText = antiqueGold,
    // 由上至下：bronzeGold → paleChampagne → bronzeGold
    scoreLabelGradient = Brush.verticalGradient(
        colors = listOf(bronzeGold, paleChampagne, bronzeGold)
    ),
    scoreValueText = coolGray,
    scoreTrailingIconTint = paleChampagne,
    checklistCardBorder = paleGold,
    checklistTitleText = antiqueGold,
    // 由上至下：bronzeGold → paleChampagne → bronzeGold
    checklistTitleGradient = Brush.verticalGradient(
        colors = listOf(bronzeGold, paleChampagne, bronzeGold)
    ),
    checklistTitleBackground = antiqueGold.copy(alpha = 0.08f),
    checklistHeaderDivider = antiqueGold.copy(alpha = 0.2f),
    checklistDivider = antiqueGold.copy(alpha = 0.2f),
    // 由上至下：bronzeGold → paleChampagne → bronzeGold
    itemIconGradient = Brush.horizontalGradient(
        colors = listOf(bronzeGold, paleChampagne, bronzeGold)
    ),
    itemTitleText = coolGray,
    itemSubtitleText = themeWhite,
    statusStepText = warmSand,
    statusStepActiveText = antiqueGold,
    chevron = warmSand.copy(alpha = 0.6f),
    pinDialog = SecurityPinDialogColors(
        background = charcoalBlack,
        border = paleGold,
        titleText = antiqueGold,
        // 由上至下：bronzeGold → paleChampagne → bronzeGold
        titleGradient = Brush.verticalGradient(
            colors = listOf(bronzeGold, paleChampagne, bronzeGold)
        ),
        titleDivider = paleGold,
        bannerBackground = antiqueGold.copy(alpha = 0.08f),
        bannerBorder = paleGold,
        bannerText = antiqueGold,
        bannerIconTint = paleChampagne,
        pinLabelText = coolGray,
        pinBoxFilledBackground = antiqueGold.copy(alpha = 0.15f),
        pinBoxFilledBorder = antiqueGold,
        pinBoxEmptyBorder = antiqueGold.copy(alpha = 0.4f),
        pinDot = antiqueGold,
        confirmButtonBorder = antiqueGold,
        confirmButtonIcon = antiqueGold,
        closeButtonBorder = coolGray,
        closeButtonIcon = coolGray,
    ),
)
