package com.example.newproject.ui.profile.security.dialog

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
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Save
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
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.newproject.R
import com.example.newproject.ui.components.neonGlow
import com.example.newproject.ui.theme.neonCyan
import com.example.newproject.ui.theme.neonPink
import com.example.newproject.ui.theme.neonPurple
import com.example.newproject.ui.theme.neonPurpleLight
import com.example.newproject.ui.theme.normalText

private val DialogBg = Color(0xFF0D1B2E)
private const val PIN_LENGTH = 6

@Composable
fun SecurityPinDialog(
    onDismiss: () -> Unit,
    onConfirm: (pin: String) -> Unit = {}
) {
    var enterPin by remember { mutableStateOf("") }
    var confirmPin by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .neonGlow(neonCyan, alpha = 0.3f, glowRadius = 12.dp, borderRadius = 20.dp)
                .background(DialogBg, RoundedCornerShape(20.dp))
                .border(1.5.dp, neonCyan.copy(alpha = 0.7f), RoundedCornerShape(20.dp))
                .padding(horizontal = 20.dp, vertical = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Title
            Text(
                text = stringResource(R.string.pin_dialog_title),
                color = neonCyan,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            // Promo banner
            Row(
                modifier = Modifier
                    .fillMaxWidth()
//                    .neonGlow(neonPurple, alpha = 0.3f, glowRadius = 6.dp, borderRadius = 10.dp)
                    .background(neonPurple.copy(alpha = 0.08f), RoundedCornerShape(10.dp))
                    .border(1.5.dp, neonPurple.copy(alpha = 0.7f), RoundedCornerShape(10.dp))
                    .padding(horizontal = 10.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = stringResource(R.string.pin_dialog_banner),
                    color = neonPurple,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f),
                    lineHeight = 14.sp
                )
                Icon(
                    painter = painterResource(R.mipmap.ic_trophy),
                    contentDescription = null,
                    tint = Color.Unspecified,
                    modifier = Modifier.size(20.dp)
                )
                Icon(
                    painter = painterResource(R.mipmap.ic_gift),
                    contentDescription = null,
                    tint = Color.Unspecified,
                    modifier = Modifier.size(18.dp)
                )
            }

            // Biometric row (disabled)
//            DisabledRow(label = stringResource(R.string.pin_dialog_biometric))

            // Enter PIN
            PinSection(
                label = stringResource(R.string.pin_dialog_enter_pin),
                value = enterPin,
                onValueChange = { if (it.length <= PIN_LENGTH && it.all(Char::isDigit)) enterPin = it }
            )

            // Confirm PIN
            PinSection(
                label = stringResource(R.string.pin_dialog_confirm_pin),
                value = confirmPin,
                onValueChange = { if (it.length <= PIN_LENGTH && it.all(Char::isDigit)) confirmPin = it }
            )

            // Security alerts row (disabled)
//            DisabledRow(label = stringResource(R.string.pin_dialog_alerts))
            Spacer(Modifier.height(30.dp))
            // Bottom action buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                // Confirm / biometric button
                Box(
                    modifier = Modifier
                        .size(40.dp)
//                        .neonGlow(neonCyan, alpha = 0.4f, glowRadius = 8.dp, borderRadius = 26.dp)
                        .border(1.5.dp, neonCyan, CircleShape)
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() },
                            onClick = {
                                if (enterPin.length == PIN_LENGTH && enterPin == confirmPin) {
                                    onConfirm(enterPin)
                                }
                            }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Save,
                        contentDescription = stringResource(R.string.pin_dialog_confirm_desc),
                        tint = neonCyan,
                        modifier = Modifier.size(20.dp)
                    )
                }

                // Close button
                Box(
                    modifier = Modifier
                        .size(40.dp)
//                        .neonGlow(neonPink, alpha = 0.4f, glowRadius = 8.dp, borderRadius = 26.dp)
                        .border(1.5.dp, neonPurpleLight, CircleShape)
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() },
                            onClick = onDismiss
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = stringResource(R.string.pin_dialog_close_desc),
                        tint = neonPurpleLight,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun PinSection(
    label: String,
    value: String,
    onValueChange: (String) -> Unit
) {
    // Hidden TextField drives input; boxes display the filled state
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
        visualTransformation = PasswordVisualTransformation(),
        cursorBrush = SolidColor(Color.Transparent),
        decorationBox = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = label,
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    repeat(PIN_LENGTH) { index ->
                        val filled = index < value.length
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(
                                    if (filled) neonCyan.copy(alpha = 0.15f) else Color.Transparent,
                                    RoundedCornerShape(8.dp)
                                )
                                .border(
                                    1.5.dp,
                                    if (filled) neonCyan else neonCyan.copy(alpha = 0.4f),
                                    RoundedCornerShape(8.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            if (filled) {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .background(neonCyan, CircleShape)
                                )
                            }
                        }
                    }
                }
            }
        }
    )
}

@Composable
private fun DisabledRow(label: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = label,
            color = normalText.copy(alpha = 0.5f),
            fontSize = 12.sp,
            modifier = Modifier.weight(1f)
        )
        Box(
            modifier = Modifier
                .size(width = 36.dp, height = 20.dp)
                .background(Color.Gray.copy(alpha = 0.3f), RoundedCornerShape(10.dp))
                .border(1.dp, Color.Gray.copy(alpha = 0.4f), RoundedCornerShape(10.dp))
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0A0E1A)
@Composable
private fun SecurityPinDialogPreview() {
    SecurityPinDialog(onDismiss = {})
}
