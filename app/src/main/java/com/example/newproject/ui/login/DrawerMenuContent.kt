package com.example.newproject.ui.login

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.Dialog
import com.example.newproject.R
import com.example.newproject.ui.theme.NeonCyan
import com.example.newproject.ui.theme.NeonPurple
import com.example.newproject.ui.theme.WelcomeBackground

val NeonBlue = Color(0xFF2E88FF)

@Composable
fun DrawerMenuContent(onClose: () -> Unit) {
    var showAccountStatusDialog by remember { mutableStateOf(false) }

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
                text = stringResource(id = R.string.tara),
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
                title = stringResource(id = R.string.account_status),
                color = NeonCyan,
                items = listOf(
                    MenuItem(Icons.Default.Search, stringResource(id = R.string.check_application_progress), stringResource(id = R.string.check_application_status)),
                    MenuItem(Icons.Default.Person, stringResource(id = R.string.verify_my_identity), stringResource(id = R.string.verify_identity))
                ),
                onClick = { showAccountStatusDialog = true }
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Product Features 紫色區塊
            MenuCard(
                title = stringResource(id = R.string.product_features),
                color = NeonPurple,
                items = listOf(
                    MenuItem(Icons.Default.Refresh, stringResource(id = R.string.real_time_fx_rates), null),
                    MenuItem(Icons.Default.Star, stringResource(id = R.string.explore_xcash_features), null)
                )
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Help & Policies 藍色區塊
            MenuCard(
                title = stringResource(id = R.string.help_policies),
                color = NeonBlue,
                items = listOf(
                    MenuItem(Icons.Default.Email, stringResource(id = R.string.help_center), stringResource(id = R.string.help_center)),
                    MenuItem(Icons.Default.Lock, stringResource(id = R.string.user_terms_policies), stringResource(id = R.string.security_privacy))
                )
            )
            Spacer(modifier = Modifier.height(24.dp))
        }
    }

    if (showAccountStatusDialog) {
        AccountStatusDialog(onDismiss = { showAccountStatusDialog = false })
    }
}


data class MenuItem(val icon: ImageVector, val title: String, val subtitle: String?)

@Composable
fun MenuCard(title: String, color: Color, items: List<MenuItem>, onClick: (() -> Unit)? = null) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(elevation = 15.dp, shape = RoundedCornerShape(16.dp), spotColor = color.copy(alpha = 0.5f))
            .let { if (onClick != null) it.clickable { onClick() } else it },
        colors = CardDefaults.cardColors(containerColor = WelcomeBackground),
        border = BorderStroke(1.5.dp, color),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp).fillMaxWidth()) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = title, 
                    color = color, 
                    fontSize = 16.sp, 
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                if (onClick != null) {
//                    Icon(
//                        imageVector = Icons.Default.KeyboardArrowRight,
//                        contentDescription = "Open",
//                        tint = color
//                    )
                }
            }
            if (items.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                items.forEachIndexed { index, item ->
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
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
}

@Preview(showBackground = true)
@Composable
fun DrawerMenuContentPreview() {
    MaterialTheme {
        DrawerMenuContent(onClose = {})
    }
}
