package com.qpay.xcash.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.geometry.Offset

// ui/home/ 資料夾底下畫面專屬顏色（NotificationsScreen 相關顏色獨立在 NotificationColors.kt）
// 目前 BalanceCard、XEssentialsCard、QuestCard、RecentActivity、DeleteAccountDialog、LogoutDialog、
//   EditEssentialsDialog 有 Black Gold 對照色；
// ui/home 其餘元件（SettingScreen、TransactionDetailScreen、UpdateLogScreen 的
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

data class DeleteDialogColors(
    val warningTint: Color,        // 警示 icon / 標題
    val deleteButtonBg: Brush,     // Delete 按鈕底色（Brush 支援漸層）
    val deleteButtonBorder: Color, // Delete 按鈕邊框（enabled 時）
    val deleteButtonText: Color,   // Delete 按鈕文字
    val cancelBg: Brush?,          // Cancel 按鈕底色（null = 無填色）
    val cancelBorder: Color,       // Cancel 按鈕邊框
    val cancelText: Color,         // Cancel 按鈕文字
)

val NeonDeleteDialogColors = DeleteDialogColors(
    warningTint = salmonRed,
    deleteButtonBg = SolidColor(darkGarnet),
    deleteButtonBorder = salmonRed,
    deleteButtonText = salmonRed,
    cancelBg = null,
    cancelBorder = pinkOrchid,
    cancelText = pinkOrchid,
)

val BlackGoldDeleteDialogColors = DeleteDialogColors(
    warningTint = coralRed,
    deleteButtonBg = Brush.horizontalGradient(
        colors = listOf(deepMaroon, dustyRose, blushPink, dustyRose, deepMaroon)
    ),
    deleteButtonBorder = wineRed,
    deleteButtonText = Color.Black,
    cancelBg = Brush.horizontalGradient(
        colors = listOf(graphiteGray, steelGray, silverMist, steelGray, graphiteGray)
    ),
    cancelBorder = pearlGray,
    cancelText = Color.Black,
)

data class HomeLogoutDialogColors(
    val background: Color,          // 對話框底色
    val border: Color,              // 對話框外框（含 alpha）
    val glowColor: Color,           // neonGlow 顏色
    val titleColor: Color,          // 標題文字（titleBrush == null 時生效）
    val titleBrush: Brush?,         // 標題文字漸層（null = 使用 titleColor）
    val dividerColor: Color,        // 分隔線顏色（含 alpha）
    val bodyTextColor: Color,       // 內文文字顏色
    val cancelButtonColor: Color,   // X 按鈕邊框 + icon
    val confirmButtonColor: Color,  // → 按鈕邊框 + icon
    val buttonBackground: Color,    // 按鈕圓形底色
)

val NeonHomeLogoutDialogColors = HomeLogoutDialogColors(
    background = twilightNavy,
    border = neonCyan.copy(alpha = 0.5f),
    glowColor = neonCyan,
    titleColor = Color.White,
    titleBrush = null,
    dividerColor = neonCyan.copy(alpha = 0.3f),
    bodyTextColor = Color.White.copy(alpha = 0.85f),
    cancelButtonColor = neonPink,
    confirmButtonColor = neonCyan,
    buttonBackground = twilightNavy,
)

val BlackGoldHomeLogoutDialogColors = HomeLogoutDialogColors(
    background = charcoalBlack,
    border = antiqueGold.copy(alpha = 0.5f),
    glowColor = antiqueGold,
    titleColor = Color.Unspecified,
    titleBrush = Brush.verticalGradient(
        colors = listOf(oldGold, amberGold, champagneGold, amberGold, oldGold)
    ),
    dividerColor = antiqueGold.copy(alpha = 0.3f),
    bodyTextColor = Color.White.copy(alpha = 0.85f),
    cancelButtonColor = platinumSilver,
    confirmButtonColor = antiqueGold,
    buttonBackground = charcoalBlack,
)

// EditEssentialsDialog 專屬顏色
data class EditEssentialsDialogColors(
    val titleText: Color,        // 標題文字（Edit Essentials）
    val hintText: Color,         // 標題下方提示文字 + items count 文字
    val sectionTitleText: Color, // My Menu / Other 區塊標題
    val itemBackground: Brush,   // 項目圖示方塊背景
    val itemBorder: Brush,       // 項目圖示方塊邊框漸層
    val itemIcon: Color,         // 項目 icon 顏色
    val itemLabel: Color,        // 項目 icon 下方標籤文字
    val removeBadge: Color,      // 移除 badge 圓形底色
    val addBadge: Color,         // 新增 badge 圓形底色
    val badgeIcon: Color,        // badge 內 icon 顏色
    val saveButtonBg: Brush,     // Save 按鈕底色
    val saveButtonGlow: Color,   // Save 按鈕 glow（Black Gold enableGlow=false 時不套用）
    val saveButtonText: Color,   // Save 按鈕文字
)

val NeonEditEssentialsDialogColors = EditEssentialsDialogColors(
    titleText = themeWhite,
    hintText = Color.Gray,
    sectionTitleText = neonCyan,
    itemBackground = Brush.linearGradient(
        colors = listOf(indigoDark, navyDark)
    ),
    itemBorder = Brush.linearGradient(
        colors = listOf(
            neonCyan.copy(alpha = 0.4f),
            neonPurple.copy(alpha = 0.8f)
        )
    ),
    itemIcon = neonCyan,
    itemLabel = themeWhite,
    removeBadge = vibrantPink,
    addBadge = limeGreen,
    badgeIcon = themeWhite,
    saveButtonBg = SolidColor(limeGreen),
    saveButtonGlow = limeGreen,
    saveButtonText = themeWhite,
)

val BlackGoldEditEssentialsDialogColors = EditEssentialsDialogColors(
    titleText = paleChampagne,
    hintText = coolGray,
    sectionTitleText = antiqueGold,
    itemBackground = SolidColor(richBlack),
    // 左下至右上：caramelBrown → taupeGold → paleChampagne → taupeGold → caramelBrown（同 EssentialsCard itemBorder）
    itemBorder = Brush.linearGradient(
        colors = listOf(caramelBrown, taupeGold, paleChampagne, taupeGold, caramelBrown),
        start = Offset(0f, Float.POSITIVE_INFINITY),
        end = Offset(Float.POSITIVE_INFINITY, 0f)
    ),
    itemIcon = paleChampagne,
    itemLabel = coolGray,
    removeBadge = blushPink,
    addBadge = antiqueGold,
    badgeIcon = themeBlack,
    // 由左至右：walnutGold → apricotGold → walnutGold（同 ProfileScreen LOG OUT 按鈕）
    saveButtonBg = Brush.horizontalGradient(
        colors = listOf(walnutGold, apricotGold, walnutGold)
    ),
    saveButtonGlow = antiqueGold,
    saveButtonText = themeBlack,
)

data class HomeColors(
    val balanceCard: BalanceCardColors,
    val essentialsCard: EssentialsCardColors,
    val questCard: QuestCardColors,
    val recentActivity: RecentActivityColors,
    val deleteDialog: DeleteDialogColors,
    val logoutDialog: HomeLogoutDialogColors,
    val editEssentials: EditEssentialsDialogColors,
)

val NeonHomeColors = HomeColors(
    balanceCard = NeonBalanceCardColors,
    essentialsCard = NeonEssentialsCardColors,
    questCard = NeonQuestCardColors,
    recentActivity = NeonRecentActivityColors,
    deleteDialog = NeonDeleteDialogColors,
    logoutDialog = NeonHomeLogoutDialogColors,
    editEssentials = NeonEditEssentialsDialogColors,
)

val BlackGoldHomeColors = HomeColors(
    balanceCard = BlackGoldBalanceCardColors,
    essentialsCard = BlackGoldEssentialsCardColors,
    questCard = BlackGoldQuestCardColors,
    recentActivity = BlackGoldRecentActivityColors,
    deleteDialog = BlackGoldDeleteDialogColors,
    logoutDialog = BlackGoldHomeLogoutDialogColors,
    editEssentials = BlackGoldEditEssentialsDialogColors,
)
