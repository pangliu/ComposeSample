package com.qpay.xcash.ui.components

import android.net.Uri

// XCash QR code 網址格式集中管理：產生（我的 QR）與辨識（ScanPayScreen 掃描）共用同一組前綴
object XcashQrUrl {
    const val STORE_PREFIX = "http://xcash_store"
    const val PERSONAL_PREFIX = "http://xcash_personal"

    // 我的 QR（個人轉帳）：http://xcash_personal.io/pay?country_code={countryCode}&phone_number={phoneNumber}
    // countryCode 帶有 "+" 號，Uri.encode 會轉成 %2B
    fun personal(countryCode: String, phoneNumber: String): String =
        "$PERSONAL_PREFIX.io/pay?country_code=${Uri.encode(countryCode)}&phone_number=${Uri.encode(phoneNumber)}"

    // 測試用假資料：商家 QR，掃到會轉跳 InputAmountScreen（用任何 QR 產生器把這串網址轉成 QR 即可測試）
    const val FAKE_STORE = "http://xcash_store.io/pay?account=hank_001&to=hank&name=hank+liu"

    // 測試用假資料：個人 QR，掃到會轉跳 GeneralTransferScreen（電話 0914123123 對應 FakeUserApiService 的 Steve Rogers，Family 標籤）
    const val FAKE_PERSONAL = "http://xcash_personal.io/pay?country_code=%2B63&phone_number=0914123123"
}
