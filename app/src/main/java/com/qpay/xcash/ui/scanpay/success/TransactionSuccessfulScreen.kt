package com.qpay.xcash.ui.scanpay.success

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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.paint
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.qpay.xcash.R
import com.qpay.xcash.ui.profile.transaction.formatAmount
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
import com.qpay.xcash.ui.theme.lemonYellow


private val earnedPoint = 3.5

@Composable
fun TransactionSuccessfulScreen(
    viewModel: ScanPayViewModel = hiltViewModel(),
    onBack: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    TransactionSuccessfulContent(uiState = uiState, onBack = onBack)
}

@Composable
private fun TransactionSuccessfulContent(
    uiState: ScanPayUiState,
    onBack: () -> Unit = {},
    onShare: () -> Unit = {}
) {
    val colors = LocalAppColors.current
    val assets = LocalAppAssets.current
    Scaffold(
        containerColor = colors.bg.page,
        contentColor = Color.White
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize()) {
            assets.successPageBackground?.let { resId ->
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
                Box(modifier = Modifier.fillMaxWidth()) {
                    assets.successHeaderBackground?.let { resId ->
                        Image(
                            modifier = Modifier.matchParentSize(),
                            contentDescription = null,
                            contentScale = ContentScale.FillWidth,
                            painter = painterResource(resId),
                            alignment = Alignment.TopCenter
                        )
                    }
                    Column {
                        SubPageTopBar(
                            title = stringResource(R.string.transaction_successful_title),
                            onBack = onBack,
                            showBack = false
                        )
//                    Spacer(Modifier.height(20.dp))
                        Box(modifier = Modifier.fillMaxWidth()) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                val leftDecor = assets.qrSectionLeftDecor
                                if (leftDecor != null) {
                                    Image(
                                        modifier = Modifier.weight(0.15f),
                                        painter = painterResource(leftDecor),
                                        contentScale = ContentScale.FillWidth,
                                        alignment = Alignment.CenterEnd,
                                        contentDescription = stringResource(R.string.scan_pay_my_qr_left_qr_code_desc)
                                    )
                                } else {
                                    Spacer(modifier = Modifier.weight(0.15f))
                                }
                                val cardModifier = if (colors.effect.enableGlow)
                                // Neon：邊框圖 + glow
                                    Modifier
                                        .neonGlow(
                                            color = colors.accent.primary,
                                            alpha = 0.5f,
                                            glowRadius = 20.dp,
                                            borderRadius = 20.dp
                                        )
                                        .paint(
                                            painter = painterResource(assets.successCardBg),
                                            contentScale = ContentScale.FillWidth
                                        )
                                else
                                // Black Gold：背景圖自帶邊框，FillBounds 撐滿內容高度
                                    Modifier
                                        .paint(
                                            painter = painterResource(assets.successCardBg),
//                                        contentScale = ContentScale.FillBounds
                                        )
                                        .padding(bottom = 20.dp)
                                Box(
                                    modifier = Modifier
                                        .align(Alignment.CenterVertically)
                                        .weight(0.7f)
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .testTag("transaction_successful")
                                            .then(cardModifier)
//                                        .align(Alignment.CenterVertically)
//                                        .weight(0.7f)
                                    ) {
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(top = 25.dp)
                                                .padding(horizontal = 20.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .size(60.dp)
                                                    .then(
                                                        if (colors.effect.enableGlow)
                                                            Modifier.neonGlow(
                                                                color = colors.accent.primary,
                                                                alpha = 0.6f,
                                                                glowRadius = 8.dp,
                                                                borderRadius = 8.dp
                                                            )
                                                        else Modifier
                                                    )
                                                    .then(
                                                        colors.scanPay.success.avatarBorder?.let { borderColor ->
                                                            Modifier.border(
                                                                width = 1.5.dp,
                                                                color = borderColor,
                                                                shape = RoundedCornerShape(12.dp)
                                                            )
                                                        } ?: Modifier
                                                    )
                                                    .background(
                                                        color = colors.bg.page,
                                                        shape = RoundedCornerShape(12.dp)
                                                    )
                                            )
                                            Spacer(Modifier.width(8.dp))
                                            Column(modifier = Modifier.fillMaxWidth()) {
                                                Text(
                                                    text = stringResource(R.string.transaction_successful_paid_to),
                                                    color = Color.White,
                                                    fontSize = 14.sp
                                                )
                                                Text(
                                                    text = "@${uiState.recipientNickName}",
                                                    color = colors.scanPay.success.nickNameText,
                                                    fontSize = 14.sp,
                                                    fontWeight = FontWeight.Bold
                                                )
                                                Text(
                                                    text = uiState.recipientName,
                                                    color = colors.scanPay.success.nameText,
                                                    fontSize = 12.sp,
                                                    fontWeight = FontWeight.Normal
                                                )
                                            }
                                        }
                                        Spacer(Modifier.height(15.dp))
                                        Spacer(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(horizontal = 30.dp)
                                                .height(1.5.dp)
                                                .background(colors.scanPay.success.divider)
                                        )
                                        Spacer(Modifier.height(8.dp))
                                        val amountGlowShadow = if (colors.effect.enableGlow)
                                            Shadow(color = colors.accent.primary, blurRadius = 15f)
                                        else null
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth(),
                                            horizontalArrangement = Arrangement.Center
                                        ) {
                                            Text(
                                                text = stringResource(R.string.input_amount_currency),
                                                color = colors.accent.primary,
                                                fontSize = 32.sp,
                                                fontWeight = FontWeight.Bold,
                                                style = TextStyle(shadow = amountGlowShadow)
                                            )
                                            Spacer(Modifier.width(5.dp))
                                            Text(
                                                text = formatAmount(
                                                    uiState.amount.toDoubleOrNull() ?: 0.0
                                                ),
                                                color = colors.accent.primary,
                                                fontSize = 32.sp,
                                                fontWeight = FontWeight.Bold,
                                                style = TextStyle(shadow = amountGlowShadow)
                                            )
                                        }
                                        Spacer(Modifier.height(8.dp))
                                        Spacer(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(horizontal = 30.dp)
                                                .height(1.5.dp)
                                                .background(colors.scanPay.success.divider)
                                        )
                                        Spacer(Modifier.height(20.dp))

                                        // TODO: 從 API 取得實際數字
                                        val originalTotal = 350.00
                                        val pointsApplied = 50.00
                                        val xPointsUsed = 5000
                                        val finalAmount = originalTotal - pointsApplied

                                        Column(
                                            modifier = Modifier
                                                .testTag("transaction-successful-content")
                                                .fillMaxWidth(),
                                            verticalArrangement = Arrangement.spacedBy(1.dp)
                                        ) {
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.Center,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Text(
                                                    text = "Original Total:",
                                                    color = Color.White,
                                                    fontSize = 11.sp,
                                                    lineHeight = 15.sp
                                                )
                                                Text(
                                                    text = "PHP %,.2f".format(originalTotal),
                                                    color = colors.scanPay.success.detailAmountText,
                                                    fontSize = 11.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    lineHeight = 15.sp
                                                )
                                            }
                                            Column(
                                                modifier = Modifier.fillMaxWidth(),
                                                verticalArrangement = Arrangement.spacedBy(0.dp)
                                            ) {
                                                Row(
                                                    modifier = Modifier.fillMaxWidth(),
                                                    horizontalArrangement = Arrangement.Center,
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    Text(
                                                        text = "Points Applied:",
                                                        color = Color.White,
                                                        fontSize = 11.sp,
                                                        lineHeight = 13.sp
                                                    )
                                                    Text(
                                                        text = "-PHP %,.2f".format(pointsApplied),
                                                        color = colors.scanPay.success.detailAmountText,
                                                        fontSize = 11.sp,
                                                        fontWeight = FontWeight.Bold,
                                                        lineHeight = 13.sp
                                                    )
                                                }
                                                Row(
                                                    modifier = Modifier.fillMaxWidth(),
                                                    horizontalArrangement = Arrangement.Center
                                                ) {
                                                    Text(
                                                        text = buildAnnotatedString {
                                                            withStyle(
                                                                SpanStyle(
                                                                    color = Color.White,
                                                                    fontWeight = FontWeight.Bold
                                                                )
                                                            ) {
                                                                append("(via ")
                                                            }
                                                            withStyle(
                                                                SpanStyle(
                                                                    color = colors.scanPay.success.detailAmountText,
                                                                    fontWeight = FontWeight.Bold
                                                                )
                                                            ) {
                                                                append("%,d".format(xPointsUsed))
                                                            }
                                                            withStyle(
                                                                SpanStyle(
                                                                    color = Color.White,
                                                                    fontWeight = FontWeight.Bold
                                                                )
                                                            ) {
                                                                append(" X-points)")
                                                            }
                                                        },
                                                        fontSize = 11.sp,
                                                        lineHeight = 13.sp
                                                    )
                                                }
                                            }
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.Center,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Text(
                                                    text = "Final Amount Paid:",
                                                    color = Color.White,
                                                    fontSize = 11.sp,
                                                    lineHeight = 15.sp
                                                )
                                                Text(
                                                    text = "PHP %,.2f".format(finalAmount),
                                                    color = colors.scanPay.success.detailAmountText,
                                                    fontSize = 11.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    lineHeight = 15.sp
                                                )
                                            }
                                        }
                                    }
                                    assets.transactionSuccessfulQrCrownDecor?.let { resId ->
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
                                val rightDecor = assets.qrSectionRightDecor
                                if (rightDecor != null) {
                                    Image(
                                        modifier = Modifier.weight(0.15f),
                                        painter = painterResource(rightDecor),
                                        contentScale = ContentScale.FillWidth,
                                        alignment = Alignment.CenterStart,
                                        contentDescription = stringResource(R.string.scan_pay_my_qr_right_qr_code_desc)
                                    )
                                } else {
                                    Spacer(modifier = Modifier.weight(0.15f))
                                }
                            }
                        }  // inner Box
                    }  // Column
                }  // outer Box
                Spacer(Modifier.height(20.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = buildAnnotatedString {
                            withStyle(
                                SpanStyle(
                                    color = colors.scanPay.success.earnedPointsText,
                                    fontWeight = FontWeight.Bold
                                )
                            ) {
                                append("+%,.2f".format(earnedPoint))
                            }
                            withStyle(
                                SpanStyle(
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                            ) {
                                append(" X-Points earned")
                            }

                        },
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                // ── Balance row ───────────────────────────────────────────────────
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp)
                ) {
                    Image(
                        painter = painterResource(assets.myQrLeftDecorIcon),
                        contentDescription = "balance_left_image",
                        modifier = Modifier
                            .size(80.dp)
                            .alpha(if (assets.transactionSuccessfulBalanceDecorVisible) 1f else 0f)
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
                        Spacer(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(1.5.dp)
                                .then(
                                    if (colors.effect.enableGlow)
                                        Modifier.neonGlow(
                                            color = colors.scanPay.success.balanceDivider.copy(alpha = 0.7f),
                                        )
                                    else Modifier
                                )
                                .background(
                                    color = colors.scanPay.success.balanceDivider
                                )
                        )
                        Spacer(Modifier.height(10.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "PAYING FROM:",
                                color = colors.scanPay.success.balanceLabelText,
                                fontSize = 14.sp
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Wallet name",
                                color = Color.White,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Available Balance: ",
                                color = colors.scanPay.success.balanceLabelText,
                                fontSize = 14.sp,
                            )
                            Text(
                                text = "PHP 1,000,000",
                                color = Color.White,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(Modifier.height(10.dp))
                        Spacer(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(1.5.dp)
                                .background(
                                    color = colors.scanPay.success.balanceDivider
                                )
                                .then(
                                    if (colors.effect.enableGlow)
                                        Modifier.neonGlow(
                                            color = lemonYellow,
                                            alpha = 0.3f,
                                            glowRadius = 30.dp
                                        )
                                    else Modifier
                                )
                        )
                    }
                    Image(
                        painter = painterResource(assets.myQrRightDecorIcon),
                        contentDescription = "balance_right_image",
                        modifier = Modifier
                            .size(80.dp)
                            .alpha(if (assets.transactionSuccessfulBalanceDecorVisible) 1f else 0f)
                            .then(
                                if (colors.effect.enableGlow)
                                    Modifier.neonGlow(
                                        color = lemonYellow,
                                        alpha = 0.3f,
                                        glowRadius = 30.dp
                                    )
                                else Modifier
                            )
                            .align(Alignment.CenterVertically)
                    )
                }

                Spacer(Modifier.height(20.dp))
                // balance
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    val leftFire = assets.successLeftFireDecor
                    if (leftFire != null) {
                        Image(
                            painter = painterResource(leftFire),
                            contentDescription = null,
                            modifier = Modifier
                                .size(60.dp)
                                .align(Alignment.CenterVertically)
                        )
                    } else {
                        Spacer(modifier = Modifier.size(60.dp))
                    }
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .align(Alignment.CenterVertically)
                    ) {
                        Text(
                            modifier = Modifier.align(Alignment.CenterHorizontally),
                            text = "Updated Points balance",
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(Modifier.height(8.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 10.dp)
                                .border(
                                    width = 1.5.dp,
                                    color = colors.accent.primary.copy(alpha = 0.5f),
                                    shape = RoundedCornerShape(15.dp)
                                )
                                .then(
                                    if (colors.effect.enableGlow)
                                        Modifier.neonGlow(
                                            color = colors.accent.primary,
                                            alpha = 0.6f,
                                            glowRadius = 8.dp,
                                            borderRadius = 8.dp
                                        )
                                    else Modifier
                                )
                                .background(
                                    color = colors.bg.page,
                                    shape = RoundedCornerShape(15.dp)
                                )
                                .padding(10.dp)
                        ) {
                            Icon(
                                painter = painterResource(assets.balanceCoinIcon),
                                contentDescription = stringResource(id = R.string.balance_coin),
                                tint = Color.Companion.Unspecified,

                                modifier = Modifier.Companion
                                    .then(
                                        if (colors.effect.enableGlow)
                                            Modifier.neonGlow(
                                                color = lemonYellow,
                                                alpha = 0.7f,
                                                glowRadius = 10.dp
                                            )
                                        else Modifier
                                    )
                                    .size(30.dp)
                            )
                            Spacer(Modifier.width(20.dp))
                            Text(
                                textAlign = TextAlign.Center,
                                text = "Balance: ",
                                color = colors.text.body,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                            Text(
                                textAlign = TextAlign.Center,
                                text = formatAmount(uiState.balance),
                                color = colors.scanPay.success.pointsBalanceText,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }
                    }
                    val rightFire = assets.successRightFireDecor
                    if (rightFire != null) {
                        Image(
                            painter = painterResource(rightFire),
                            contentDescription = null,
                            modifier = Modifier
                                .size(60.dp)
                                .align(Alignment.CenterVertically)
                        )
                    } else {
                        Spacer(modifier = Modifier.size(60.dp))
                    }
                }
                Spacer(Modifier.height(20.dp))
                // ── Buttons ───────────────────────────────────────────────────────
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 50.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Done（Neon = neonCyan 填滿；Black Gold = 深藍紫橫向漸層 + 金色邊框）
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(
                                width = 1.5.dp,
                                color = colors.scanPay.success.doneButtonBorder,
                                shape = RoundedCornerShape(25.dp)
                            )
                            .then(
                                if (colors.effect.enableGlow)
                                    Modifier.neonGlow(
                                        color = colors.accent.primary,
                                        alpha = 0.6f,
                                        glowRadius = 25.dp,
                                        borderRadius = 25.dp
                                    )
                                else Modifier
                            )
                            .clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }
                            ) { onBack() }
                            .padding(5.dp)
                            .background(
                                brush = colors.scanPay.success.doneButtonFill,
                                shape = RoundedCornerShape(20.dp)
                            )
                            .padding(vertical = 10.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.transaction_successful_done),
                            color = colors.scanPay.success.doneButtonText,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(Modifier.height(15.dp))
                    // Share My Experience（Neon = outlined；Black Gold = 深紫橫向漸層 + 金色邊框）
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                            .border(
                                width = 1.5.dp,
                                color = colors.scanPay.success.shareButtonBorder,
                                shape = RoundedCornerShape(25.dp)
                            )
                            .then(
                                if (colors.effect.enableGlow)
                                    Modifier.neonGlow(
                                        color = colors.accent.secondary,
                                        alpha = 0.4f,
                                        glowRadius = 25.dp,
                                        borderRadius = 25.dp
                                    )
                                else Modifier
                            )
                            .background(
                                brush = colors.scanPay.success.shareButtonFill,
                                shape = RoundedCornerShape(25.dp)
                            )
                            .clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }
                            ) { onShare() }
                            .padding(vertical = 10.dp)
                    ) {
                        Icon(
                            modifier = Modifier.size(24.dp),
                            imageVector = Icons.Default.Share,
                            tint = colors.scanPay.success.shareIconTint,
                            contentDescription = null
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(
                            text = stringResource(R.string.transaction_successful_share),
                            fontSize = 12.sp,
                            color = colors.scanPay.success.shareText,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                Spacer(modifier = Modifier.weight(1f))
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    assets.scanPayYellowStarDecor?.let { resId ->
                        Image(
                            painter = painterResource(resId),
                            contentDescription = null,
                            modifier = Modifier
                                .then(
                                    if (colors.effect.enableGlow)
                                        Modifier.neonGlow(color = lemonYellow, alpha = 0.1f, glowRadius = 30.dp)
                                    else Modifier
                                )
                        )
                    }
                    assets.scanPayTreeDecor?.let { resId ->
                        Image(
                            painter = painterResource(resId),
                            contentDescription = null,
                            modifier = Modifier
                                .size(80.dp)
                                .then(
                                    if (colors.effect.enableGlow)
                                        Modifier.neonGlow(color = lemonYellow, alpha = 0.2f, glowRadius = 30.dp)
                                    else Modifier
                                )
                        )
                    }
                }
            }
        }
    }
}

@Preview(name = "Neon", showBackground = true, backgroundColor = 0xFF030F1B)
@Composable
private fun TransactionSuccessfulScreenPreviewNeon() {
    AppTheme(colors = NeonColors) {
        TransactionSuccessfulContent(
            uiState = ScanPayUiState(
                recipientNickName = "bruceb",
                recipientName = "Bruce Banner",
                amount = "100.00"
            )
        )
    }
}

@Preview(name = "Black Gold", showBackground = true, backgroundColor = 0xFF050505)
@Composable
private fun TransactionSuccessfulScreenPreviewBlackGold() {
    AppTheme(colors = BlackGoldColors, assets = BlackGoldAssets) {
        TransactionSuccessfulContent(
            uiState = ScanPayUiState(
                recipientNickName = "bruceb",
                recipientName = "Bruce Banner",
                amount = "100.00"
            )
        )
    }
}
