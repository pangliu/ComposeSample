package com.qpay.xcash.ui.transfer.confirm

import android.annotation.SuppressLint
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.qpay.xcash.R
import com.qpay.xcash.ui.components.SubPageTopBar
import com.qpay.xcash.ui.theme.AppTheme
import com.qpay.xcash.ui.theme.BlackGoldAssets
import com.qpay.xcash.ui.theme.BlackGoldColors
import com.qpay.xcash.ui.theme.ConfirmTransferColors
import com.qpay.xcash.ui.theme.LocalAppAssets
import com.qpay.xcash.ui.theme.LocalAppColors
import com.qpay.xcash.ui.theme.NeonAssets
import com.qpay.xcash.ui.theme.NeonColors
import com.qpay.xcash.ui.transfer.wallet.WalletTransferUiState
import com.qpay.xcash.ui.transfer.wallet.WalletTransferViewModel

@Composable
fun ConfirmTransferScreen(
    onBack: () -> Unit = {},
    onNext: () -> Unit = {},
    viewModel: WalletTransferViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    ConfirmTransferContent(uiState = uiState, onBack = onBack, onNext = onNext)
}

@SuppressLint("DefaultLocale")
@Composable
private fun ConfirmTransferContent(
    uiState: WalletTransferUiState = WalletTransferUiState(),
    onBack: () -> Unit = {},
    onNext: () -> Unit = {}
) {
    val colors = LocalAppColors.current
    val assets = LocalAppAssets.current
    val confirmColors = colors.confirmTransfer

    val amountValue = uiState.amount.toLongOrNull() ?: 0L
    val totalValue = amountValue + uiState.fee
    val currencySymbol = stringResource(R.string.confirm_transfer_currency_symbol)

    Scaffold(
        containerColor = colors.bg.page,
        contentColor = colors.text.body
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            SubPageTopBar(
                title = stringResource(R.string.confirm_transfer_title),
                onBack = onBack,
                titleBrush = colors.gradient.goldShimmer
            )

            Text(
                text = stringResource(R.string.confirm_transfer_subtitle),
                color = confirmColors.subtitleText,
                fontSize = 13.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            )

            Spacer(Modifier.height(20.dp))

            AccountSummaryCard(
                iconRes = assets.confirmTransferAccountIcon,
                name = uiState.selfDefaultBankName.replaceFirstChar { it.uppercase() },
                subText = if (uiState.selfDefaultCardNumber.isEmpty()) "" else "**** ${uiState.selfDefaultCardNumber}",
                colors = confirmColors,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            )

            Spacer(Modifier.height(20.dp))

            Text(
                text = stringResource(R.string.confirm_transfer_pay_label),
                color = confirmColors.sectionLabelText,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(R.string.confirm_transfer_currency),
                    color = confirmColors.amountText,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = String.format("%,.2f", amountValue.toDouble()),
                    color = confirmColors.amountText,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(Modifier.height(14.dp))

            Text(
                text = stringResource(R.string.confirm_transfer_to_label),
                color = confirmColors.sectionLabelText,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(14.dp))

            AccountSummaryCard(
                iconRes = assets.confirmTransferAccountIcon,
                name = uiState.accountName,
                subText = uiState.accountNumber,
                colors = confirmColors,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            )

            Spacer(Modifier.height(24.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 28.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = stringResource(R.string.confirm_transfer_fee_label),
                        color = confirmColors.feeLabelText,
                        fontSize = 13.sp
                    )
                    Text(
                        text = uiState.fee.toString(),
                        color = confirmColors.feeValueText,
                        fontSize = 13.sp
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = stringResource(R.string.confirm_transfer_total_payment_label),
                        color = confirmColors.feeLabelText,
                        fontSize = 13.sp
                    )
                    Text(
                        text = "$currencySymbol ${String.format("%,d", totalValue)}",
                        color = confirmColors.feeValueText,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(Modifier.weight(1f))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp)
                    .height(52.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(confirmColors.nextButtonFill)
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) { onNext() },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.confirm_transfer_next_button),
                    color = confirmColors.nextButtonText,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun AccountSummaryCard(
    iconRes: Int,
    name: String,
    subText: String,
    colors: ConfirmTransferColors,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(colors.cardBackground)
            .border(1.5.dp, colors.cardBorder, RoundedCornerShape(16.dp))
            .padding(vertical = 18.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(colors.iconBackground),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(iconRes),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier.size(26.dp)
            )
        }

        Spacer(Modifier.height(10.dp))

        Text(
            text = name,
            color = colors.nameText,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold
        )

        if (subText.isNotEmpty()) {
            Spacer(Modifier.height(4.dp))
            Text(
                text = subText,
                color = colors.subText,
                fontSize = 12.sp
            )
        }
    }
}

private val previewUiState = WalletTransferUiState(
    accountName = "I. Torres",
    accountNumber = "091*****687",
    amount = "500",
    fee = 0,
    selfDefaultBankName = "gcash",
    selfDefaultCardNumber = "5353"
)

@Preview(name = "Neon", showBackground = true, backgroundColor = 0xFF030F1B)
@Composable
private fun ConfirmTransferScreenPreviewNeon() {
    AppTheme(colors = NeonColors, assets = NeonAssets) {
        ConfirmTransferContent(uiState = previewUiState)
    }
}

@Preview(name = "Black Gold", showBackground = true, backgroundColor = 0xFF050505)
@Composable
private fun ConfirmTransferScreenPreviewBlackGold() {
    AppTheme(colors = BlackGoldColors, assets = BlackGoldAssets) {
        ConfirmTransferContent(uiState = previewUiState)
    }
}
