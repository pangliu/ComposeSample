package com.qpay.xcash.ui.transfer.wallet

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
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
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
import com.qpay.xcash.ui.theme.LocalAppAssets
import com.qpay.xcash.ui.theme.LocalAppColors
import com.qpay.xcash.ui.theme.NeonAssets
import com.qpay.xcash.ui.theme.NeonColors

private const val TAB_INSTANT = 0
private const val MODE_MANUAL = 0

@Composable
fun WalletTransferScreen(
    onBack: () -> Unit = {},
    onNext: () -> Unit = {},
    viewModel: WalletTransferViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    WalletTransferContent(uiState = uiState, onBack = onBack, onNext = onNext)
}

@SuppressLint("DefaultLocale")
@Composable
private fun WalletTransferContent(
    uiState: WalletTransferUiState = WalletTransferUiState(),
    onBack: () -> Unit = {},
    onNext: () -> Unit = {}
) {
    val colors = LocalAppColors.current
    val assets = LocalAppAssets.current
    val transferColors = colors.walletTransfer

    var selectedTab by rememberSaveable { mutableIntStateOf(TAB_INSTANT) }
    var selectedMode by rememberSaveable { mutableIntStateOf(MODE_MANUAL) }
    var isBalanceHidden by rememberSaveable { mutableStateOf(false) }
    var accountNo by rememberSaveable { mutableStateOf(uiState.accountNumber) }
    var amount by rememberSaveable { mutableStateOf("") }

    Scaffold(
        containerColor = colors.bg.page,
        contentColor = colors.text.body
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize()) {
            (assets.subPageBackground ?: assets.scanPayBackground)?.let { resId ->
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
                    title = stringResource(R.string.wallet_transfer_title),
                    onBack = onBack,
                    titleBrush = colors.gradient.goldShimmer
                )

                TransferTabs(selectedTab = selectedTab, onTabSelected = { selectedTab = it })

                Spacer(Modifier.height(16.dp))

                val currencySymbol = stringResource(R.string.wallet_transfer_currency_symbol)
                val balanceText = if (isBalanceHidden) "$currencySymbol ••••••"
                else currencySymbol + String.format("%,.0f", uiState.availableBalance)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.wallet_transfer_available_balance, balanceText),
                        color = transferColors.balanceText,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(Modifier.width(8.dp))
                    Icon(
                        imageVector = if (isBalanceHidden) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                        contentDescription = stringResource(R.string.wallet_transfer_balance_toggle_desc),
                        tint = transferColors.balanceIconTint,
                        modifier = Modifier
                            .size(18.dp)
                            .clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }
                            ) { isBalanceHidden = !isBalanceHidden }
                    )
                    Spacer(Modifier.weight(1f))
                    Text(
                        text = stringResource(R.string.wallet_transfer_bonus, uiState.transferBonusRemaining),
                        color = transferColors.bonusText,
                        fontSize = 9.sp
                    )
                }

                Spacer(Modifier.height(12.dp))

                Row(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    ModeChip(
                        text = stringResource(R.string.wallet_transfer_mode_manual),
                        isSelected = selectedMode == MODE_MANUAL,
                        onClick = { selectedMode = MODE_MANUAL }
                    )
                    ModeChip(
                        text = stringResource(R.string.wallet_transfer_mode_friends),
                        isSelected = selectedMode != MODE_MANUAL,
                        onClick = { selectedMode = 1 }
                    )
                }

                Spacer(Modifier.height(16.dp))

                Column(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    TransferField {
                        Text(
                            text = uiState.accountName,
                            color = transferColors.fieldSelectedText,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    TransferField {
                        FieldInput(
                            value = accountNo,
                            onValueChange = { accountNo = it },
                            placeholder = stringResource(R.string.wallet_transfer_account_hint),
                            modifier = Modifier.weight(1f)
                        )
                        Icon(
                            imageVector = Icons.Default.QrCodeScanner,
                            contentDescription = stringResource(R.string.wallet_transfer_scan_desc),
                            tint = transferColors.fieldIconTint,
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    TransferField {
                        FieldInput(
                            value = amount,
                            onValueChange = { newValue -> if (newValue.all(Char::isDigit)) amount = newValue },
                            placeholder = stringResource(R.string.wallet_transfer_amount_hint),
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            text = stringResource(R.string.wallet_transfer_limit),
                            color = transferColors.fieldSideText,
                            fontSize = 10.sp
                        )
                    }
                }

                Spacer(Modifier.height(20.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 28.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = stringResource(R.string.wallet_transfer_fee_label),
                        color = transferColors.feeLabelText,
                        fontSize = 13.sp
                    )
                    Text(
                        text = String.format("%,.2f", uiState.fee.toDouble()),
                        color = transferColors.feeValueText,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(Modifier.weight(1f))

                val isAmountEntered = (amount.toLongOrNull() ?: 0L) > 0L

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 16.dp)
                        .height(52.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(
                            if (isAmountEntered) transferColors.nextButtonEnabledFill
                            else SolidColor(transferColors.nextButtonDisabledFill)
                        )
                        .clickable(
                            enabled = isAmountEntered,
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) { onNext() },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.wallet_transfer_next_button),
                        color = if (isAmountEntered) transferColors.nextButtonEnabledText else transferColors.nextButtonDisabledText,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
private fun TransferTabs(selectedTab: Int, onTabSelected: (Int) -> Unit) {
    val colors = LocalAppColors.current.walletTransfer
    val labels = listOf(
        stringResource(R.string.wallet_transfer_tab_instant),
        stringResource(R.string.wallet_transfer_tab_scheduled)
    )
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            labels.forEachIndexed { index, label ->
                val isSelected = index == selectedTab
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) { onTabSelected(index) }
                        .padding(top = 12.dp),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = label,
                            color = if (isSelected) colors.tabSelectedText else colors.tabUnselectedText,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            textAlign = TextAlign.Center
                        )
                        Spacer(Modifier.height(10.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(2.dp)
                                .then(if (isSelected) Modifier.background(colors.tabIndicator) else Modifier)
                        )
                    }
                }
            }
        }
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(colors.tabDivider)
        )
    }
}

@Composable
private fun ModeChip(text: String, isSelected: Boolean, onClick: () -> Unit) {
    val colors = LocalAppColors.current.walletTransfer
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(if (isSelected) colors.chipSelectedBackground else colors.chipUnselectedBackground)
            .border(
                1.dp,
                if (isSelected) colors.chipSelectedBorder else colors.chipUnselectedBorder,
                RoundedCornerShape(8.dp)
            )
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { onClick() }
            .padding(horizontal = 14.dp, vertical = 8.dp)
    ) {
        Text(
            text = text,
            color = if (isSelected) colors.chipSelectedText else colors.chipUnselectedText,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun TransferField(content: @Composable RowScope.() -> Unit) {
    val colors = LocalAppColors.current.walletTransfer
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(colors.fieldBackground)
            .border(1.dp, colors.fieldBorder, RoundedCornerShape(10.dp))
            .padding(horizontal = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        content = content
    )
}

@Composable
private fun FieldInput(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier
) {
    val colors = LocalAppColors.current.walletTransfer
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        textStyle = TextStyle(color = colors.fieldText, fontSize = 14.sp),
        cursorBrush = SolidColor(colors.fieldText),
        modifier = modifier,
        decorationBox = { innerTextField ->
            Box(contentAlignment = Alignment.CenterStart) {
                if (value.isEmpty()) {
                    Text(
                        text = placeholder,
                        color = colors.fieldPlaceholderText,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
                innerTextField()
            }
        }
    )
}

private val previewUiState = WalletTransferUiState(
    accountName = "Xcash Wallet",
    fee = 0,
    availableBalance = 10166.0
)

@Preview(name = "Neon", showBackground = true, backgroundColor = 0xFF030F1B)
@Composable
private fun WalletTransferScreenPreviewNeon() {
    AppTheme(colors = NeonColors, assets = NeonAssets) {
        WalletTransferContent(uiState = previewUiState)
    }
}

@Preview(name = "Black Gold", showBackground = true, backgroundColor = 0xFF050505)
@Composable
private fun WalletTransferScreenPreviewBlackGold() {
    AppTheme(colors = BlackGoldColors, assets = BlackGoldAssets) {
        WalletTransferContent(uiState = previewUiState)
    }
}
