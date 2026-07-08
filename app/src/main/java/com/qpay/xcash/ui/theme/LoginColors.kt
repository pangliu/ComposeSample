package com.qpay.xcash.ui.theme

import androidx.compose.ui.graphics.Color

// ui/login/ 資料夾底下所有畫面專屬顏色
// （LoginScreen、DrawerMenuContent、LanguageSelector、AccountStatusDialog、
//   FindAppDialog、LoginBottomSheet、VerifyMobileDialog）

data class LoginButtonColors(
    val loginBackground: Color,      // Login 按鈕填色
    val loginBorder: Color,          // Login 按鈕邊框（Neon 無邊框用 Transparent）
    val loginText: Color,            // Login 按鈕文字
    val telegramBackground: Color,   // Telegram 按鈕填色
    val telegramBorder: Color,       // Telegram 按鈕邊框
    val telegramText: Color,         // Telegram 按鈕文字
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

data class AccountDialogColors(
    val button1: Color,                 // Check Application Progress 按鈕邊框
    val button2: Color,                 // Verify My Identity 按鈕邊框
)

data class LoginSheetColors(
    val outerBorder: Color,             // Card 外框邊框
    val inputAccent: Color,             // 輸入框邊框 + eye icon tint（非錯誤狀態）
    val hint: Color,                    // 密碼提示文字（Uppercase、Number）
    val submitButton: Color,            // 送出按鈕邊框 + icon
)

data class LoginColors(
    val button: LoginButtonColors,
    val drawer: DrawerColors,
    val selector: SelectorColors,
    val accountDialog: AccountDialogColors,
    val loginSheet: LoginSheetColors,
)

val NeonLoginButtonColors = LoginButtonColors(
    loginBackground = neonPurple,
    loginBorder = Color.Transparent,
    loginText = Color.White,
    telegramBackground = neonGreen,
    telegramBorder = neonGreenLight,
    telegramText = Color.White,
)

val BlackGoldLoginButtonColors = LoginButtonColors(
    loginBackground = Color.Transparent,
    loginBorder = antiqueGold,
    loginText = antiqueGold,
    telegramBackground = Color.Transparent,
    telegramBorder = powderBlue,
    telegramText = powderBlue,
)

val NeonDrawerColors = DrawerColors(
    accountCard = DrawerCardColors(title = tealMedium, border = aquaLight),
    productCard = DrawerCardColors(title = orchidDark, border = orchidLight),
    helpCard = DrawerCardColors(title = royalBlue, border = cornflowerBlue),
)

val BlackGoldDrawerColors = DrawerColors(
    accountCard = DrawerCardColors(title = paleGold, border = paleGold),
    productCard = DrawerCardColors(title = terracottaGold, border = terracottaGold),
    helpCard = DrawerCardColors(title = cornflowerBlue, border = cornflowerBlue),
)

val NeonSelectorColors = SelectorColors(
    border = neonCyanLight,
    selectedBackground = Color(0xFF1E3A58),
)

val BlackGoldSelectorColors = SelectorColors(
    border = paleGold,
    selectedBackground = caramelBrown,
)

val NeonAccountDialogColors = AccountDialogColors(
    button1 = neonPurpleLight,
    button2 = neonBlue,
)

val BlackGoldAccountDialogColors = AccountDialogColors(
    button1 = terracottaGold,
    button2 = cornflowerBlue,
)

val NeonLoginSheetColors = LoginSheetColors(
    outerBorder = neonPurpleLight,
    inputAccent = neonCyanLight,
    hint = neonMint,
    submitButton = neonMint,
)

val BlackGoldLoginSheetColors = LoginSheetColors(
    outerBorder = antiqueGold,
    inputAccent = antiqueGold,
    hint = antiqueGold,
    submitButton = sunGold,
)

val NeonLoginColors = LoginColors(
    button = NeonLoginButtonColors,
    drawer = NeonDrawerColors,
    selector = NeonSelectorColors,
    accountDialog = NeonAccountDialogColors,
    loginSheet = NeonLoginSheetColors,
)

val BlackGoldLoginColors = LoginColors(
    button = BlackGoldLoginButtonColors,
    drawer = BlackGoldDrawerColors,
    selector = BlackGoldSelectorColors,
    accountDialog = BlackGoldAccountDialogColors,
    loginSheet = BlackGoldLoginSheetColors,
)
