package com.qpay.xcash.ui.theme

// 主題種類的單一事實來源（SSOT）。
// 新增主題只需在這裡加一個 entry，colors/assets 的 when 分支會強制編譯器要求補齊。
enum class ThemeMode {
    NEON,
    BLACK_GOLD;

    val colors: AppColors
        get() = when (this) {
            NEON -> NeonColors
            BLACK_GOLD -> BlackGoldColors
        }

    val assets: AppAssets
        get() = when (this) {
            NEON -> NeonAssets
            BLACK_GOLD -> BlackGoldAssets
        }
}
