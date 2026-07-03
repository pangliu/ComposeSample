package com.qpay.xcash.ui.login.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.DialogProperties
import com.qpay.xcash.R
import com.qpay.xcash.ui.components.neonGlow
import com.qpay.xcash.ui.theme.AppTheme
import com.qpay.xcash.ui.theme.BlackGoldColors
import com.qpay.xcash.ui.theme.LocalAppColors
import com.qpay.xcash.ui.theme.NeonColors

@Composable
fun AccountStatusDialog(onDismiss: () -> Unit) {
    val colors = LocalAppColors.current
    var showFindAppDialog by remember { mutableStateOf(false) }

    if (showFindAppDialog) {
        FindAppDialog(
            onDismiss = { showFindAppDialog = false },
            onSubmit = { _, _, _ -> showFindAppDialog = false }
        )
        return
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = true,
            dismissOnClickOutside = true
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .then(if (colors.effect.enableGlow) Modifier.neonGlow(
                    color = colors.selector.border,
                    alpha = 0.6f,
                    glowRadius = 16.dp,
                    borderRadius = 24.dp
                ) else Modifier)
                .background(colors.bg.page, RoundedCornerShape(24.dp))
                .border(2.dp, colors.selector.border, RoundedCornerShape(24.dp))
                .padding(24.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Title
                Text(
                    text = stringResource(id = R.string.select_action),
                    color = colors.selector.border,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    style = TextStyle(
                        shadow = Shadow(color = colors.accent.primary, blurRadius = 15f)
                    )
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Button 1: Check Application Progress
                ActionDialogButton(
                    icon = R.mipmap.ic_check_progress,
                    text = stringResource(id = R.string.check_application_progress),
                    borderColor = colors.accountDialog.button1,
                    onClick = { showFindAppDialog = true }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Button 2: Verify My Identity
                ActionDialogButton(
                    icon = R.mipmap.ic_verify_id,
                    text = stringResource(id = R.string.verify_my_identity),
                    borderColor = colors.accountDialog.button2,
                    onClick = {
                        // TODO: 處理點擊事件
                        onDismiss()
                    }
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Close Button
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .then(if (colors.effect.enableGlow) Modifier.neonGlow(
                            color = colors.selector.border,
                            alpha = 0.6f,
                            glowRadius = 16.dp,
                            borderRadius = 24.dp
                        ) else Modifier)
                        .border(2.dp, colors.selector.border, RoundedCornerShape(24.dp))
                        .background(
                            color = colors.bg.page,
                            shape = RoundedCornerShape(24.dp)
                        )
                        .clickable { onDismiss() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = stringResource(id = R.string.close),
                        tint = colors.selector.border
                    )
                }
            }
        }
    }
}

@Composable
fun ActionDialogButton(icon: Int, text: String, borderColor: Color, onClick: () -> Unit) {
    val colors = LocalAppColors.current
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .then(if (colors.effect.enableGlow) Modifier.neonGlow(
                color = borderColor,
                alpha = 0.6f,
                glowRadius = 16.dp,
                borderRadius = 16.dp
            ) else Modifier)
            .background(colors.bg.page, RoundedCornerShape(16.dp))
            .border(2.dp, borderColor, RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .padding(start = 10.dp, top = 20.dp, end = 3.dp, bottom = 20.dp)
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = null,
            tint = borderColor,
            modifier = Modifier.size(32.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            color = Color.White,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.weight(1f)
        )
        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = stringResource(id = R.string.go),
            tint = borderColor
        )
    }
}

@Preview(name = "Neon", showBackground = true, backgroundColor = 0xFF030F1B)
@Composable
private fun AccountStatusDialogPreviewNeon() {
    AppTheme(colors = NeonColors) {
        Box(modifier = Modifier.fillMaxSize()) {
            AccountStatusDialog(onDismiss = {})
        }
    }
}

@Preview(name = "Black Gold", showBackground = true, backgroundColor = 0xFF050505)
@Composable
private fun AccountStatusDialogPreviewBlackGold() {
    AppTheme(colors = BlackGoldColors) {
        Box(modifier = Modifier.fillMaxSize()) {
            AccountStatusDialog(onDismiss = {})
        }
    }
}
