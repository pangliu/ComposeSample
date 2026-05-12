package com.example.newproject.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
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
import com.example.newproject.ui.home.balance.BalanceCard
import com.example.newproject.ui.home.quests.QuestCard
import com.example.newproject.ui.profile.ProfileScreen
import com.example.newproject.ui.profile.ProfileViewModel
import com.example.newproject.ui.quests.QuestsScreen
import com.example.newproject.ui.theme.NeonCyan
import com.example.newproject.ui.theme.TabActiveColor
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
        is HomeState.Loading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
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
                    .padding(horizontal = 24.dp, vertical = 16.dp)
            ) {
                // 頂部狀態區 (Header)
                HeaderSection(userName = s.userInfo.userName)

                Spacer(modifier = Modifier.height(10.dp))

                // B. 總資產卡片
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
                QuestCard()
                // TODO: D. 任務與行銷橫幅
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

// ── Bottom Navigation ────────────────────────────────────────────────────────

@Composable
fun CustomBottomNavigation(
    selectedIndex: Int,
    onTabSelected: (Int) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(90.dp), // 留出空間讓中間按鈕可以超出
        contentAlignment = Alignment.BottomCenter
    ) {
        // 導覽列背景層
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
                .background(WelcomeBackground)
        ) {
//            HorizontalDivider(color = NavDivider, thickness = 1.dp)
        }

        // 所有的 Item 放同一排，以底部對齊文字
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            BottomNavItem(
                modifier = Modifier.weight(1f),
                defaultIconRes = R.mipmap.ic_home,
                activeIconRes = R.mipmap.ic_home_active,
                title = stringResource(R.string.home_nav_home), 
                isSelected = selectedIndex == 0, 
                onClick = { onTabSelected(0) }
            )
            BottomNavItem(
                modifier = Modifier.weight(1f),
                defaultIconRes = R.mipmap.ic_card,
                activeIconRes = R.mipmap.ic_card_active,
                title = stringResource(R.string.home_nav_cards), 
                isSelected = selectedIndex == 1, 
                onClick = { onTabSelected(1) }
            )
            ScanAndPayFab()
            BottomNavItem(
                modifier = Modifier.weight(1f),
                defaultIconRes = R.mipmap.ic_quest,
                activeIconRes = R.mipmap.ic_quest_active,
                title = stringResource(R.string.home_nav_quests), 
                isSelected = selectedIndex == 2, 
                onClick = { onTabSelected(2) }
            )
            BottomNavItem(
                modifier = Modifier.weight(1f),
                defaultIconRes = R.mipmap.ic_profile,
                activeIconRes = R.mipmap.ic_profile_active,
                title = stringResource(R.string.home_nav_profile), 
                isSelected = selectedIndex == 3, 
                onClick = { onTabSelected(3) }
            )
        }
    }
}

@Composable
fun BottomNavItem(
    defaultIconRes: Int,
    activeIconRes: Int,
    title: String,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    
    // 依據狀態決定文字顏色與要顯示的圖片
    val isActive = isPressed || isSelected
    val color = if (isActive) TabActiveColor else Color.Gray
    val fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
    val iconRes = if (isActive) activeIconRes else defaultIconRes

    Column(
        modifier = modifier
            .clickable(
                interactionSource = interactionSource,
                indication = null, // 移除預設的方塊水波紋
                onClick = onClick
            )
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = androidx.compose.ui.res.painterResource(id = iconRes),
            contentDescription = title,
            tint = Color.Unspecified, // 使用圖片原始顏色
            modifier = Modifier.size(28.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = title,
            color = color,
            fontSize = 12.sp,
            fontWeight = fontWeight,
            maxLines = 1,
            softWrap = false
        )
    }
}

@Composable
fun ScanAndPayFab(modifier: Modifier = Modifier) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    // 掃碼按鈕被按壓時的顏色變化
    val textColor = Color.White // 文字永遠保持白色
    val iconColor = if (isPressed) NeonCyan else Color.White

    Box(
        modifier = modifier
            .clickable(
                interactionSource = interactionSource,
                indication = null, // 移除預設的方塊水波紋
                onClick = { /* TODO: 點擊開啟 Scan / Pay */ }
            )
            .padding(8.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        // 大圓圈按鈕，用 offset 往上提，避免佔用排版高度而被擠壓成橢圓形
        Box(
            modifier = Modifier
                .offset(y = (-22).dp)
                .width(80.dp)
                .height(64.dp),
//                .size(64.dp),
//                .shadow(elevation = 8.dp, spotColor = NeonCyan, shape = CircleShape)
//                .border(3.dp, NeonCyan, CircleShape)
//                .background(DarkOverlay, CircleShape),
            contentAlignment = Alignment.Center
        ) {

            Image(
                painter = painterResource(R.mipmap.bg_scanner),
                contentDescription = null,
                contentScale = ContentScale.FillWidth,
                modifier = Modifier.fillMaxWidth()
            )
            Icon(
//                imageVector = Icons.Default.Add,
                painter = painterResource(R.mipmap.ic_scanner),
                contentDescription = stringResource(R.string.home_scan_icon_desc),
                tint = Color.Unspecified,
                modifier = Modifier.size(24.dp)
            )
//            }
        }

        // 文字固定在最底部
        Text(
            text = stringResource(R.string.home_scan_pay),
            color = textColor,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            softWrap = false
        )
    }
}
