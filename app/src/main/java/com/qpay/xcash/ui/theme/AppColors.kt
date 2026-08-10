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
    val effect: EffectColors,
    val gradient: GradientColors,
    val login: LoginColors,
    val home: HomeColors,
    val bottomNav: BottomNavColors,
    val notification: NotificationColors,
    val setting: SettingColors,
    val transactionDetail: TransactionDetailColors,
    val transactionHistory: TransactionHistoryColors,
    val updateLog: UpdateLogColors,
    val cards: CardsColors,
    val addNewCard: AddNewCardColors,
    val linkedSuccess: LinkedSuccessColors,
    val cardDetail: CardDetailColors,
    val scanPay: ScanPayColors,
    val profile: ProfileColors,
    val profileEdit: ProfileEditColors,
    val verification: VerificationColors,
    val security: SecurityColors,
    val editAvatar: EditAvatarColors,
    val camera: CameraColors,
    val avatarDialog: AvatarDialogColors,
    val friend: FriendColors,
    val friendList: FriendListColors,
)

val NeonColors = AppColors(
    accent = AccentColors(
        primary = neonCyan,
        secondary = neonPurple,
        secondaryDark = neonDarkPurple,
    ),
    bg = BgColors(
        page = deepMidnight,
        surface = twilightNavy,
    ),
    text = TextColors(
        body = silverGray,
        onPrimary = Color.White,
    ),
    effect = EffectColors(
        enableGlow = true,
    ),
    gradient = GradientColors(
        goldShimmer = null,
        silverShimmer = null,
    ),
    login = NeonLoginColors,
    home = NeonHomeColors,
    bottomNav = NeonBottomNavColors,
    notification = NeonNotificationColors,
    setting = NeonSettingColors,
    transactionDetail = NeonTransactionDetailColors,
    transactionHistory = NeonTransactionHistoryColors,
    updateLog = NeonUpdateLogColors,
    cards = NeonCardsColors,
    addNewCard = NeonAddNewCardColors,
    linkedSuccess = NeonLinkedSuccessColors,
    cardDetail = NeonCardDetailColors,
    scanPay = NeonScanPayColors,
    profile = NeonProfileColors,
    profileEdit = NeonProfileEditColors,
    verification = NeonVerificationColors,
    security = NeonSecurityColors,
    editAvatar = NeonEditAvatarColors,
    camera = NeonCameraColors,
    avatarDialog = NeonAvatarDialogColors,
    friend = NeonFriendColors,
    friendList = NeonFriendListColors,
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
    login = BlackGoldLoginColors,
    home = BlackGoldHomeColors,
    bottomNav = BlackGoldBottomNavColors,
    notification = BlackGoldNotificationColors,
    setting = BlackGoldSettingColors,
    transactionDetail = BlackGoldTransactionDetailColors,
    transactionHistory = BlackGoldTransactionHistoryColors,
    updateLog = BlackGoldUpdateLogColors,
    cards = BlackGoldCardsColors,
    addNewCard = BlackGoldAddNewCardColors,
    linkedSuccess = BlackGoldLinkedSuccessColors,
    cardDetail = BlackGoldCardDetailColors,
    scanPay = BlackGoldScanPayColors,
    profile = BlackGoldProfileColors,
    profileEdit = BlackGoldProfileEditColors,
    verification = BlackGoldVerificationColors,
    security = BlackGoldSecurityColors,
    editAvatar = BlackGoldEditAvatarColors,
    camera = BlackGoldCameraColors,
    avatarDialog = BlackGoldAvatarDialogColors,
    friend = BlackGoldFriendColors,
    friendList = BlackGoldFriendListColors,
)
