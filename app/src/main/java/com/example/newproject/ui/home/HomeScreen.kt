package com.example.newproject.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
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
                        
                        // TODO: B. 總資產卡片
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
                    cashBalance = 12500.50,
                    tokenBalance = 8888.0
                )
            )
        )
    }
}

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
            modifier = Modifier.offset(x = 12.dp) // 抵銷 IconButton 內建的 padding，讓圖示視覺上更靠邊緣
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
            modifier = Modifier.size(28.dp) // 放大 icon
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
                .offset(y = (-22).dp) // 視覺上往上提，遠離文字
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
