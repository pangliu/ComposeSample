package com.qpay.xcash.ui.components

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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
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
import androidx.compose.ui.res.painterResource
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
fun UploadAvatarErrorDialog(
    isVisible: Boolean,
    onCancel: () -> Unit,
    onTryAgain: () -> Unit
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
                .navigationBarsPadding()
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() },
                    onClick = { dismissWithAnimation(onCancel) }
                ),
            contentAlignment = Alignment.BottomCenter
        ) {
            AnimatedVisibility(
                visible = contentVisible,
                enter = slideInVertically(initialOffsetY = { it }, animationSpec = tween(300)) + fadeIn(animationSpec = tween(300)),
                exit = slideOutVertically(targetOffsetY = { it }, animationSpec = tween(300)) + fadeOut(animationSpec = tween(300))
            ) {
                UploadAvatarErrorDialogContent(
                    onCancel = { dismissWithAnimation(onCancel) },
                    onTryAgain = { dismissWithAnimation(onTryAgain) }
                )
            }
        }
    }
}

@Composable
private fun UploadAvatarErrorDialogContent(
    onCancel: () -> Unit,
    onTryAgain: () -> Unit
) {
    val colors = LocalAppColors.current
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 12.dp)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick = {}
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(colors.editAvatar.errorDialogBackground, RoundedCornerShape(20.dp))
                .border(1.5.dp, colors.editAvatar.errorDialogBorder, RoundedCornerShape(20.dp))
                .padding(horizontal = 24.dp, vertical = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .border(1.5.dp, colors.editAvatar.errorDialogIconRing, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(R.mipmap.ic_warning),
                    contentDescription = stringResource(R.string.upload_avatar_error_dialog_warning_desc),
                    tint = colors.editAvatar.errorDialogIconTint,
                    modifier = Modifier.size(26.dp)
                )
            }

            Text(
                text = stringResource(R.string.upload_avatar_error_dialog_title),
                color = colors.editAvatar.errorDialogTitleText,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Text(
                text = stringResource(R.string.upload_avatar_error_dialog_message),
                color = colors.editAvatar.errorDialogMessageText,
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                lineHeight = 20.sp
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
                        .border(1.5.dp, colors.editAvatar.errorDialogCancelBorder, RoundedCornerShape(8.dp))
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() },
                            onClick = onCancel
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    GradientText(
                        text = stringResource(R.string.upload_avatar_error_dialog_cancel),
                        brush = colors.editAvatar.errorDialogCancelText,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .background(colors.editAvatar.errorDialogTryAgainFill, RoundedCornerShape(8.dp))
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() },
                            onClick = onTryAgain
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.upload_avatar_error_dialog_try_again),
                        color = colors.editAvatar.errorDialogTryAgainText,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Preview(name = "Neon", showBackground = true, backgroundColor = 0xFF030F1B)
@Composable
private fun UploadAvatarErrorDialogPreviewNeon() {
    AppTheme(colors = NeonColors) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
            UploadAvatarErrorDialogContent(onCancel = {}, onTryAgain = {})
        }
    }
}

@Preview(name = "Black Gold", showBackground = true, backgroundColor = 0xFF050505)
@Composable
private fun UploadAvatarErrorDialogPreviewBlackGold() {
    AppTheme(colors = BlackGoldColors) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
            UploadAvatarErrorDialogContent(onCancel = {}, onTryAgain = {})
        }
    }
}
