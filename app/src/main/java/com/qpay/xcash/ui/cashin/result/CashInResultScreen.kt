package com.qpay.xcash.ui.cashin.result

import android.annotation.SuppressLint
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.qpay.xcash.R
import com.qpay.xcash.ui.cashin.CashInResultInfo
import com.qpay.xcash.ui.cashin.CashInUiState
import com.qpay.xcash.ui.cashin.CashInViewModel
import com.qpay.xcash.ui.theme.AppTheme
import com.qpay.xcash.ui.theme.BlackGoldAssets
import com.qpay.xcash.ui.theme.BlackGoldColors
import com.qpay.xcash.ui.theme.LocalAppAssets
import com.qpay.xcash.ui.theme.LocalAppColors
import com.qpay.xcash.ui.theme.NeonAssets
import com.qpay.xcash.ui.theme.NeonColors
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun CashInResultScreen(
    onConfirm: () -> Unit = {},
    onTransactionHistoryClick: () -> Unit = {},
    viewModel: CashInViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    CashInResultContent(
        uiState = uiState,
        onConfirm = onConfirm,
        onTransactionHistoryClick = onTransactionHistoryClick
    )
}

@SuppressLint("SimpleDateFormat")
@Composable
private fun CashInResultContent(
    uiState: CashInUiState = CashInUiState(),
    onConfirm: () -> Unit = {},
    onTransactionHistoryClick: () -> Unit = {}
) {
    val colors = LocalAppColors.current
    val assets = LocalAppAssets.current
    val resultColors = colors.cashInResult
    val result = uiState.topUpResult ?: return
    val isSuccess = result.isSuccess

    Scaffold(
        containerColor = colors.bg.page,
        contentColor = colors.text.body
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize()) {
            val backgroundRes = if (isSuccess) assets.successPageBackground else assets.scanPayBackground
            backgroundRes?.let { resId ->
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
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(Modifier.height(48.dp))

                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .border(
                            1.5.dp,
                            if (isSuccess) resultColors.successIconBorder else resultColors.failIconBorder,
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (isSuccess) Icons.Default.Check else Icons.Default.Close,
                        contentDescription = stringResource(R.string.cash_in_result_status_icon_desc),
                        tint = if (isSuccess) resultColors.successIconTint else resultColors.failIconTint,
                        modifier = Modifier.size(28.dp)
                    )
                }

                Spacer(Modifier.height(16.dp))

                Text(
                    text = stringResource(
                        if (isSuccess) R.string.cash_in_result_success_title else R.string.cash_in_result_failed_title
                    ),
                    color = resultColors.titleText,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.height(12.dp))

                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text = stringResource(R.string.cash_in_currency),
                        color = if (isSuccess) resultColors.successAmountText else resultColors.failAmountText,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        text = String.format("%,.2f", result.amount.toDouble()),
                        color = if (isSuccess) resultColors.successAmountText else resultColors.failAmountText,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(Modifier.height(8.dp))

                val dateText = remember(result.timestampMillis) {
                    SimpleDateFormat("yyyy/M/d HH:mm", Locale.getDefault()).format(Date(result.timestampMillis))
                }
                Text(
                    text = stringResource(
                        if (isSuccess) R.string.cash_in_result_success_subtitle else R.string.cash_in_result_failed_subtitle,
                        dateText
                    ),
                    color = resultColors.subtitleText,
                    fontSize = 12.sp
                )

                if (!isSuccess && result.failureReason.isNotEmpty()) {
                    Text(
                        text = result.failureReason,
                        color = resultColors.failureReasonText,
                        fontSize = 12.sp
                    )
                }

                Spacer(Modifier.height(28.dp))

                InfoRow(
                    icon = Icons.Default.AccountBalance,
                    label = result.bankName.ifEmpty { "-" },
                    subLabel = "**** ${result.cardNumber}"
                )

                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = stringResource(R.string.cash_in_result_transfer_arrow_desc),
                    tint = resultColors.transferArrowTint,
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                        .size(20.dp)
                        .rotate(90f)
                )

                InfoRow(
                    icon = Icons.Default.AccountBalanceWallet,
                    label = stringResource(R.string.cash_in_result_wallet_label),
                    subLabel = stringResource(R.string.cash_in_result_wallet_subtitle)
                )

                Spacer(Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = stringResource(R.string.cash_in_result_balance_after_label),
                        color = resultColors.balanceLabelText,
                        fontSize = 13.sp
                    )
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "${stringResource(R.string.cash_in_currency)} ${String.format("%,.0f", result.balanceAfter)}",
                            color = resultColors.balanceValueText,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = stringResource(R.string.cash_in_result_transaction_history),
                            color = resultColors.transactionHistoryLinkText,
                            fontSize = 12.sp,
                            textDecoration = TextDecoration.Underline,
                            modifier = Modifier.clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }
                            ) { onTransactionHistoryClick() }
                        )
                    }
                }

                Spacer(Modifier.height(12.dp))

                Text(
                    text = stringResource(R.string.cash_in_result_ref_no, result.transactionId),
                    color = resultColors.refNoText,
                    fontSize = 12.sp,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.weight(1f))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp)
                        .height(52.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(resultColors.confirmButtonFill)
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) { onConfirm() },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.cash_in_result_confirm_button),
                        color = resultColors.confirmButtonText,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
private fun InfoRow(icon: ImageVector, label: String, subLabel: String) {
    val resultColors = LocalAppColors.current.cashInResult
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(resultColors.cardRowBackground)
            .border(1.dp, resultColors.cardRowBorder, RoundedCornerShape(12.dp))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(resultColors.cardIconBackground),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = resultColors.transferArrowTint,
                modifier = Modifier.size(20.dp)
            )
        }
        Spacer(Modifier.width(12.dp))
        Column {
            Text(
                text = label,
                color = resultColors.cardLabelText,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = subLabel,
                color = resultColors.cardSubText,
                fontSize = 12.sp
            )
        }
    }
}

private val previewSuccessResult = CashInResultInfo(
    isSuccess = true,
    amount = 1_000_000L,
    bankName = "BDO Unibank",
    cardNumber = "1234",
    balanceAfter = 10166.0,
    transactionId = "01234567891234"
)

private val previewFailedResult = previewSuccessResult.copy(
    isSuccess = false,
    failureReason = "Insufficient available balance."
)

@Preview(name = "Black Gold - Success", showBackground = true, backgroundColor = 0xFF050505)
@Composable
private fun CashInResultScreenPreviewSuccess() {
    AppTheme(colors = BlackGoldColors, assets = BlackGoldAssets) {
        CashInResultContent(uiState = CashInUiState(topUpResult = previewSuccessResult))
    }
}

@Preview(name = "Black Gold - Failed", showBackground = true, backgroundColor = 0xFF050505)
@Composable
private fun CashInResultScreenPreviewFailed() {
    AppTheme(colors = BlackGoldColors, assets = BlackGoldAssets) {
        CashInResultContent(uiState = CashInUiState(topUpResult = previewFailedResult))
    }
}

@Preview(name = "Neon - Success", showBackground = true, backgroundColor = 0xFF030F1B)
@Composable
private fun CashInResultScreenPreviewNeonSuccess() {
    AppTheme(colors = NeonColors, assets = NeonAssets) {
        CashInResultContent(uiState = CashInUiState(topUpResult = previewSuccessResult))
    }
}

@Preview(name = "Neon - Failed", showBackground = true, backgroundColor = 0xFF030F1B)
@Composable
private fun CashInResultScreenPreviewNeonFailed() {
    AppTheme(colors = NeonColors, assets = NeonAssets) {
        CashInResultContent(uiState = CashInUiState(topUpResult = previewFailedResult))
    }
}
