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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.qpay.xcash.R
import com.qpay.xcash.ui.theme.AppTheme
import com.qpay.xcash.ui.theme.BlackGoldColors
import com.qpay.xcash.ui.theme.LocalAppColors
import com.qpay.xcash.ui.theme.NeonColors
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun FriendRemoveConfirmDialog(
    isVisible: Boolean,
    onCancel: () -> Unit,
    onConfirm: () -> Unit
) {
    if (!isVisible) return

    var contentVisible by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) { contentVisible = true }

    fun dismissWithAnimation(action: () -> Unit) {
        contentVisible = false
        scope.launch {
            delay(300)
            action()
        }
    }

    Dialog(
        onDismissRequest = { dismissWithAnimation(onCancel) },
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.6f))
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() },
                    onClick = { dismissWithAnimation(onCancel) }
                ),
            contentAlignment = Alignment.Center
        ) {
            AnimatedVisibility(
                visible = contentVisible,
                enter = slideInVertically(initialOffsetY = { it / 4 }, animationSpec = tween(300)) + fadeIn(animationSpec = tween(300)),
                exit = slideOutVertically(targetOffsetY = { it / 4 }, animationSpec = tween(300)) + fadeOut(animationSpec = tween(300))
            ) {
                FriendRemoveConfirmDialogContent(
                    onCancel = { dismissWithAnimation(onCancel) },
                    onConfirm = { dismissWithAnimation(onConfirm) }
                )
            }
        }
    }
}

@Composable
private fun FriendRemoveConfirmDialogContent(
    onCancel: () -> Unit,
    onConfirm: () -> Unit
) {
    val colors = LocalAppColors.current.friendDetail
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick = {}
            )
            .background(colors.removeDialogBackground, RoundedCornerShape(20.dp))
            .border(1.dp, colors.removeDialogBorder, RoundedCornerShape(20.dp))
            .padding(horizontal = 24.dp, vertical = 28.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = stringResource(R.string.friend_remove_dialog_title),
            color = colors.removeDialogTitleText,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Text(
            text = stringResource(R.string.friend_remove_dialog_message),
            color = colors.removeDialogMessageText,
            fontSize = 13.sp,
            textAlign = TextAlign.Center,
            lineHeight = 18.sp
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
                    .border(1.5.dp, colors.removeDialogCancelBorder, RoundedCornerShape(10.dp))
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() },
                        onClick = onCancel
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.friend_remove_dialog_cancel),
                    color = colors.removeDialogCancelText,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
                    .background(colors.removeDialogConfirmFill, RoundedCornerShape(10.dp))
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() },
                        onClick = onConfirm
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.friend_remove_dialog_confirm),
                    color = colors.removeDialogConfirmText,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Preview(name = "Neon", showBackground = true, backgroundColor = 0xFF030F1B)
@Composable
private fun FriendRemoveConfirmDialogPreviewNeon() {
    AppTheme(colors = NeonColors) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            FriendRemoveConfirmDialogContent(onCancel = {}, onConfirm = {})
        }
    }
}

@Preview(name = "Black Gold", showBackground = true, backgroundColor = 0xFF050505)
@Composable
private fun FriendRemoveConfirmDialogPreviewBlackGold() {
    AppTheme(colors = BlackGoldColors) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            FriendRemoveConfirmDialogContent(onCancel = {}, onConfirm = {})
        }
    }
}
