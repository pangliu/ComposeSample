package com.qpay.xcash.ui.home.components

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
import com.qpay.xcash.R
import com.qpay.xcash.network.model.response.OrderHistoryResponse
import com.qpay.xcash.network.model.response.OrderStatus
import com.qpay.xcash.network.model.response.OrderType
import com.qpay.xcash.ui.components.GradientText
import com.qpay.xcash.ui.components.neonGlow
import com.qpay.xcash.ui.theme.AppTheme
import com.qpay.xcash.ui.theme.BlackGoldColors
import com.qpay.xcash.ui.theme.LocalAppColors
import com.qpay.xcash.ui.theme.NeonColors
import com.qpay.xcash.ui.theme.deepNavy
import com.qpay.xcash.ui.theme.neonCyanLight
import com.qpay.xcash.ui.theme.themeWhite

/**
 * @param orders null → loading；emptyList → 無資料；否則顯示列表
 */
@Composable
fun RecentActivity(
    orders: List<OrderHistoryResponse>,
    onItemClick: (OrderHistoryResponse) -> Unit = {}
) {
    val colors = LocalAppColors.current
    Column(modifier = Modifier.fillMaxWidth()) {
        GradientText(
            text = stringResource(R.string.recent_activity_title),
            color = themeWhite,
            brush = colors.gradient.silverShimmer,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(10.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .then(
                    if (colors.effect.enableGlow)
                        Modifier.neonGlow(
                            color = neonCyanLight,
                            alpha = 0.6f,
                            glowRadius = 18.dp,
                            borderRadius = 18.dp
                        )
                    else Modifier
                )
                .border(
                    width = 2.dp,
                    brush = colors.recentActivity.border,
                    shape = RoundedCornerShape(18.dp)
                )
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
    val iconColor = if (isCashIn) {
        colors.recentActivity.cashInIconTint
    } else {
        colors.recentActivity.cashOutIconTint
    }
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
                .background(iconColor.copy(alpha = 0.3f)),
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
            fontWeight = FontWeight.Bold,
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

private val previewOrders: List<OrderHistoryResponse>
    @Composable get() {
        val now = System.currentTimeMillis()
        return listOf(
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
    }

@Preview(name = "Neon", showBackground = true, backgroundColor = 0xFF0A0E1A)
@Composable
private fun RecentActivityPreviewNeon() {
    AppTheme(colors = NeonColors) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(deepNavy)
                .padding(16.dp)
        ) {
            RecentActivity(orders = previewOrders)
        }
    }
}

@Preview(name = "Black Gold", showBackground = true, backgroundColor = 0xFF050505)
@Composable
private fun RecentActivityPreviewBlackGold() {
    AppTheme(colors = BlackGoldColors) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            RecentActivity(orders = previewOrders)
        }
    }
}
