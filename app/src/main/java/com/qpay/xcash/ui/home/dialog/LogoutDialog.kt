package com.qpay.xcash.ui.home.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.qpay.xcash.R
import com.qpay.xcash.ui.components.neonGlow
import com.qpay.xcash.ui.theme.AppTheme
import com.qpay.xcash.ui.theme.BlackGoldColors
import com.qpay.xcash.ui.theme.LocalAppColors
import com.qpay.xcash.ui.theme.NeonColors

@Composable
fun LogoutDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    val colors = LocalAppColors.current
    val logoutDialog = colors.home.logoutDialog
    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .then(
                    if (colors.effect.enableGlow)
                        Modifier.neonGlow(
                            logoutDialog.glowColor,
                            alpha = 0.3f,
                            glowRadius = 12.dp,
                            borderRadius = 20.dp
                        )
                    else Modifier
                )
                .background(logoutDialog.background, RoundedCornerShape(20.dp))
                .border(1.5.dp, logoutDialog.border, RoundedCornerShape(20.dp))
                .padding(horizontal = 24.dp, vertical = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = stringResource(R.string.logout_dialog_title),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = if (logoutDialog.titleBrush != null) Color.Unspecified else logoutDialog.titleColor,
                style = logoutDialog.titleBrush?.let { TextStyle(brush = it) } ?: TextStyle.Default,
            )

            HorizontalDivider(
                color = logoutDialog.dividerColor,
                thickness = 0.5.dp
            )

            Text(
                text = stringResource(R.string.logout_dialog_message),
                color = logoutDialog.bodyTextColor,
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                lineHeight = 22.sp
            )

            Spacer(Modifier.height(50.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(80.dp),
            ) {
                // Cancel (X)
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .then(
                            if (colors.effect.enableGlow)
                                Modifier.neonGlow(
                                    logoutDialog.cancelButtonColor,
                                    alpha = 0.25f,
                                    glowRadius = 8.dp,
                                    borderRadius = 28.dp
                                )
                            else Modifier
                        )
                        .background(logoutDialog.buttonBackground, CircleShape)
                        .border(1.5.dp, logoutDialog.cancelButtonColor.copy(0.7f), CircleShape)
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() },
                            onClick = onDismiss
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = null,
                        tint = logoutDialog.cancelButtonColor,
                        modifier = Modifier.size(24.dp)
                    )
                }

                // Confirm (→)
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .then(
                            if (colors.effect.enableGlow)
                                Modifier.neonGlow(
                                    logoutDialog.confirmButtonColor,
                                    alpha = 0.25f,
                                    glowRadius = 8.dp,
                                    borderRadius = 28.dp
                                )
                            else Modifier
                        )
                        .background(logoutDialog.buttonBackground, CircleShape)
                        .border(1.5.dp, logoutDialog.confirmButtonColor.copy(0.7f), CircleShape)
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() },
                            onClick = onConfirm
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        tint = logoutDialog.confirmButtonColor,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}

@Preview(name = "Neon", showBackground = true, backgroundColor = 0xFF030F1B)
@Composable
private fun LogoutDialogPreviewNeon() {
    AppTheme(colors = NeonColors) {
        LogoutDialog(onConfirm = {}, onDismiss = {})
    }
}

@Preview(name = "Black Gold", showBackground = true, backgroundColor = 0xFF050505)
@Composable
private fun LogoutDialogPreviewBlackGold() {
    AppTheme(colors = BlackGoldColors) {
        LogoutDialog(onConfirm = {}, onDismiss = {})
    }
}
