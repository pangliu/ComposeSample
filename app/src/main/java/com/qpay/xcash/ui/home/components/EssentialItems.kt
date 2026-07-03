    package com.qpay.xcash.ui.home.essential

import androidx.annotation.DrawableRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.automirrored.filled.*
import androidx.compose.ui.graphics.vector.ImageVector
import com.qpay.xcash.R

/**
 * X-Essentials 功能項目的資料模型
 */
data class EssentialItem(
    val iconVector: ImageVector? = null,
    val label: String,
    val onClick: () -> Unit = {},
    @DrawableRes val iconRes: Int? = null,
    val useOriginalColor: Boolean = false
)

/**
 * 所有可用的 Essential 功能項目（唯一資料來源）
 *
 * - XEssentialsCard 顯示前 16 個
 * - EditEssentialsDialog 顯示全部
 */
val allEssentialItems = listOf(
    EssentialItem(iconVector = null, label = "Send", iconRes = R.mipmap.ic_essent_send, useOriginalColor = false),
    EssentialItem(iconVector = null, label = "Bills", iconRes = R.mipmap.ic_essent_bills, useOriginalColor = false),
    EssentialItem(iconVector = null, label = "Load Up", iconRes = R.mipmap.ic_essent_load_up, useOriginalColor = false),
    EssentialItem(iconVector = null, label = "Cash In", iconRes = R.mipmap.ic_essent_cash_in, useOriginalColor = false),
    EssentialItem(iconVector = null, label = "Cash Out", iconRes = R.mipmap.ic_essent_cash_out, useOriginalColor = false),
    EssentialItem(iconVector = null, label = "KYC", iconRes = R.mipmap.ic_essent_kyc, useOriginalColor = false),
    EssentialItem(iconVector = null, label = "Vouchers", iconRes = R.mipmap.ic_essent_vouchers, useOriginalColor = false),
    EssentialItem(iconVector = null, label = "Cards", iconRes = R.mipmap.ic_essent_cards, useOriginalColor = false),
    EssentialItem(iconVector = null, label = "Scan QR/Pay", iconRes = R.mipmap.ic_essent_scanqr_pay, useOriginalColor = false),
    EssentialItem(iconVector = null, label = "Show My QR", iconRes = R.mipmap.ic_essent_show_myqr, useOriginalColor = false),
    EssentialItem(iconVector = null, label = "Profile", iconRes = R.mipmap.ic_essent_profile, useOriginalColor = false),
    EssentialItem(iconVector = null, label = "Transaction History", iconRes = R.mipmap.ic_essent_transaction_history, useOriginalColor = false),
    EssentialItem(iconVector = null, label = "Bill Payments", iconRes = R.mipmap.ic_essent_bill_payments, useOriginalColor = false),
    EssentialItem(iconVector = null, label = "Bank Transfer", iconRes = R.mipmap.ic_essent_bank_transfer, useOriginalColor = false),
    EssentialItem(iconVector = null, label = "Govt. Records", iconRes = R.mipmap.ic_essent_govt_records, useOriginalColor = false),
    EssentialItem(iconVector = null, label = "Transfer Invitation", iconRes = R.mipmap.ic_essent_transfer_invitation, useOriginalColor = false),
    EssentialItem(iconVector = null, label = "Buy Load", iconRes = R.mipmap.ic_essent_buy_load, useOriginalColor = false),
    EssentialItem(iconVector = null, label = "Gaming Pins", iconRes = R.mipmap.ic_essent_gameing_pins, useOriginalColor = false),
    EssentialItem(iconVector = null, label = "Pot Gifting", iconRes = R.mipmap.ic_essent_pot_gifting, useOriginalColor = false),
    EssentialItem(iconVector = null, label = "Send Money", iconRes = R.mipmap.ic_essent_send_money, useOriginalColor = false),
    EssentialItem(iconVector = null, label = "Vouchers Deals", iconRes = R.mipmap.ic_essent_vouchers_deals, useOriginalColor = false),
    EssentialItem(iconVector = null, label = "Credit Loans", iconRes = R.mipmap.ic_essent_credit_loans, useOriginalColor = false),
    EssentialItem(iconVector = null, label = "Insurance", iconRes = R.mipmap.ic_essent_insurance, useOriginalColor = false),

//    EssentialItem(Icons.AutoMirrored.Filled.List, "Bills"),
//    EssentialItem(Icons.Default.Phone, "Load Up"),
//    EssentialItem(Icons.Default.Add, "Cash In"),

//    EssentialItem(Icons.Default.ShoppingCart, "Cash Out"),
//    EssentialItem(Icons.Default.Person, "KYC"),
//    EssentialItem(Icons.Default.Star, "Vouchers"),
//    EssentialItem(Icons.Default.Favorite, "Cards"),
//    EssentialItem(Icons.Default.LocationOn, "Stores"),
//    EssentialItem(Icons.Default.Share, "Refer"),
//    EssentialItem(Icons.Default.Lock, "Security"),
//    EssentialItem(Icons.Default.Info, "Support"),
//    EssentialItem(Icons.Default.Build, "Tools"),
//    EssentialItem(Icons.Default.Email, "Inbox"),
//    EssentialItem(Icons.Default.DateRange, "Schedule"),
//    EssentialItem(Icons.Default.Search, "Explore"),
//    EssentialItem(Icons.Default.ThumbUp, "Rewards"),
//    EssentialItem(Icons.Default.Refresh, "Sync"),
//    EssentialItem(Icons.Default.Settings, "Settings"),
//    EssentialItem(Icons.Default.Home, "Home")
)

/** XEssentialsCard 最多顯示的項目數量 */
const val ESSENTIALS_DISPLAY_COUNT = 16

/** 每頁顯示的項目數量（2 排 x 4 欄） */
const val ITEMS_PER_PAGE = 8
