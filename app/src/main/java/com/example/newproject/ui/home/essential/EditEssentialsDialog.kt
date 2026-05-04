package com.example.newproject.ui.home.essential

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.newproject.ui.theme.CardGradientMid
import com.example.newproject.ui.theme.CardGradientStart
import com.example.newproject.ui.theme.CashInGreen
import com.example.newproject.ui.theme.DarkBackground
import com.example.newproject.ui.theme.NeonCyan
import com.example.newproject.ui.theme.NeonPurple
import com.example.newproject.ui.theme.SendPink
import com.example.newproject.ui.theme.WelcomeBackground
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// ── Edit Essentials Dialog（從底部滑入） ──────────────────────────────────────

@Composable
fun EditEssentialsDialog(
    myMenuItems: List<EssentialItem>,
    otherItems: List<EssentialItem>,
    onDismissWithResult: (myMenu: List<EssentialItem>) -> Unit
) {
    var isVisible by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    // 可變動的分類狀態
    val editableMyMenu = remember { myMenuItems.toMutableStateList() }
    val editableOthers = remember { otherItems.toMutableStateList() }

    LaunchedEffect(Unit) { isVisible = true }

    fun dismissWithAnimation() {
        isVisible = false
        scope.launch {
            delay(300)
            onDismissWithResult(editableMyMenu.toList())
        }
    }

    Dialog(
        onDismissRequest = { dismissWithAnimation() },
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = { dismissWithAnimation() }
                ),
            contentAlignment = Alignment.BottomCenter
        ) {
            AnimatedVisibility(
                visible = isVisible,
                enter = slideInVertically(initialOffsetY = { it }, animationSpec = tween(300)) + fadeIn(animationSpec = tween(300)),
                exit = slideOutVertically(targetOffsetY = { it }, animationSpec = tween(300)) + fadeOut(animationSpec = tween(300))
            ) {
                EditEssentialsContent(
                    myMenuItems = editableMyMenu,
                    otherItems = editableOthers,
                    onRemoveFromMyMenu = { item ->
                        editableMyMenu.remove(item)
                        editableOthers.add(item)
                    },
                    onAddToMyMenu = { item ->
                        editableOthers.remove(item)
                        editableMyMenu.add(item)
                    },
                    onClose = { dismissWithAnimation() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.9f)
                )
            }
        }
    }
}

@Composable
fun EditEssentialsContent(
    myMenuItems: List<EssentialItem>,
    otherItems: List<EssentialItem>,
    onRemoveFromMyMenu: (EssentialItem) -> Unit = {},
    onAddToMyMenu: (EssentialItem) -> Unit = {},
    onClose: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(horizontal = 12.dp, vertical = 12.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = {}
            )
            .background(WelcomeBackground, RoundedCornerShape(28.dp))
            .border(2.dp, NeonPurple, RoundedCornerShape(28.dp))
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 標題
        Text(
            text = "Edit Essentials",
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

//        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "Drag to reorder your shortcuts",
            color = Color.Gray,
            fontSize = 12.sp
        )

//        Spacer(modifier = Modifier.height(8.dp))

        // 帶分類標題的 Grid
        LazyVerticalGrid(
            columns = GridCells.Fixed(4),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            // ── My Menu 區塊 ──
            item(span = { GridItemSpan(maxLineSpan) }) {
                SectionHeader(title = "My Menu", count = myMenuItems.size)
            }

            items(myMenuItems, key = { it.label }) { item ->
                EditableEssentialItemView(
                    item = item,
                    badgeType = BadgeType.REMOVE,
                    onClick = { onRemoveFromMyMenu(item) }
                )
            }

            // ── Other 區塊 ──
            if (otherItems.isNotEmpty()) {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    SectionHeader(
                        title = "Other",
                        count = otherItems.size,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }

                items(otherItems, key = { it.label }) { item ->
                    EditableEssentialItemView(
                        item = item,
                        badgeType = BadgeType.ADD,
                        onClick = { onAddToMyMenu(item) }
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(15.dp))
        // 儲存按鈕
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(50.dp))
                .background(
//                    brush = Brush.horizontalGradient(
//                        colors = listOf(NeonCyan, NeonPurple)
//                    )
                    color = CashInGreen
                )
                .clickable { onClose() }
                .padding(vertical = 8.dp, horizontal = 20.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Save My Change",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(25.dp))
    }
}

// ── Badge 類型 ───────────────────────────────────────────────────────────────

enum class BadgeType { ADD, REMOVE }

@Composable
fun EditableEssentialItemView(
    item: EssentialItem,
    badgeType: BadgeType,
    onClick: () -> Unit
) {
    // My Menu 的 icon 加上抖動動畫（類似 iOS 長按編輯模式）
    val jiggleRotation = if (badgeType == BadgeType.REMOVE) {
        val infiniteTransition = rememberInfiniteTransition(label = "jiggle")
        infiniteTransition.animateFloat(
            initialValue = -1.5f,
            targetValue = 1.5f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 150, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "jiggleRotation"
        ).value
    } else {
        0f
    }

    Box(
        modifier = Modifier
            .width(70.dp)
            .rotate(jiggleRotation)
            .clickable { onClick() }
    ) {
        // 底層：原本的 icon + 文字
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .width(60.dp)
                    .height(50.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(CardGradientStart, CardGradientMid)
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

            Text(
                text = item.label,
                color = Color.White,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium
            )
        }

        // 右上角的小圓形 Badge
        val badgeColor = when (badgeType) {
            BadgeType.REMOVE -> SendPink
            BadgeType.ADD -> CashInGreen
        }
        val badgeIcon = when (badgeType) {
            BadgeType.REMOVE -> Icons.Default.Close
            BadgeType.ADD -> Icons.Default.Add
        }

        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset(x = 2.dp, y = (-4).dp)
                .size(18.dp)
                .clip(CircleShape)
                .background(badgeColor),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = badgeIcon,
                contentDescription = if (badgeType == BadgeType.REMOVE) "Remove" else "Add",
                tint = Color.White,
                modifier = Modifier.size(12.dp)
            )
        }
    }
}

// ── Section Header ───────────────────────────────────────────────────────────

@Composable
fun SectionHeader(
    title: String,
    count: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            color = NeonCyan,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "$count items",
            color = Color.Gray,
            fontSize = 12.sp
        )
    }
}

// ── Preview ──────────────────────────────────────────────────────────────────

@Preview(showBackground = true, backgroundColor = 0xFF0A0E1A)
@Composable
fun EditEssentialsContentPreview() {
    val myMenu = allEssentialItems.take(ESSENTIALS_DISPLAY_COUNT)
    val others = allEssentialItems.drop(ESSENTIALS_DISPLAY_COUNT)

    MaterialTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(DarkBackground)
                .padding(16.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            EditEssentialsContent(
                myMenuItems = myMenu,
                otherItems = others,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.9f)
            )
        }
    }
}
