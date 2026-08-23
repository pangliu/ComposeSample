package com.qpay.xcash.ui.theme

import androidx.compose.ui.graphics.Brush

// ui/friend/components/FriendTopBar 專屬顏色

data class FriendTopBarColors(
    val titleGradient: Brush,   // 標題文字漸層
)

val NeonFriendTopBarColors = FriendTopBarColors(
    // 由左至右：neonPurple → neonCyan → neonPurple
    titleGradient = Brush.horizontalGradient(
        colors = listOf(neonPurple, neonCyan, neonPurple)
    ),
)

val BlackGoldFriendTopBarColors = FriendTopBarColors(
    // 由左至右：oldGold → amberGold → champagneGold → amberGold → oldGold（與 gradient.goldShimmer 一致）
    titleGradient = Brush.verticalGradient(
        colors = listOf(oldGold, amberGold, champagneGold, amberGold, oldGold)
    ),
)
