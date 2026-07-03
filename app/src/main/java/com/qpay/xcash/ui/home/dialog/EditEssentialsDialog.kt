package com.qpay.xcash.ui.home.dialog

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.qpay.xcash.R
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.zIndex
import com.qpay.xcash.ui.components.neonGlow
import com.qpay.xcash.ui.home.essential.ESSENTIALS_DISPLAY_COUNT
import com.qpay.xcash.ui.home.essential.EssentialItem
import com.qpay.xcash.ui.home.essential.allEssentialItems
import com.qpay.xcash.ui.theme.LocalAppColors
import com.qpay.xcash.ui.theme.navyDark
import com.qpay.xcash.ui.theme.indigoDark
import com.qpay.xcash.ui.theme.limeGreen
import com.qpay.xcash.ui.theme.deepNavy
import com.qpay.xcash.ui.theme.vibrantPink
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
                    onReorderMyMenu = { from, to ->
                        val item = editableMyMenu.removeAt(from)
                        editableMyMenu.add(to, item)
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
    onReorderMyMenu: (from: Int, to: Int) -> Unit = { _, _ -> },
    onClose: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val colors = LocalAppColors.current
    Column(
        modifier = modifier
            .padding(horizontal = 12.dp, vertical = 12.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = {}
            )
            .neonGlow(
                color = colors.accent.secondary,
                alpha = 0.6f,
                glowRadius = 15.dp,
                borderRadius = 28.dp
            )
            .background(colors.bg.page, RoundedCornerShape(28.dp))
            .border(2.dp, colors.accent.secondary, RoundedCornerShape(28.dp))
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.edit_essentials_title),
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = stringResource(R.string.edit_essentials_hint),
            color = Color.Gray,
            fontSize = 12.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            // ── My Menu 區塊 ──
            SectionHeader(title = stringResource(R.string.edit_essentials_my_menu), count = myMenuItems.size)
            Spacer(modifier = Modifier.height(16.dp))

            ReorderableEssentialGrid(
                items = myMenuItems,
                onReorder = onReorderMyMenu,
                onRemove = onRemoveFromMyMenu
            )

            Spacer(modifier = Modifier.height(8.dp))

            // ── Other 區塊 ──
            if (otherItems.isNotEmpty()) {
                SectionHeader(
                    title = stringResource(R.string.edit_essentials_other),
                    count = otherItems.size,
                    modifier = Modifier.padding(top = 0.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))

                otherItems.chunked(4).forEach { row ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        row.forEach { item ->
                            key(item.label) {
                                DraggableEssentialItem(
                                    item = item,
                                    badgeType = BadgeType.ADD,
                                    onClick = { onAddToMyMenu(item) },
                                    onDragMoved = { onAddToMyMenu(item) }
                                )
                            }
                        }
                        repeat(4 - row.size) { Spacer(Modifier.width(70.dp)) }
                    }
                    Spacer(Modifier.height(16.dp))
                }
            }
        }

        Spacer(modifier = Modifier.height(15.dp))

        Box(
            modifier = Modifier
                .neonGlow(
                    color = limeGreen,
                    alpha = 0.6f,
                    glowRadius = 15.dp,
                    borderRadius = 8.dp
                )
                .clip(RoundedCornerShape(50.dp))
                .background(color = limeGreen)
                .clickable { onClose() }
                .padding(vertical = 8.dp, horizontal = 20.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(R.string.edit_essentials_save),
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(25.dp))
    }
}

// ── My Menu 拖曳排序格子 ──────────────────────────────────────────────────────

@Composable
private fun ReorderableEssentialGrid(
    items: List<EssentialItem>,
    columns: Int = 4,
    onReorder: (from: Int, to: Int) -> Unit,
    onRemove: (EssentialItem) -> Unit,
) {
    val colors = LocalAppColors.current
    val density = LocalDensity.current
    val cellHeightDp = 90.dp

    var draggingIndex by remember { mutableStateOf<Int?>(null) }
    var dragX by remember { mutableFloatStateOf(0f) }
    var dragY by remember { mutableFloatStateOf(0f) }
    var hoverIdx by remember { mutableStateOf<Int?>(null) }

    val cellHeightPx = with(density) { cellHeightDp.toPx() }
    val rowCount = (items.size + columns - 1) / columns
    var cellWidthPx by remember { mutableFloatStateOf(0f) }

    fun displayOf(raw: Int): Int {
        val from = draggingIndex ?: return raw
        val to = hoverIdx ?: return raw
        return when {
            raw == from -> to
            from < to && raw in (from + 1)..to -> raw - 1
            from > to && raw in to until from -> raw + 1
            else -> raw
        }
    }

    fun centerOf(idx: Int): Pair<Float, Float> = Pair(
        (idx % columns) * cellWidthPx + cellWidthPx / 2f,
        (idx / columns) * cellHeightPx + cellHeightPx / 2f
    )

    fun hoverAt(x: Float, y: Float): Int? {
        if (y > cellHeightPx * rowCount) return null   // 拖到格子下方 → 移至 Other
        val c = (x / cellWidthPx).toInt().coerceIn(0, columns - 1)
        val r = (y / cellHeightPx).toInt().coerceIn(0, rowCount - 1)
        return (r * columns + c).coerceIn(0, items.lastIndex)
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(cellHeightDp * rowCount)
            .onSizeChanged { size -> cellWidthPx = size.width.toFloat() / columns }
    ) {
        if (cellWidthPx == 0f) return@Box
            items.forEachIndexed { index, item ->
                key(item.label) {
                    val isDragging = index == draggingIndex
                    val (targetX, targetY) = centerOf(displayOf(index))

                    val animX by animateFloatAsState(
                        targetValue = if (isDragging) dragX else targetX,
                        animationSpec = if (isDragging) snap() else spring(stiffness = Spring.StiffnessMediumLow),
                        label = "x_${item.label}"
                    )
                    val animY by animateFloatAsState(
                        targetValue = if (isDragging) dragY else targetY,
                        animationSpec = if (isDragging) snap() else spring(stiffness = Spring.StiffnessMediumLow),
                        label = "y_${item.label}"
                    )
                    val scale by animateFloatAsState(
                        targetValue = if (isDragging) 1.15f else 1f,
                        label = "scale_${item.label}"
                    )

                    val infiniteTransition = rememberInfiniteTransition(label = "jiggle_${item.label}")
                    val jiggle by infiniteTransition.animateFloat(
                        initialValue = -1.5f,
                        targetValue = 1.5f,
                        animationSpec = infiniteRepeatable(
                            animation = tween(durationMillis = 150, easing = LinearEasing),
                            repeatMode = RepeatMode.Reverse
                        ),
                        label = "rot_${item.label}"
                    )

                    Box(
                        modifier = Modifier
                            .zIndex(if (isDragging) 10f else 0f)
                            .graphicsLayer {
                                translationX = animX - cellWidthPx / 2f
                                translationY = animY - cellHeightPx / 2f
                                scaleX = scale
                                scaleY = scale
                                rotationZ = if (isDragging) 0f else jiggle
                                alpha = if (isDragging) 0.9f else 1f
                                clip = false
                            }
                            .requiredWidth(with(density) { cellWidthPx.toDp() })
                            .height(cellHeightDp)
                            .pointerInput(item.label) {
                                detectDragGesturesAfterLongPress(
                                    onDragStart = { touchOffset ->
                                        val idx = items.indexOfFirst { it.label == item.label }
                                        draggingIndex = idx
                                        hoverIdx = idx
                                        dragX = (idx % columns) * cellWidthPx + touchOffset.x
                                        dragY = (idx / columns) * cellHeightPx + touchOffset.y
                                    },
                                    onDrag = { change, dragAmount ->
                                        change.consume()
                                        dragX += dragAmount.x
                                        dragY += dragAmount.y
                                        hoverIdx = hoverAt(dragX, dragY)
                                    },
                                    onDragEnd = {
                                        val from = draggingIndex
                                        val to = hoverIdx
                                        draggingIndex = null
                                        hoverIdx = null
                                        when {
                                            from == null -> {}
                                            to == null -> onRemove(item)   // 拖出格子 → 移至 Other
                                            from != to -> onReorder(from, to)
                                        }
                                    },
                                    onDragCancel = {
                                        draggingIndex = null
                                        hoverIdx = null
                                    }
                                )
                            }
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) { onRemove(item) }
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .align(Alignment.TopCenter)
                                .padding(top = 4.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .width(60.dp)
                                    .height(50.dp)
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(
                                        brush = Brush.linearGradient(
                                            colors = listOf(indigoDark, navyDark)
                                        )
                                    )
                                    .border(
                                        width = 1.5.dp,
                                        brush = Brush.linearGradient(
                                            colors = listOf(
                                                colors.accent.primary.copy(alpha = 0.4f),
                                                colors.accent.secondary.copy(alpha = 0.8f)
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
                                        tint = colors.accent.primary,
                                        modifier = Modifier.size(30.dp)
                                    )
                                } else if (item.iconRes != null) {
                                    val tint = if (item.useOriginalColor) Color.Unspecified else colors.accent.primary
                                    Icon(
                                        painter = painterResource(id = item.iconRes),
                                        contentDescription = item.label,
                                        tint = tint,
                                        modifier = Modifier.size(30.dp)
                                    )
                                }
                            }
                            Spacer(Modifier.height(6.dp))
                            Text(
                                text = item.label,
                                color = Color.White,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }

                        if (!isDragging) {
                            Box(
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .offset(x = 1.dp, y = (-2).dp)
                                    .size(18.dp)
                                    .clip(CircleShape)
                                    .background(vibrantPink),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = stringResource(R.string.edit_essentials_remove_desc),
                                    tint = Color.White,
                                    modifier = Modifier.size(12.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
}

// ── 可拖曳的項目（Other 區塊用） ──────────────────────────────────────────────

private const val DRAG_THRESHOLD = 120f

enum class BadgeType { ADD, REMOVE }

@Composable
fun DraggableEssentialItem(
    item: EssentialItem,
    badgeType: BadgeType,
    onClick: () -> Unit,
    onDragMoved: () -> Unit
) {
    val colors = LocalAppColors.current
    var offsetX by remember { mutableFloatStateOf(0f) }
    var offsetY by remember { mutableFloatStateOf(0f) }
    var isDragging by remember { mutableStateOf(false) }

    val scale by animateFloatAsState(
        targetValue = if (isDragging) 1.15f else 1f,
        label = "dragScale"
    )

    val jiggleRotation = if (badgeType == BadgeType.REMOVE && !isDragging) {
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
            .zIndex(if (isDragging) 10f else 0f)
            .graphicsLayer {
                translationX = offsetX
                translationY = offsetY
                scaleX = scale
                scaleY = scale
                rotationZ = jiggleRotation
                alpha = if (isDragging) 0.9f else 1f
                clip = false
            }
            .pointerInput(Unit) {
                detectDragGesturesAfterLongPress(
                    onDragStart = { isDragging = true },
                    onDrag = { change, dragAmount ->
                        change.consume()
                        offsetX += dragAmount.x
                        offsetY += dragAmount.y
                    },
                    onDragEnd = {
                        isDragging = false
                        val shouldMove = when (badgeType) {
                            BadgeType.REMOVE -> offsetY > DRAG_THRESHOLD
                            BadgeType.ADD -> offsetY < -DRAG_THRESHOLD
                        }
                        if (shouldMove) onDragMoved()
                        offsetX = 0f
                        offsetY = 0f
                    },
                    onDragCancel = {
                        isDragging = false
                        offsetX = 0f
                        offsetY = 0f
                    }
                )
            }
            .requiredWidth(70.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { onClick() }
    ) {
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
                            colors = listOf(indigoDark, navyDark)
                        )
                    )
                    .border(
                        width = 1.5.dp,
                        brush = Brush.linearGradient(
                            colors = listOf(
                                colors.accent.primary.copy(alpha = 0.4f),
                                colors.accent.secondary.copy(alpha = 0.8f)
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
                        tint = colors.accent.primary,
                        modifier = Modifier.size(30.dp)
                    )
                } else if (item.iconRes != null) {
                    val tint = if (item.useOriginalColor) Color.Unspecified else colors.accent.primary
                    Icon(
                        painter = painterResource(id = item.iconRes),
                        contentDescription = item.label,
                        tint = tint,
                        modifier = Modifier.size(30.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = item.label,
                color = Color.White,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium
            )
        }

        if (!isDragging) {
            val badgeColor = when (badgeType) {
                BadgeType.REMOVE -> vibrantPink
                BadgeType.ADD -> limeGreen
            }
            val badgeIcon = when (badgeType) {
                BadgeType.REMOVE -> Icons.Default.Close
                BadgeType.ADD -> Icons.Default.Add
            }

            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(x = 1.dp, y = (-2).dp)
                    .size(18.dp)
                    .clip(CircleShape)
                    .background(badgeColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = badgeIcon,
                    contentDescription = if (badgeType == BadgeType.REMOVE) stringResource(R.string.edit_essentials_remove_desc) else stringResource(R.string.edit_essentials_add_desc),
                    tint = Color.White,
                    modifier = Modifier.size(12.dp)
                )
            }
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
    val colors = LocalAppColors.current
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            color = colors.accent.primary,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = stringResource(R.string.edit_essentials_items_count, count),
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
                .background(deepNavy)
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
