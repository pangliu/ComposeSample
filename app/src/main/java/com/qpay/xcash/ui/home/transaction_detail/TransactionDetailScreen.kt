package com.qpay.xcash.ui.home.transaction_detail

import android.widget.Toast
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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.qpay.xcash.R
import com.qpay.xcash.network.model.response.OrderStatus
import com.qpay.xcash.network.model.response.TransactionDetailResponse
import com.qpay.xcash.ui.UiEvent
import com.qpay.xcash.ui.components.LoadingDialog
import com.qpay.xcash.ui.components.RowIcon
import com.qpay.xcash.ui.components.RowIconImage
import com.qpay.xcash.ui.components.neonGlow
import com.qpay.xcash.ui.theme.LocalAppColors
import com.qpay.xcash.ui.theme.cyberPurple
import com.qpay.xcash.ui.theme.neonBlushPink
import com.qpay.xcash.ui.theme.neonCyan
import com.qpay.xcash.ui.theme.neonMint
import com.qpay.xcash.ui.theme.neonPink
import com.qpay.xcash.ui.theme.neonPurpleLight

private val CardBg = Color(0xFF0A1628)

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
                is UiEvent.ShowToast -> Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                is UiEvent.ShowDialog -> Toast.makeText(context, event.message, Toast.LENGTH_LONG).show()
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
    val isSuccess = detail.status == OrderStatus.SUCCESS
    val statusColor = if (isSuccess) colors.accent.primary else neonPink
    val statusText = if (isSuccess) stringResource(R.string.tx_detail_successful) else stringResource(R.string.tx_detail_failed)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(paddingValues)
            .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(24.dp))

            Text(
                text = stringResource(R.string.tx_detail_title),
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(24.dp))

            // Status icon + text
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Icon(
                    painter = painterResource(R.mipmap.ic_success_transaction),
                    contentDescription = null,
                    tint = Color.Unspecified,
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
            Text(
                text = "${detail.currency} ${String.format("%,.2f", detail.amount)}",
                color = neonPurpleLight,
                fontSize = 36.sp,
                fontWeight = FontWeight.ExtraBold
            )

            Spacer(Modifier.height(20.dp))

            // Action buttons
            Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                ActionIconButton(
                    icon = RowIcon.Resource(R.mipmap.ic_reload),
                    tint = Color.Unspecified,
                    onClick = {})
                ActionIconButton(
                    icon = RowIcon.Resource(R.mipmap.ic_favorite),
                    tint = Color.Unspecified,
                    onClick = {})
                ActionIconButton(
                    icon = RowIcon.Resource(R.mipmap.ic_share),
                    tint = Color.Unspecified,
                    onClick = {})
            }

            Spacer(Modifier.height(24.dp))

            // Pay To / Pay From card
            InfoCard(
                borderColor = colors.accent.primary
            ) {
                PartyRow(
                    label = stringResource(R.string.tx_detail_pay_to),
                    labelColor = neonMint,
                    name = detail.payToName,
                    account = detail.payToAccount,
                    bank = detail.payToBank
                )
                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 12.dp),
                    color = colors.accent.primary.copy(alpha = 0.15f),
                    thickness = 0.5.dp
                )
                PartyRow(
                    label = stringResource(R.string.tx_detail_pay_from),
                    labelColor = neonBlushPink,
                    name = detail.payFromName,
                    account = detail.payFromAccount,
                    bank = detail.payFromBank
                )
            }

            Spacer(Modifier.height(12.dp))

            // Amount breakdown card
            InfoCard(
                borderColor = cyberPurple
            ) {
                AmountRow(label = stringResource(R.string.tx_detail_amount), value = "${detail.currency} ${String.format("%,.2f", detail.amount)}")
                Spacer(Modifier.height(10.dp))
                AmountRow(label = stringResource(R.string.tx_detail_fee), value = "${detail.currency} ${String.format("%,.2f", detail.fee)}")
                Spacer(Modifier.height(10.dp))
                AmountRow(label = stringResource(R.string.tx_detail_total), value = "${detail.currency} ${String.format("%,.2f", detail.total)}")
            }

            Spacer(Modifier.height(12.dp))

            // Reference card
            InfoCard(
                borderColor = colors.accent.primary
            ) {
                LabelValueRow(label = stringResource(R.string.tx_detail_reference_no), value = detail.referenceNo)
                Spacer(Modifier.height(10.dp))
                LabelValueRow(label = stringResource(R.string.tx_detail_date), value = detail.date)
            }

            Spacer(Modifier.height(28.dp))

            // Close button
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .neonGlow(neonPurpleLight, alpha = 0.6f, glowRadius = 16.dp, borderRadius = 26.dp)
                    .background(colors.bg.page, CircleShape)
                    .border(1.5.dp, neonPurpleLight, CircleShape)
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
                    tint = neonPurpleLight,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(Modifier.height(32.dp))
        }
}

@Composable
private fun InfoCard(borderColor: Color = neonCyan, content: @Composable () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .neonGlow(borderColor, alpha = 0.15f, glowRadius = 10.dp, borderRadius = 16.dp)
            .background(CardBg, RoundedCornerShape(16.dp))
            .border(1.5.dp, borderColor.copy(alpha = 0.9f), RoundedCornerShape(16.dp))
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        content()
    }
}

@Composable
private fun PartyRow(label: String, labelColor: Color, name: String, account: String, bank: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            color = labelColor,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.width(88.dp)
        )
        Column {
            Text(text = name, color = labelColor, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            Text(text = account, color = labelColor.copy(alpha = 0.8f), fontSize = 13.sp)
            Text(text = bank, color = Color.White.copy(alpha = 0.9f), fontSize = 12.sp)
        }
    }
}

@Composable
private fun AmountRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, color = cyberPurple, fontSize = 14.sp, fontWeight = FontWeight.Medium)
        Text(text = value, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Medium)
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
        Text(text = label, color = colors.accent.primary, fontSize = 14.sp, fontWeight = FontWeight.Medium)
        Text(
            text = value,
            color = colors.text.body,
            fontSize = 13.sp,
            textAlign = TextAlign.End,
            modifier = Modifier.weight(1f).padding(start = 12.dp)
        )
    }
}

@Composable
private fun ActionIconButton(icon: RowIcon, tint: Color, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(48.dp)
            .neonGlow(tint, alpha = 0.3f, glowRadius = 12.dp, borderRadius = 24.dp)
//            .background(CardBg, CircleShape)
//            .border(1.5.dp, tint.copy(alpha = 0.6f), CircleShape)
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

@Preview(showBackground = true, backgroundColor = 0xFF0A0E1A)
@Composable
private fun TransactionDetailPreview() {
    MaterialTheme {
        TransactionDetailContent(
            detail = TransactionDetailResponse(
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
            ),
            paddingValues = PaddingValues(),
            onBack = {}
        )
    }
}
