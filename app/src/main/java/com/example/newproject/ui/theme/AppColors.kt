package com.example.newproject.ui.theme

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

data class ButtonColors(
    val loginBackground: Color,     // Login 按鈕填色
    val loginBorder: Color,         // Login 按鈕邊框（Neon 無邊框用 Transparent）
    val loginText: Color,           // Login 按鈕文字
    val telegramBackground: Color,  // Telegram 按鈕填色
    val telegramBorder: Color,      // Telegram 按鈕邊框
    val telegramText: Color,        // Telegram 按鈕文字
)

data class EffectColors(
    val enableGlow: Boolean,    // Neon = true，Black Gold = false
)

data class DrawerCardColors(
    val title: Color,           // 卡片標題文字色
    val border: Color,          // 卡片邊框色
)

data class DrawerColors(
    val accountCard: DrawerCardColors,   // Account Status 卡片
    val productCard: DrawerCardColors,   // Product Features 卡片
    val helpCard: DrawerCardColors,      // Help & Policies 卡片
)

data class SelectorColors(
    val border: Color,                  // 下拉框邊框色
    val selectedBackground: Color,      // 選中項目背景色
)

data class AppColors(
    val accent: AccentColors,
    val bg: BgColors,
    val text: TextColors,
    val button: ButtonColors,
    val effect: EffectColors,
    val drawer: DrawerColors,
    val selector: SelectorColors,
)

val NeonColors = AppColors(
    accent = AccentColors(
        primary = neonCyan,
        secondary = neonPurple,
        secondaryDark = neonDarkPurple,
    ),
    bg = BgColors(
        page = loginBackground,
        surface = Color(0xFF0D1B2E),
    ),
    text = TextColors(
        body = normalText,
        onPrimary = Color.White,
    ),
    button = ButtonColors(
        loginBackground = neonPurple,
        loginBorder = Color.Transparent,
        loginText = Color.White,
        telegramBackground = neonGreen,
        telegramBorder = neonGreenLight,
        telegramText = Color.White,
    ),
    effect = EffectColors(
        enableGlow = true,
    ),
    drawer = DrawerColors(
        accountCard = DrawerCardColors(title = tealMedium, border = aquaLight),
        productCard = DrawerCardColors(title = orchidDark, border = orchidLight),
        helpCard = DrawerCardColors(title = royalBlue, border = cornflowerBlue),
    ),
    selector = SelectorColors(
        border = neonCyanLight,
        selectedBackground = Color(0xFF1E3A58),
    ),
)

val BlackGoldColors = AppColors(
    accent = AccentColors(
        primary = goldPrimary,
        secondary = goldPrimary,
        secondaryDark = goldDark,
    ),
    bg = BgColors(
        page = goldBackground,
        surface = goldSurface,
    ),
    text = TextColors(
        body = goldText,
        onPrimary = goldPrimary,
    ),
    button = ButtonColors(
        loginBackground = goldLoginButtonBackground,
        loginBorder = goldPrimary,
        loginText = goldPrimary,
        telegramBackground = goldLoginButtonBackground,
        telegramBorder = goldTelegramButtonColor,
        telegramText = goldTelegramButtonColor,
    ),
    effect = EffectColors(
        enableGlow = false,
    ),
    drawer = DrawerColors(
        accountCard = DrawerCardColors(title = goldDrawerAccount, border = goldDrawerAccount),
        productCard = DrawerCardColors(title = goldDrawerProduct, border = goldDrawerProduct),
        helpCard = DrawerCardColors(title = goldDrawerHelp, border = goldDrawerHelp),
    ),
    selector = SelectorColors(
        border = goldSelectorBorder,
        selectedBackground = goldSelectorSelectedBg,
    ),
)
