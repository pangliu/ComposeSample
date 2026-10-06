package com.qpay.xcash.ui.components

import androidx.annotation.DrawableRes
import com.qpay.xcash.R

// 轉帳渠道本地 icon（API image_url 尚未提供時使用）
// TransferChannelScreen 依 id 查詢；ConfirmTransferScreen 只拿得到 accountName，依 name 查詢
// 使用明確對照表而非 getIdentifier：編譯期檢查、且不會被 R8 資源壓縮誤刪
private data class ChannelIconEntry(
    val id: String,
    val names: List<String>,  // 顯示名稱與別名（GeneralTransfer / QR code payment_gateway 可能帶入的寫法）
    @DrawableRes val iconRes: Int
)

private val channelIconEntries = listOf(
    // E-Wallet
    ChannelIconEntry("xcash", listOf("Xcash", "Xcash Wallet"), R.mipmap.ic_ewallet_xcash),
    ChannelIconEntry("gcash", listOf("GCash"), R.mipmap.ic_ewallet_gcash),
    ChannelIconEntry("maya", listOf("Maya", "PayMaya"), R.mipmap.ic_ewallet_maya),
    ChannelIconEntry("gotyme", listOf("GoTyme"), R.mipmap.ic_ewallet_gotyme),
    ChannelIconEntry("grab", listOf("Grab", "GrabPay"), R.mipmap.ic_ewallet_grab),
    ChannelIconEntry("qrph", listOf("QRPh"), R.mipmap.ic_ewallet_qr_ph),
    ChannelIconEntry("shopee_pay", listOf("ShopeePay"), R.mipmap.ic_ewallet_shopee_pay),
    // Bank
    ChannelIconEntry("bdo_business", listOf("BDO Business"), R.mipmap.ic_bank_bdo_business),
    ChannelIconEntry("uno_digital_bank", listOf("UNO Digital Bank"), R.mipmap.ic_bank_uno_digital_bank),
    ChannelIconEntry("komo", listOf("Komo"), R.mipmap.ic_bank_komo),
    ChannelIconEntry("metrobank", listOf("Metrobank"), R.mipmap.ic_bank_metrobank),
    ChannelIconEntry("tayocash", listOf("TayoCash"), R.mipmap.ic_bank_tayo_cash),
    ChannelIconEntry("landbank", listOf("LANDBANK"), R.mipmap.ic_bank_land_bank),
    ChannelIconEntry("coin_ph", listOf("Coins.ph"), R.mipmap.ic_bank_coins_ph),
    ChannelIconEntry("star_pay", listOf("StarPay"), R.mipmap.ic_bank_star_pay),
    ChannelIconEntry("union_digital_bank", listOf("UnionDigital Bank"), R.mipmap.ic_bank_ud_union_digital_bank),
    ChannelIconEntry("cimb_bank", listOf("CIMB Bank"), R.mipmap.ic_bank_cimb_bank),
    // TODO: rcbc 尚無本地 icon，暫時顯示預設白框
)

private val iconById: Map<String, Int> = channelIconEntries.associate { it.id to it.iconRes }

// key = 正規化後的名稱（id 本身也可當名稱比對，例如 QR code 帶 "shopee_pay"）
private val iconByNormalizedName: Map<String, Int> = channelIconEntries
    .flatMap { entry -> (entry.names + entry.id).map { normalizeChannelName(it) to entry.iconRes } }
    .toMap()

// 轉小寫並移除空白與符號，讓 "GCash" / "gcash" / "G Cash" / "Coins.ph" / "coins_ph" 都能對上
private fun normalizeChannelName(name: String): String =
    name.lowercase().filter { it.isLetterOrDigit() }

@DrawableRes
fun channelIconRes(channelId: String): Int? = iconById[channelId]

@DrawableRes
fun channelIconResByName(channelName: String): Int? = iconByNormalizedName[normalizeChannelName(channelName)]
