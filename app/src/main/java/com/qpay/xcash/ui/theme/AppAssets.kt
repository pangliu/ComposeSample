package com.qpay.xcash.ui.theme

import androidx.annotation.DrawableRes
import androidx.annotation.RawRes
import androidx.compose.runtime.staticCompositionLocalOf
import com.qpay.xcash.R

data class AppAssets(
    @RawRes val centerLogoVideo: Int,           // LoginScreen 中央 logo 影片
    @DrawableRes val centerLogoImage: Int?,     // LoginScreen 中央 logo 靜態圖（null = 播影片）
    @DrawableRes val xcashWordmark: Int,        // 頂部 "xcash" 文字 logo
    @DrawableRes val loginBackground: Int?,  // LoginScreen 頁面背景圖（null = 純色）
    @DrawableRes val balanceCardBackground: Int, // BalanceCard 背景圖
    @DrawableRes val balanceCoinIcon: Int,       // BalanceCard Token 金幣圖示
    @DrawableRes val cashInIcon: Int,            // BalanceCard Cash In 按鈕圖示
    @DrawableRes val balanceSendIcon: Int,       // BalanceCard Send 按鈕圖示
    @DrawableRes val essentialsCardBackground: Int, // XEssentialsCard 背景圖
    val essentialsCardAspectRatio: Float,        // essentialsCardBackground 圖片寬高比（width / height）
    @DrawableRes val questCardBackground: Int,   // QuestCard 背景圖
    @DrawableRes val scanButtonBackground: Int,  // ScanAndPayTab bg_scanner 背景圖
    @DrawableRes val scanIcon: Int,              // ScanAndPayTab 扫描圖示
    @DrawableRes val notifyGiftIcon: Int,        // NotificationCard PROMO 圖示
    @DrawableRes val notifySecurityIcon: Int,    // NotificationCard SYSTEM 圖示
    @DrawableRes val notifyRocketIcon: Int,      // NotificationCard ACTIVITY 圖示
    @DrawableRes val transactionDetailCardBg: Int?, // TransactionDetailScreen InfoCard 背景圖（null = 純色）
    @DrawableRes val transactionDetailCardBgSmall: Int?, // TransactionDetailScreen InfoCard 背景圖（null = 純色）
)

val NeonAssets = AppAssets(
    centerLogoVideo = R.raw.bg_type3,
    centerLogoImage = null,
    xcashWordmark = R.drawable.ic_xcash,
    loginBackground = null,
    balanceCardBackground = R.drawable.bg_balance_card,
    balanceCoinIcon = R.mipmap.ic_balance_coin,
    cashInIcon = R.drawable.ic_cash_in,
    balanceSendIcon = R.drawable.ic_balance_send,
    essentialsCardBackground = R.drawable.bg_home_essentials,
    essentialsCardAspectRatio = 1083f / 579f,
    questCardBackground = R.drawable.bg_quest_card,
    scanButtonBackground = R.drawable.bg_scanner,
    scanIcon = R.drawable.ic_scanner,
    notifyGiftIcon = R.drawable.ic_notify_gift,
    notifySecurityIcon = R.drawable.ic_notify_security,
    notifyRocketIcon = R.drawable.ic_notify_rocket,
    transactionDetailCardBg = null,
    transactionDetailCardBgSmall = null
)

val BlackGoldAssets = AppAssets(
    centerLogoVideo = R.raw.bg_type3,           // TODO: 替換為 Black Gold 影片
    centerLogoImage = R.drawable.ic_xcash_logo_black_gold,
    xcashWordmark = R.drawable.ic_xcash_black_gold,
    loginBackground = R.drawable.bg_login_black_gold,
    balanceCardBackground = R.drawable.bg_balance_card_black_gold,
    balanceCoinIcon = R.drawable.ic_balance_coin_black_gold,
    cashInIcon = R.drawable.ic_cash_in_black_gold,
    balanceSendIcon = R.drawable.ic_balance_send_black_gold,
    essentialsCardBackground = R.drawable.bg_home_essentials_black_gold,
    essentialsCardAspectRatio = 1080f / 579f,
    questCardBackground = R.drawable.bg_quest_card_black_gold,
    scanButtonBackground = R.drawable.bg_scanner_black_gold,
    scanIcon = R.drawable.ic_scanner_black_gold,
    notifyGiftIcon = R.drawable.ic_notify_gift_black_gold,
    notifySecurityIcon = R.drawable.ic_notify_security_black_gold,
    notifyRocketIcon = R.drawable.ic_notify_rocket_black_gold,
    transactionDetailCardBg = R.drawable.bg_transaction_detail_black_gold,
    transactionDetailCardBgSmall = R.drawable.bg_transaction_detail_small_black_gold
)

val LocalAppAssets = staticCompositionLocalOf { NeonAssets }
