package com.qpay.xcash.ui.home.transaction_detail

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.StarBorder
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.qpay.xcash.R
import com.qpay.xcash.network.model.response.OrderStatus
import com.qpay.xcash.network.model.response.TransactionDetailResponse
import com.qpay.xcash.ui.UiEvent
import com.qpay.xcash.ui.components.GradientText
import com.qpay.xcash.ui.components.LoadingDialog
import com.qpay.xcash.ui.components.RowIcon
import com.qpay.xcash.ui.components.RowIconImage
import com.qpay.xcash.ui.components.SubPageTopBar
import com.qpay.xcash.ui.components.neonGlow
import com.qpay.xcash.ui.theme.AppTheme
import com.qpay.xcash.ui.theme.BlackGoldAssets
import com.qpay.xcash.ui.theme.BlackGoldColors
import com.qpay.xcash.ui.theme.LocalAppAssets
import com.qpay.xcash.ui.theme.LocalAppColors
import com.qpay.xcash.ui.theme.NeonAssets
import com.qpay.xcash.ui.theme.NeonColors
import com.qpay.xcash.ui.theme.neonCyan
import java.util.Locale

@Composable
fun TransactionDetailScreen(
    viewModel: TransactionDetailViewModel,
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.eventFlow.collect { event ->
            when (event) {
                is UiEvent.ShowToast -> Toast.makeText(context, event.message, Toast.LENGTH_SHORT)
                    .show()

                is UiEvent.ShowDialog -> Toast.makeText(context, event.message, Toast.LENGTH_LONG)
                    .show()
            }
        }
    }

    val colors = LocalAppColors.current
    Scaffold(
        containerColor = colors.bg.page,
        contentColor = Color.White
    ) { paddingValues ->
        if (uiState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                LoadingDialog(isShowing = true)
            }
        } else {
            uiState.detail?.let { detail ->
                TransactionDetailContent(
                    detail = detail,
                    paddingValues = paddingValues,
                    onBack = onBack
                )
            }
        }
    }
}

@Composable
private fun TransactionDetailContent(
    detail: TransactionDetailResponse,
    paddingValues: PaddingValues,
    onBack: () -> Unit
) {
    val colors = LocalAppColors.current
    val assets = LocalAppAssets.current
    val isSuccess = detail.status == OrderStatus.SUCCESS
    val statusColor =
        if (isSuccess) colors.accent.primary else colors.transactionDetail.failedStatusText
    val statusText =
        if (isSuccess) stringResource(R.string.tx_detail_successful) else stringResource(R.string.tx_detail_failed)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        SubPageTopBar(
            title = stringResource(R.string.tx_detail_title),
            onBack = onBack
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(8.dp))

            // Status icon + text
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Icon(
                    painter = painterResource(R.mipmap.ic_success_transaction),
                    contentDescription = null,
                    tint = colors.transactionDetail.successIcon,
                    modifier = Modifier.size(56.dp)
                )
                Text(
                    text = statusText,
                    color = statusColor,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 26.sp
                )
            }

            Spacer(Modifier.height(16.dp))

            // Amount
            GradientText(
                text = "${detail.currency} ${String.format(Locale.US, "%,.2f", detail.amount)}",
                color = colors.transactionDetail.amountText,
                brush = colors.gradient.goldShimmer,
                fontSize = 36.sp,
                fontWeight = FontWeight.ExtraBold
            )

            Spacer(Modifier.height(20.dp))

            // Action buttons
            Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                ActionIconButton(
                    icon = RowIcon.Resource(R.mipmap.ic_reload),
                    tint = colors.transactionDetail.reloadIcon,
                    onClick = {})
                ActionIconButton(
                    icon = RowIcon.Resource(R.mipmap.ic_favorite),
                    tint = colors.transactionDetail.favoriteIcon,
                    onClick = {})
                ActionIconButton(
                    icon = RowIcon.Resource(R.mipmap.ic_share),
                    tint = colors.transactionDetail.shareIcon,
                    onClick = {})
            }

            Spacer(Modifier.height(24.dp))

            // Pay To / Pay From card
            InfoCard(
                background = assets.transactionDetailCardBg,
                borderColor = colors.transactionDetail.paymentInfoCardBorder
            ) {
                PartyRow(
                    label = stringResource(R.string.tx_detail_pay_to),
                    labelColor = colors.transactionDetail.payToLabel,
                    titleColor = colors.transactionDetail.payToTitle,
                    name = detail.payToName,
                    account = detail.payToAccount,
                    bank = detail.payToBank
                )
                Box(
                    modifier = Modifier
                        .padding(vertical = 12.dp)
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(
                            brush = colors.transactionDetail.dividerBrush
                        )
                )
                PartyRow(
                    label = stringResource(R.string.tx_detail_pay_from),
                    labelColor = colors.transactionDetail.payFromLabel,
                    titleColor = colors.transactionDetail.payFromTitle,
                    name = detail.payFromName,
                    account = detail.payFromAccount,
                    bank = detail.payFromBank
                )
            }

            Spacer(Modifier.height(12.dp))

            // Amount breakdown card
            InfoCard(
                background = assets.transactionDetailCardBgSmall,
                borderColor = colors.transactionDetail.amountCardBorder
            ) {
                AmountRow(
                    label = stringResource(R.string.tx_detail_amount),
                    value = "${detail.currency} ${String.format(Locale.US, "%,.2f", detail.amount)}"
                )
                Spacer(Modifier.height(15.dp))
                AmountRow(
                    label = stringResource(R.string.tx_detail_fee),
                    value = "${detail.currency} ${String.format(Locale.US, "%,.2f", detail.fee)}"
                )
                Spacer(Modifier.height(15.dp))
                AmountRow(
                    label = stringResource(R.string.tx_detail_total),
                    value = "${detail.currency} ${String.format(Locale.US, "%,.2f", detail.total)}"
                )
            }

            Spacer(Modifier.height(12.dp))

            // Reference card
            InfoCard(
                background = assets.transactionDetailCardBgSmall,
                borderColor = colors.transactionDetail.transactionCardBorder
            ) {
                Spacer(Modifier.height(10.dp))
                LabelValueRow(
                    label = stringResource(R.string.tx_detail_reference_no),
                    value = detail.referenceNo
                )
                Spacer(Modifier.height(15.dp))
                LabelValueRow(label = stringResource(R.string.tx_detail_date), value = detail.date)
                Spacer(Modifier.height(10.dp))
            }

            Spacer(Modifier.height(28.dp))

            // Close button
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .then(
                        if (colors.effect.enableGlow)
                            Modifier.neonGlow(
                                colors.transactionDetail.closeButton,
                                alpha = 0.6f,
                                glowRadius = 16.dp,
                                borderRadius = 26.dp
                            )
                        else Modifier
                    )
                    .background(colors.bg.page, CircleShape)
                    .border(1.5.dp, colors.transactionDetail.closeButton, CircleShape)
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() },
                        onClick = onBack
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = null,
                    tint = colors.transactionDetail.closeButton,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(Modifier.height(32.dp))
        }
    }
}

@Composable
private fun InfoCard(
    background: Int?,
    borderColor: Color = neonCyan,
    content: @Composable () -> Unit
) {
    val colors = LocalAppColors.current
    val assets = LocalAppAssets.current
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .then(
                other = if (colors.effect.enableGlow)
                    Modifier.neonGlow(
                        borderColor,
                        alpha = 0.6f,
                        glowRadius = 10.dp,
                        borderRadius = 16.dp
                    )
                else Modifier
            )
//            .clip(RoundedCornerShape(16.dp))
            .background(
                color = colors.transactionDetail.cardBackground,
                shape = RoundedCornerShape(16.dp)
            )
            .border(
                width = 1.5.dp,
                color = borderColor,
                shape = RoundedCornerShape(16.dp)
            )
    ) {
        background?.let { resId ->
            Image(
                painter = painterResource(resId),
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.matchParentSize()
            )
        }
        Column(
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp),
        ) {
            content()
        }
    }
}

@Composable
private fun PartyRow(
    label: String,
    labelColor: Color,
    titleColor: Color,
    name: String,
    account: String,
    bank: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            color = titleColor,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            Text(text = name, color = labelColor, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            Text(text = account, color = labelColor.copy(alpha = 0.8f), fontSize = 13.sp)
            Text(
                text = bank,
                color = Color.White.copy(alpha = 0.9f),
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun AmountRow(label: String, value: String) {
    val colors = LocalAppColors.current
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            color = colors.transactionDetail.amountRowTitle,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        )
        Text(
            text = value,
            color = colors.transactionDetail.amountRowLabel,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        )
    }
}

@Composable
private fun LabelValueRow(label: String, value: String) {
    val colors = LocalAppColors.current
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            color = colors.transactionDetail.labelValueRowTitle,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        )
        Text(
            text = value,
            color = colors.transactionDetail.labelValueRowLabel,
            fontSize = 13.sp,
            modifier = Modifier
                .weight(1f)
        )
    }
}

@Composable
private fun ActionIconButton(icon: RowIcon, tint: Color, onClick: () -> Unit) {
    val colors = LocalAppColors.current
    Box(
        modifier = Modifier
            .size(48.dp)
            .then(
                if (colors.effect.enableGlow)
                    Modifier.neonGlow(tint, alpha = 0.3f, glowRadius = 12.dp, borderRadius = 24.dp)
                else Modifier
            )
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        RowIconImage(
            icon = icon,
            tint = tint,
            size = 48.dp
        )
    }
}

private val previewDetail = TransactionDetailResponse(
    orderId = "ORD20250001",
    status = OrderStatus.SUCCESS,
    currency = "PHP",
    amount = 300.0,
    fee = 0.0,
    payToName = "Kenny",
    payToAccount = "••••••••6438",
    payToBank = "Bank Name",
    payFromName = "Barbie",
    payFromAccount = "••••••••1637",
    payFromBank = "Bank Name",
    referenceNo = "ITR260526142836004",
    date = "18 Jun 2026 at 10:28 PM"
)

@Preview(name = "Neon", showBackground = true, backgroundColor = 0xFF0A0E1A)
@Composable
private fun TransactionDetailPreviewNeon() {
    AppTheme(colors = NeonColors, assets = NeonAssets) {
        TransactionDetailContent(
            detail = previewDetail,
            paddingValues = PaddingValues(),
            onBack = {})
    }
}

@Preview(name = "Black Gold", showBackground = true, backgroundColor = 0xFF050505)
@Composable
private fun TransactionDetailPreviewBlackGold() {
    AppTheme(colors = BlackGoldColors, assets = BlackGoldAssets) {
        TransactionDetailContent(
            detail = previewDetail,
            paddingValues = PaddingValues(),
            onBack = {})
    }
}
