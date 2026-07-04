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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
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
import com.qpay.xcash.ui.theme.LocalAppColors
import com.qpay.xcash.ui.theme.neonCyan
import com.qpay.xcash.ui.theme.neonCyanLight
import com.qpay.xcash.ui.theme.neonGreen
import com.qpay.xcash.ui.theme.neonMint
import com.qpay.xcash.ui.theme.neonPurpleLight


private val lemonYellow = Color(0xFFFEF27C)
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
    Scaffold(
        containerColor = colors.bg.page,
        contentColor = Color.White
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Box(modifier = Modifier.fillMaxWidth()) {
                Image(
                    modifier = Modifier.matchParentSize(),
                    contentDescription = null,
                    contentScale = ContentScale.FillWidth,
                    painter = painterResource(R.mipmap.bg_success_payment),
                    alignment = Alignment.TopCenter
                )
                Column {
                    SubPageTopBar(
                        title = stringResource(R.string.transaction_successful_title),
                        onBack = onBack,
                        showBack = false
                    )
                    Spacer(Modifier.height(40.dp))
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                modifier = Modifier.weight(0.15f),
                                painter = painterResource(R.mipmap.bg_left_qrcode),
                                contentScale = ContentScale.FillWidth,
                                alignment = Alignment.CenterEnd,
                                contentDescription = stringResource(R.string.scan_pay_my_qr_left_qr_code_desc)
                            )
                            Column(
                                modifier = Modifier
                                    .testTag("transaction_successful")
                                    //                        .border(width = 1.5.dp, color = neonCyan, shape = RoundedCornerShape(15.dp))
                                    .neonGlow(
                                        color = colors.accent.primary,
                                        alpha = 0.5f,
                                        glowRadius = 20.dp,
                                        borderRadius = 20.dp
                                    )
                                    //                        .background(color = slateGray, shape = RoundedCornerShape(15.dp))
                                    .paint(
                                        painter = painterResource(R.mipmap.bg_success_payment_border),
                                        contentScale = ContentScale.FillWidth
                                    )
                                    .align(Alignment.CenterVertically)
                                    .weight(0.7f)
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
                                            .neonGlow(
                                                color = colors.accent.primary,
                                                alpha = 0.6f,
                                                glowRadius = 8.dp,
                                                borderRadius = 8.dp
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
                                            color = neonCyanLight,
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            text = uiState.recipientName,
                                            color = neonPurpleLight,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Normal
                                        )
                                    }
                                }
                                Spacer(Modifier.height(15.dp))
                                Spacer(modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 30.dp)
                                    .height(1.5.dp)
                                    .background(neonCyan))
                                Spacer(Modifier.height(8.dp))
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
                                        style = TextStyle(
                                            shadow = Shadow(
                                                color = colors.accent.primary,
                                                blurRadius = 15f
                                            )
                                        )
                                    )
                                    Spacer(Modifier.width(5.dp))
                                    Text(
                                        text = formatAmount(uiState.amount.toDoubleOrNull() ?: 0.0),
                                        color = colors.accent.primary,
                                        fontSize = 32.sp,
                                        fontWeight = FontWeight.Bold,
                                        style = TextStyle(
                                            shadow = Shadow(
                                                color = colors.accent.primary,
                                                blurRadius = 15f
                                            )
                                        )
                                    )
                                }
                                Spacer(Modifier.height(8.dp))
                                Spacer(modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 30.dp)
                                    .height(1.5.dp)
                                    .background(neonCyan))
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
                                            color = neonMint,
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
                                                color = neonMint,
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
                                                            color = neonMint,
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
                                            color = neonMint,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            lineHeight = 15.sp
                                        )
                                    }
                                }
                            }
                            Image(
                                modifier = Modifier.weight(0.15f),
                                painter = painterResource(R.mipmap.bg_right_qrcode),
                                contentScale = ContentScale.FillWidth,
                                alignment = Alignment.CenterStart,
                                contentDescription = stringResource(R.string.scan_pay_my_qr_right_qr_code_desc)
                            )
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
                                color = neonCyan,
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
                    painter = painterResource(R.mipmap.ic_car),
                    contentDescription = null,
                    modifier = Modifier
                        .size(80.dp)
                        .neonGlow(
                            color = colors.accent.secondary,
                            alpha = 0.25f,
                            glowRadius = 30.dp
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
                        .neonGlow(
                            color = neonCyan.copy(0.7f),
                        )
                        .background(
                            color = neonCyan.copy(0.6f)
                        )
                    )
                    Spacer(Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "PAYING FROM:", color = colors.text.body, fontSize = 14.sp)
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
                            color = colors.text.body,
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
                    Spacer(modifier = Modifier
                        .fillMaxWidth()
                        .height(1.5.dp)

                        .background(
                            color = neonCyan.copy(0.6f)
                        )
                        .neonGlow(color = lemonYellow, alpha = 0.3f, glowRadius = 30.dp))
                }
                Image(
                    painter = painterResource(R.mipmap.ic_monkey),
                    contentDescription = null,
                    modifier = Modifier
                        .size(60.dp)
                        .neonGlow(color = lemonYellow, alpha = 0.3f, glowRadius = 30.dp)
                        .align(Alignment.CenterVertically)
                )
            }

            Spacer(Modifier.height(20.dp))
            // balance
            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Image(
                    painter = painterResource(R.mipmap.ic_left_sigal_fire),
                    contentDescription = null,
                    modifier = Modifier
                        .size(60.dp)
                        .align(Alignment.CenterVertically)
                )
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
                            .neonGlow(
                                color = colors.accent.primary,
                                alpha = 0.6f,
                                glowRadius = 8.dp,
                                borderRadius = 8.dp
                            )
                            .background(
                                color = colors.bg.page,
                                shape = RoundedCornerShape(15.dp)
                            )
                            .padding(10.dp)
                    ) {
                        Icon(
                            painter = painterResource(R.mipmap.ic_balance_coin),
                            contentDescription = stringResource(id = R.string.balance_coin),
                            tint = Color.Companion.Unspecified,

                            modifier = Modifier.Companion
                                .neonGlow(
                                    color = lemonYellow,
                                    alpha = 0.7f,
                                    glowRadius = 10.dp
                                )
                                .size(30.dp)
                        )
                        Spacer(Modifier.width(20.dp))
                        Text(
//                            modifier = Modifier
//                                .fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            text = "Balance: ",
                            color = colors.text.body,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                        Text(
//                            modifier = Modifier
//                                .fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            text = formatAmount(uiState.balance),
                            color = neonCyan,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                }
                Image(
                    painter = painterResource(R.mipmap.ic_right_sigal_fire),
                    contentDescription = null,
                    modifier = Modifier
                        .size(60.dp)
                        .align(Alignment.CenterVertically)
                )
            }
            Spacer(Modifier.height(20.dp))
            // ── Buttons ───────────────────────────────────────────────────────
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 50.dp),
                horizontalAlignment = Alignment.CenterHorizontally
//                verticalAlignment = Alignment.CenterVertically,
//                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Done (neonCyan filled — same as CONFIRM & PAY)
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            width = 1.5.dp,
                            color = colors.accent.primary,
                            shape = RoundedCornerShape(25.dp)
                        )
                        .neonGlow(
                            color = colors.accent.primary,
                            alpha = 0.6f,
                            glowRadius = 25.dp,
                            borderRadius = 25.dp
                        )
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) { onBack() }
                        .padding(5.dp)
                        .background(
                            color = colors.accent.primary,
                            shape = RoundedCornerShape(20.dp)
                        )
                        .padding(vertical = 10.dp)
                ) {
                    Text(
                        text = stringResource(R.string.transaction_successful_done),
                        color = Color.Black,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(Modifier.height(15.dp))
                // Share My Experience (neonPurple outlined — same as CANCEL)
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .border(
                            width = 1.5.dp,
                            color = colors.accent.secondary,
                            shape = RoundedCornerShape(25.dp)
                        )
                        .neonGlow(
                            color = colors.accent.secondary,
                            alpha = 0.4f,
                            glowRadius = 25.dp,
                            borderRadius = 25.dp
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
                        tint = colors.accent.secondary,
                        contentDescription = null
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        text = stringResource(R.string.transaction_successful_share),
                        fontSize = 12.sp,
                        color = neonPurpleLight,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0B1327)
@Composable
private fun TransactionSuccessfulScreenPreview() {
    AppTheme {
        TransactionSuccessfulContent(
            uiState = ScanPayUiState(
                recipientNickName = "bruceb",
                recipientName = "Bruce Banner",
                amount = "100.00"
            )
        )
    }
}
