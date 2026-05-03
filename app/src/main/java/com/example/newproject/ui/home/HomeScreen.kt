package com.example.newproject.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.newproject.network.model.response.UserInfoResponse
import com.example.newproject.ui.theme.NeonCyan
import com.example.newproject.ui.theme.WelcomeBackground

@Composable
fun HomeScreen(viewModel: HomeViewModel) {
    val state by viewModel.homeState.collectAsState()
    HomeScreenContent(state = state)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenContent(state: HomeState) {
    Scaffold(
        containerColor = WelcomeBackground,
        contentColor = Color.White,
        bottomBar = { CustomBottomNavigation() }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (val s = state) {
                is HomeState.Loading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                is HomeState.Error -> {
                    Text(
                        text = "Error: ${s.message}",
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

                        Spacer(modifier = Modifier.height(20.dp))

                        // B. 總資產卡片
                        BalanceCard(
                            cashBalance = s.userInfo.cashBalance,
                            tokenBalance = s.userInfo.tokenBalance
                        )

                        // TODO: C. 快捷功能區
                        // TODO: D. 任務與行銷橫幅
                    }
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
                text = "Hi, $userName",
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
                    contentDescription = "Notifications",
                    tint = Color.Gray,
                    modifier = Modifier.size(28.dp)
                )
            }
            IconButton(onClick = { /* TODO: 系統設定 */ }) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Settings",
                    tint = Color.Gray,
                    modifier = Modifier.size(28.dp)
                )
            }
        }
    }
}

// ── Balance Card ─────────────────────────────────────────────────────────────

@Composable
fun BalanceCard(cashBalance: Double, tokenBalance: Double) {
    var isBalanceHidden by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(elevation = 20.dp, spotColor = Color(0xFF7B2FBE).copy(alpha = 0.6f), shape = RoundedCornerShape(20.dp))
            .clip(RoundedCornerShape(20.dp))
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color(0xFF1A1040),
                        Color(0xFF0D1B35),
                        Color(0xFF0A1228)
                    )
                )
            )
            .border(1.dp, Color(0xFF4A3080).copy(alpha = 0.8f), RoundedCornerShape(20.dp))
            .padding(horizontal = 20.dp, vertical = 18.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {

            // ── 第一行：BALANCE 標題 + 眼睛 + Cash In 按鈕 ──
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 左側：BALANCE 標題 + 鎖圖示
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "BALANCE",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Toggle Balance",
                        tint = if (isBalanceHidden) NeonCyan else Color.Gray,
                        modifier = Modifier
                            .size(18.dp)
                            .clickable { isBalanceHidden = !isBalanceHidden }
                    )
                }

                // 右側：Cash In 綠色膠囊按鈕
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50.dp))
                        .background(Color(0xFF00C853))
                        .clickable { /* TODO: Cash In */ }
                        .padding(horizontal = 14.dp, vertical = 7.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Cash In",
                            tint = Color.Black,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Cash In",
                            color = Color.Black,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // ── 第二行：大金額 + Send 粉紫色膠囊按鈕 ──
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (isBalanceHidden) "TWD ••••••" else "TWD ${String.format("%,.0f", cashBalance)}",
                    color = Color.White,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.ExtraBold
                )

                // Send 粉紫色膠囊按鈕
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50.dp))
                        .background(Color(0xFFCC00AA))
                        .clickable { /* TODO: Send */ }
                        .padding(horizontal = 14.dp, vertical = 7.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Send,
                            contentDescription = "Send",
                            tint = Color.White,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Send",
                            color = Color.White,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // ── 第三行：Token 金幣 + 數量 + Balance Switch 按鈕 ──
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 左側：金幣圓圈 + Token 數量
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .shadow(6.dp, CircleShape, spotColor = Color(0xFFFFD700))
                            .background(
                                brush = Brush.radialGradient(
                                    colors = listOf(Color(0xFFFFD700), Color(0xFFFF8C00))
                                ),
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "X",
                            color = Color(0xFF4A0080),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = if (isBalanceHidden) "••••" else String.format("%,.0f", tokenBalance),
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // 右側：Balance Switch 深色膠囊按鈕
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50.dp))
                        .background(Color(0xFF1E1E3A))
                        .border(1.dp, Color(0xFF4A3080), RoundedCornerShape(50.dp))
                        .clickable { /* TODO: Balance Switch */ }
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Balance Switch",
                            tint = Color.Gray,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Balance Switch",
                            color = Color.Gray,
                            fontSize = 11.sp
                        )
                    }
                }
            }
        }
    }
}

// ── Bottom Navigation ────────────────────────────────────────────────────────

@Composable
fun CustomBottomNavigation() {
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
            HorizontalDivider(color = Color(0xFF1A2235), thickness = 1.dp)
        }

        // 所有的 Item 放同一排，以底部對齊文字
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            BottomNavItem(icon = Icons.Default.Home, title = "Home", isSelected = true, onClick = { /* TODO */ })
            BottomNavItem(icon = Icons.Default.List, title = "Cards", isSelected = false, onClick = { /* TODO */ })
            ScanAndPayFab()
            BottomNavItem(icon = Icons.Default.Star, title = "Quests", isSelected = false, onClick = { /* TODO */ })
            BottomNavItem(icon = Icons.Default.Person, title = "Profile", isSelected = false, onClick = { /* TODO */ })
        }
    }
}

@Composable
fun BottomNavItem(icon: androidx.compose.ui.graphics.vector.ImageVector, title: String, isSelected: Boolean, onClick: () -> Unit) {
    val color = if (isSelected) NeonCyan else Color.Gray
    val fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal

    Column(
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = color,
            modifier = Modifier.size(28.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = title,
            color = color,
            fontSize = 12.sp,
            fontWeight = fontWeight
        )
    }
}

@Composable
fun ScanAndPayFab() {
    Box(
        modifier = Modifier
            .clickable { /* TODO: 點擊開啟 Scan / Pay */ }
            .padding(8.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        // 大圓圈按鈕，用 offset 往上提，避免佔用排版高度而被擠壓成橢圓形
        Box(
            modifier = Modifier
                .offset(y = (-22).dp)
                .size(64.dp)
                .shadow(elevation = 8.dp, spotColor = NeonCyan, shape = CircleShape)
                .border(3.dp, NeonCyan, CircleShape)
                .background(Color(0xFF0A0F1A), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .border(1.dp, NeonCyan.copy(alpha = 0.5f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Scan Icon",
                    tint = Color.White,
                    modifier = Modifier.size(28.dp)
                )
            }
        }

        // 文字固定在最底部
        Text(
            text = "Scan / Pay",
            color = NeonCyan,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
