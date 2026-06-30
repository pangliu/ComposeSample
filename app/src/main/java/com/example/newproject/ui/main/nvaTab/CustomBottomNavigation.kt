package com.example.newproject.ui.main.nvaTab

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.newproject.ui.theme.LocalAppColors
import com.example.newproject.ui.theme.tabActiveColor

private val NavBarHeight = 60.dp

@Composable
    fun CustomBottomNavigation(
    selectedIndex: Int,
    onTabSelected: (Int) -> Unit,
    onScanPayClick: () -> Unit = {}
) {
    val colors = LocalAppColors.current
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(NavBarHeight), // 固定高度，讓 Scaffold 計算正確的 bottom padding
        contentAlignment = Alignment.BottomCenter
    ) {
        // Nav bar 背景列：中間用 Spacer 佔位，給 ScanAndPayTab overlay 用
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(NavBarHeight)
                .background(colors.bg.page),
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
            // 中間佔位：與其他四個 item 等寬，使 ScanAndPayTab 能水平置中
            Spacer(modifier = Modifier.weight(1f))
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

        // 掃碼按鈕：overlay 在 Box 底部正中央
        // wrapContentHeight(unbounded = true) 讓 Column 突破父層 60dp 限制往上延伸，
        // 圖片因此自然超出 nav bar 高度，不需要 offset 或 requiredSize
        ScanAndPayTab(isSelected = selectedIndex == 4, onClick = onScanPayClick)
    }
}

@Composable
private fun BottomNavItem(
    defaultIconRes: Int,
    activeIconRes: Int,
    title: String,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val isActive = isPressed || isSelected
    val color = if (isActive) tabActiveColor else Color.Gray
    val fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
    val iconRes = if (isActive) activeIconRes else defaultIconRes

    Column(
        modifier = modifier
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
            .padding(bottom = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = painterResource(id = iconRes),
            contentDescription = title,
            tint = Color.Unspecified,
            modifier = Modifier.size(24.dp)
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
private fun ScanAndPayTab(modifier: Modifier = Modifier, isSelected: Boolean = false, onClick: () -> Unit = {}) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val isActive = isPressed || isSelected
    val color = if (isActive) tabActiveColor else Color.Gray
    val fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal

    Column(
        modifier = modifier
            .width(80.dp)
            // unbounded = true：Column 突破父層高度上限，可以往 nav bar 上方延伸
            .wrapContentHeight(align = Alignment.Bottom, unbounded = true)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
            .padding(bottom = 8.dp), // 與 BottomNavItem 相同的底部 padding，確保文字對齊
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 掃碼圖片：不設高度，由圖片原始比例決定，ContentScale.FillWidth 填滿 80dp 寬
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(R.mipmap.bg_scanner),
                contentDescription = null,
                contentScale = ContentScale.FillWidth,
                modifier = Modifier.fillMaxWidth()
            )
            Icon(
                painter = painterResource(R.mipmap.ic_scanner),
                contentDescription = stringResource(R.string.home_scan_icon_desc),
                tint = Color.Unspecified,
                modifier = Modifier.size(24.dp)
            )
        }
        Text(
            text = stringResource(R.string.home_scan_pay),
            color = color,
            fontSize = 12.sp,
            fontWeight = fontWeight,
            maxLines = 1,
            softWrap = false
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CustomBottomNavigationPreview() {
    var selectedIndex by remember { mutableStateOf(0) }
    MaterialTheme {
        CustomBottomNavigation(
            selectedIndex = selectedIndex,
            onTabSelected = { selectedIndex = it }
        )
    }
}
