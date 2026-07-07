package com.qpay.xcash.ui.components

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer

// 將 Icon/Image 套上漸層色（例如 silverShimmer、goldShimmer）。
// brush 為 null 時原樣顯示（例如 Neon 主題不套用漸層裝飾），與 GradientText 的 brush 規則一致。
fun Modifier.gradientTint(brush: Brush?): Modifier {
    if (brush == null) return this
    return this
        .graphicsLayer { compositingStrategy = CompositingStrategy.Offscreen }
        .drawWithContent {
            drawContent()
            drawRect(brush = brush, blendMode = BlendMode.SrcAtop)
        }
}
