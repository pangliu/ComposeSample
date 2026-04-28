package com.example.newproject.ui.login

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.newproject.ui.theme.NeonCyan
import com.example.newproject.ui.theme.NeonPurple
import com.example.newproject.ui.theme.WelcomeBackground

val NeonBlue = Color(0xFF2E88FF)

@Composable
fun DrawerMenuContent(onClose: () -> Unit) {
    // 置中外層 Box，佔滿可用空間但背景透明
    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClose
            ),
        contentAlignment = Alignment.Center
    ) {
        // 抽屜內容容器，依內容高度自適應
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = {} // 攔截點擊事件，避免傳遞給外層 Box
                )
                .background(
                    color = WelcomeBackground.copy(alpha = 0.95f),
                    shape = RoundedCornerShape(topEnd = 32.dp, bottomEnd = 32.dp)
                )
                .padding(horizontal = 24.dp, vertical = 24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Tara! 大標題
            Text(
                text = "Tara!",
                color = Color.White,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                style = TextStyle(shadow = Shadow(
                    color = NeonCyan.copy(alpha = 0.6f),
                    blurRadius = 25f
                )
                )
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Account Status 藍綠色區塊
            MenuCard(
                title = "Account Status",
                color = NeonCyan,
                items = listOf(
                    MenuItem(Icons.Default.Search, "Check Application Progress", "Check Application Status"),
                    MenuItem(Icons.Default.Person, "Verify My Identity", "Verify Identity")
                )
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Product Features 紫色區塊
            MenuCard(
                title = "Product Features",
                color = NeonPurple,
                items = listOf(
                    MenuItem(Icons.Default.Refresh, "Real-time FX Rates", null),
                    MenuItem(Icons.Default.Star, "Explore Xcash Features", null)
                )
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Help & Policies 藍色區塊
            MenuCard(
                title = "Help & Policies",
                color = NeonBlue,
                items = listOf(
                    MenuItem(Icons.Default.Email, "Help Center", "Help Center"),
                    MenuItem(Icons.Default.Lock, "User Terms & Policies", "Security & Privacy")
                )
            )
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

data class MenuItem(val icon: ImageVector, val title: String, val subtitle: String?)

@Composable
fun MenuCard(title: String, color: Color, items: List<MenuItem>) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(elevation = 15.dp, shape = RoundedCornerShape(16.dp), spotColor = color.copy(alpha = 0.5f)),
        colors = CardDefaults.cardColors(containerColor = WelcomeBackground),
        border = BorderStroke(1.5.dp, color),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp).fillMaxWidth()) {
            Text(text = title, color = color, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))
            items.forEachIndexed { index, item ->
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth().clickable { }) {
                    Icon(imageVector = item.icon, contentDescription = item.title, tint = color, modifier = Modifier.size(24.dp))
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(text = item.title, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Medium)
                        if (item.subtitle != null) {
                            Text(text = item.subtitle, color = Color.LightGray, fontSize = 10.sp)
                        }
                    }
                }
                if (index < items.size - 1) {
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DrawerMenuContentPreview() {
    MaterialTheme {
        DrawerMenuContent(onClose = {})
    }
}
