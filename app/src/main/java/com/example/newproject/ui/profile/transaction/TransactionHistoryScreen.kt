package com.example.newproject.ui.profile.transaction

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.newproject.R
import com.example.newproject.network.model.response.OrderHistoryResponse
import com.example.newproject.network.model.response.OrderStatus
import com.example.newproject.network.model.response.OrderType
import com.example.newproject.ui.components.SubPageTopBar
import com.example.newproject.ui.components.neonGlow
import com.example.newproject.ui.theme.neonCyan
import com.example.newproject.ui.theme.neonMint
import com.example.newproject.ui.theme.neonPink
import com.example.newproject.ui.theme.neonPurple
import com.example.newproject.ui.theme.neonRed
import com.example.newproject.ui.theme.normalText
import com.example.newproject.ui.theme.welcomeBackground
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private val CardBg = Color(0xFF0D1829)

private val avatarPalette = listOf(
    Color(0xFF2EFFF5), Color(0xFFDF4CFF), Color(0xFF3993D0),
    Color(0xFFFF31A0), Color(0xFF94EEB5), Color(0xFFFEF27C),
    Color(0xFF66A3CF), Color(0xFFFF4C4C)
)

private fun avatarColorForName(name: String): Color {
    val index = (name.firstOrNull()?.code ?: 0) % avatarPalette.size
    return avatarPalette[index]
}

private fun formatAmount(amount: Double): String {
    val fmt = NumberFormat.getNumberInstance(Locale.US)
    fmt.maximumFractionDigits = 2
    fmt.minimumFractionDigits = 2
    return fmt.format(amount)
}

private fun formatTimestamp(ts: Long): String {
    return try {
        SimpleDateFormat("MMM dd, hh:mm a", Locale.US).format(Date(ts))
    } catch (e: Exception) {
        ""
    }
}

@Composable
fun TransactionHistoryScreen(
    viewModel: TransactionHistoryViewModel,
    onBack: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    TransactionHistoryContent(
        uiState = uiState,
        onBack = onBack,
        onTimeFilter = { viewModel.setTimeFilter(it) },
        onCategoryFilter = { viewModel.setCategoryFilter(it) }
    )
}

@Composable
private fun TransactionHistoryContent(
    uiState: TransactionHistoryUiState = TransactionHistoryUiState(),
    onBack: () -> Unit = {},
    onTimeFilter: (TimeFilter) -> Unit = {},
    onCategoryFilter: (CategoryFilter) -> Unit = {}
) {
    var balanceVisible by remember { mutableStateOf(true) }

    Scaffold(
        containerColor = welcomeBackground,
        contentColor = Color.White
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            SubPageTopBar(
                title = stringResource(R.string.profile_transaction_history),
                onBack = onBack
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(start = 16.dp, end = 16.dp, bottom = 24.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
            BalanceCard(
                balance = uiState.balance,
                tokenBalance = uiState.tokenBalance,
                balanceVisible = balanceVisible,
                onToggleVisibility = { balanceVisible = !balanceVisible }
            )

            TimeFilterRow(selected = uiState.selectedTimeFilter, onSelect = onTimeFilter)

            CategoryFilterRow(selected = uiState.selectedCategory, onSelect = onCategoryFilter)

            TransactionListCard(
                transactions = uiState.filteredTransactions,
                isLoading = uiState.isLoading
            )
            }
        }
    }
}

@Composable
private fun BalanceCard(
    balance: Double,
    tokenBalance: Double,
    balanceVisible: Boolean,
    onToggleVisibility: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .neonGlow(neonCyan, alpha = 0.3f, glowRadius = 10.dp, borderRadius = 16.dp)
            .background(CardBg, RoundedCornerShape(16.dp))
            .border(
                1.5.dp,
                Brush.linearGradient(listOf(neonCyan.copy(0.5f), neonPurple.copy(0.7f))),
                RoundedCornerShape(16.dp)
            )
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = stringResource(R.string.tx_balance_label),
                    color = normalText,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
                Icon(
                    imageVector = if (balanceVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                    contentDescription = null,
                    tint = normalText,
                    modifier = Modifier
                        .size(16.dp)
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) { onToggleVisibility() }
                )
            }
            Spacer(Modifier.height(6.dp))
            Text(
                text = if (balanceVisible) "PHP ${formatAmount(balance)}" else "PHP ●●●,●●●",
                color = Color.White,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(Modifier.width(12.dp))

        Box(
            modifier = Modifier
                .size(76.dp)
                .background(
                    Brush.radialGradient(listOf(neonPurple.copy(0.35f), neonCyan.copy(0.15f))),
                    CircleShape
                )
                .border(1.5.dp, neonCyan.copy(0.5f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = if (balanceVisible) formatAmount(tokenBalance).substringBefore(".") else "●●●",
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = stringResource(R.string.tx_x_points_label),
                    color = normalText,
                    fontSize = 8.sp,
                    letterSpacing = 0.5.sp
                )
            }
        }
    }
}

@Composable
private fun TimeFilterRow(selected: TimeFilter, onSelect: (TimeFilter) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        TimeFilter.entries.forEach { filter ->
            val isSelected = filter == selected
            val label = when (filter) {
                TimeFilter.TODAY -> stringResource(R.string.tx_filter_today)
                TimeFilter.THIS_WEEK -> stringResource(R.string.tx_filter_this_week)
                TimeFilter.THIS_MONTH -> stringResource(R.string.tx_filter_this_month)
            }
            Box(
                modifier = Modifier
                    .border(
                        1.dp,
                        if (isSelected) neonCyan else neonCyan.copy(0.3f),
                        RoundedCornerShape(50)
                    )
                    .background(
                        if (isSelected) neonCyan.copy(0.12f) else Color.Transparent,
                        RoundedCornerShape(50)
                    )
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) { onSelect(filter) }
                    .padding(horizontal = 16.dp, vertical = 7.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = label,
                    color = if (isSelected) neonCyan else normalText,
                    fontSize = 12.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                )
            }
        }
    }
}

@Composable
private fun CategoryFilterRow(selected: CategoryFilter, onSelect: (CategoryFilter) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        CategoryFilter.entries.forEach { category ->
            val isSelected = category == selected
            val label = when (category) {
                CategoryFilter.ALL -> stringResource(R.string.tx_category_all)
                CategoryFilter.SPENT -> stringResource(R.string.tx_category_spent)
                CategoryFilter.RECEIVED -> stringResource(R.string.tx_category_received)
                CategoryFilter.MERCHANT_SERVICE -> stringResource(R.string.tx_category_merchant)
            }
            Box(
                modifier = Modifier
                    .background(
                        if (isSelected) neonPurple.copy(0.25f) else CardBg,
                        RoundedCornerShape(8.dp)
                    )
                    .border(
                        1.dp,
                        if (isSelected) neonPurple.copy(0.8f) else neonPurple.copy(0.2f),
                        RoundedCornerShape(8.dp)
                    )
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) { onSelect(category) }
                    .padding(horizontal = 14.dp, vertical = 7.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = label,
                    color = if (isSelected) Color.White else normalText,
                    fontSize = 12.sp,
                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                )
            }
        }
    }
}

@Composable
private fun TransactionListCard(
    transactions: List<OrderHistoryResponse>,
    isLoading: Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .neonGlow(neonCyan, alpha = 0.15f, glowRadius = 8.dp, borderRadius = 16.dp)
            .background(CardBg, RoundedCornerShape(16.dp))
            .border(
                1.dp,
                Brush.linearGradient(listOf(neonCyan.copy(0.25f), neonPurple.copy(0.3f))),
                RoundedCornerShape(16.dp)
            )
    ) {
        when {
            isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = neonCyan, modifier = Modifier.size(32.dp))
                }
            }
            transactions.isEmpty() -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.tx_empty),
                        color = normalText,
                        fontSize = 14.sp
                    )
                }
            }
            else -> {
                transactions.forEachIndexed { index, tx ->
                    TransactionItem(tx)
                    if (index < transactions.lastIndex) {
                        HorizontalDivider(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            color = neonCyan.copy(0.08f),
                            thickness = 0.5.dp
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TransactionItem(tx: OrderHistoryResponse) {
    val isIncoming = tx.type == OrderType.INCOMING
    val avatarColor = avatarColorForName(tx.paymentName)
    val formattedDate = formatTimestamp(tx.expiredAt)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 13.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(46.dp)
                .background(avatarColor.copy(0.18f), CircleShape)
                .border(1.dp, avatarColor.copy(0.5f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = tx.paymentName.firstOrNull()?.uppercaseChar()?.toString() ?: "?",
                color = avatarColor,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }

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
                color = normalText,
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
                color = if (isIncoming) neonMint else neonPink,
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
    Box(
        modifier = Modifier
            .background(neonPurple.copy(0.2f), RoundedCornerShape(50))
            .border(1.dp, neonPurple.copy(0.6f), RoundedCornerShape(50))
            .padding(horizontal = 8.dp, vertical = 2.dp)
    ) {
        Text(
            text = stringResource(R.string.tx_cop_points),
            color = neonPurple,
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.5.sp
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0B1327)
@Composable
private fun TransactionHistoryPreview() {
    MaterialTheme {
        TransactionHistoryContent()
    }
}
