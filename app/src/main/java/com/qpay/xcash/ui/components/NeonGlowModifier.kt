package com.qpay.xcash.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.qpay.xcash.ui.theme.LocalAppColors

fun Modifier.neonGlow(
    color: Color,
    alpha: Float = 0.7f,
    borderRadius: Dp = 25.dp,
    glowRadius: Dp = 20.dp,
    blurStyle: android.graphics.BlurMaskFilter.Blur = android.graphics.BlurMaskFilter.Blur.NORMAL
) = this.drawBehind {
    val shadowColor = color.copy(alpha = alpha).toArgb()

    val paint = Paint().asFrameworkPaint().apply {
        this.color = shadowColor
        maskFilter = android.graphics.BlurMaskFilter(
            glowRadius.toPx(),
            blurStyle
        )
    }

    drawIntoCanvas { canvas ->
        canvas.nativeCanvas.drawRoundRect(
            0f, 0f, size.width, size.height,
            borderRadius.toPx(), borderRadius.toPx(),
            paint
        )
    }
}

// Theme-aware 版本：Black Gold 模式下自動跳過 glow 效果
@Composable
@ReadOnlyComposable
fun glowEnabled(): Boolean = LocalAppColors.current.effect.enableGlow
