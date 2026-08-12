package com.qpay.xcash.ui

object Routes {
    const val WELCOME = "welcome"
    const val LOGIN = "login"
    const val MAIN = "main?tab={tab}"

    fun mainAtTab(tab: Int) = "main?tab=$tab"

    // Profile sub-pages
    const val PROFILE_EDIT = "profile_edit"
    const val SECURITY_CENTER = "security_center"
    const val TRANSACTION_HISTORY = "transaction_history"
    const val VERIFICATION_STATUS = "verification_status"
    const val EDIT_AVATAR = "edit_avatar?imageUri={imageUri}"
    const val CAMERA = "camera"

    fun editAvatar(imageUri: String) = "edit_avatar?imageUri=${android.net.Uri.encode(imageUri)}"

    // ScanPay sub-pages
    const val SCAN_PAY_INPUT_AMOUNT = "scan_pay_input_amount"
    const val SCAN_PAY_CONFIRM_PAYMENT = "scan_pay_confirm_payment"
    const val SCAN_PAY_TRANSACTION_SUCCESSFUL = "scan_pay_transaction_successful"

    // Home sub-pages
    const val SETTINGS = "settings"
    const val UPDATE_LOG = "update_log"
    const val NOTIFICATIONS = "notifications"

    // Friend sub-pages
    const val FRIEND = "friend"
    const val FRIEND_LIST = "friend_list"
    const val ADD_FRIEND = "add_friend"
    const val FRIEND_QR_CODE = "friend_qr_code"

    // Cards sub-pages
    const val SELECT_CARD_TYPE = "select_card_type"
    const val ADD_NEW_CARD = "add_new_card"
    const val CARD_LINKED_SUCCESS = "card_linked_success"
    const val CARD_DETAIL = "card_detail/{cardId}"
    const val TRANSACTION_DETAIL = "transaction_detail/{orderId}"

    fun cardDetail(cardId: Int) = "card_detail/$cardId"
    fun transactionDetail(orderId: String) = "transaction_detail/$orderId"
}
