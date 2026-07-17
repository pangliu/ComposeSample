package com.qpay.xcash.ui.home.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
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
import com.qpay.xcash.ui.theme.neonPink
import com.qpay.xcash.ui.theme.neonRed

private val InputBg = Color(0xFF0A1220)

@Composable
fun DeleteAccountDialog(
    userEmail: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    var emailInput by remember { mutableStateOf("") }
    val confirmEnabled = emailInput.trim().equals(userEmail.trim(), ignoreCase = true)

    val colors = LocalAppColors.current
    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .then(
                    if (colors.effect.enableGlow)
                        Modifier.neonGlow(neonPink, alpha = 0.3f, glowRadius = 12.dp, borderRadius = 20.dp)
                    else Modifier
                )
                .background(colors.bg.surface, RoundedCornerShape(20.dp))
                .border(1.5.dp, colors.accent.primary.copy(0.5f), RoundedCornerShape(20.dp))
                .padding(horizontal = 24.dp, vertical = 28.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Warning icon
            Icon(
                painter = painterResource(R.mipmap.ic_warning),
                contentDescription = null,
                tint = colors.home.deleteDialog.warningTint,
                modifier = Modifier
                    .size(68.dp)
                    .align(Alignment.CenterHorizontally)
            )

            // Title
            Text(
                text = stringResource(R.string.delete_account_dialog_title),
                color = colors.home.deleteDialog.warningTint,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            // Warning label + body
            Text(
                text = stringResource(R.string.delete_account_dialog_warning_label),
                color = Color.White,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = stringResource(R.string.delete_account_dialog_warning_body),
                color = Color.White.copy(alpha = 0.85f),
                fontSize = 13.sp,
                lineHeight = 20.sp
            )

            // Email confirmation hint
            Text(
                text = stringResource(R.string.delete_account_dialog_email_hint),
                color = neonRed,
                fontSize = 13.sp,
                lineHeight = 19.sp
            )

            // Email input field
//            BasicTextField(
//                value = emailInput,
//                onValueChange = { emailInput = it },
//                textStyle = TextStyle(color = Color.White, fontSize = 14.sp),
//                cursorBrush = SolidColor(neonPink),
//                singleLine = true,
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .background(InputBg, RoundedCornerShape(8.dp))
//                    .border(1.dp, neonPink.copy(0.4f), RoundedCornerShape(8.dp))
//                    .padding(horizontal = 12.dp, vertical = 10.dp)
//            )

            // Delete Account button
            val deleteDialog = colors.home.deleteDialog
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .alpha(if (confirmEnabled) 1f else 0.4f)
                    .background(brush = deleteDialog.deleteButtonBg, shape = RoundedCornerShape(25.dp))
                    .border(1.5.dp, deleteDialog.deleteButtonBorder.copy(0.8f), RoundedCornerShape(25.dp))
                    .clickable(
                        enabled = confirmEnabled,
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() },
                        onClick = onConfirm
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.delete_account_dialog_confirm),
                    color = deleteDialog.deleteButtonText,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // Cancel button
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .then(
                        if (deleteDialog.cancelBg != null)
                            Modifier.background(brush = deleteDialog.cancelBg, shape = RoundedCornerShape(25.dp))
                        else Modifier
                    )
                    .border(1.5.dp, deleteDialog.cancelBorder.copy(0.8f), RoundedCornerShape(25.dp))
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() },
                        onClick = onDismiss
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.delete_account_dialog_cancel),
                    color = deleteDialog.cancelText,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Preview(name = "Neon", showBackground = true, backgroundColor = 0xFF030F1B)
@Composable
private fun DeleteAccountDialogPreviewNeon() {
    AppTheme(colors = NeonColors) {
        DeleteAccountDialog(
            userEmail = "user@example.com",
            onConfirm = {},
            onDismiss = {}
        )
    }
}

@Preview(name = "Black Gold", showBackground = true, backgroundColor = 0xFF050505)
@Composable
private fun DeleteAccountDialogPreviewBlackGold() {
    AppTheme(colors = BlackGoldColors) {
        DeleteAccountDialog(
            userEmail = "user@example.com",
            onConfirm = {},
            onDismiss = {}
        )
    }
}
