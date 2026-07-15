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
    @DrawableRes val cardPrimaryBg: Int,
    @DrawableRes val cardSecondaryBg: Int,
    @DrawableRes val cardDetailBg: Int,
    val cardDetailAspectRatio: Float,            // cardDetailBg 圖片寬高比（width / height）
    @DrawableRes val voucherTicketBg: Int,
    @DrawableRes val subPageBackground: Int?,    // 子頁面全屏背景圖（null = 純色 bg.page）
    @DrawableRes val scanPayBackground: Int?,    // ScanPayScreen 頁面背景圖（null = 純色 bg.page）
    @DrawableRes val qrCodeBorder: Int,          // MyQrContent QR code 外框圖
    @DrawableRes val myQrPanelBg: Int?,          // MyQrContent MY_QR 模式疊在外框圖上的內層底圖（null = 不顯示）
    @DrawableRes val qrSectionLeftDecor: Int?,   // MyQrContent QR code 區塊左側裝飾圖（null = 不顯示）
    @DrawableRes val qrSectionRightDecor: Int?,  // MyQrContent QR code 區塊右側裝飾圖（null = 不顯示）
    @DrawableRes val myQrLeftDecorIcon: Int,     // MyQrContent / InputAmountScreen Balance 區塊左側裝飾圖（Neon 車子 / Black Gold 皇冠）
    @DrawableRes val myQrRightDecorIcon: Int,    // MyQrContent / InputAmountScreen Balance 區塊右側裝飾圖（Neon 猴子 / Black Gold 獅子）
    @DrawableRes val myQrActionButtonBg: Int?,   // MyQrActionButton 背景圖（null = 純色 + 邊框）
    @DrawableRes val myQrQuestCardBg: Int?,      // MyQrContent Daily Quest 卡片背景圖（null = 純色 + 邊框）
    @DrawableRes val myQrQuestCardIcon: Int,     // MyQrContent Daily Quest 卡片右側人物圖（Neon 女孩 / Black Gold 男人）
    @DrawableRes val inputAmountBackground: Int?, // InputAmountScreen 金額輸入區背景圖（null = 不顯示）
    @DrawableRes val confirmPaymentCardBg: Int?, // ConfirmPaymentScreen 付款資訊卡片背景圖（null = 純色 + 邊框）
    @DrawableRes val successPageBackground: Int?,   // TransactionSuccessfulScreen 全屏背景圖（null = 純色 bg.page）
    @DrawableRes val successHeaderBackground: Int?, // TransactionSuccessfulScreen 頁首背景圖（null = 不顯示）
    @DrawableRes val successCardBg: Int,            // TransactionSuccessfulScreen 明細卡片背景圖
    @DrawableRes val successLeftFireDecor: Int?,    // TransactionSuccessfulScreen Points balance 左側火焰裝飾（null = 不顯示）
    @DrawableRes val successRightFireDecor: Int?,   // TransactionSuccessfulScreen Points balance 右側火焰裝飾（null = 不顯示）
    @DrawableRes val contactFacebookIcon: Int,   // SelectPartnerItem contact_type = facebook 圖示
    @DrawableRes val contactPhoneIcon: Int,      // SelectPartnerItem contact_type = phone_num 圖示
    @DrawableRes val friendMaleAvatar: Int,      // SelectPartnerItem 好友頭像（男）
    @DrawableRes val friendFemaleAvatar: Int,    // ProfileScreen IdentityCard 頭像（女）
    @DrawableRes val verificationAvatar: Int,    // VerificationStatusScreen IdentityCard 頭像（女）
    @DrawableRes val profileEditBanner: Int,     // ProfileEditScreen 頂部 PromoBanner 背景圖
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
    transactionDetailCardBgSmall = null,
    cardPrimaryBg = R.drawable.bg_card_primary,
    cardSecondaryBg = R.drawable.bg_card_secondary,
    cardDetailBg = R.drawable.bg_card_primary,
    cardDetailAspectRatio = 1113f / 561f,
    voucherTicketBg = R.drawable.bg_voucher_ticket,
    subPageBackground = R.mipmap.bg_sub_page,
    scanPayBackground = null,
    qrCodeBorder = R.drawable.bg_qrcode_border,
    myQrPanelBg = null,
    qrSectionLeftDecor = R.mipmap.bg_left_qrcode,
    qrSectionRightDecor = R.mipmap.bg_right_qrcode,
    myQrLeftDecorIcon = R.mipmap.ic_car,
    myQrRightDecorIcon = R.mipmap.ic_monkey,
    myQrActionButtonBg = null,
    myQrQuestCardBg = null,
    myQrQuestCardIcon = R.drawable.ic_girl,
    inputAmountBackground = R.mipmap.bg_input_amount,
    confirmPaymentCardBg = null,
    successPageBackground = null,
    successHeaderBackground = R.mipmap.bg_success_payment,
    successCardBg = R.mipmap.bg_success_payment_border,
    successLeftFireDecor = R.mipmap.ic_left_sigal_fire,
    successRightFireDecor = R.mipmap.ic_right_sigal_fire,
    contactFacebookIcon = R.drawable.ic_facebook_friend,
    contactPhoneIcon = R.drawable.ic_phone_friend,
    friendMaleAvatar = R.drawable.ic_friend_male,
    friendFemaleAvatar = R.drawable.ic_friend_female,
    verificationAvatar = R.drawable.ic_girl,
    profileEditBanner = R.drawable.bg_profile_edit_top,
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
    transactionDetailCardBgSmall = R.drawable.bg_transaction_detail_small_black_gold,
    cardPrimaryBg = R.drawable.bg_card_primary_black_gold,
    cardSecondaryBg = R.drawable.bg_card_secondary_black_gold,
    cardDetailBg = R.drawable.bg_card_primary_black_gold,
    cardDetailAspectRatio = 954f / 561f,
    voucherTicketBg = R.drawable.bg_voucher_ticket_black_gold,
    subPageBackground = null,
    scanPayBackground = R.drawable.bg_scan_pay_black_gold,
    qrCodeBorder = R.drawable.bg_qrcode_border_black_gold,
    myQrPanelBg = R.drawable.bg_my_qrcode_black_gold,
    qrSectionLeftDecor = null,
    qrSectionRightDecor = null,
    myQrLeftDecorIcon = R.drawable.ic_crown_black_gold,
    myQrRightDecorIcon = R.drawable.ic_lion_black_gold,
    myQrActionButtonBg = R.drawable.bg_myqr_action_button_black_gold,
    myQrQuestCardBg = R.drawable.bg_detail_quest_card_black_gold,
    myQrQuestCardIcon = R.drawable.ic_man,
    inputAmountBackground = null,
    confirmPaymentCardBg = R.drawable.bg_confirm_payment_black_gold,
    successPageBackground = R.drawable.bg_payment_success_black_gold,
    successHeaderBackground = null,
    successCardBg = R.drawable.bg_confirm_payment_black_gold,
    successLeftFireDecor = null,
    successRightFireDecor = null,
    contactFacebookIcon = R.drawable.ic_facebook_friend_black_gold,
    contactPhoneIcon = R.drawable.ic_phone_friend_black_gold,
    friendMaleAvatar = R.drawable.ic_friend_male_black_gold,
    friendFemaleAvatar = R.drawable.ic_friend_female_black_gold,
    verificationAvatar = R.drawable.ic_friend_female_black_gold,    // TODO: 替換為 ic_girl_black_gold（資源尚未加入 res/drawable）
    profileEditBanner = R.drawable.bg_profile_edit_top_black_gold,    // TODO: 替換為 bg_profile_edit_top_black_gold（資源尚未加入）
)

val LocalAppAssets = staticCompositionLocalOf { NeonAssets }
