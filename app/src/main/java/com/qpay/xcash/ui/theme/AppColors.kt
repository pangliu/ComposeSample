package com.qpay.xcash.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

data class AccentColors(
    val primary: Color,         // 主要強調色：border、key text、glow
    val secondary: Color,       // 次要強調色：filled button、highlight
    val secondaryDark: Color,   // secondary 的深色版（pressed / variant）
)

data class BgColors(
    val page: Color,            // 頁面底色
    val surface: Color,         // Card / Dialog 底色
)

data class TextColors(
    val body: Color,            // 一般 body 文字
    val onPrimary: Color,       // 放在 primary 色塊上的文字
)

data class EffectColors(
    val enableGlow: Boolean,    // Neon = true，Black Gold = false
)

data class GradientColors(
    val goldShimmer: Brush?,     // 直向金色文字漸層裝飾效果；Neon = null（不套用，維持單色文字）
    val silverShimmer: Brush?,   // 直向銀色文字漸層裝飾效果；Neon = null（不套用，維持單色文字）
)

data class AppColors(
    val accent: AccentColors,
    val bg: BgColors,
    val text: TextColors,
    val loginButton: LoginButtonColors,
    val effect: EffectColors,
    val gradient: GradientColors,
    val drawer: DrawerColors,
    val selector: SelectorColors,
    val accountDialog: AccountDialogColors,
    val loginSheet: LoginSheetColors,
    val balanceCard: BalanceCardColors,
    val essentialsCard: EssentialsCardColors,
    val questCard: QuestCardColors,
    val recentActivity: RecentActivityColors,
    val bottomNav: BottomNavColors,
)

val NeonColors = AppColors(
    accent = AccentColors(
        primary = neonCyan,
        secondary = neonPurple,
        secondaryDark = neonDarkPurple,
    ),
    bg = BgColors(
        page = deepMidnight,
        surface = Color(0xFF0D1B2E),
    ),
    text = TextColors(
        body = silverGray,
        onPrimary = Color.White,
    ),
    loginButton = NeonLoginButtonColors,
    effect = EffectColors(
        enableGlow = true,
    ),
    gradient = GradientColors(
        goldShimmer = null,
        silverShimmer = null,
    ),
    drawer = NeonDrawerColors,
    selector = NeonSelectorColors,
    accountDialog = NeonAccountDialogColors,
    loginSheet = NeonLoginSheetColors,
    balanceCard = NeonBalanceCardColors,
    essentialsCard = NeonEssentialsCardColors,
    questCard = NeonQuestCardColors,
    recentActivity = NeonRecentActivityColors,
    bottomNav = NeonBottomNavColors,
)

val BlackGoldColors = AppColors(
    accent = AccentColors(
        primary = antiqueGold,
        secondary = antiqueGold,
        secondaryDark = deepBronze,
    ),
    bg = BgColors(
        page = nearBlack,
        surface = charcoalBlack,
    ),
    text = TextColors(
        body = warmSand,
        onPrimary = antiqueGold,
    ),
    loginButton = BlackGoldLoginButtonColors,
    effect = EffectColors(
        enableGlow = false,
    ),
    gradient = GradientColors(
        goldShimmer = Brush.verticalGradient(
            listOf(oldGold, amberGold, champagneGold, amberGold, oldGold)
        ),
        silverShimmer = Brush.verticalGradient(
            listOf(graphiteGray, steelGray, silverMist, steelGray, graphiteGray)
        ),
    ),
    drawer = BlackGoldDrawerColors,
    selector = BlackGoldSelectorColors,
    accountDialog = BlackGoldAccountDialogColors,
    loginSheet = BlackGoldLoginSheetColors,
    balanceCard = BlackGoldBalanceCardColors,
    essentialsCard = BlackGoldEssentialsCardColors,
    questCard = BlackGoldQuestCardColors,
    recentActivity = BlackGoldRecentActivityColors,
    bottomNav = BlackGoldBottomNavColors,
)
