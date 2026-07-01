package com.example.newproject.ui.cards.detail

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
import androidx.compose.material3.MaterialTheme
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
import com.example.newproject.R
import com.example.newproject.ui.components.neonGlow
import com.example.newproject.ui.theme.neonPurpleLight
import com.example.newproject.ui.theme.vibrantPink

private val warningRed = Color(0xFF790103)
private val neonRed = Color(0xFFA34248)
private val dialogBg = Color(0xFF0F1828)
private val dialogBorderColor = Color(0xFF1E2D4A)

@Composable
fun UnlinkCardDialog(
    last4: String,
    isVisible: Boolean,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
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
                    .background(dialogBg)
                    .border(1.dp, dialogBorderColor, RoundedCornerShape(20.dp))
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
                    tint = Color.Unspecified,
                    modifier = Modifier.size(80.dp)
                )
                // Title
                Text(
                    text = stringResource(R.string.card_detail_unlink_dialog_title),
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )

                // 卡號後四碼
                Text(
                    text = "**** $last4",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 2.sp
                )

                // Description
                Text(
                    text = stringResource(R.string.card_detail_unlink_dialog_desc),
                    color = Color.White.copy(alpha = 0.6f),
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    lineHeight = 20.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Unlink Card button（紅色填滿）
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(warningRed.copy(alpha = 0.5f), RoundedCornerShape(24.dp))
                        .border(1.5.dp, neonRed.copy(alpha = 0.6f), RoundedCornerShape(24.dp))
                        .neonGlow(neonRed, alpha = 0.5f, glowRadius = 24.dp, borderRadius = 24.dp)
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) { onConfirm() }
                        .padding(vertical = 14.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.card_detail_unlink_btn),
                        color = neonRed,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Cancel button（粉紅邊框）
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .neonGlow(vibrantPink, alpha = 0.5f, glowRadius = 24.dp, borderRadius = 24.dp)
                        .background(dialogBg)
                        .border(1.5.dp, vibrantPink, RoundedCornerShape(24.dp))
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) { onDismiss() }
                        .padding(vertical = 14.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        modifier = Modifier.neonGlow(vibrantPink, alpha = 0.5f, glowRadius = 24.dp, borderRadius = 24.dp),
                        text = stringResource(R.string.card_detail_unlink_dialog_cancel),
                        color = vibrantPink,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0B1327)
@Composable
private fun UnlinkCardDialogPreview() {
    MaterialTheme {
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
