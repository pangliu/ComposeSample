package com.qpay.xcash.ui.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit

// 可套用漸層的文字元件，跨頁面共用。
// brush 不為 null 時顯示漸層；為 null 時退回單色 color（例如 Neon 主題不套用漸層裝飾）。
@Composable
fun GradientText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    brush: Brush? = null,
    fontSize: TextUnit = TextUnit.Unspecified,
    fontWeight: FontWeight? = null,
    letterSpacing: TextUnit = TextUnit.Unspecified,
    textAlign: TextAlign? = null,
) {
    Text(
        text = text,
        modifier = modifier,
        style = if (brush != null) TextStyle(brush = brush) else TextStyle(color = color),
        fontSize = fontSize,
        fontWeight = fontWeight,
        letterSpacing = letterSpacing,
        textAlign = textAlign,
    )
}
