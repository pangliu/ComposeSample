package com.example.newproject.ui.home.dialog

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
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.newproject.R
import com.example.newproject.ui.components.neonGlow
import com.example.newproject.ui.theme.neonCyan
import com.example.newproject.ui.theme.neonPink
import com.example.newproject.ui.theme.neonRed

private val DialogBg = Color(0xFF0D1B2E)
private val InputBg = Color(0xFF0A1220)
private val warringText = Color(0xFFFF7474)
private val cancelText = Color(0xFFF6A5F9)
private val DeleteButtonBg = Color(0xFF5C1010)

@Composable
fun DeleteAccountDialog(
    userEmail: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    var emailInput by remember { mutableStateOf("") }
    val confirmEnabled = emailInput.trim().equals(userEmail.trim(), ignoreCase = true)

    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .neonGlow(neonPink, alpha = 0.3f, glowRadius = 12.dp, borderRadius = 20.dp)
                .background(DialogBg, RoundedCornerShape(20.dp))
                .border(1.5.dp, neonCyan.copy(0.5f), RoundedCornerShape(20.dp))
                .padding(horizontal = 24.dp, vertical = 28.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Warning icon
            Icon(
//                imageVector = Icons.Outlined.Warning,
                painter = painterResource(R.mipmap.ic_warning),
                contentDescription = null,
                tint = warringText,
                modifier = Modifier
                    .size(68.dp)
                    .align(Alignment.CenterHorizontally)
            )

            // Title
            Text(
                text = stringResource(R.string.delete_account_dialog_title),
                color = warringText,
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
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .background(
                        if (confirmEnabled) DeleteButtonBg else DeleteButtonBg.copy(alpha = 0.4f),
                        RoundedCornerShape(25.dp)
                    )
                    .border(1.5.dp, warringText.copy(if (confirmEnabled) 0.8f else 0.3f), RoundedCornerShape(25.dp))
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
                    color = if (confirmEnabled) warringText else warringText.copy(alpha = 0.4f),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // Cancel button
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .border(1.5.dp, cancelText.copy(0.7f), RoundedCornerShape(25.dp))
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() },
                        onClick = onDismiss
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.delete_account_dialog_cancel),
                    color = cancelText,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0A0E1A)
@Composable
private fun DeleteAccountDialogPreview() {
    MaterialTheme {
        DeleteAccountDialog(
            userEmail = "user@example.com",
            onConfirm = {},
            onDismiss = {}
        )
    }
}
