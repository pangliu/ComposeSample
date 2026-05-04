package com.example.newproject.ui.home.essential

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.newproject.ui.theme.CardBorder
import com.example.newproject.ui.theme.CardGradientEnd
import com.example.newproject.ui.theme.CardGradientMid
import com.example.newproject.ui.theme.CardGradientStart
import com.example.newproject.ui.theme.DarkBackground
import com.example.newproject.ui.theme.NeonCyan
import com.example.newproject.ui.theme.NeonPurple

// ── X-Essentials 快捷功能區 ──────────────────────────────────────────────────

@Composable
fun XEssentialsCard() {
    // 可變動的 My Menu 清單（初始取前 16 個）
    var myMenuItems by remember {
        mutableStateOf(allEssentialItems.take(ESSENTIALS_DISPLAY_COUNT))
    }

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
                text = "X-Essentials",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                // More 按鈕
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(
                                    NeonCyan.copy(alpha = 0.2f),
                                    NeonPurple.copy(alpha = 0.2f)
                                )
                            )
                        )
                        .border(1.dp, NeonCyan.copy(alpha = 0.5f), RoundedCornerShape(20.dp))
                        .clickable { /* TODO: More */ }
                        .padding(horizontal = 16.dp, vertical = 6.dp)
                ) {
                    Text("More", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                }

                // Edit 按鈕
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(
                                    NeonCyan.copy(alpha = 0.2f),
                                    NeonPurple.copy(alpha = 0.2f)
                                )
                            )
                        )
                        .border(1.dp, NeonCyan.copy(alpha = 0.5f), RoundedCornerShape(20.dp))
                        .clickable { showEditDialog = true }
                        .padding(horizontal = 16.dp, vertical = 6.dp)
                ) {
                    Text("Edit", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Medium)
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
                            CardGradientStart.copy(alpha = 0.7f),
                            CardGradientMid.copy(alpha = 0.7f),
                            CardGradientEnd.copy(alpha = 0.7f)
                        )
                    )
                )
                .border(1.dp, CardBorder.copy(alpha = 0.5f), RoundedCornerShape(20.dp))
                .padding(horizontal = 8.dp, vertical = 16.dp)
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
                        modifier = Modifier.height(148.dp) // 固定高度：兩排 70dp + 8dp 間距
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

                Spacer(modifier = Modifier.height(16.dp))

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
                                        if (isSelected) NeonCyan
                                        else Color.White.copy(alpha = 0.3f)
                                    )
                            )
                        }
                    }
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
                myMenuItems = updatedMyMenu
                showEditDialog = false
            }
        )
    }
}

@Composable
fun EssentialItemView(item: EssentialItem) {
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
                            CardGradientStart,
                            CardGradientMid
                        )
                    )
                )
                .border(
                    width = 1.5.dp,
                    brush = Brush.linearGradient(
                        colors = listOf(
                            NeonPurple.copy(alpha = 0.8f),
                            NeonCyan.copy(alpha = 0.4f)
                        )
                    ),
                    shape = RoundedCornerShape(16.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = item.icon,
                contentDescription = item.label,
                tint = NeonCyan,
                modifier = Modifier.size(30.dp)
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        // 標籤文字
        Text(
            text = item.label,
            color = Color.White,
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
                .background(DarkBackground)
                .padding(16.dp)
        ) {
            XEssentialsCard()
        }
    }
}
