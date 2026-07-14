package com.qpay.xcash.ui.profile.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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
import com.qpay.xcash.R
import com.qpay.xcash.ui.components.neonGlow
import com.qpay.xcash.ui.theme.AppTheme
import com.qpay.xcash.ui.theme.BlackGoldColors
import com.qpay.xcash.ui.theme.LocalAppColors
import com.qpay.xcash.ui.theme.NeonColors

@Composable
fun LogoutConfirmDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    val colors = LocalAppColors.current
    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .then(
                    if (colors.effect.enableGlow)
                        Modifier.neonGlow(colors.accent.primary, alpha = 0.3f, glowRadius = 12.dp, borderRadius = 20.dp)
                    else Modifier
                )
                .background(colors.profile.logoutDialog.background, RoundedCornerShape(20.dp))
                .border(1.5.dp, colors.profile.logoutDialog.border, RoundedCornerShape(20.dp))
                .padding(horizontal = 24.dp, vertical = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
//                imageVector = Icons.Outlined.Warning,
                painter = painterResource(R.mipmap.ic_warning),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier.size(52.dp)
            )
            Text(
                text = stringResource(R.string.logout_dialog_title),
                color = colors.profile.logoutDialog.titleText,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = stringResource(R.string.logout_dialog_message),
                color = colors.text.body,
                fontSize = 13.sp,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(4.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .background(colors.profile.logoutDialog.confirmButtonFill, RoundedCornerShape(24.dp))
                    .then(
                        colors.profile.logoutDialog.confirmButtonBorder?.let {
                            Modifier.border(1.5.dp, it, RoundedCornerShape(24.dp))
                        } ?: Modifier
                    )
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() },
                        onClick = onConfirm
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.logout_dialog_confirm),
                    color = colors.profile.logoutDialog.confirmButtonText,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(Modifier.height(8.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .background(colors.profile.logoutDialog.cancelButtonFill, RoundedCornerShape(24.dp))
                    .border(1.5.dp, colors.profile.logoutDialog.cancelButtonBorder, RoundedCornerShape(24.dp))
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() },
                        onClick = onDismiss
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.logout_dialog_cancel),
                    color = colors.profile.logoutDialog.cancelButtonText,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Preview(name = "Neon", showBackground = true, backgroundColor = 0xFF030F1B)
@Composable
private fun LogoutConfirmDialogPreviewNeon() {
    AppTheme(colors = NeonColors) {
        LogoutConfirmDialog(onConfirm = {}, onDismiss = {})
    }
}

@Preview(name = "Black Gold", showBackground = true, backgroundColor = 0xFF050505)
@Composable
private fun LogoutConfirmDialogPreviewBlackGold() {
    AppTheme(colors = BlackGoldColors) {
        LogoutConfirmDialog(onConfirm = {}, onDismiss = {})
    }
}
