package com.example.newproject.ui.home.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.newproject.R
import com.example.newproject.ui.home.dialog.EditEssentialsDialog
import com.example.newproject.ui.home.essential.ESSENTIALS_DISPLAY_COUNT
import com.example.newproject.ui.home.essential.EssentialItem
import com.example.newproject.ui.home.essential.ITEMS_PER_PAGE
import com.example.newproject.ui.home.essential.allEssentialItems
import com.example.newproject.ui.theme.LocalAppColors
import com.example.newproject.ui.theme.cardBorder
import com.example.newproject.ui.theme.cardGradientMid
import com.example.newproject.ui.theme.cardGradientStart
import com.example.newproject.ui.theme.darkBackground
import com.example.newproject.ui.theme.essentialCardTitle

private val cardGradientEnd = Color(0xFF0A1228)
private val essentialEdit = Color(0xFF3E4155)
private val essentialMore = Color(0xFF48B4C9)

// ── X-Essentials 快捷功能區 ──────────────────────────────────────────────────

@Composable
fun XEssentialsCard(
    myMenuItems: List<EssentialItem>,
    onSaveMyMenu: (List<EssentialItem>) -> Unit,
) {
    val colors = LocalAppColors.current
    // 將 myMenuItems 依照每頁 8 個分頁
    val pages = myMenuItems.chunked(ITEMS_PER_PAGE)
    val pagerState = rememberPagerState(pageCount = { pages.size })
    var showEditDialog by remember { mutableStateOf(false) }
    Column {
        // ── 標題列：X-Essentials + More / Edit ──
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.essentials_title),
                color = essentialCardTitle,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                // More 按鈕
//                Box(
//                    modifier = Modifier
//                        .clip(RoundedCornerShape(10.dp))
//                        .border(
//                            width = 2.dp,
//                            color = essentialMore,
//                            shape = RoundedCornerShape(10.dp)
//                        )
//                        .clickable { /* TODO: More */ }
//                        .padding(horizontal = 16.dp, vertical = 6.dp)
//                ) {
//                    Text(
//                        text = stringResource(R.string.essentials_more),
//                        color = essentialMore,
//                        fontSize = 13.sp,
//                        fontWeight = FontWeight.Medium
//                    )
//                }

                // Edit 按鈕
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(essentialEdit)
                        .clickable { showEditDialog = true }
                        .padding(horizontal = 16.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = stringResource(R.string.essentials_edit),
                        color = colors.onBackground,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // ── 可左右滑動的功能圖示 Pager ──
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            cardGradientStart.copy(alpha = 0.7f),
                            cardGradientMid.copy(alpha = 0.7f),
                            cardGradientEnd.copy(alpha = 0.7f)
                        )
                    )
                )
                .border(1.dp, cardBorder.copy(alpha = 0.5f), RoundedCornerShape(20.dp))
                .padding(horizontal = 8.dp, vertical = 8.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier.fillMaxWidth()
                ) { pageIndex ->
                    val pageItems = pages[pageIndex]
                    // 每頁分成上下兩排
                    val firstRow = pageItems.take(4)
                    val secondRow = pageItems.drop(4)

                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.height(150.dp) // 固定高度：兩排 70dp + 8dp 間距
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            firstRow.forEach { item -> EssentialItemView(item) }
                            // 若不足 4 個，用空白佔位
                            repeat(4 - firstRow.size) {
                                Spacer(modifier = Modifier.width(70.dp))
                            }
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            secondRow.forEach { item -> EssentialItemView(item) }
                            repeat(4 - secondRow.size) {
                                // 空白佔位，維持與 EssentialItemView 相同寬高
                                Spacer(modifier = Modifier.width(70.dp).height(70.dp))
                            }
                        }
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        // ── Page Indicator (圓點指示器) ──
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            repeat(pages.size) { index ->
                val isSelected = pagerState.currentPage == index
                // 外層固定大小，避免切換時高度跳動
                Box(
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                        .size(8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(if (isSelected) 8.dp else 6.dp)
                            .clip(CircleShape)
                            .background(
                                if (isSelected) colors.primary
                                else Color.White.copy(alpha = 0.3f)
                            )
                    )
                }
            }
        }
    }

    // ── 編輯 Dialog ──
    if (showEditDialog) {
        // 計算不在 myMenu 中的 items 為 Others
        val otherItems = allEssentialItems.filter { it !in myMenuItems }

        EditEssentialsDialog(
            myMenuItems = myMenuItems,
            otherItems = otherItems,
            onDismissWithResult = { updatedMyMenu ->
                onSaveMyMenu(updatedMyMenu)
                showEditDialog = false
            }
        )
    }
}

@Composable
fun EssentialItemView(item: EssentialItem) {
    val colors = LocalAppColors.current
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(70.dp)
            .clickable { item.onClick() }
    ) {
        // 圖示方塊
        Box(
            modifier = Modifier
                .width(60.dp)
                .height(50.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            cardGradientMid,
                            cardGradientStart
                        )
                    )
                )
                .border(
                    width = 1.5.dp,
                    brush = Brush.linearGradient(
                        colors = listOf(
                            colors.primary.copy(alpha = 0.4f),
                            colors.secondary.copy(alpha = 0.8f)
                        )
                    ),
                    shape = RoundedCornerShape(16.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            if (item.iconVector != null) {
                Icon(
                    imageVector = item.iconVector,
                    contentDescription = item.label,
                    tint = colors.primary,
                    modifier = Modifier.size(30.dp)
                )
            } else if (item.iconRes != null) {
                val tint = if (item.useOriginalColor) Color.Unspecified else colors.primary
                Icon(
                    painter = painterResource(id = item.iconRes),
                    contentDescription = item.label,
                    tint = tint,
                    modifier = Modifier.size(30.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(3.dp))

        // 標籤文字
        Text(
            text = item.label,
            color = colors.onBackground,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0A0E1A)
@Composable
fun XEssentialsCardPreview() {
    MaterialTheme {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(darkBackground)
                .padding(16.dp)
        ) {
            XEssentialsCard(
                myMenuItems = allEssentialItems.take(ESSENTIALS_DISPLAY_COUNT),
                onSaveMyMenu = {}
            )
        }
    }
}
