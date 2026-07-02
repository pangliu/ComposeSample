package com.example.newproject.ui.home

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.newproject.R
import com.example.newproject.network.model.response.OrderHistoryResponse
import com.example.newproject.network.model.response.UserInfoResponse
import com.example.newproject.ui.Routes
import com.example.newproject.ui.UiEvent
import com.example.newproject.ui.components.LoadingDialogContent
import com.example.newproject.ui.home.components.BalanceCard
import com.example.newproject.ui.home.components.XEssentialsCard
import com.example.newproject.ui.home.components.QuestCard
import com.example.newproject.ui.home.components.RecentActivity
import com.example.newproject.ui.home.essential.ESSENTIALS_DISPLAY_COUNT
import com.example.newproject.ui.home.essential.EssentialItem
import com.example.newproject.ui.home.essential.allEssentialItems

@Composable
fun HomeScreen(viewModel: HomeViewModel = hiltViewModel(), onNavigate: (String) -> Unit = {}) {
    val uiState by viewModel.uiState.collectAsState()
    val myMenuItems by viewModel.myMenuItems.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.eventFlow.collect { event ->
            when (event) {
                is UiEvent.ShowToast -> Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                is UiEvent.ShowDialog -> Toast.makeText(context, event.message, Toast.LENGTH_LONG).show()
            }
        }
    }

    HomeScreenContent(
        uiState = uiState,
        myMenuItems = myMenuItems,
        onSaveMyMenu = viewModel::saveMyMenu,
        onSelectOrder = viewModel::selectOrder,
        onNavigate = onNavigate
    )
}

@Composable
private fun HomeScreenContent(
    uiState: HomeUiState,
    myMenuItems: List<EssentialItem> = allEssentialItems.take(ESSENTIALS_DISPLAY_COUNT),
    onSaveMyMenu: (List<EssentialItem>) -> Unit = {},
    onSelectOrder: (OrderHistoryResponse) -> Unit = {},
    onNavigate: (String) -> Unit = {},
) {
    Box(modifier = Modifier.fillMaxSize()) {
        if (uiState.isLoading) {
            LoadingDialogContent()
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {
                // 頂部狀態區 (Header) — 固定不滾動
                HeaderSection(userName = uiState.userInfo.userName, onNavigate = onNavigate)

                // Header 以下的區域可滾動
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                ) {
                    Spacer(modifier = Modifier.height(8.dp))
                    BalanceCard(
                        cashBalance = uiState.userInfo.cashBalance,
                        tokenBalance = uiState.userInfo.tokenBalance
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    XEssentialsCard(
                        myMenuItems = myMenuItems,
                        onSaveMyMenu = onSaveMyMenu
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    // TODO: D. 任務與行銷橫幅
                    QuestCard()
                    Spacer(modifier = Modifier.height(10.dp))
                    RecentActivity(
                        orders = uiState.orders,
                        onItemClick = { order ->
                            onSelectOrder(order)
                            onNavigate(Routes.transactionDetail(order.orderId))
                        }
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0A0E1A)
@Composable
private fun HomeScreenPreview() {
    MaterialTheme {
        HomeScreenContent(
            uiState = HomeUiState(
                isLoadingUserInfo = false,
                isLoadingOrders = false,
                userInfo = UserInfoResponse(
                    userId = "U12345",
                    userName = "Hank Liu",
                    userPhone = "0912345678",
                    userEmail = "test@example.com",
                    cashBalance = 12500.0,
                    tokenBalance = 888.0,
                    nickName = "hankHaHa"
                ),
                orders = emptyList()
            )
        )
    }
}

// ── Header ──────────────────────────────────────────────────────────────────

@Composable
fun HeaderSection(userName: String, onNavigate: (String) -> Unit = {}) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 24.dp, bottom = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = stringResource(R.string.home_greeting, userName),
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
//            modifier = Modifier.offset(x = 12.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Notifications,
                contentDescription = stringResource(R.string.home_notifications_desc),
                tint = Color.Gray,
                modifier = Modifier
                    .size(24.dp)
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) { onNavigate(Routes.NOTIFICATIONS) }
            )
            Spacer(modifier = Modifier.width(8.dp))
            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = stringResource(R.string.home_settings_desc),
                tint = Color.Gray,
                modifier = Modifier
                    .size(24.dp)
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) { onNavigate(Routes.SETTINGS) }
            )
        }
    }
}
