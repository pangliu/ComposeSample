package com.qpay.xcash.ui.cashin.paymentmethod

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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import android.widget.Toast
import com.qpay.xcash.R
import com.qpay.xcash.network.model.response.CreditCardResponse
import com.qpay.xcash.ui.UiEvent
import com.qpay.xcash.ui.Routes
import com.qpay.xcash.ui.cashin.CashInUiState
import com.qpay.xcash.ui.cashin.CashInViewModel
import com.qpay.xcash.ui.components.LoadingDialog
import com.qpay.xcash.ui.components.SubPageTopBar
import com.qpay.xcash.ui.theme.AppTheme
import com.qpay.xcash.ui.theme.BlackGoldColors
import com.qpay.xcash.ui.theme.LocalAppColors
import com.qpay.xcash.ui.theme.NeonColors

@Composable
fun PaymentMethodScreen(
    onBack: () -> Unit = {},
    onNavigate: (String) -> Unit = {},
    viewModel: CashInViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    // 每次進入 PaymentMethod 都要重新呼叫,不依賴 ViewModel 的 init(scoped 到 MAIN,只會建立一次)
    LaunchedEffect(Unit) {
        viewModel.fetchCreditCardList()
    }

    LaunchedEffect(Unit) {
        viewModel.eventFlow.collect { event ->
            when (event) {
                is UiEvent.ShowToast -> Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                is UiEvent.ShowDialog -> Unit
            }
        }
    }

    PaymentMethodContent(
        uiState = uiState,
        onClose = onBack,
        onConfirm = { selectedCardId ->
            selectedCardId?.let { viewModel.selectCard(it) }
            onBack()
        },
        onLinkCard = { onNavigate(Routes.SELECT_CARD_TYPE) }
    )
}

@Composable
private fun PaymentMethodContent(
    uiState: CashInUiState = CashInUiState(),
    onClose: () -> Unit = {},
    onConfirm: (Int?) -> Unit = {},
    onLinkCard: () -> Unit = {}
) {
    val colors = LocalAppColors.current
    // 只在畫面上暫存挑選結果，按 Confirm 才真的寫回共用的 CashInViewModel
    var pendingSelectedId by rememberSaveable(uiState.cards) { mutableStateOf(uiState.selectedCardId) }

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
                title = stringResource(R.string.payment_method_title),
                onBack = onClose,
                titleBrush = colors.gradient.goldShimmer
            )

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                items(uiState.cards, key = { it.id }) { card ->
                    CardRow(
                        card = card,
                        isSelected = card.id == pendingSelectedId,
                        onClick = { pendingSelectedId = card.id }
                    )
                }
            }

            Spacer(Modifier.height(12.dp))

            LinkCardRow(
                onClick = onLinkCard,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )

            ConfirmButton(
                onClick = { onConfirm(pendingSelectedId) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp)
            )
        }
    }

    LoadingDialog(isShowing = uiState.isLoading)
}

@Composable
private fun CardRow(card: CreditCardResponse, isSelected: Boolean, onClick: () -> Unit) {
    val colors = LocalAppColors.current.paymentMethod
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(colors.cardRowBackground)
            .border(
                width = 1.5.dp,
                brush = if (isSelected) colors.cardRowBorderSelected else SolidColor(colors.cardRowBorderUnselected),
                shape = RoundedCornerShape(14.dp)
            )
            .clickable(
                enabled = !card.isExpired,
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { onClick() }
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioDot(isSelected = isSelected)

        Spacer(Modifier.width(12.dp))

        MiniCardBadge(cardType = card.cardType)

        Spacer(Modifier.width(12.dp))

        Column {
            if (card.isPrimary) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(colors.premiumTagBackground)
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = stringResource(R.string.payment_method_premium_tag),
                        color = colors.premiumTagText,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(Modifier.height(4.dp))
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = card.bankName.replaceFirstChar { it.uppercase() },
                    color = colors.bankNameText,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.width(6.dp))
                Text(
                    text = "**** ${card.cardNumber}",
                    color = colors.cardNumberText,
                    fontSize = 14.sp
                )
            }
            if (card.isExpired) {
                Text(
                    text = stringResource(R.string.payment_method_expired_hint),
                    color = colors.expiredText,
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Composable
private fun RadioDot(isSelected: Boolean) {
    val colors = LocalAppColors.current.paymentMethod
    Box(
        modifier = Modifier
            .size(20.dp)
            .then(
                if (isSelected) Modifier.border(1.5.dp, colors.radioSelectedBorder, CircleShape)
                else Modifier.border(1.5.dp, colors.radioBorder, CircleShape)
            ),
        contentAlignment = Alignment.Center
    ) {
        if (isSelected) {
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .background(colors.radioSelectedDot, CircleShape)
            )
        }
    }
}

@Composable
private fun MiniCardBadge(cardType: String) {
    val colors = LocalAppColors.current.paymentMethod
    Box(
        modifier = Modifier
            .size(width = 40.dp, height = 26.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(colors.badgeBackground)
            .padding(4.dp)
    ) {
        Box(
            modifier = Modifier
                .size(width = 8.dp, height = 6.dp)
                .align(Alignment.TopStart)
                .clip(RoundedCornerShape(1.dp))
                .background(colors.badgeChip)
        )
        Text(
            text = cardType.uppercase(),
            color = colors.badgeText,
            fontSize = 7.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.BottomEnd)
        )
    }
}

@Composable
private fun LinkCardRow(onClick: () -> Unit, modifier: Modifier = Modifier) {
    val colors = LocalAppColors.current.paymentMethod
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(colors.linkCardBackground)
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(colors.linkCardIconBackground),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "🤔", fontSize = 16.sp)
        }

        Spacer(Modifier.width(10.dp))

        Text(
            text = stringResource(R.string.payment_method_link_card_hint),
            color = colors.linkCardText,
            fontSize = 13.sp,
            modifier = Modifier.weight(1f)
        )

        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
                .border(1.dp, colors.linkCardBorder, RoundedCornerShape(20.dp))
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) { onClick() }
                .padding(horizontal = 14.dp, vertical = 8.dp)
        ) {
            Text(
                text = stringResource(R.string.payment_method_link_card_button),
                color = colors.linkCardText,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun ConfirmButton(onClick: () -> Unit, modifier: Modifier = Modifier) {
    val colors = LocalAppColors.current.paymentMethod
    Box(
        modifier = modifier
            .height(52.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(colors.confirmButtonFill)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(R.string.payment_method_confirm_button),
            color = colors.confirmButtonText,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

private val previewCards = listOf(
    CreditCardResponse(1, "Visa", "Main", "1234", "BDO Unibank", isPrimary = true),
    CreditCardResponse(2, "Visa", "Travel", "3456", "Metrobank"),
    CreditCardResponse(3, "Visa", "Shopping", "2468", "PNB", isExpired = true),
    CreditCardResponse(4, "Visa", "Sample", "0000", "Sample"),
    CreditCardResponse(5, "Visa", "Shopping", "2222", "PNB", isExpired = true),
)

private val previewUiState = CashInUiState(
    isLoadingCards = false,
    cards = previewCards,
    selectedCardId = 1
)

@Preview(name = "Neon", showBackground = true, backgroundColor = 0xFF030F1B)
@Composable
private fun PaymentMethodScreenPreviewNeon() {
    AppTheme(colors = NeonColors) {
        PaymentMethodContent(uiState = previewUiState)
    }
}

@Preview(name = "Black Gold", showBackground = true, backgroundColor = 0xFF050505)
@Composable
private fun PaymentMethodScreenPreviewBlackGold() {
    AppTheme(colors = BlackGoldColors) {
        PaymentMethodContent(uiState = previewUiState)
    }
}
