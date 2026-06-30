package com.example.newproject.ui.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.newproject.R
import com.example.newproject.network.model.response.OrderHistoryResponse
import com.example.newproject.network.model.response.OrderStatus
import com.example.newproject.network.model.response.OrderType
import com.example.newproject.ui.components.neonGlow
import com.example.newproject.ui.theme.LocalAppColors
import com.example.newproject.ui.theme.cashInGreen
import com.example.newproject.ui.theme.darkBackground
import com.example.newproject.ui.theme.neonCyanLight
import com.example.newproject.ui.theme.sendPink
import com.example.newproject.ui.theme.themeWhite

/**
 * @param orders null → loading；emptyList → 無資料；否則顯示列表
 */
@Composable
fun RecentActivity(orders: List<OrderHistoryResponse>, onItemClick: (OrderHistoryResponse) -> Unit = {}) {
    val colors = LocalAppColors.current
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = stringResource(R.string.recent_activity_title),
            color = themeWhite,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(10.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .neonGlow(color = neonCyanLight, alpha = 0.6f, glowRadius = 18.dp, borderRadius = 18.dp)
                .border(width = 2.dp, color = neonCyanLight, shape = RoundedCornerShape(18.dp))
                .background(color = colors.bg.page, shape = RoundedCornerShape(18.dp))
                .padding(horizontal = 16.dp, vertical = 4.dp),
            contentAlignment = Alignment.Center
        ) {
            when {
                orders.isEmpty() -> {
                    Text(
                        text = stringResource(R.string.recent_empty),
                        color = colors.text.body,
                        fontSize = 13.sp,
                        modifier = Modifier.padding(vertical = 24.dp)
                    )
                }
                else -> {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        orders.forEachIndexed { index, order ->
                            TransactionRow(order = order, onClick = { onItemClick(order) })
                            if (index < orders.lastIndex) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(1.dp)
                                        .background(colors.text.body.copy(alpha = 0.15f))
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TransactionRow(order: OrderHistoryResponse, onClick: () -> Unit) {
    val colors = LocalAppColors.current
    val isCashIn = order.type == OrderType.INCOMING
    val iconColor = if (isCashIn) cashInGreen else sendPink
    val icon = if (isCashIn) Icons.Default.ArrowDownward else Icons.Default.ArrowUpward
    val descRes = if (isCashIn) R.string.recent_cash_in else R.string.recent_cash_out
    val amountText = if (isCashIn)
        "+${String.format("%,.0f", order.amount)}"
    else
        "-${String.format("%,.0f", order.amount)}"

    val interactionSource = remember { MutableInteractionSource() }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(interactionSource = interactionSource, indication = null, onClick = onClick)
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(iconColor.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = stringResource(descRes),
            color = colors.text.body,
            fontSize = 14.sp,
            modifier = Modifier.weight(1f)
        )

        Text(
            text = amountText,
            color = colors.text.body,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )

        Spacer(modifier = Modifier.width(4.dp))

        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null,
            tint = colors.text.body,
            modifier = Modifier.size(20.dp)
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0A0E1A)
@Composable
fun RecentActivityPreview() {
    val now = System.currentTimeMillis()
    MaterialTheme {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(darkBackground)
                .padding(16.dp)
        ) {
            RecentActivity(
                orders = listOf(
                    OrderHistoryResponse(
                        orderId = "ORD001",
                        amount = 500.0,
                        type = OrderType.INCOMING,
                        paymentName = "GCash",
                        account = "09123456789",
                        targetAccount = "09987654321",
                        status = OrderStatus.SUCCESS,
                        expiredAt = now - 3_600_000
                    ),
                    OrderHistoryResponse(
                        orderId = "ORD002",
                        amount = 200.0,
                        type = OrderType.OUTGOING,
                        paymentName = "GoTyme",
                        account = "09123456789",
                        targetAccount = "09111222333",
                        status = OrderStatus.SUCCESS,
                        expiredAt = now - 7_200_000
                    ),
                    OrderHistoryResponse(
                        orderId = "ORD003",
                        amount = 1200.0,
                        type = OrderType.INCOMING,
                        paymentName = "GCash",
                        account = "09123456789",
                        targetAccount = "09444555666",
                        status = OrderStatus.FAILED,
                        expiredAt = now - 86_400_000
                    )
                )
            )
        }
    }
}
