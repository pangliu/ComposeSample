package com.example.newproject.ui

object Routes {
    const val WELCOME = "welcome"
    const val LOGIN = "login"
    const val MAIN = "main"

    // Profile sub-pages
    const val PROFILE_EDIT = "profile_edit"
    const val SECURITY_CENTER = "security_center"
    const val TRANSACTION_HISTORY = "transaction_history"
    const val VERIFICATION_STATUS = "verification_status"

    // ScanPay sub-pages
    const val SCAN_PAY_INPUT_AMOUNT = "scan_pay_input_amount"
    const val SCAN_PAY_CONFIRM_PAYMENT = "scan_pay_confirm_payment"
    const val SCAN_PAY_TRANSACTION_SUCCESSFUL = "scan_pay_transaction_successful"

    // Cards sub-pages
    const val SELECT_CARD_TYPE = "select_card_type"
    const val ADD_NEW_CARD = "add_new_card"
    const val CARD_DETAIL = "card_detail/{cardId}"

    fun cardDetail(cardId: Int) = "card_detail/$cardId"
}
