package com.qpay.xcash.ui.login.components

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
import com.qpay.xcash.ui.components.neonGlow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.annotation.DrawableRes
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.Dialog
import com.qpay.xcash.R
import com.qpay.xcash.ui.theme.AppTheme
import com.qpay.xcash.ui.theme.BlackGoldColors
import com.qpay.xcash.ui.theme.LocalAppColors
import com.qpay.xcash.ui.theme.NeonColors

@Composable
fun DrawerMenuContent(onClose: () -> Unit, onAccountStatusClick: () -> Unit = {}) {
    val colors = LocalAppColors.current
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
                    color = Color.Transparent,
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
                style = TextStyle(
                    shadow = Shadow(
                        color = colors.accent.primary.copy(alpha = 0.6f),
                        blurRadius = 25f
                    )
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Account Status 藍綠色區塊
            MenuCard(
                title = stringResource(id = R.string.account_status),
                titleColor = colors.drawer.accountCard.title,
                borderColor = colors.drawer.accountCard.border,
                items = listOf(
                    MenuItem(
                        icon = MenuIcon.Resource(R.mipmap.ic_check_progress),
                        title = stringResource(id = R.string.check_application_progress),
                        subtitle = stringResource(id = R.string.check_application_status)
                    ),
                    MenuItem(
                        icon = MenuIcon.Resource(R.mipmap.ic_verify_id),
                        title = stringResource(id = R.string.verify_my_identity),
                        subtitle = stringResource(id = R.string.verify_identity)
                    )
                ),
                onClick = onAccountStatusClick
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Product Features 紫色區塊
            MenuCard(
                title = stringResource(id = R.string.product_features),
                titleColor = colors.drawer.productCard.title,
                borderColor = colors.drawer.productCard.border,
                items = listOf(
                    MenuItem(
                        icon = MenuIcon.Resource(R.mipmap.ic_money_exchange),
                        title = stringResource(id = R.string.real_time_fx_rates),
                        subtitle = null
                    ),
                    MenuItem(
                        icon = MenuIcon.Resource(R.mipmap.ic_explore),
                        title = stringResource(id = R.string.explore_xcash_features),
                        subtitle = null
                    )
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Help & Policies 藍色區塊
            MenuCard(
                title = stringResource(id = R.string.help_policies),
                titleColor = colors.drawer.helpCard.title,
                borderColor = colors.drawer.helpCard.border,
                items = listOf(
                    MenuItem(
                        icon = MenuIcon.Resource(R.mipmap.ic_center_help),
                        title = stringResource(id = R.string.help_center),
                        subtitle = stringResource(id = R.string.help_center)
                    ),
                    MenuItem(
                        icon = MenuIcon.Resource(R.mipmap.ic_user_item),
                        title = stringResource(id = R.string.user_terms_policies),
                        subtitle = stringResource(id = R.string.security_privacy)
                    )
                )
            )
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}


sealed class MenuIcon {
    data class Vector(val imageVector: ImageVector) : MenuIcon()
    data class Resource(@DrawableRes val resId: Int, val useOriginalColor: Boolean = false) :
        MenuIcon()
}

data class MenuItem(
    val icon: MenuIcon,
    val title: String,
    val subtitle: String?
)

@Composable
fun MenuCard(
    title: String,
    titleColor: Color,
    borderColor: Color,
    items: List<MenuItem>,
    onClick: (() -> Unit)? = null
) {
    val colors = LocalAppColors.current
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .then(
                if (colors.effect.enableGlow) Modifier.neonGlow(
                    color = borderColor,
                    alpha = 0.5f,
                    glowRadius = 15.dp,
                    borderRadius = 16.dp
                ) else Modifier
            )
            .let { if (onClick != null) it.clickable { onClick() } else it },
        colors = CardDefaults.cardColors(containerColor = colors.bg.page),
        border = BorderStroke(1.5.dp, borderColor),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = title,
                    color = titleColor,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                if (onClick != null) {
//                    Icon(
//                        imageVector = Icons.Default.KeyboardArrowRight,
//                        contentDescription = "Open",
//                        tint = titleColor
//                    )
                }
            }
            if (items.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                items.forEachIndexed { index, item ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        when (val icon = item.icon) {
                            is MenuIcon.Vector -> Icon(
                                imageVector = icon.imageVector,
                                contentDescription = item.title,
                                tint = borderColor,
                                modifier = Modifier.size(35.dp)
                            )

                            is MenuIcon.Resource -> Icon(
                                painter = painterResource(icon.resId),
                                contentDescription = item.title,
                                tint = if (icon.useOriginalColor) Color.Unspecified else borderColor,
                                modifier = Modifier.size(35.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text(
                                text = item.title,
                                color = Color.White,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium
                            )
                            if (item.subtitle != null) {
                                Text(
                                    text = item.subtitle,
                                    color = Color.LightGray,
                                    fontSize = 10.sp
                                )
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

@Preview(name = "Neon", showBackground = true, backgroundColor = 0xFF030F1B)
@Composable
private fun DrawerMenuContentPreviewNeon() {
    AppTheme(colors = NeonColors) {
        DrawerMenuContent(onClose = {})
    }
}

@Preview(name = "Black Gold", showBackground = true, backgroundColor = 0xFF050505)
@Composable
private fun DrawerMenuContentPreviewBlackGold() {
    AppTheme(colors = BlackGoldColors) {
        DrawerMenuContent(onClose = {})
    }
}
