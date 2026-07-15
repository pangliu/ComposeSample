package com.qpay.xcash.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor

// ui/profile/ ProfileScreen 專屬顏色

data class ProfileColors(
    val sectionHeaderText: Color,     // 各區塊標題文字（Identity / Social & Rewards...）
    val sectionHeaderGradient: Brush, // 各區塊標題文字漸層
    val identityCardBorder: Brush,    // IdentityCard 外框；Neon = cyan→purple 漸層
    val userNameText: Color,          // IdentityCard Hi, name 文字
    val xcashIdText: Color,           // IdentityCard xcash ID 文字
    val socialCardBorder: Color,      // SocialRewardsCard 外框
    val socialTitleText: Color,       // Invite & Earn / My Badges 標題文字
    val socialCodeText: Color,        // G-code 文字
    val socialEarnedText: Color,      // Earned x badges 文字
    val socialDivider: Color,         // SocialRewardsCard 中間直向分隔線
    val inviteButtonFill: Brush,      // Invite Friends 按鈕填滿；Black Gold = 金色橫向漸層
    val inviteButtonBorder: Color,    // Invite Friends 按鈕邊框
    val inviteButtonText: Color,      // Invite Friends 按鈕文字
    val menuAccountBorder: Color,     // Account 選單卡片外框
    val menuSecurityBorder: Color,    // Security 選單卡片外框
    val menuSupportBorder: Color,     // Support 選單卡片外框
    val menuAccountIconTint: Color,   // Account 選單項目 icon tint
    val menuSecurityIconTint: Color,  // Security 選單項目 icon tint
    val menuSupportIconTint: Color,   // Support 選單項目 icon tint
    val menuItemText: Color,          // 選單項目文字
    val menuChevron: Color,           // 選單項目右側箭頭
    val logoutButtonFill: Brush,      // LOG OUT 按鈕填滿；Black Gold = 金色橫向漸層
    val logoutButtonBorder: Color?,   // LOG OUT 按鈕邊框；Neon = null（無邊框）
    val logoutButtonText: Color,      // LOG OUT 按鈕文字
    val badge: ProfileBadgeColors,           // FullyVerifiedBadge 專屬顏色
    val logoutDialog: LogoutDialogColors,    // LogoutConfirmDialog 專屬顏色
    val inviteDialog: InviteDialogColors,    // InviteFriendsDialog 專屬顏色
)

// FullyVerifiedBadge 專屬顏色
data class ProfileBadgeColors(
    val border: Brush,          // 膠囊外框；Black Gold = 金色橫向漸層
    val text: Color,            // FULLY VERIFIED 文字
    val iconBackground: Color,  // 盾牌 icon 圓形底色
)

// LogoutConfirmDialog 專屬顏色
data class LogoutDialogColors(
    val background: Color,             // Dialog 底色
    val border: Color,                 // Dialog 外框邊框
    val titleText: Color,              // 標題文字；Black Gold = coralRed
    val confirmButtonFill: Brush,      // Log Out 按鈕填滿；Black Gold = 紅色橫向漸層
    val confirmButtonBorder: Color?,   // Log Out 按鈕邊框；Neon = null（無邊框）
    val confirmButtonText: Color,      // Log Out 按鈕文字；Black Gold = 黑色
    val cancelButtonFill: Brush,       // Cancel 按鈕填滿；Neon = 透明
    val cancelButtonBorder: Color,     // Cancel 按鈕邊框
    val cancelButtonText: Color,       // Cancel 按鈕文字；Black Gold = 黑色
)

// InviteFriendsDialog 專屬顏色
data class InviteDialogColors(
    val background: Color,          // Dialog 底色
    val border: Color,              // Dialog 外框邊框
    val titleText: Color,           // 標題文字
    val accentGradient: Brush?,     // 標題 / 分享 icon / System share 漸層（由上至下）；Neon = null（維持單色）
    val qrCodeColor: Color,         // QR code 本體顏色
    val qrFrameBorder: Color,       // QR code 外框邊框
    val divider: Color,             // 區塊分隔線
    val shareLabelText: Color,      // Share invite link 標籤文字
    val taglineText: Color,         // 底部 tagline 文字
    val closeButtonBorder: Color,   // 關閉按鈕圓形邊框
    val closeButtonIcon: Color,     // 關閉按鈕 X icon
)

val NeonProfileColors = ProfileColors(
    sectionHeaderText = themeWhite,
    sectionHeaderGradient = SolidColor(themeWhite),
    identityCardBorder = Brush.linearGradient(
        colors = listOf(neonCyan.copy(alpha = 0.4f), neonPurple.copy(alpha = 0.8f))
    ),
    userNameText = themeWhite,
    xcashIdText = Color.LightGray,
    socialCardBorder = neonCyan.copy(alpha = 0.5f),
    socialTitleText = themeWhite,
    socialCodeText = themeWhite,
    socialEarnedText = silverGray,
    socialDivider = neonCyan.copy(alpha = 0.25f),
    inviteButtonFill = SolidColor(neonCyan.copy(alpha = 0.1f)),
    inviteButtonBorder = neonCyan.copy(alpha = 0.5f),
    inviteButtonText = neonCyanLight,
    menuAccountBorder = neonCyan,
    menuSecurityBorder = neonPurple,
    menuSupportBorder = neonDarkPurple,
    menuAccountIconTint = themeWhite,
    menuSecurityIconTint = neonPurple,
    menuSupportIconTint = neonDarkPurple,
    menuItemText = themeWhite,
    menuChevron = themeWhite.copy(alpha = 0.4f),
    logoutButtonFill = SolidColor(neonPurple),
    logoutButtonBorder = null,
    logoutButtonText = themeWhite,
    badge = ProfileBadgeColors(
        border = Brush.horizontalGradient(
            colors = listOf(neonPurple.copy(alpha = 0.7f), neonPink.copy(alpha = 0.9f))
        ),
        text = neonPurple,
        iconBackground = midnightNavy,
    ),
    logoutDialog = LogoutDialogColors(
        background = twilightNavy,
        border = neonCyan.copy(alpha = 0.6f),
        titleText = neonCyan,
        confirmButtonFill = SolidColor(crimsonDark),
        confirmButtonBorder = null,
        confirmButtonText = themeWhite,
        cancelButtonFill = SolidColor(Color.Transparent),
        cancelButtonBorder = neonPurple.copy(alpha = 0.8f),
        cancelButtonText = themeWhite,
    ),
    inviteDialog = InviteDialogColors(
        background = twilightNavy,
        border = neonCyan.copy(alpha = 0.6f),
        titleText = neonCyan,
        accentGradient = null,
        qrCodeColor = neonCyan,
        qrFrameBorder = neonPurple.copy(alpha = 0.8f),
        divider = neonCyan.copy(alpha = 0.2f),
        shareLabelText = themeWhite,
        taglineText = neonPurple,
        closeButtonBorder = neonPurpleLight,
        closeButtonIcon = neonPurpleLight,
    ),
)

val BlackGoldProfileColors = ProfileColors(
    sectionHeaderText = antiqueGold,
    sectionHeaderGradient = Brush.verticalGradient(
        listOf(oldGold, amberGold, champagneGold, amberGold, oldGold)
    ),
    identityCardBorder = SolidColor(paleGold),
    userNameText = coolGray,
    xcashIdText = coolGray,
    socialCardBorder = paleGold,
    socialTitleText = apricotGold,
    socialCodeText = mistGray,
    socialEarnedText = mistGray,
    socialDivider = antiqueGold.copy(alpha = 0.25f),
    // 由左至右：bronzeGold → paleChampagne → bronzeGold
    inviteButtonFill = Brush.horizontalGradient(
        colors = listOf(bronzeGold, paleChampagne, bronzeGold)
    ),
    inviteButtonBorder = paleGold,
    inviteButtonText = themeBlack,
    menuAccountBorder = paleGold,
    menuSecurityBorder = paleGold,
    menuSupportBorder = paleGold,
    menuAccountIconTint = antiqueGold,
    menuSecurityIconTint = antiqueGold,
    menuSupportIconTint = antiqueGold,
    menuItemText = themeWhite,
    menuChevron = warmSand.copy(alpha = 0.6f),
    // 由左至右：walnutGold → apricotGold → walnutGold（與 Confirm & Pay 按鈕一致）
    logoutButtonFill = Brush.horizontalGradient(
        colors = listOf(walnutGold, apricotGold, walnutGold)
    ),
    logoutButtonBorder = apricotGold,
    logoutButtonText = themeBlack,
    badge = ProfileBadgeColors(
        // 由左至右：caramelGold → apricotGold → caramelGold（與 QrModeTabSelector 一致）
        border = Brush.horizontalGradient(
            colors = listOf(caramelGold, apricotGold, caramelGold)
        ),
        text = antiqueGold,
        iconBackground = charcoalBlack,
    ),
    logoutDialog = LogoutDialogColors(
        background = charcoalBlack,
        border = paleGold,
        titleText = coralRed,
        // 由左至右：deepMaroon → dustyRose → blushPink → dustyRose → deepMaroon（與 UnlinkCardDialog Unlink 按鈕一致）
        confirmButtonFill = Brush.horizontalGradient(
            colors = listOf(deepMaroon, dustyRose, blushPink, dustyRose, deepMaroon)
        ),
        confirmButtonBorder = blushPink,
        confirmButtonText = themeBlack,
        // 由左至右：graphiteGray → steelGray → silverMist → steelGray → graphiteGray（同 gradient.silverShimmer 色階）
        cancelButtonFill = Brush.horizontalGradient(
            colors = listOf(graphiteGray, steelGray, silverMist, steelGray, graphiteGray)
        ),
        cancelButtonBorder = silverMist,
        cancelButtonText = themeBlack,
    ),
    inviteDialog = InviteDialogColors(
        background = charcoalBlack,
        border = paleGold,
        titleText = paleChampagne,
        // 由上至下：tawnyGold → paleChampagne → tawnyGold
        accentGradient = Brush.verticalGradient(
            colors = listOf(tawnyGold, paleChampagne, tawnyGold)
        ),
        qrCodeColor = antiqueGold,
        qrFrameBorder = apricotGold,
        divider = antiqueGold.copy(alpha = 0.2f),
        shareLabelText = themeWhite,
        taglineText = antiqueGold,
        closeButtonBorder = coolGray,
        closeButtonIcon = coolGray,
    ),
)
