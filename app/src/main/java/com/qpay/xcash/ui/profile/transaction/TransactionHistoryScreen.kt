package com.qpay.xcash.ui.profile.transaction

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.qpay.xcash.R
import com.qpay.xcash.network.model.response.OrderHistoryResponse
import com.qpay.xcash.ui.components.GradientText
import com.qpay.xcash.ui.components.SubPageTopBar
import com.qpay.xcash.ui.components.neonGlow
import com.qpay.xcash.ui.profile.components.TransactionItem
import com.qpay.xcash.ui.theme.AppTheme
import com.qpay.xcash.ui.theme.BlackGoldColors
import com.qpay.xcash.ui.theme.LocalAppAssets
import com.qpay.xcash.ui.theme.LocalAppColors
import com.qpay.xcash.ui.theme.NeonColors
import com.qpay.xcash.ui.theme.lemonYellow
import com.qpay.xcash.ui.theme.mistGray
import java.text.NumberFormat
import java.util.Locale

fun formatAmount(amount: Double): String {
    val fmt = NumberFormat.getNumberInstance(Locale.US)
    fmt.maximumFractionDigits = 2
    fmt.minimumFractionDigits = 2
    return fmt.format(amount)
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
    val colors = LocalAppColors.current

    var balanceVisible by remember { mutableStateOf(true) }

    Scaffold(
        containerColor = colors.bg.page,
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
                verticalArrangement = Arrangement.spacedBy(10.dp)
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
    val colors = LocalAppColors.current
    val assets = LocalAppAssets.current
    val txColors = colors.transactionHistory
//    Row(
//        modifier = Modifier
//            .fillMaxWidth()
//            .then(
//                if (colors.effect.enableGlow) {
//                    Modifier.neonGlow(colors.accent.primary, alpha = 0.3f, glowRadius = 10.dp, borderRadius = 16.dp)
//                } else Modifier
//            )
//            .background(txColors.cardBackground, RoundedCornerShape(16.dp))
//            .border(1.5.dp, txColors.balanceCardBorder, RoundedCornerShape(16.dp))
//            .padding(16.dp),
//        verticalAlignment = Alignment.CenterVertically
//    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 1.5.dp,
                    brush = txColors.balanceCardBorder,
                    shape = RoundedCornerShape(16.dp))
                .padding(16.dp),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = stringResource(R.string.tx_balance_label),
                    color = txColors.balanceLabelText,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    imageVector = if (balanceVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                    contentDescription = null,
                    tint = txColors.balanceLabelText,
                    modifier = Modifier
                        .size(24.dp)
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) { onToggleVisibility() }
                )
            }
            Text(
                text = if (balanceVisible) "PHP ${formatAmount(balance)}" else "PHP ••••••",
                color = txColors.balanceValueText,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 3.dp)
            )
            Spacer(modifier = Modifier.height(6.dp))
            Row(verticalAlignment = Alignment.Companion.CenterVertically) {
                Icon(
                    painter = painterResource(assets.balanceCoinIcon),
                    contentDescription = stringResource(id = R.string.balance_coin),
                    tint = Color.Companion.Unspecified,

                    modifier = Modifier.Companion
                        .then(
                            if (colors.effect.enableGlow)
                                Modifier.neonGlow(color = lemonYellow, alpha = 0.7f, glowRadius = 10.dp)
                            else Modifier
                        )
                        .size(50.dp)
                )
                Spacer(modifier = Modifier.Companion.width(10.dp))
                GradientText(
                    text = if (!balanceVisible) "••••" else String.format(
                        Locale.US,
                        "%,.0f",
                        tokenBalance
                    ),
                    color = mistGray,
                    brush = colors.gradient.goldShimmer,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Companion.Bold
                )
            }
        }

//        Spacer(Modifier.width(12.dp))
//
//        Box(
//            modifier = Modifier
//                .size(76.dp)
//                .background(txColors.tokenCircleBackground, CircleShape)
//                .border(1.5.dp, txColors.tokenCircleBorder, CircleShape),
//            contentAlignment = Alignment.Center
//        ) {
//            Column(horizontalAlignment = Alignment.CenterHorizontally) {
//                Text(
//                    text = if (balanceVisible) formatAmount(tokenBalance).substringBefore(".") else "●●●",
//                    color = txColors.tokenValueText,
//                    fontSize = 14.sp,
//                    fontWeight = FontWeight.Bold
//                )
//                Text(
//                    text = stringResource(R.string.tx_x_points_label),
//                    color = txColors.tokenLabelText,
//                    fontSize = 8.sp,
//                    letterSpacing = 0.5.sp
//                )
//            }
//        }
//    }
}

@Composable
private fun TimeFilterRow(selected: TimeFilter, onSelect: (TimeFilter) -> Unit) {
    val txColors = LocalAppColors.current.transactionHistory
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(CategoryFilterRowHeight),
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
                    .weight(1f)
                    .fillMaxHeight()
                    .border(1.5.dp, txColors.timeFilterBorder, RoundedCornerShape(10.dp))
                    .background(
                        if (isSelected) txColors.timeFilterSelectedBackground else Color.Transparent,
                        RoundedCornerShape(10.dp)
                    )
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) { onSelect(filter) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = label,
                    color = if (isSelected) txColors.timeFilterSelectedText else txColors.timeFilterText,
                    fontSize = 12.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                )
            }
        }
    }
}

private val CategoryFilterRowHeight = 30.dp

@Composable
private fun CategoryFilterRow(selected: CategoryFilter, onSelect: (CategoryFilter) -> Unit) {
    val txColors = LocalAppColors.current.transactionHistory
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        val segments = listOf(
            CategoryFilter.ALL to stringResource(R.string.tx_category_all),
            CategoryFilter.SPENT to stringResource(R.string.tx_category_spent),
            CategoryFilter.RECEIVED to stringResource(R.string.tx_category_received)
        )
        Row(
            modifier = Modifier
                .weight(2f)
                .height(CategoryFilterRowHeight)
                .border(1.5.dp, txColors.categoryFilterBorder, RoundedCornerShape(10.dp))
                .padding(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            segments.forEach { (category, label) ->
                val isSelected = category == selected
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .background(
                            if (isSelected) txColors.categorySelectedBackground else Color.Transparent,
                            RoundedCornerShape(6.dp)
                        )
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) { onSelect(category) },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = label,
                        color = txColors.categoryText,
                        fontSize = 12.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                }
            }
        }

        val isMerchantSelected = selected == CategoryFilter.MERCHANT_SERVICE
        Row(
            modifier = Modifier
                .weight(1.5f)
                .height(CategoryFilterRowHeight)
                .border(1.5.dp, txColors.categoryFilterBorder, RoundedCornerShape(10.dp))
                .background(
                    if (isMerchantSelected) txColors.categorySelectedBackground else Color.Transparent,
                    RoundedCornerShape(10.dp)
                )
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) { onSelect(CategoryFilter.MERCHANT_SERVICE) }
                .padding(horizontal = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterHorizontally)
        ) {
            Text(
                text = stringResource(R.string.tx_category_merchant),
                color = txColors.categoryText,
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal
            )
            Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = null,
                tint = txColors.categoryText,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@Composable
private fun TransactionListCard(
    transactions: List<OrderHistoryResponse>,
    isLoading: Boolean
) {
    val colors = LocalAppColors.current
    val txColors = colors.transactionHistory
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .then(
                if (colors.effect.enableGlow) {
                    Modifier.neonGlow(colors.accent.primary, alpha = 0.15f, glowRadius = 8.dp, borderRadius = 16.dp)
                } else Modifier
            )
            .background(txColors.cardBackground, RoundedCornerShape(16.dp))
            .border(1.dp, txColors.listCardBorder, RoundedCornerShape(16.dp))
    ) {
        when {
            isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = colors.accent.primary, modifier = Modifier.size(32.dp))
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
                        color = txColors.emptyText,
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
                            color = txColors.listDivider,
                            thickness = 0.5.dp
                        )
                    }
                }
            }
        }
    }
}

@Preview(name = "Neon", showBackground = true, backgroundColor = 0xFF030F1B)
@Composable
private fun TransactionHistoryPreviewNeon() {
    AppTheme(colors = NeonColors) {
        TransactionHistoryContent()
    }
}

@Preview(name = "Black Gold", showBackground = true, backgroundColor = 0xFF050505)
@Composable
private fun TransactionHistoryPreviewBlackGold() {
    AppTheme(colors = BlackGoldColors) {
        TransactionHistoryContent()
    }
}
