package com.qpay.xcash.ui.transfer

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.qpay.xcash.R
import com.qpay.xcash.network.model.response.ContactType
import com.qpay.xcash.network.model.response.CreditCardResponse
import com.qpay.xcash.network.model.response.FriendResponse
import com.qpay.xcash.ui.UiEvent
import com.qpay.xcash.ui.components.LoadingDialog
import com.qpay.xcash.ui.components.SubPageTopBar
import com.qpay.xcash.ui.theme.AppTheme
import com.qpay.xcash.ui.theme.BlackGoldAssets
import com.qpay.xcash.ui.theme.BlackGoldColors
import com.qpay.xcash.ui.theme.LocalAppAssets
import com.qpay.xcash.ui.theme.LocalAppColors
import com.qpay.xcash.ui.theme.NeonAssets
import com.qpay.xcash.ui.theme.NeonColors

@Composable
fun GeneralTransferScreen(
    onBack: () -> Unit = {},
    onNext: (accountName: String, fee: Int, accountNumber: String) -> Unit = { _, _, _ -> },
    onManualInput: () -> Unit = {},
    viewModel: GeneralTransferViewModel = hiltViewModel()
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

    GeneralTransferContent(
        uiState = uiState,
        onBack = onBack,
        onSelectAccount = viewModel::selectAccount,
        onManualInput = onManualInput,
        onNext = onNext
    )
}

@Composable
private fun GeneralTransferContent(
    uiState: GeneralTransferUiState = GeneralTransferUiState(),
    onBack: () -> Unit = {},
    onSelectAccount: (Int?) -> Unit = {},
    onManualInput: () -> Unit = {},
    onNext: (accountName: String, fee: Int, accountNumber: String) -> Unit = { _, _, _ -> }
) {
    val colors = LocalAppColors.current
    val assets = LocalAppAssets.current
    val transferColors = colors.generalTransfer

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
                    title = stringResource(R.string.general_transfer_title),
                    onBack = onBack,
                    titleBrush = colors.gradient.goldShimmer
                )

                RecipientCard(
                    recipient = uiState.recipient,
                    fallbackPhoneNumber = uiState.scannedPhoneNumber,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                )

                Spacer(Modifier.height(16.dp))

                Text(
                    text = stringResource(R.string.general_transfer_select_account_hint),
                    color = transferColors.hintText,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(vertical = 10.dp)
                ) {
                    item(key = "xcash_wallet") {
                        AccountRow(
                            icon = Icons.Default.AccountBalanceWallet,
                            name = stringResource(R.string.general_transfer_xcash_wallet),
                            numberText = null,
                            subText = stringResource(R.string.general_transfer_no_fee),
                            isSelected = uiState.selectedCardId == null,
                            isExpired = false,
                            onClick = { onSelectAccount(null) }
                        )
                    }
                    items(uiState.cards, key = { it.id }) { card ->
                        AccountRow(
                            icon = Icons.Default.AccountBalance,
                            name = card.bankName.replaceFirstChar { it.uppercase() },
                            numberText = "**** ${card.cardNumber}",
                            subText = if (card.fee > 0) {
                                stringResource(R.string.general_transfer_fee, card.fee)
                            } else {
                                stringResource(R.string.general_transfer_no_fee)
                            },
                            isSelected = card.id == uiState.selectedCardId,
                            isExpired = card.isExpired,
                            onClick = { onSelectAccount(card.id) }
                        )
                    }
                }

                ManualInputRow(
                    onClick = onManualInput,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                )

                val walletName = stringResource(R.string.general_transfer_xcash_wallet)
                NextButton(
                    onClick = {
                        val card = uiState.cards.firstOrNull { it.id == uiState.selectedCardId }
                        if (card == null) {
                            onNext(walletName, 0, uiState.selfPhoneNumber)
                        } else {
                            onNext(card.bankName.replaceFirstChar { it.uppercase() }, card.fee, "**** ${card.cardNumber}")
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 16.dp)
                )
            }
        }
    }

    LoadingDialog(isShowing = uiState.isLoading)
}

private fun maskPhone(phone: String?): String {
    if (phone.isNullOrEmpty()) return ""
    return if (phone.length >= 7) phone.take(3) + "****" + phone.takeLast(3) else phone
}

@Composable
private fun RecipientCard(
    recipient: FriendResponse?,
    fallbackPhoneNumber: String,
    modifier: Modifier = Modifier
) {
    val colors = LocalAppColors.current.generalTransfer
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(colors.recipientCardBackground)
            .border(1.5.dp, colors.recipientCardBorder, RoundedCornerShape(16.dp))
            .padding(vertical = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(colors.avatarBackground, CircleShape)
                .border(1.5.dp, colors.avatarBorder, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(R.string.general_transfer_avatar_label),
                color = colors.avatarText,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(Modifier.height(10.dp))

        Text(
            text = recipient?.name.orEmpty(),
            color = colors.recipientNameText,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(4.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = maskPhone(recipient?.phoneNumber?.takeIf { it.isNotEmpty() } ?: fallbackPhoneNumber),
                color = colors.recipientPhoneText,
                fontSize = 12.sp
            )
            recipient?.tagLabel?.let { tag ->
                Spacer(Modifier.width(8.dp))
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(colors.tagBackground)
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = tag,
                        color = colors.tagText,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Composable
private fun AccountRow(
    icon: ImageVector,
    name: String,
    numberText: String?,
    subText: String,
    isSelected: Boolean,
    isExpired: Boolean,
    onClick: () -> Unit
) {
    val colors = LocalAppColors.current.generalTransfer
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(if (isSelected) colors.accountRowSelectedBackground else colors.accountRowBackground)
            .border(
                width = 1.5.dp,
                brush = if (isSelected) colors.accountRowSelectedBorder else SolidColor(colors.accountRowBorder),
                shape = RoundedCornerShape(12.dp)
            )
            .clickable(
                enabled = !isExpired,
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { onClick() }
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(colors.accountIconBackground),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = colors.accountIconTint,
                modifier = Modifier.size(28.dp)
            )
        }

        Spacer(Modifier.width(12.dp))

        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = name,
                    color = colors.accountNameText,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
                numberText?.let {
                    Spacer(Modifier.width(6.dp))
                    Text(text = it, color = colors.accountNumberText, fontSize = 13.sp)
                }
            }
            if (isExpired) {
                Text(
                    text = stringResource(R.string.general_transfer_expired_hint),
                    color = colors.expiredText,
                    fontSize = 12.sp
                )
            } else {
                Text(text = subText, color = colors.feeText, fontSize = 12.sp)
            }
        }
    }
}

@Composable
private fun ManualInputRow(onClick: () -> Unit, modifier: Modifier = Modifier) {
    val colors = LocalAppColors.current.generalTransfer
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(colors.manualInputBackground)
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(colors.manualInputIconBackground),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "🤔", fontSize = 16.sp)
        }

        Spacer(Modifier.width(10.dp))

        Text(
            text = stringResource(R.string.general_transfer_manual_input_hint),
            color = colors.manualInputText,
            fontSize = 13.sp,
            modifier = Modifier.weight(1f)
        )

        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
                .border(1.dp, colors.manualInputBorder, RoundedCornerShape(20.dp))
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) { onClick() }
                .padding(horizontal = 14.dp, vertical = 8.dp)
        ) {
            Text(
                text = stringResource(R.string.general_transfer_manual_input_button),
                color = colors.manualInputText,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun NextButton(onClick: () -> Unit, modifier: Modifier = Modifier) {
    val colors = LocalAppColors.current.generalTransfer
    Box(
        modifier = modifier
            .height(52.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(colors.nextButtonFill)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(R.string.general_transfer_next_button),
            color = colors.nextButtonText,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

private val previewUiState = GeneralTransferUiState(
    isLoadingCards = false,
    recipient = FriendResponse(
        id = "F001",
        name = "I. Torres",
        nickName = "itorres",
        contactType = ContactType.PHONE_NUM,
        avatarUrl = "",
        isFavorite = false,
        tagLabel = "Family",
        phoneNumber = "0911234687"
    ),
    cards = listOf(
        CreditCardResponse(1, "Visa", "Main", "1234", "BDO Unibank", fee = 10),
        CreditCardResponse(2, "Visa", "Travel", "5678", "BDO Unibank", fee = 10),
        CreditCardResponse(3, "Visa", "Maya", "3456", "PayMaya", fee = 10),
        CreditCardResponse(4, "Visa", "Old", "2468", "PNB", isExpired = true, fee = 10),
    ),
    selectedCardId = null
)

@Preview(name = "Neon", showBackground = true, backgroundColor = 0xFF030F1B)
@Composable
private fun GeneralTransferScreenPreviewNeon() {
    AppTheme(colors = NeonColors, assets = NeonAssets) {
        GeneralTransferContent(uiState = previewUiState)
    }
}

@Preview(name = "Black Gold", showBackground = true, backgroundColor = 0xFF050505)
@Composable
private fun GeneralTransferScreenPreviewBlackGold() {
    AppTheme(colors = BlackGoldColors, assets = BlackGoldAssets) {
        GeneralTransferContent(uiState = previewUiState)
    }
}
