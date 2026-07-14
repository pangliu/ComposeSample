package com.qpay.xcash.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor

// ui/scanpay/ 資料夾底下畫面專屬顏色

data class ScanPayColors(
    val qrFrameBackground: Color,    // MyQrContent QR code 區塊底色
    val qrCodeColor: Color,          // MyQrContent QR code 本體顏色
    val qrNickNameText: Color,       // MyQrContent MY_QR 上方 @nickName 文字
    val qrUserNameText: Color,       // MyQrContent MY_QR 下方 userName 文字
    val balanceAmountText: Color,    // MyQrContent Balance 金額文字
    val balanceToggleIcon: Color,    // MyQrContent Balance 顯示/隱藏眼睛圖示
    val questCardBackground: Color,  // MyQrContent Daily Quest 卡片底色
    val questTitleText: Color,       // MyQrContent Daily Quest 標題文字
    val questProgressText: Color,    // MyQrContent Daily Quest 進度文字
    val questProgressIndicator: Color, // MyQrContent Daily Quest 進度條顏色
    val questProgressTrack: Color,   // MyQrContent Daily Quest 進度條 track
    val qrTabBorder: Brush,          // QrModeTabSelector 外框邊框；Black Gold = 金色橫向漸層
    val qrTabSelectedFill: Brush,    // QrModeTabSelector 選中 Tab 填滿；Black Gold = 金色橫向漸層
    val actionButtonIconTint: Color, // MyQrActionButton icon tint；Neon = Unspecified（保留 icon 原色）
    val actionButtonIconBorder: Color, // MyQrActionButton icon 圓形邊框
    val actionButtonText: Color,     // MyQrActionButton 文字
    val input: InputAmountColors,    // InputAmountScreen 專屬顏色
    val confirm: ConfirmPaymentColors, // ConfirmPaymentScreen 專屬顏色
    val success: TransactionSuccessColors, // TransactionSuccessfulScreen 專屬顏色
    val selectPartner: SelectPartnerDialogColors, // SelectSplitPartnerDialog 專屬顏色
    val splitBill: SplitBillDialogColors,   // SplitBillDialog 專屬顏色
    val splitPartners: SplitPartnersRowColors, // SplitPartnersRow 專屬顏色
)

// InputAmountScreen 專屬顏色
data class InputAmountColors(
    val recipientText: Color,       // 收款人 @nickName / 姓名文字
    val cardOuterBorder: Color,     // 收款人卡片外層邊框
    val cardInnerBorder: Color?,    // 收款人卡片內層邊框；Black Gold = null（不顯示）
    val balanceLabelText: Color,    // Balance 標籤文字
    val balanceAmountText: Color,   // Balance 金額文字
    val reviewButtonFill: Brush,    // Review Details 按鈕填滿；Black Gold = 金色橫向漸層
    val reviewButtonBorder: Color?, // Review Details 按鈕邊框；Neon = null（無邊框）
)

val NeonInputAmountColors = InputAmountColors(
    recipientText = neonPurpleLight,
    cardOuterBorder = neonCyan.copy(alpha = 0.8f),
    cardInnerBorder = neonPurple.copy(alpha = 0.6f),
    balanceLabelText = silverGray,
    balanceAmountText = themeWhite,
    reviewButtonFill = SolidColor(neonPurple),
    reviewButtonBorder = null,
)

val BlackGoldInputAmountColors = InputAmountColors(
    recipientText = camelGold,
    cardOuterBorder = apricotGold,
    cardInnerBorder = null,
    balanceLabelText = camelGold,
    balanceAmountText = silverGray,
    // 由左至右：camelGold → apricotGold → camelGold
    reviewButtonFill = Brush.horizontalGradient(
        colors = listOf(camelGold, apricotGold, camelGold)
    ),
    reviewButtonBorder = apricotGold,
)

// ConfirmPaymentScreen 專屬顏色
data class ConfirmPaymentColors(
    val cardBackground: Color,      // 付款資訊卡片底色
    val avatarBorder: Color?,       // Pay To 頭像框邊框；Neon = null（不顯示）
    val nickNameText: Color,        // Pay To @nickName 文字
    val nameText: Color,            // Pay To 姓名文字
    val hintText: Color,            // X points / 確認提示文字
    val balanceLabelText: Color,    // PAYING FROM / Available balance 標籤文字
    val balanceValueText: Color,    // Available balance 金額文字
    val cancelButtonFill: Brush,    // Cancel 按鈕填滿；Neon = 透明
    val cancelButtonBorder: Color,  // Cancel 按鈕邊框
    val cancelIcon: Color,          // Cancel 按鈕 X icon
    val cancelText: Color,          // Cancel 按鈕文字
    val payButtonFill: Brush,       // Confirm & Pay 按鈕填滿；Black Gold = 金色橫向漸層
)

val NeonConfirmPaymentColors = ConfirmPaymentColors(
    cardBackground = slateGray,
    avatarBorder = null,
    nickNameText = neonCyanLight,
    nameText = neonPurpleLight,
    hintText = silverGray,
    balanceLabelText = silverGray,
    balanceValueText = silverGray,
    cancelButtonFill = SolidColor(Color.Transparent),
    cancelButtonBorder = neonPurple,
    cancelIcon = neonPurple,
    cancelText = neonPurpleLight,
    payButtonFill = SolidColor(neonCyan),
)

val BlackGoldConfirmPaymentColors = ConfirmPaymentColors(
    cardBackground = charcoalBlack,
    avatarBorder = apricotGold,
    nickNameText = antiqueGold,
    nameText = camelGold,
    hintText = themeWhite,
    balanceLabelText = camelGold,
    balanceValueText = themeWhite,
    // 由左至右：walnutGold → apricotGold → walnutGold（與 Confirm & Pay 按鈕一致）
    cancelButtonFill = Brush.horizontalGradient(
        colors = listOf(walnutGold, apricotGold, walnutGold)
    ),
    cancelButtonBorder = apricotGold,
    cancelIcon = themeBlack,
    cancelText = themeBlack,
    // 由左至右：walnutGold → apricotGold → walnutGold（與 Cancel 按鈕一致）
    payButtonFill = Brush.horizontalGradient(
        colors = listOf(walnutGold, apricotGold, walnutGold)
    ),
)

// TransactionSuccessfulScreen 專屬顏色
data class TransactionSuccessColors(
    val avatarBorder: Color?,       // Paid to 頭像框邊框；Neon = null（不顯示）
    val nickNameText: Color,        // Paid to @nickName 文字
    val nameText: Color,            // Paid to 姓名文字
    val divider: Color,             // 明細卡片內金額分隔線
    val detailAmountText: Color,    // Original Total / Points Applied / Final Amount 金額文字
    val earnedPointsText: Color,    // +X-Points earned 數字文字
    val balanceDivider: Color,      // Balance 區塊上下分隔線
    val balanceLabelText: Color,    // PAYING FROM / Available Balance 標籤文字
    val pointsBalanceText: Color,   // Updated Points balance 金額文字
    val doneButtonFill: Brush,      // Done 按鈕填滿；Black Gold = 深藍紫橫向漸層
    val doneButtonBorder: Color,    // Done 按鈕邊框
    val doneButtonText: Color,      // Done 按鈕文字
    val shareButtonFill: Brush,     // Share My Experience 按鈕填滿；Neon = 透明
    val shareButtonBorder: Color,   // Share My Experience 按鈕邊框
    val shareIconTint: Color,       // Share My Experience 按鈕 icon
    val shareText: Color,           // Share My Experience 按鈕文字
)

val NeonTransactionSuccessColors = TransactionSuccessColors(
    avatarBorder = null,
    nickNameText = neonCyanLight,
    nameText = neonPurpleLight,
    divider = neonCyan,
    detailAmountText = neonMint,
    earnedPointsText = neonCyan,
    balanceDivider = neonCyan.copy(alpha = 0.6f),
    balanceLabelText = silverGray,
    pointsBalanceText = neonCyan,
    doneButtonFill = SolidColor(neonCyan),
    doneButtonBorder = neonCyan,
    doneButtonText = themeBlack,
    shareButtonFill = SolidColor(Color.Transparent),
    shareButtonBorder = neonPurple,
    shareIconTint = neonPurple,
    shareText = neonPurpleLight,
)

val BlackGoldTransactionSuccessColors = TransactionSuccessColors(
    avatarBorder = apricotGold,
    nickNameText = antiqueGold,
    nameText = camelGold,
    divider = paleChampagne,
    detailAmountText = antiqueGold,
    earnedPointsText = antiqueGold,
    balanceDivider = antiqueGold.copy(alpha = 0.6f),
    balanceLabelText = camelGold,
    pointsBalanceText = antiqueGold,
    // 由左至右：midnightIndigo → slateViolet → midnightIndigo
    doneButtonFill = Brush.horizontalGradient(
        colors = listOf(midnightIndigo, slateViolet, midnightIndigo)
    ),
    doneButtonBorder = apricotGold,
    doneButtonText = apricotGold,
    // 由左至右：deepViolet → amethystPurple → deepViolet
    shareButtonFill = Brush.horizontalGradient(
        colors = listOf(deepViolet, amethystPurple, deepViolet)
    ),
    shareButtonBorder = apricotGold,
    shareIconTint = apricotGold,
    shareText = apricotGold,
)

// SelectSplitPartnerDialog 專屬顏色
data class SelectPartnerDialogColors(
    val dialogBackground: Color,            // Dialog 底色
    val dialogBorder: Color,                // Dialog 外框邊框
    val titleText: Color,                   // 標題文字
    val countHintText: Color,               // 已選人數提示文字（尚未選擇）
    val countHintActiveText: Color,         // 已選人數提示文字（已有選擇）
    val itemNameText: Color,                // 好友姓名文字
    val itemNickNameText: Color,            // 好友 @nickName 文字
    val searchBorder: Color,                // 搜尋輸入框邊框
    val searchText: Color,                  // 搜尋輸入框文字
    val searchHintText: Color,              // 搜尋輸入框 placeholder 文字
    val searchIcon: Color,                  // 搜尋 icon
    val indicatorBorder: Color,             // 勾選框邊框（未選取）
    val indicatorSelected: Color,           // 勾選框邊框 / Check icon（選取時）
    val totalBarBackground: Color,          // Total to Split 區塊底色
    val totalBarBorder: Color,              // Total to Split 區塊邊框
    val totalLabelText: Color,              // Total to Split 標籤文字
    val totalAmountText: Color,             // Total to Split 金額文字
    val confirmButtonFill: Brush,           // Assign Amounts 按鈕填滿（enabled）；Black Gold = 金色橫向漸層
    val confirmButtonDisabledFill: Brush,   // Assign Amounts 按鈕填滿（disabled）
    val confirmButtonBorder: Color,         // Assign Amounts 按鈕邊框（enabled）
    val confirmButtonDisabledBorder: Color, // Assign Amounts 按鈕邊框（disabled）
    val confirmButtonText: Color,           // Assign Amounts 按鈕文字（enabled）
    val confirmButtonDisabledText: Color,   // Assign Amounts 按鈕文字（disabled）
)

// SplitPartnersRow 專屬顏色
data class SplitPartnersRowColors(
    val partnerNameText: Color,        // 參與者名稱文字
    val editButtonBorder: Color,       // Edit 按鈕邊框
    val editButtonBackground: Color,   // Edit 按鈕底色
    val editButtonIcon: Color,         // Edit 按鈕 icon
    val cancelButtonBorder: Color,     // Cancel 按鈕邊框
    val cancelButtonBackground: Color, // Cancel 按鈕底色
    val cancelButtonIcon: Color,       // Cancel 按鈕 X icon
)

val NeonSplitPartnersRowColors = SplitPartnersRowColors(
    partnerNameText = themeWhite,
    editButtonBorder = lemonYellow,
    editButtonBackground = lemonYellow.copy(alpha = 0.12f),
    editButtonIcon = themeWhite,
    cancelButtonBorder = neonPink,
    cancelButtonBackground = neonPink.copy(alpha = 0.12f),
    cancelButtonIcon = themeWhite,
)

val BlackGoldSplitPartnersRowColors = SplitPartnersRowColors(
    partnerNameText = themeWhite,
    editButtonBorder = antiqueGold,
    editButtonBackground = espressoBrown.copy(alpha = 1f),
    editButtonIcon = antiqueGold,
    cancelButtonBorder = coralRed,
    cancelButtonBackground = coralRed.copy(alpha = 0.2f),
    cancelButtonIcon = coralRed,
)

// SplitBillDialog 專屬顏色
data class SplitBillDialogColors(
    val dialogBackground: Color,            // Dialog 底色
    val dialogBorder: Color,                // Dialog 外框邊框
    val titleText: Color,                   // 標題文字
    val toggleBorder: Brush,                // EQUALLY / CUSTOM 切換膠囊外框；Black Gold = 金色橫向漸層
    val toggleBackground: Color,            // EQUALLY / CUSTOM 切換膠囊底色
    val toggleSelectedFill: Brush,          // 選中模式填滿；Black Gold = 金色橫向漸層
    val toggleSelectedText: Color,          // 選中模式文字
    val toggleUnselectedText: Color,        // 未選中模式文字
    val manualSplitText: Color,             // Manual-Split 提示文字
    val participantNameText: Color,         // 參與者名稱文字
    val inputBackground: Color,             // 金額輸入框底色
    val inputBorder: Color,                 // 金額輸入框邊框（可輸入）
    val inputDisabledBorder: Color,         // 金額輸入框邊框（EQUALLY 模式鎖定）
    val inputText: Color,                   // 金額輸入框文字（可輸入）
    val inputDisabledText: Color,           // 金額輸入框文字（EQUALLY 模式鎖定）
    val inputCursor: Color,                 // 金額輸入框游標
    val remainingBarBackground: Color,      // Remaining 區塊底色
    val remainingBarBorder: Color,          // Remaining 區塊邊框
    val remainingLabelText: Color,          // Remaining 標籤文字
    val remainingAmountText: Color,         // Remaining 金額文字
    val confirmButtonFill: Brush,           // Confirm Request 按鈕填滿（enabled）；Black Gold = 金色橫向漸層
    val confirmButtonDisabledFill: Brush,   // Confirm Request 按鈕填滿（disabled）
    val confirmButtonBorder: Color,         // Confirm Request 按鈕邊框（enabled）
    val confirmButtonDisabledBorder: Color, // Confirm Request 按鈕邊框（disabled）
    val confirmButtonText: Color,           // Confirm Request 按鈕文字（enabled）
    val confirmButtonDisabledText: Color,   // Confirm Request 按鈕文字（disabled）
)

val NeonSplitBillDialogColors = SplitBillDialogColors(
    dialogBackground = deepMidnight,
    dialogBorder = neonCyan.copy(alpha = 0.7f),
    titleText = neonPurple,
    toggleBorder = Brush.linearGradient(
        colors = listOf(neonCyan.copy(alpha = 0.4f), neonPurple.copy(alpha = 0.8f))
    ),
    toggleBackground = inkNavy,
    toggleSelectedFill = SolidColor(neonCyan),
    toggleSelectedText = deepNavy,
    toggleUnselectedText = silverGray,
    manualSplitText = neonCyan,
    participantNameText = themeWhite,
    inputBackground = inkNavy,
    inputBorder = neonCyan.copy(alpha = 0.4f),
    inputDisabledBorder = neonCyan.copy(alpha = 0.25f),
    inputText = themeWhite,
    inputDisabledText = themeWhite.copy(alpha = 0.7f),
    inputCursor = neonCyan,
    remainingBarBackground = inkNavy,
    remainingBarBorder = neonCyan.copy(alpha = 0.4f),
    remainingLabelText = themeWhite,
    remainingAmountText = neonMint,
    confirmButtonFill = SolidColor(neonPurple.copy(alpha = 0.15f)),
    confirmButtonDisabledFill = SolidColor(neonPurple.copy(alpha = 0.05f)),
    confirmButtonBorder = neonPurple,
    confirmButtonDisabledBorder = neonPurple.copy(alpha = 0.3f),
    confirmButtonText = neonPurple,
    confirmButtonDisabledText = neonPurple.copy(alpha = 0.35f),
)

val BlackGoldSplitBillDialogColors = SplitBillDialogColors(
    dialogBackground = charcoalBlack,
    dialogBorder = paleGold,
    titleText = paleChampagne,
    // 由左至右：caramelGold → apricotGold → caramelGold（與 QrModeTabSelector 一致）
    toggleBorder = Brush.horizontalGradient(
        colors = listOf(caramelGold, apricotGold, caramelGold)
    ),
    toggleBackground = richBlack,
    toggleSelectedFill = Brush.horizontalGradient(
        colors = listOf(camelGold, apricotGold, camelGold)
    ),
    toggleSelectedText = themeBlack,
    toggleUnselectedText = warmSand,
    manualSplitText = antiqueGold,
    participantNameText = themeWhite,
    inputBackground = richBlack,
    inputBorder = paleChampagne,
    inputDisabledBorder = paleChampagne.copy(alpha = 0.35f),
    inputText = themeWhite,
    inputDisabledText = themeWhite.copy(alpha = 0.7f),
    inputCursor = antiqueGold,
    remainingBarBackground = charcoalBlack,
    remainingBarBorder = paleChampagne,
    remainingLabelText = themeWhite,
    remainingAmountText = antiqueGold,
    // 由左至右：camelGold → apricotGold → camelGold（與 Assign Amounts 按鈕一致）
    confirmButtonFill = Brush.horizontalGradient(
        colors = listOf(camelGold, apricotGold, camelGold)
    ),
    confirmButtonDisabledFill = SolidColor(deepBronze.copy(alpha = 0.35f)),
    confirmButtonBorder = apricotGold,
    confirmButtonDisabledBorder = apricotGold.copy(alpha = 0.3f),
    confirmButtonText = themeBlack,
    confirmButtonDisabledText = warmSand.copy(alpha = 0.4f),
)

val NeonSelectPartnerDialogColors = SelectPartnerDialogColors(
    dialogBackground = deepMidnight,
    dialogBorder = neonCyan.copy(alpha = 0.7f),
    titleText = neonPurple,
    countHintText = silverGray,
    countHintActiveText = neonCyan,
    itemNameText = themeWhite,
    itemNickNameText = silverGray,
    searchBorder = neonCyan,
    searchText = themeWhite,
    searchHintText = silverGray.copy(alpha = 0.7f),
    searchIcon = neonCyan,
    indicatorBorder = silverGray.copy(alpha = 0.5f),
    indicatorSelected = neonCyan,
    totalBarBackground = deepMidnight,
    totalBarBorder = neonCyan.copy(alpha = 0.7f),
    totalLabelText = themeWhite,
    totalAmountText = neonCyan,
    confirmButtonFill = SolidColor(neonPurple.copy(alpha = 0.15f)),
    confirmButtonDisabledFill = SolidColor(neonPurple.copy(alpha = 0.05f)),
    confirmButtonBorder = neonPurple,
    confirmButtonDisabledBorder = neonPurple.copy(alpha = 0.3f),
    confirmButtonText = themeWhite,
    confirmButtonDisabledText = themeWhite.copy(alpha = 0.35f),
)

val BlackGoldSelectPartnerDialogColors = SelectPartnerDialogColors(
    dialogBackground = charcoalBlack,
    dialogBorder = paleGold,
    titleText = paleChampagne,
    countHintText = warmSand,
    countHintActiveText = antiqueGold,
    itemNameText = themeWhite,
    itemNickNameText = warmSand,
    searchBorder = paleChampagne,
    searchText = themeWhite,
    searchHintText = warmSand.copy(alpha = 0.7f),
    searchIcon = paleChampagne,
    indicatorBorder = warmSand.copy(alpha = 0.5f),
    indicatorSelected = antiqueGold,
    totalBarBackground = charcoalBlack,
    totalBarBorder = paleChampagne,
    totalLabelText = themeWhite,
    totalAmountText = antiqueGold,
    // 由左至右：camelGold → apricotGold → camelGold（與 Review Details 按鈕一致）
    confirmButtonFill = Brush.horizontalGradient(
        colors = listOf(camelGold, apricotGold, camelGold)
    ),
    confirmButtonDisabledFill = SolidColor(deepBronze.copy(alpha = 0.35f)),
    confirmButtonBorder = apricotGold,
    confirmButtonDisabledBorder = apricotGold.copy(alpha = 0.3f),
    confirmButtonText = themeBlack,
    confirmButtonDisabledText = warmSand.copy(alpha = 0.4f),
)

val NeonScanPayColors = ScanPayColors(
    qrFrameBackground = slateGray,
    qrCodeColor = neonCyan,
    qrNickNameText = neonCyan,
    qrUserNameText = silverGray,
    balanceAmountText = themeWhite,
    balanceToggleIcon = Color.Gray,
    questCardBackground = themeBlack.copy(alpha = 0.5f),
    questTitleText = themeWhite,
    questProgressText = silverGray,
    questProgressIndicator = neonCyan,
    questProgressTrack = themeWhite.copy(alpha = 0.2f),
    qrTabBorder = Brush.linearGradient(
        colors = listOf(neonPurple.copy(alpha = 0.8f), neonCyan.copy(alpha = 0.4f))
    ),
    qrTabSelectedFill = SolidColor(neonCyan),
    actionButtonIconTint = Color.Unspecified,
    actionButtonIconBorder = neonCyan,
    actionButtonText = silverGray,
    input = NeonInputAmountColors,
    confirm = NeonConfirmPaymentColors,
    success = NeonTransactionSuccessColors,
    selectPartner = NeonSelectPartnerDialogColors,
    splitBill = NeonSplitBillDialogColors,
    splitPartners = NeonSplitPartnersRowColors,
)

val BlackGoldScanPayColors = ScanPayColors(
    qrFrameBackground = charcoalBlack,
    qrCodeColor = espressoBrown,
    qrNickNameText = espressoBrown,
    qrUserNameText = espressoBrown,
    balanceAmountText = antiqueGold,
    balanceToggleIcon = warmSand,
    questCardBackground = charcoalBlack,
    questTitleText = espressoBrown,
    questProgressText = espressoBrown,
    questProgressIndicator = espressoBrown,
    questProgressTrack = espressoBrown.copy(alpha = 0.55f),
    // 由左至右：caramelGold → apricotGold → caramelGold
    qrTabBorder = Brush.horizontalGradient(
        colors = listOf(caramelGold, apricotGold, caramelGold)
    ),
    qrTabSelectedFill = Brush.horizontalGradient(
        colors = listOf(caramelGold, apricotGold, caramelGold)
    ),
    actionButtonIconTint = espressoBrown,
    actionButtonIconBorder = espressoBrown,
    actionButtonText = espressoBrown,
    input = BlackGoldInputAmountColors,
    confirm = BlackGoldConfirmPaymentColors,
    success = BlackGoldTransactionSuccessColors,
    selectPartner = BlackGoldSelectPartnerDialogColors,
    splitBill = BlackGoldSplitBillDialogColors,
    splitPartners = BlackGoldSplitPartnersRowColors,
)
