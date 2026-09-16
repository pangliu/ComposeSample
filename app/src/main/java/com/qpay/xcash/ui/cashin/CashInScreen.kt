package com.qpay.xcash.ui.cashin

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.material.icons.automirrored.filled.Backspace
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.FragmentActivity
import androidx.hilt.navigation.compose.hiltViewModel
import com.qpay.xcash.R
import com.qpay.xcash.network.model.response.CreditCardResponse
import com.qpay.xcash.ui.Routes
import com.qpay.xcash.ui.UiEvent
import com.qpay.xcash.ui.components.LoadingDialog
import com.qpay.xcash.ui.components.SubPageTopBar
import com.qpay.xcash.ui.theme.AppTheme
import com.qpay.xcash.ui.theme.BlackGoldAssets
import com.qpay.xcash.ui.theme.BlackGoldColors
import com.qpay.xcash.ui.theme.LocalAppColors
import com.qpay.xcash.ui.theme.NeonAssets
import com.qpay.xcash.ui.theme.NeonColors
import com.qpay.xcash.utils.BiometricHelper

private val quickAmounts = listOf(100L, 5000L, 3000L)

private val keypadKeys = listOf(
    "1", "2", "3",
    "4", "5", "6",
    "7", "8", "9",
    "00", "0", "⌫"
)

@Composable
fun CashInScreen(
    onBack: () -> Unit = {},
    onNavigate: (String) -> Unit = {},
    viewModel: CashInViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.eventFlow.collect { event ->
            when (event) {
                is UiEvent.ShowToast -> Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                is UiEvent.ShowDialog -> Unit
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.navigationEvent.collect { event ->
            when (event) {
                is CashInNavigationEvent.NavigateToResult -> onNavigate(Routes.CASH_IN_RESULT)
            }
        }
    }

    CashInContent(
        uiState = uiState,
        onBack = onBack,
        onNavigate = onNavigate,
        onTopUpClick = { amount ->
            // allowDeviceCredential = true：有生物辨識就跳生物辨識，沒有就自動退回手機系統 PIN/圖案/密碼，交給系統處理
            BiometricHelper.showPrompt(
                activity = context as FragmentActivity,
                title = context.getString(R.string.biometric_prompt_cashin_title),
                subtitle = context.getString(R.string.biometric_prompt_cashin_subtitle),
                allowDeviceCredential = true,
                onSuccess = { viewModel.submitTopUp(amount) },
                onError = { message -> Toast.makeText(context, message, Toast.LENGTH_SHORT).show() }
            )
        }
    )
}

@SuppressLint("DefaultLocale")
@Composable
private fun CashInContent(
    uiState: CashInUiState = CashInUiState(),
    onBack: () -> Unit = {},
    onNavigate: (String) -> Unit = {},
    onTopUpClick: (Long) -> Unit = {}
) {
    val colors = LocalAppColors.current
    var amount by rememberSaveable { mutableLongStateOf(0L) }

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
                title = stringResource(R.string.cash_in_title),
                onBack = onBack,
                titleBrush = colors.gradient.goldShimmer
            )

            Spacer(Modifier.height(15.dp))

            TransferRow(
                bankName = uiState.displayBankName,
                cardNumber = uiState.displayCardNumber,
                hasBankCard = uiState.hasBankCardToDisplay,
                onEditClick = { onNavigate(Routes.PAYMENT_METHOD) }
            )

            Spacer(Modifier.height(30.dp))

            AmountSection(amount = amount, cashBalance = uiState.cashBalance)

            Spacer(Modifier.height(16.dp))

            QuickAmountRow(
                onQuickAmountClick = { quickAmount ->
                    amount = (amount + quickAmount).coerceAtMost(MAX_AMOUNT)
                }
            )

            Spacer(Modifier.height(12.dp))

            CashInKeypad(
                modifier = Modifier.weight(1f),
                onDigit = { digit ->
                    if (digitCount(amount) < MAX_DIGITS) {
                        amount = amount * 10 + digit
                    }
                },
                onDoubleZero = {
                    if (digitCount(amount) + 2 <= MAX_DIGITS) {
                        amount *= 100
                    }
                },
                onBackspace = {
                    amount /= 10
                }
            )

            TopUpButton(
                isEnabled = amount > 0,
                onClick = { onTopUpClick(amount) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp)
            )
        }
    }

    LoadingDialog(isShowing = uiState.isLoading)
}

private const val MAX_DIGITS = 7
private const val MAX_AMOUNT = 9_999_999L

private fun digitCount(amount: Long): Int = if (amount == 0L) 0 else amount.toString().length

private fun formatBankLabel(bankName: String, cardNumber: String): String {
    val cleanNumber = cardNumber.replace(" ", "").replace("-", "")
    val last4 = if (cleanNumber.length >= 4) cleanNumber.takeLast(4) else cleanNumber
    return "${bankName.uppercase()} ****$last4"
}

@Composable
private fun TransferRow(
    bankName: String = "",
    cardNumber: String = "",
    hasBankCard: Boolean = false,
    onEditClick: () -> Unit = {}
) {
    val colors = LocalAppColors.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 30.dp, vertical = 12.dp),
    ) {
        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.Center
        ) {
            if (hasBankCard) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(colors.cashIn.bankIconBackground),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AccountBalance,
                            contentDescription = null,
                            tint = colors.cashIn.bankIconTint,
                            modifier = Modifier.size(30.dp)
                        )
                    }
                    Spacer(Modifier.height(6.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }
                            ) { onEditClick() }
                    ) {
                        Text(
                            text = formatBankLabel(bankName, cardNumber),
                            color = colors.cashIn.bankLabelText,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f, fill = false)
                        )
                        Spacer(Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = stringResource(R.string.cash_in_bank_select_desc),
                            tint = colors.cashIn.bankChevronTint,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }

        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
            contentDescription = stringResource(R.string.cash_in_transfer_arrow_desc),
            tint = colors.cashIn.transferArrowTint,
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .size(35.dp)
        )

        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(colors.cashIn.walletIconBackground),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.AccountBalanceWallet,
                        contentDescription = null,
                        tint = colors.cashIn.walletIconTint,
                        modifier = Modifier.size(30.dp)
                    )
                }
                Spacer(Modifier.height(6.dp))
                Text(
                    text = stringResource(R.string.cash_in_wallet_label),
                    color = colors.cashIn.walletLabelText,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@SuppressLint("DefaultLocale")
@Composable
private fun AmountSection(amount: Long, cashBalance: Double) {
    val colors = LocalAppColors.current
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(verticalAlignment = Alignment.Bottom) {
            Text(
                text = stringResource(R.string.cash_in_currency),
                color = colors.cashIn.amountCurrencyText,
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.width(8.dp))
            Text(
                text = if (amount == 0L)
                    stringResource(R.string.cash_in_amount_hint)
                else
                    String.format("%,d", amount),
                color = if (amount == 0L) colors.cashIn.amountPlaceholderText else colors.cashIn.amountValueText,
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(Modifier.height(40.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(R.string.cash_in_current_balance_label),
                color = colors.cashIn.currentBalanceLabelText,
                fontSize = 13.sp
            )
            Text(
                text = stringResource(R.string.cash_in_currency) + " " + String.format("%,.0f", cashBalance),
                color = colors.cashIn.currentBalanceValueText,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun QuickAmountRow(onQuickAmountClick: (Long) -> Unit) {
    val colors = LocalAppColors.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        quickAmounts.forEach { amount ->
            Box(
                modifier = Modifier
                    .weight(1f)
                    .background(colors.cashIn.quickAmountChipBackground, RoundedCornerShape(10.dp))
                    .border(1.dp, colors.cashIn.quickAmountChipBorder, RoundedCornerShape(10.dp))
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) { onQuickAmountClick(amount) }
                    .padding(vertical = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "+${String.format("%,d", amount)}",
                    color = colors.cashIn.quickAmountChipText,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun CashInKeypad(
    modifier: Modifier = Modifier,
    onDigit: (Int) -> Unit,
    onDoubleZero: () -> Unit,
    onBackspace: () -> Unit
) {
    val colors = LocalAppColors.current
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        keypadKeys.chunked(3).forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                row.forEach { key ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(2f)
                            .clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }
                            ) {
                                when (key) {
                                    "⌫" -> onBackspace()
                                    "00" -> onDoubleZero()
                                    else -> onDigit(key.toInt())
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        if (key == "⌫") {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.Backspace,
                                contentDescription = stringResource(R.string.cash_in_backspace_desc),
                                tint = colors.cashIn.keypadBackspaceIconTint,
                                modifier = Modifier.size(24.dp)
                            )
                        } else {
                            Text(
                                text = key,
                                color = colors.cashIn.keypadNumberText,
                                fontSize = 30.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TopUpButton(isEnabled: Boolean, onClick: () -> Unit = {}, modifier: Modifier = Modifier) {
    val colors = LocalAppColors.current
    Box(
        modifier = modifier
            .height(52.dp)
            .background(
                brush = if (isEnabled) colors.cashIn.topUpButtonEnabledFill
                else SolidColor(colors.cashIn.topUpButtonDisabledFill),
                shape = RoundedCornerShape(10.dp)
            )
            .clickable(
                enabled = isEnabled,
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(R.string.cash_in_top_up_button),
            color = if (isEnabled) colors.cashIn.topUpButtonEnabledText else colors.cashIn.topUpButtonDisabledText,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

private val previewCard = CreditCardResponse(
    id = 1,
    cardType = "visa",
    cardName = "Sample Card",
    cardNumber = "0000",
    bankName = "sample bank",
    isPrimary = true
)

private val previewUiState = CashInUiState(
    isLoadingCards = false,
    cards = listOf(previewCard),
    selectedCardId = previewCard.id,
    cashBalance = 12500.0
)

@Preview(name = "Neon", showBackground = true, backgroundColor = 0xFF030F1B)
@Composable
private fun CashInScreenPreviewNeon() {
    AppTheme(colors = NeonColors, assets = NeonAssets) {
        CashInContent(uiState = previewUiState)
    }
}

@Preview(name = "Black Gold", showBackground = true, backgroundColor = 0xFF050505)
@Composable
private fun CashInScreenPreviewBlackGold() {
    AppTheme(colors = BlackGoldColors, assets = BlackGoldAssets) {
        CashInContent(uiState = previewUiState)
    }
}
