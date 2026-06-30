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
import com.example.newproject.ui.theme.LocalAppColors
import com.example.newproject.ui.theme.neonPink
import com.example.newproject.ui.theme.neonPurpleLight

private val DialogBg = Color(0xFF0D1B2E)
private const val PIN_LENGTH = 6

@Composable
fun SecurityPinDialog(
    onDismiss: () -> Unit,
    onConfirm: (pin: String) -> Unit = {}
) {
    var enterPin by remember { mutableStateOf("") }
    var confirmPin by remember { mutableStateOf("") }

    val colors = LocalAppColors.current
    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .neonGlow(colors.accent.primary, alpha = 0.3f, glowRadius = 12.dp, borderRadius = 20.dp)
                .background(DialogBg, RoundedCornerShape(20.dp))
                .border(1.5.dp, colors.accent.primary.copy(alpha = 0.7f), RoundedCornerShape(20.dp))
                .padding(horizontal = 20.dp, vertical = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Title
            Text(
                text = stringResource(R.string.pin_dialog_title),
                color = colors.accent.primary,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            // Promo banner
            Row(
                modifier = Modifier
                    .fillMaxWidth()
//                    .neonGlow(colors.accent.secondary, alpha = 0.3f, glowRadius = 6.dp, borderRadius = 10.dp)
                    .background(colors.accent.secondary.copy(alpha = 0.08f), RoundedCornerShape(10.dp))
                    .border(1.5.dp, colors.accent.secondary.copy(alpha = 0.7f), RoundedCornerShape(10.dp))
                    .padding(horizontal = 10.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = stringResource(R.string.pin_dialog_banner),
                    color = colors.accent.secondary,
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
//                        .neonGlow(colors.accent.primary, alpha = 0.4f, glowRadius = 8.dp, borderRadius = 26.dp)
                        .border(1.5.dp, colors.accent.primary, CircleShape)
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
                        tint = colors.accent.primary,
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
    val colors = LocalAppColors.current
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
                                    if (filled) colors.accent.primary.copy(alpha = 0.15f) else Color.Transparent,
                                    RoundedCornerShape(8.dp)
                                )
                                .border(
                                    1.5.dp,
                                    if (filled) colors.accent.primary else colors.accent.primary.copy(alpha = 0.4f),
                                    RoundedCornerShape(8.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            if (filled) {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .background(colors.accent.primary, CircleShape)
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
    val colors = LocalAppColors.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = label,
            color = colors.text.body.copy(alpha = 0.5f),
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
