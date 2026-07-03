package com.qpay.xcash.ui.cards.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.qpay.xcash.ui.components.neonGlow
import com.qpay.xcash.ui.theme.neonPurple

@Composable
fun VoucherTicket(
    amount: String,
    ticketColor: Color = neonPurple
) {
    Box(
        modifier = Modifier
            .graphicsLayer { clip = false }
            .width(80.dp)
            .height(56.dp)
            .neonGlow(color = ticketColor, alpha = 0.3f, glowRadius = 14.dp, borderRadius = 8.dp)
            .drawWithContent {
                val notchRadius = 10.dp.toPx()
                val cornerRadius = 8.dp.toPx()
                val strokeWidth = 2.dp.toPx()

                val path = Path().apply {
                    moveTo(cornerRadius, 0f)
                    lineTo(size.width - cornerRadius, 0f)
                    arcTo(
                        rect = Rect(size.width - cornerRadius * 2, 0f, size.width, cornerRadius * 2),
                        startAngleDegrees = -90f, sweepAngleDegrees = 90f, forceMoveTo = false
                    )
                    lineTo(size.width, size.height / 2 - notchRadius)
                    arcTo(
                        rect = Rect(size.width - notchRadius, size.height / 2 - notchRadius, size.width + notchRadius, size.height / 2 + notchRadius),
                        startAngleDegrees = -90f, sweepAngleDegrees = -180f, forceMoveTo = false
                    )
                    lineTo(size.width, size.height - cornerRadius)
                    arcTo(
                        rect = Rect(size.width - cornerRadius * 2, size.height - cornerRadius * 2, size.width, size.height),
                        startAngleDegrees = 0f, sweepAngleDegrees = 90f, forceMoveTo = false
                    )
                    lineTo(cornerRadius, size.height)
                    arcTo(
                        rect = Rect(0f, size.height - cornerRadius * 2, cornerRadius * 2, size.height),
                        startAngleDegrees = 90f, sweepAngleDegrees = 90f, forceMoveTo = false
                    )
                    lineTo(0f, size.height / 2 + notchRadius)
                    arcTo(
                        rect = Rect(-notchRadius, size.height / 2 - notchRadius, notchRadius, size.height / 2 + notchRadius),
                        startAngleDegrees = 90f, sweepAngleDegrees = -180f, forceMoveTo = false
                    )
                    lineTo(0f, cornerRadius)
                    arcTo(
                        rect = Rect(0f, 0f, cornerRadius * 2, cornerRadius * 2),
                        startAngleDegrees = 180f, sweepAngleDegrees = 90f, forceMoveTo = false
                    )
                    close()
                }

                drawPath(path = path, color = ticketColor.copy(alpha = 0.12f))
                drawPath(path = path, color = ticketColor, style = Stroke(width = strokeWidth))
                drawContent()
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = amount,
            color = ticketColor,
            fontSize = 24.sp,
            fontWeight = FontWeight.ExtraBold,
            letterSpacing = 1.sp
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0A0E1A)
@Composable
private fun VoucherTicketPreview() {
    MaterialTheme {
        VoucherTicket(amount = "₱50")
    }
}
