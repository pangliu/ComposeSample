package com.qpay.xcash.utils

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.provider.Settings
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DeviceInfoProvider @Inject constructor(
    @ApplicationContext private val context: Context
) {

    /** 取得設備唯一識別碼 (Android ID) */
    val deviceId: String
        @SuppressLint("HardwareIds")
        get() = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID) ?: "unknown"

    /** 取得設備名稱 (例如：Samsung SM-G9980) */
    val deviceName: String
        get() = "${Build.MANUFACTURER} ${Build.MODEL}"

    /** 取得 App 版本號 (例如：1.0.0) */
    val appVersion: String
        get() = try {
            val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            packageInfo.versionName ?: "unknown"
        } catch (e: Exception) {
            "unknown"
        }

    /** 取得當前語系 (例如：zh-TW, en-US) */
    val locale: String
        get() = Locale.getDefault().toLanguageTag()

    /** 取得封裝好的所有設備資訊 */
    fun getDeviceData(): com.qpay.xcash.network.model.request.DeviceData {
        return com.qpay.xcash.network.model.request.DeviceData(
            deviceId = this.deviceId,
            deviceName = this.deviceName,
            appVersion = this.appVersion,
            locale = this.locale
        )
    }
}
