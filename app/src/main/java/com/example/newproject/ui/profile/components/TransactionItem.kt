package com.example.newproject.ui.profile.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.newproject.R
import com.example.newproject.network.model.response.OrderHistoryResponse
import com.example.newproject.network.model.response.OrderStatus
import com.example.newproject.network.model.response.OrderType
import com.example.newproject.ui.profile.transaction.formatAmount
import com.example.newproject.ui.theme.AppTheme
import com.example.newproject.ui.theme.BlackGoldColors
import com.example.newproject.ui.theme.LocalAppColors
import com.example.newproject.ui.theme.NeonColors
import com.example.newproject.ui.theme.neonCyan
import com.example.newproject.ui.theme.neonMint
import com.example.newproject.ui.theme.neonPink
import com.example.newproject.ui.theme.neonPurple
import com.example.newproject.ui.theme.neonPurpleLight
import com.example.newproject.ui.theme.neonRed
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private val CardBg = Color(0xFF0D1829)

private fun formatTimestamp(ts: Long): String {
    return try {
        SimpleDateFormat("MMM dd, hh:mm a", Locale.US).format(Date(ts))
    } catch (e: Exception) {
        ""
    }
}

@Composable
fun TransactionItem(tx: OrderHistoryResponse) {
    val colors = LocalAppColors.current
    val isIncoming = tx.type == OrderType.INCOMING
    val typeColor = if (isIncoming) neonMint else neonPink
    val formattedDate = formatTimestamp(tx.expiredAt)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 13.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
//        Box(
//            modifier = Modifier
//                .size(46.dp)
//                .background(typeColor.copy(0.18f), CircleShape)
//                .border(1.dp, typeColor.copy(0.5f), CircleShape),
//            contentAlignment = Alignment.Center
//        ) {
            Icon(
                painter = painterResource(
                    id = if (isIncoming) R.mipmap.ic_essent_cash_in else R.mipmap.ic_essent_send
                ),
                contentDescription = tx.paymentName,
                tint = Color.Unspecified,
                modifier = Modifier.size(40.dp)
            )
//        }

        Spacer(Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = tx.paymentName,
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(Modifier.height(2.dp))
            Text(
                text = "${tx.account} · $formattedDate",
                color = colors.text.body,
                fontSize = 11.sp
            )
            if (isIncoming && tx.status == OrderStatus.SUCCESS) {
                Spacer(Modifier.height(5.dp))
                CopPointsChip()
            }
        }

        Spacer(Modifier.width(8.dp))

        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = "${if (isIncoming) "+" else "-"}PHP ${formatAmount(tx.amount)}",
                color = if (isIncoming) neonCyan else neonPurpleLight,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
            if (tx.status == OrderStatus.FAILED) {
                Spacer(Modifier.height(2.dp))
                Text(
                    text = stringResource(R.string.tx_status_failed),
                    color = neonRed,
                    fontSize = 10.sp
                )
            }
        }
    }
}

@Composable
private fun CopPointsChip() {
    val colors = LocalAppColors.current
    Box(
        modifier = Modifier
            .background(colors.accent.secondary.copy(0.2f), RoundedCornerShape(50))
            .border(1.dp, colors.accent.secondary.copy(0.6f), RoundedCornerShape(50))
            .padding(horizontal = 8.dp, vertical = 2.dp)
    ) {
        Text(
            text = stringResource(R.string.tx_cop_points),
            color = colors.accent.secondary,
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.5.sp
        )
    }
}

private val previewTransactions = listOf(
    OrderHistoryResponse(
        orderId = "1",
        amount = 1250.0,
        type = OrderType.INCOMING,
        paymentName = "Maria Santos",
        account = "0917****123",
        targetAccount = "",
        status = OrderStatus.SUCCESS,
        expiredAt = System.currentTimeMillis()
    ),
    OrderHistoryResponse(
        orderId = "2",
        amount = 89.5,
        type = OrderType.OUTGOING,
        paymentName = "7-Eleven Store",
        account = "0928****456",
        targetAccount = "",
        status = OrderStatus.SUCCESS,
        expiredAt = System.currentTimeMillis()
    ),
    OrderHistoryResponse(
        orderId = "3",
        amount = 500.0,
        type = OrderType.OUTGOING,
        paymentName = "John Cruz",
        account = "0939****789",
        targetAccount = "",
        status = OrderStatus.FAILED,
        expiredAt = System.currentTimeMillis()
    )
)

@Preview(name = "Neon", showBackground = true, backgroundColor = 0xFF030F1B)
@Composable
private fun TransactionItemPreviewNeon() {
    AppTheme(colors = NeonColors) {
        Column(modifier = Modifier.background(CardBg)) {
            previewTransactions.forEachIndexed { index, tx ->
                TransactionItem(tx)
                if (index < previewTransactions.lastIndex) {
                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        color = NeonColors.accent.primary.copy(0.08f),
                        thickness = 0.5.dp
                    )
                }
            }
        }
    }
}

@Preview(name = "Black Gold", showBackground = true, backgroundColor = 0xFF050505)
@Composable
private fun TransactionItemPreviewBlackGold() {
    AppTheme(colors = BlackGoldColors) {
        Column(modifier = Modifier.background(CardBg)) {
            previewTransactions.forEachIndexed { index, tx ->
                TransactionItem(tx)
                if (index < previewTransactions.lastIndex) {
                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        color = BlackGoldColors.accent.primary.copy(0.08f),
                        thickness = 0.5.dp
                    )
                }
            }
        }
    }
}
