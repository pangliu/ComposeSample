package com.qpay.xcash.ui.cards.detail

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.qpay.xcash.R
import com.qpay.xcash.ui.components.neonGlow
import com.qpay.xcash.ui.theme.AppTheme
import com.qpay.xcash.ui.theme.BlackGoldAssets
import com.qpay.xcash.ui.theme.BlackGoldColors
import com.qpay.xcash.ui.theme.LocalAppColors
import com.qpay.xcash.ui.theme.NeonColors
import com.qpay.xcash.ui.theme.dustyCrimson
import com.qpay.xcash.ui.theme.vibrantPink

@Composable
fun UnlinkCardDialog(
    last4: String,
    isVisible: Boolean,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    val colors = LocalAppColors.current
    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.72f))
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) { onDismiss() },
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
                    .animateEnterExit(
                        enter = slideInVertically { it / 4 },
                        exit = slideOutVertically { it / 4 }
                    )
                    .clip(RoundedCornerShape(20.dp))
                    .background(colors.cardDetail.dialogBackground)
                    .border(1.5.dp, colors.cardDetail.dialogBorder, RoundedCornerShape(20.dp))
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) { /* 攔截點擊，避免穿透到背景 */ }
                    .padding(horizontal = 24.dp, vertical = 28.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Icon(
                    painter = painterResource(R.mipmap.ic_warning),
                    contentDescription = null,
                    tint = colors.cardDetail.dialogWarningTint,
                    modifier = Modifier.size(80.dp)
                )
                // Title
                Text(
                    text = stringResource(R.string.card_detail_unlink_dialog_title),
                    color = colors.cardDetail.dialogTitleText,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )

                // 卡號後四碼
                Text(
                    text = "**** $last4",
                    color = colors.cardDetail.dialogCardNumberText,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 2.sp
                )

                // Description
                Text(
                    text = stringResource(R.string.card_detail_unlink_dialog_desc),
                    color = colors.cardDetail.dialogDescText,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    lineHeight = 20.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Unlink Card button（Neon: 紅色填滿；Black Gold: 紅色橫向漸層）
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            colors.cardDetail.dialogUnlinkBackground,
                            RoundedCornerShape(24.dp)
                        )
                        .border(
                            1.5.dp,
                            colors.cardDetail.dialogUnlinkBorder,
                            RoundedCornerShape(24.dp)
                        )
                        .then(
                            if (colors.effect.enableGlow)
                                Modifier.neonGlow(
                                    dustyCrimson,
                                    alpha = 0.5f,
                                    glowRadius = 24.dp,
                                    borderRadius = 24.dp
                                )
                            else Modifier
                        )
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) { onConfirm() }
                        .padding(vertical = 14.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.card_detail_unlink_btn),
                        color = colors.cardDetail.dialogUnlinkText,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Cancel button（Neon: 粉紅邊框；Black Gold: silverShimmer 橫向漸層）
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .then(
                            if (colors.effect.enableGlow)
                                Modifier.neonGlow(
                                    vibrantPink,
                                    alpha = 0.5f,
                                    glowRadius = 24.dp,
                                    borderRadius = 24.dp
                                )
                            else Modifier
                        )
                        .background(
                            colors.cardDetail.dialogCancelBackground,
                            RoundedCornerShape(24.dp)
                        )
                        .border(
                            1.5.dp,
                            colors.cardDetail.dialogCancelBorder,
                            RoundedCornerShape(24.dp)
                        )
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) { onDismiss() }
                        .padding(vertical = 14.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        modifier = Modifier.then(
                            if (colors.effect.enableGlow)
                                Modifier.neonGlow(
                                    vibrantPink,
                                    alpha = 0.5f,
                                    glowRadius = 24.dp,
                                    borderRadius = 24.dp
                                )
                            else Modifier
                        ),
                        text = stringResource(R.string.card_detail_unlink_dialog_cancel),
                        color = colors.cardDetail.dialogCancelText,
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
private fun UnlinkCardDialogPreviewNeon() {
    AppTheme(colors = NeonColors) {
        Box(modifier = Modifier.fillMaxSize()) {
            UnlinkCardDialog(
                last4 = "1234",
                isVisible = true,
                onConfirm = {},
                onDismiss = {}
            )
        }
    }
}

@Preview(name = "Black Gold", showBackground = true, backgroundColor = 0xFF050505)
@Composable
private fun UnlinkCardDialogPreviewBlackGold() {
    AppTheme(colors = BlackGoldColors, assets = BlackGoldAssets) {
        Box(modifier = Modifier.fillMaxSize()) {
            UnlinkCardDialog(
                last4 = "1234",
                isVisible = true,
                onConfirm = {},
                onDismiss = {}
            )
        }
    }
}
