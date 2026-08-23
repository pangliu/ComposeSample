package com.qpay.xcash.ui.home.components

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
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.qpay.xcash.R
import com.qpay.xcash.ui.components.GradientText
import com.qpay.xcash.ui.home.dialog.EditEssentialsDialog
import com.qpay.xcash.ui.home.essential.ESSENTIALS_DISPLAY_COUNT
import com.qpay.xcash.ui.home.essential.EssentialItem
import com.qpay.xcash.ui.home.essential.ITEMS_PER_PAGE
import com.qpay.xcash.ui.home.essential.allEssentialItems
import com.qpay.xcash.ui.theme.AppTheme
import com.qpay.xcash.ui.theme.BlackGoldAssets
import com.qpay.xcash.ui.theme.BlackGoldColors
import com.qpay.xcash.ui.theme.LocalAppAssets
import com.qpay.xcash.ui.theme.LocalAppColors
import com.qpay.xcash.ui.theme.NeonAssets
import com.qpay.xcash.ui.theme.NeonColors
import com.qpay.xcash.ui.theme.plumPurple
import com.qpay.xcash.ui.theme.deepNavy
import com.qpay.xcash.ui.theme.paleCyan

private val essentialEdit = Color(0xFF3E4155)

// ── X-Essentials 快捷功能區 ──────────────────────────────────────────────────

@Composable
fun XEssentialsCard(
    myMenuItems: List<EssentialItem>,
    onSaveMyMenu: (List<EssentialItem>) -> Unit,
    onNavigate: (String) -> Unit = {},
) {
    val colors = LocalAppColors.current
    val assets = LocalAppAssets.current
    // 將 myMenuItems 依照每頁 8 個分頁
    val pages = myMenuItems.chunked(ITEMS_PER_PAGE)
    val pagerState = rememberPagerState(pageCount = { pages.size })
    var showEditDialog by remember { mutableStateOf(false) }
    var pagerHeight by remember { mutableStateOf(0.dp) }
    val density = LocalDensity.current
    Column {
        // ── 標題列：X-Essentials + More / Edit ──
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            GradientText(
                text = stringResource(R.string.essentials_title),
                color = paleCyan,
                brush = colors.gradient.silverShimmer,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                // Edit 按鈕
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(colors.home.essentialsCard.editTextBg)
                        .clickable { showEditDialog = true }
                        .border(
                            width = 1.dp,
                            color = colors.home.essentialsCard.editTextBorder,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = stringResource(R.string.essentials_edit),
                        color = colors.home.essentialsCard.editText,
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
                .aspectRatio(assets.essentialsCardAspectRatio)
                .paint(
                    painter = painterResource(id = assets.essentialsCardBackground),
                    contentScale = ContentScale.FillBounds
                )
                .padding(horizontal = 10.dp, vertical = 10.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .then(
                            if (pagerHeight > 0.dp)
                                Modifier.height(pagerHeight)
                            else
                                Modifier.onSizeChanged { size ->
                                    pagerHeight = with(density) { size.height.toDp() }
                                }
                        )
                ) { pageIndex ->
                    val pageItems = pages[pageIndex]
                    // 補齊到 ITEMS_PER_PAGE 個 null，確保兩排高度固定
                    val paddedItems: List<EssentialItem?> =
                        pageItems + List(ITEMS_PER_PAGE - pageItems.size) { null }
                    val firstRow = paddedItems.take(4)
                    val secondRow = paddedItems.drop(4)

                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally)
                        ) {
                            firstRow.forEach { item -> EssentialItemView(item, onNavigate) }
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally)
                        ) {
                            secondRow.forEach { item -> EssentialItemView(item, onNavigate) }
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
                                if (isSelected) colors.accent.primary
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
fun EssentialItemView(item: EssentialItem?, onNavigate: (String) -> Unit = {}) {
    val colors = LocalAppColors.current
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(70.dp)
            .then(if (item?.route != null) Modifier.clickable { onNavigate(item.route) } else Modifier)
            .then(if (item == null) Modifier.alpha(0f) else Modifier)
    ) {
        // 圖示方塊
        Box(
            modifier = Modifier
                .width(60.dp)
                .height(50.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(brush = colors.home.essentialsCard.itemBackground)
                .border(
                    width = 1.5.dp,
                    brush = colors.home.essentialsCard.itemBorder,
                    shape = RoundedCornerShape(8.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            if (item?.iconVector != null) {
                Icon(
                    imageVector = item.iconVector,
                    contentDescription = item.label,
                    tint = colors.home.essentialsCard.itemIcon,
                    modifier = Modifier.size(30.dp)
                )
            } else if (item?.iconRes != null) {
                val tint = if (item.useOriginalColor) Color.Unspecified else colors.home.essentialsCard.itemIcon
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
            modifier = Modifier
                .fillMaxWidth(),
            textAlign = TextAlign.Center,
            text = item?.label ?: "",
            color = colors.home.essentialsCard.itemLabel,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Preview(name = "Neon", showBackground = true, backgroundColor = 0xFF030F1B)
@Composable
private fun XEssentialsCardPreviewNeon() {
    AppTheme(colors = NeonColors, assets = NeonAssets) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(deepNavy)
                .padding(16.dp)
        ) {
            XEssentialsCard(
                myMenuItems = allEssentialItems.take(ESSENTIALS_DISPLAY_COUNT),
                onSaveMyMenu = {}
            )
        }
    }
}

@Preview(name = "Black Gold", showBackground = true, backgroundColor = 0xFF050505)
@Composable
private fun XEssentialsCardPreviewBlackGold() {
    AppTheme(colors = BlackGoldColors, assets = BlackGoldAssets) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            XEssentialsCard(
                myMenuItems = allEssentialItems.take(ESSENTIALS_DISPLAY_COUNT),
                onSaveMyMenu = {}
            )
        }
    }
}
