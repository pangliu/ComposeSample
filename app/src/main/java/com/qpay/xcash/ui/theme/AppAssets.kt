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
)

val NeonAssets = AppAssets(
    centerLogoVideo = R.raw.bg_type3,
    centerLogoImage = null,
    xcashWordmark = R.drawable.ic_xcash,
    loginBackground = null,
)

val BlackGoldAssets = AppAssets(
    centerLogoVideo = R.raw.bg_type3,           // TODO: 替換為 Black Gold 影片
    centerLogoImage = R.drawable.ic_xcash_logo_black_gold,
    xcashWordmark = R.drawable.ic_xcash_black_gold,
    loginBackground = R.drawable.bg_login_black_gold,
)

val LocalAppAssets = staticCompositionLocalOf { NeonAssets }
