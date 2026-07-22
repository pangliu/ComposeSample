package com.qpay.xcash.ui.scanpay.input

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.qpay.xcash.R
import com.qpay.xcash.ui.Routes
import com.qpay.xcash.ui.scanpay.ScanPayUiState
import com.qpay.xcash.ui.scanpay.ScanPayViewModel
import com.qpay.xcash.ui.components.SubPageTopBar
import com.qpay.xcash.ui.components.neonGlow
import com.qpay.xcash.ui.theme.AppTheme
import com.qpay.xcash.ui.theme.BlackGoldAssets
import com.qpay.xcash.ui.theme.BlackGoldColors
import com.qpay.xcash.ui.theme.LocalAppAssets
import com.qpay.xcash.ui.theme.LocalAppColors
import com.qpay.xcash.ui.theme.NeonColors

private class CurrencyVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val raw = text.text
        if (raw.isEmpty()) return TransformedText(text, OffsetMapping.Identity)

        val dotIdx = raw.indexOf('.')
        val intPart = if (dotIdx < 0) raw else raw.substring(0, dotIdx)
        val decPart = if (dotIdx < 0) null else raw.substring(dotIdx + 1)
        val realDecLen = decPart?.length ?: 0
        val paddedZeros = if (decPart != null) (2 - realDecLen).coerceAtLeast(0) else 0

        val intFormatted = buildString {
            intPart.forEachIndexed { i, c ->
                if (i > 0 && (intPart.length - i) % 3 == 0) append(',')
                append(c)
            }
        }
        val transformed = if (decPart != null) "$intFormatted.${"$decPart".padEnd(2, '0')}" else intFormatted

        val origToTrans = IntArray(raw.length + 1)
        val transToOrig = IntArray(transformed.length + 1) // initialized to 0

        var origPos = 0
        for (tc in transformed.indices) {
            val c = transformed[tc]
            val isPaddedZero = decPart != null &&
                tc > intFormatted.length &&
                (tc - intFormatted.length - 1) >= realDecLen
            when {
                c == ',' -> transToOrig[tc + 1] = origPos
                isPaddedZero -> transToOrig[tc + 1] = raw.length
                else -> {
                    origToTrans[origPos] = tc
                    origPos++
                    transToOrig[tc + 1] = origPos
                }
            }
        }
        origToTrans[raw.length] = transformed.length - paddedZeros

        return TransformedText(
            AnnotatedString(transformed),
            object : OffsetMapping {
                override fun originalToTransformed(offset: Int) =
                    origToTrans.getOrElse(offset) { transformed.length }
                override fun transformedToOriginal(offset: Int) =
                    transToOrig.getOrElse(offset) { raw.length }
            }
        )
    }
}

@Composable
fun InputAmountScreen(
    viewModel: ScanPayViewModel = hiltViewModel(),
    onBack: () -> Unit = {},
    onNavigate: (String) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    InputAmountContent(
        uiState = uiState,
        onBack = onBack,
        onReviewDetails = { amount ->
            viewModel.setAmount(amount)
            onNavigate(Routes.SCAN_PAY_CONFIRM_PAYMENT)
        }
    )
}

@Composable
private fun InputAmountContent(
    uiState: ScanPayUiState,
    onBack: () -> Unit = {},
    onReviewDetails: (String) -> Unit = {}
) {
    val colors = LocalAppColors.current
    val assets = LocalAppAssets.current
    var amount by remember { mutableStateOf("") }
    var isBalanceVisible by remember { mutableStateOf(false) }
    Scaffold(
        containerColor = colors.bg.page,
        contentColor = Color.White
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize()) {
            assets.scanPayBackground?.let { resId ->
                Image(
                    painter = painterResource(resId),
                    contentDescription = null,
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier.fillMaxSize()
                )
            }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            SubPageTopBar(
                title = stringResource(R.string.input_amount_title),
                onBack = onBack
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 50.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()

                        .then(
                            if (colors.effect.enableGlow)
                                Modifier.neonGlow(
                                    color = colors.accent.primary,
                                    alpha = 0.4f,
                                    glowRadius = 12.dp,
                                    borderRadius = 12.dp
                                )
                            else Modifier
                        )
                        .background(color = colors.bg.page, shape = RoundedCornerShape(12.dp))
                        .border(
                            width = 1.5.dp,
                            color = colors.scanPay.input.cardOuterBorder,
                            shape = RoundedCornerShape(12.dp)
                        )
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .then(
                                if (colors.effect.enableGlow)
                                    Modifier.neonGlow(
                                        color = colors.accent.secondary,
                                        alpha = 0.25f,
                                        glowRadius = 12.dp,
                                        borderRadius = 12.dp
                                    )
                                else Modifier
                            )
                            .padding(10.dp)
//                        .background(colors.bg.page, RoundedCornerShape(12.dp))
                            .then(
                                colors.scanPay.input.cardInnerBorder?.let { innerBorder ->
                                    Modifier.border(1.5.dp, innerBorder, RoundedCornerShape(12.dp))
                                } ?: Modifier
                            )
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Spacer(Modifier.height(10.dp))
                            Box(
                                modifier = Modifier
                                    .size(80.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(color = colors.bg.page)
                                    .border(
                                        width = 1.5.dp,
                                        brush = Brush.linearGradient(
                                            colors = listOf(
                                                colors.accent.primary.copy(alpha = 0.7f),
                                                colors.accent.secondary.copy(alpha = 0.7f)
                                            )
                                        ),
                                        shape = RoundedCornerShape(10.dp)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {

                            }
                            Spacer(Modifier.height(5.dp))
                            Text(
                                text = "@${uiState.recipientNickName}",
                                color = colors.scanPay.input.recipientText,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = uiState.recipientName,
                                color = colors.scanPay.input.recipientText,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(Modifier.height(8.dp))
                        }
                    }
                }
                assets.inputAmountQrCrownDecor?.let { resId ->
                    Image(
                        painter = painterResource(resId),
                        contentDescription = "qrcode_crown",
                        modifier = Modifier
                            .size(80.dp)
                            .offset(x = -35.dp, y = -40.dp)
                            .rotate(-35f)
                    )
                }
            }
            val inputAmountBg = assets.inputAmountBackground
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .then(
                        if (inputAmountBg != null)
                            Modifier.paint(
                                painter = painterResource(inputAmountBg),
                                contentScale = ContentScale.FillWidth
                            )
                        else Modifier.padding(vertical = 40.dp)
                    ),
                verticalArrangement = Arrangement.Center,
            ) {
                // ── Amount Input ─────────────────────────────────────────────────
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    val amountGlowShadow = if (colors.effect.enableGlow)
                        Shadow(color = colors.accent.primary, blurRadius = 15f)
                    else null
                    BasicTextField(
                        value = amount,
                        onValueChange = { newValue ->
                            val filtered = newValue.filter { it.isDigit() || it == '.' }
                            val dotIndex = filtered.indexOf('.')
                            val isValid = when {
                                filtered.count { it == '.' } > 1 -> false
                                dotIndex == -1 && filtered.length > 6 -> false
                                dotIndex != -1 && dotIndex > 6 -> false
                                dotIndex != -1 && filtered.length - dotIndex - 1 > 2 -> false
                                else -> true
                            }
                            if (isValid) amount = filtered
                        },
                        textStyle = TextStyle(
                            color = colors.accent.primary,
                            fontSize = 40.sp,
                            fontWeight = FontWeight.Bold,
                            shadow = amountGlowShadow
                        ),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        visualTransformation = remember { CurrencyVisualTransformation() },
                        cursorBrush = SolidColor(colors.accent.primary),
                        singleLine = true,
                        modifier = Modifier.width(IntrinsicSize.Min),
                        decorationBox = { innerTextField ->
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = stringResource(R.string.input_amount_currency),
                                    color = colors.accent.primary,
                                    fontSize = 48.sp,
                                    fontWeight = FontWeight.Bold,
                                    style = TextStyle(shadow = amountGlowShadow)
                                )
                                Spacer(Modifier.width(12.dp))
                                Box {
                                    // 永遠佔位確保最小寬度，空白時顯示，有字時透明
                                    Text(
                                        text = stringResource(R.string.input_amount_hint),
                                        color = if (amount.isEmpty())
                                            colors.accent.primary.copy(alpha = 0.3f)
                                        else
                                            Color.Transparent,
                                        fontSize = 40.sp,
                                        fontWeight = FontWeight.Bold,
                                        style = if (amount.isEmpty()) TextStyle(shadow = amountGlowShadow) else TextStyle()
                                    )
                                    innerTextField()
                                }
                            }
                        }
                    )
                }
            }
            Spacer(Modifier.height(10.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp)
            ) {
                Image(
                    painter = painterResource(assets.myQrLeftDecorIcon),
                    contentDescription = "balance_left_image",
                    modifier = Modifier
                        .size(80.dp)
                        .alpha(if (assets.inputAmountBalanceLeftDecorVisible) 1f else 0f)
                        .then(
                            if (colors.effect.enableGlow)
                                Modifier.neonGlow(
                                    color = colors.accent.secondary,
                                    alpha = 0.25f,
                                    glowRadius = 30.dp
                                )
                            else Modifier
                        )
                )
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .align(Alignment.CenterVertically)
                ) {
                    Spacer(modifier = Modifier
                        .fillMaxWidth()
                        .height(1.5.dp)
                        .then(
                            if (colors.effect.enableGlow)
                                Modifier.neonGlow(color = colors.accent.primary.copy(0.7f))
                            else Modifier
                        )
                        .background(
                            color = colors.accent.primary.copy(0.6f)
                        )
                    )
                    Spacer(Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Balance: ",
                            color = colors.scanPay.input.balanceLabelText,
                            fontSize = 14.sp,
                        )
                        Text(
                            text = "PHP 1,000,000",
                            color = colors.scanPay.input.balanceAmountText,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(Modifier.height(10.dp))
                    Spacer(modifier = Modifier
                        .fillMaxWidth()
                        .height(1.5.dp)
                        .background(
                            color = colors.accent.primary.copy(0.6f)
                        )
                        .then(
                            if (colors.effect.enableGlow)
                                Modifier.neonGlow(color = colors.accent.primary, alpha = 0.3f, glowRadius = 30.dp)
                            else Modifier
                        )
                    )
                }
                Image(
                    painter = painterResource(assets.myQrRightDecorIcon),
                    contentDescription = null,
                    modifier = Modifier
                        .size(80.dp)
                        .then(
                            if (colors.effect.enableGlow)
                                Modifier.neonGlow(color = colors.accent.primary, alpha = 0.3f, glowRadius = 30.dp)
                            else Modifier
                        )
                        .align(Alignment.CenterVertically)
                )
            }
            val isReviewEnabled = (amount.toDoubleOrNull() ?: 0.0) > 0.0
            Box(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .then(
                        if (isReviewEnabled && colors.effect.enableGlow)
                            Modifier.neonGlow(color = colors.accent.secondary, alpha = 0.7f, glowRadius = 12.dp, borderRadius = 15.dp)
                        else Modifier
                    )
                    .background(
                        brush = colors.scanPay.input.reviewButtonFill,
                        shape = RoundedCornerShape(30.dp),
                        alpha = if (isReviewEnabled) 1f else 0.3f
                    )
                    .then(
                        colors.scanPay.input.reviewButtonBorder?.let { borderColor ->
                            Modifier.border(
                                width = 1.5.dp,
                                color = if (isReviewEnabled) borderColor else borderColor.copy(alpha = 0.3f),
                                shape = RoundedCornerShape(30.dp)
                            )
                        } ?: Modifier
                    )
                    .clickable(
                        enabled = isReviewEnabled,
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) { onReviewDetails(amount) }
                    .padding(horizontal = 40.dp, vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.input_amount_review_details),
                    color = if (isReviewEnabled) Color.Black else Color.Black.copy(alpha = 0.35f),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        }
    }
}

@Preview(name = "Neon", showBackground = true, backgroundColor = 0xFF030F1B)
@Composable
private fun InputAmountScreenPreviewNeon() {
    AppTheme(colors = NeonColors) {
        InputAmountContent(
            uiState = ScanPayUiState(
                recipientNickName = "bruceb",
                recipientName = "Bruce Banner"
            )
        )
    }
}

@Preview(name = "Black Gold", showBackground = true, backgroundColor = 0xFF050505)
@Composable
private fun InputAmountScreenPreviewBlackGold() {
    AppTheme(colors = BlackGoldColors, assets = BlackGoldAssets) {
        InputAmountContent(
            uiState = ScanPayUiState(
                recipientNickName = "bruceb",
                recipientName = "Bruce Banner"
            )
        )
    }
}
