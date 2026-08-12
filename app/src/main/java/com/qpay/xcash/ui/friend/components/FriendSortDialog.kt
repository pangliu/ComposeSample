package com.qpay.xcash.ui.friend.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.qpay.xcash.R
import com.qpay.xcash.ui.friend.list.FriendListSortOrder
import com.qpay.xcash.ui.theme.AppTheme
import com.qpay.xcash.ui.theme.BlackGoldColors
import com.qpay.xcash.ui.theme.LocalAppColors
import com.qpay.xcash.ui.theme.NeonColors
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun FriendSortDialog(
    currentSortOrder: FriendListSortOrder,
    onDismiss: () -> Unit,
    onConfirm: (FriendListSortOrder) -> Unit
) {
    var isVisible by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) { isVisible = true }

    fun dismissWithAnimation() {
        isVisible = false
        scope.launch {
            delay(300)
            onDismiss()
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
                FriendSortDialogContent(
                    currentSortOrder = currentSortOrder,
                    onCancel = { dismissWithAnimation() },
                    onConfirm = { sortOrder ->
                        onConfirm(sortOrder)
                        dismissWithAnimation()
                    }
                )
            }
        }
    }
}

@Composable
private fun FriendSortDialogContent(
    currentSortOrder: FriendListSortOrder,
    onCancel: () -> Unit,
    onConfirm: (FriendListSortOrder) -> Unit
) {
    val colors = LocalAppColors.current.friendList.sortDialog
    var selectedSortOrder by remember { mutableStateOf(currentSortOrder) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = {}
            )
            .background(colors.dialogBackground, RoundedCornerShape(24.dp))
            .border(1.dp, colors.dialogBorder, RoundedCornerShape(24.dp))
            .padding(20.dp)
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .width(40.dp)
                .height(4.dp)
                .background(colors.dragHandle, RoundedCornerShape(2.dp))
        )

        Spacer(Modifier.height(20.dp))

        Text(
            text = stringResource(R.string.friend_sort_dialog_title),
            color = colors.titleText,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
        )

        Spacer(Modifier.height(16.dp))

        val options = listOf(
            FriendListSortOrder.A_TO_Z to stringResource(R.string.friend_sort_a_to_z),
            FriendListSortOrder.Z_TO_A to stringResource(R.string.friend_sort_z_to_a),
            FriendListSortOrder.RECENTLY_CONTACTED to stringResource(R.string.friend_sort_recently_contacted)
        )
        options.forEachIndexed { index, (sortOrder, label) ->
            FriendSortOptionRow(
                label = label,
                isSelected = selectedSortOrder == sortOrder,
                onClick = { selectedSortOrder = sortOrder }
            )
            if (index != options.lastIndex) Spacer(Modifier.height(12.dp))
        }

        Spacer(Modifier.height(20.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
                    .border(1.5.dp, colors.cancelButtonBorder, RoundedCornerShape(50.dp))
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) { onCancel() },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.friend_sort_dialog_cancel),
                    color = colors.cancelButtonText,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
                    .background(colors.confirmButtonFill, RoundedCornerShape(50.dp))
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) { onConfirm(selectedSortOrder) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.friend_sort_dialog_confirm),
                    color = colors.confirmButtonText,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun FriendSortOptionRow(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val colors = LocalAppColors.current.friendList.sortDialog
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(colors.optionBackground, RoundedCornerShape(14.dp))
            .border(1.dp, colors.optionBorder, RoundedCornerShape(14.dp))
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { onClick() }
            .padding(horizontal = 16.dp, vertical = 14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            color = colors.optionText,
            fontSize = 14.sp
        )
        Box(
            modifier = Modifier
                .size(20.dp)
                .border(
                    width = 1.5.dp,
                    color = if (isSelected) colors.radioSelectedBorder else colors.radioBorder,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            if (isSelected) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .background(colors.radioSelectedDot, CircleShape)
                )
            }
        }
    }
}

@Composable
private fun FriendSortDialogPreviewContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        FriendSortDialogContent(
            currentSortOrder = FriendListSortOrder.A_TO_Z,
            onCancel = {},
            onConfirm = {}
        )
    }
}

@Preview(name = "Neon", showBackground = true, backgroundColor = 0xFF030F1B)
@Composable
private fun FriendSortDialogPreviewNeon() {
    AppTheme(colors = NeonColors) {
        FriendSortDialogPreviewContent()
    }
}

@Preview(name = "Black Gold", showBackground = true, backgroundColor = 0xFF050505)
@Composable
private fun FriendSortDialogPreviewBlackGold() {
    AppTheme(colors = BlackGoldColors) {
        FriendSortDialogPreviewContent()
    }
}
