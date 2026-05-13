package com.example.newproject.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.newproject.R
import com.example.newproject.network.model.response.UserInfoResponse
import com.example.newproject.ui.home.essential.EssentialItem
import com.example.newproject.ui.home.essential.XEssentialsCard
import com.example.newproject.ui.home.essential.allEssentialItems
import com.example.newproject.ui.home.essential.ESSENTIALS_DISPLAY_COUNT
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.newproject.ui.cards.CardsScreen
import com.example.newproject.ui.components.LoadingDialogContent
import com.example.newproject.ui.home.balance.BalanceCard
import com.example.newproject.ui.home.nvaTab.CustomBottomNavigation
import com.example.newproject.ui.home.quests.QuestCard
import com.example.newproject.ui.home.recent.RecentActivity
import com.example.newproject.ui.profile.ProfileScreen
import com.example.newproject.ui.profile.ProfileViewModel
import com.example.newproject.ui.quests.QuestsScreen
import com.example.newproject.ui.theme.WelcomeBackground

@Composable
fun HomeScreen(viewModel: HomeViewModel) {
    val state by viewModel.homeState.collectAsState()
    val myMenuItems by viewModel.myMenuItems.collectAsState()
    HomeScreenContent(
        state = state,
        myMenuItems = myMenuItems,
        onSaveMyMenu = viewModel::saveMyMenu
        /**
         * Method Reference 寫法效果等同於
         * onSaveMyMenu = { items ->
         *         viewModel.saveMyMenu(items)
         * }
         */
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenContent(
    state: HomeState,
    myMenuItems: List<EssentialItem> = allEssentialItems.take(ESSENTIALS_DISPLAY_COUNT),
    onSaveMyMenu: (List<EssentialItem>) -> Unit = {},
) {
    var selectedIndex by remember { mutableStateOf(0) }

    Scaffold(
        containerColor = WelcomeBackground,
        contentColor = Color.White,
        bottomBar = { 
            CustomBottomNavigation(
                selectedIndex = selectedIndex,
                onTabSelected = { selectedIndex = it }
            ) 
        }
    ) { paddingValues ->
        AnimatedContent(
            targetState = selectedIndex,
            transitionSpec = {
                if (targetState > initialState) {
                    slideInHorizontally { it } togetherWith slideOutHorizontally { -it }
                } else {
                    slideInHorizontally { -it } togetherWith slideOutHorizontally { it }
                }
            },
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            label = "tab_content"
        ) { index ->
            Box(modifier = Modifier.fillMaxSize()) {
                when (index) {
                    0 -> HomeTabContent(
                        state = state,
                        myMenuItems = myMenuItems,
                        onSaveMyMenu = onSaveMyMenu
                    )
                    1 -> CardsScreen()
                    2 -> QuestsScreen()
                    3 -> {
                        val profileViewModel: ProfileViewModel = hiltViewModel()
                        ProfileScreen(viewModel = profileViewModel)
                    }
                }
            }
        }
    }
}

@Composable
fun BoxScope.HomeTabContent(
    state: HomeState,
    myMenuItems: List<EssentialItem>,
    onSaveMyMenu: (List<EssentialItem>) -> Unit
) {
    when (val s = state) {
        is HomeState.Loading -> LoadingDialogContent()
        is HomeState.Error -> {
            Text(
                text = stringResource(R.string.home_error_message, s.message ?: ""),
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.align(Alignment.Center)
            )
        }
        is HomeState.Success -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {
                // 頂部狀態區 (Header) — 固定不滾動
                HeaderSection(userName = s.userInfo.userName)

//                Spacer(modifier = Modifier.height(16.dp))

                // Header 以下的區域可滾動
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                ) {
                    // B. 總資產卡片
                    Spacer(modifier = Modifier.height(16.dp))
                    BalanceCard(
                        cashBalance = s.userInfo.cashBalance,
                        tokenBalance = s.userInfo.tokenBalance
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    // C. X-Essentials 快捷功能區
                    XEssentialsCard(
                        myMenuItems = myMenuItems,
                        onSaveMyMenu = onSaveMyMenu
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    // TODO: D. 任務與行銷橫幅
                    QuestCard()
                    Spacer(modifier = Modifier.height(10.dp))
                    RecentActivity()
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    MaterialTheme {
        HomeScreenContent(
            state = HomeState.Success(
                userInfo = UserInfoResponse(
                    userId = "U12345",
                    userName = "Hank Liu",
                    userPhone = "0912345678",
                    userEmail = "test@example.com",
                    cashBalance = 12500.0,
                    tokenBalance = 888.0
                )
            )
        )
    }
}

// ── Header ──────────────────────────────────────────────────────────────────

@Composable
fun HeaderSection(userName: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 使用者問候
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = stringResource(R.string.home_greeting, userName),
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        }

        // 右側圖示
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.offset(x = 12.dp)
        ) {
            IconButton(onClick = { /* TODO: 通知中心 */ }) {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = stringResource(R.string.home_notifications_desc),
                    tint = Color.Gray,
                    modifier = Modifier.size(28.dp)
                )
            }
            IconButton(onClick = { /* TODO: 系統設定 */ }) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = stringResource(R.string.home_settings_desc),
                    tint = Color.Gray,
                    modifier = Modifier.size(28.dp)
                )
            }
        }
    }
}

