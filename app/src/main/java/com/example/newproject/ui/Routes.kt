package com.example.newproject.ui

object Routes {
    const val WELCOME = "welcome"
    const val LOGIN = "login"
    const val MAIN = "main"

    // Profile sub-pages
    const val SECURITY_CENTER = "security_center"

    // Cards sub-pages
    const val SELECT_CARD_TYPE = "select_card_type"
    const val ADD_NEW_CARD = "add_new_card"
    const val CARD_DETAIL = "card_detail/{cardId}"

    fun cardDetail(cardId: Int) = "card_detail/$cardId"
}
