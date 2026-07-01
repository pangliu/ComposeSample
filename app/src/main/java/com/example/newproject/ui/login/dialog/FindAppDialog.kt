package com.example.newproject.ui.login.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.newproject.R
import com.example.newproject.ui.components.neonGlow
import com.example.newproject.ui.theme.AppTheme
import com.example.newproject.ui.theme.BlackGoldColors
import com.example.newproject.ui.theme.LocalAppColors
import com.example.newproject.ui.theme.NeonColors
import com.example.newproject.ui.theme.darkCharcoal

@Composable
fun FindAppDialog(
    onDismiss: () -> Unit,
    onSubmit: (idNumber: String, mobile: String, email: String) -> Unit
) {
    val colors = LocalAppColors.current
    var idNumber by remember { mutableStateOf("") }
    var mobile by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var idVisible by remember { mutableStateOf(false) }
    var mobileVisible by remember { mutableStateOf(true) }
    var emailVisible by remember { mutableStateOf(true) }

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
                .then(if (colors.effect.enableGlow) Modifier.neonGlow(color = colors.selector.border, alpha = 0.6f, glowRadius = 16.dp, borderRadius = 20.dp) else Modifier)
                .background(colors.bg.page, RoundedCornerShape(20.dp))
                .border(2.dp, colors.selector.border, RoundedCornerShape(20.dp))
                .padding(horizontal = 24.dp, vertical = 28.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Title
                Text(
                    text = stringResource(R.string.find_app_title),
                    color = colors.accent.primary,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Subtitle
                Text(
                    text = stringResource(R.string.find_app_subtitle),
                    color = colors.text.body,
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Gov't Issued ID Number field
                FindAppInputField(
                    label = stringResource(R.string.find_app_id_label),
                    value = idNumber,
                    onValueChange = { idNumber = it },
                    trailingIcon = if (idVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                    trailingIconDesc = stringResource(R.string.find_app_toggle_id_visibility),
                    visualTransformation = if (idVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardType = KeyboardType.Text,
                    onTrailingClick = { idVisible = !idVisible }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Mobile Number field
                FindAppInputField(
                    label = stringResource(R.string.mobile_number),
                    value = mobile,
                    onValueChange = { mobile = it },
                    trailingIcon = if (mobileVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                    trailingIconDesc = stringResource(R.string.find_app_mobile_icon_desc),
                    visualTransformation = if (mobileVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardType = KeyboardType.Phone,
                    onTrailingClick = { mobileVisible = !mobileVisible }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Email Address field
                FindAppInputField(
                    label = stringResource(R.string.find_app_email_label),
                    value = email,
                    onValueChange = { email = it },
                    trailingIcon = if (emailVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                    trailingIconDesc = stringResource(R.string.find_app_email_icon_desc),
                    visualTransformation = if (emailVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardType = KeyboardType.Email,
                    onTrailingClick = { emailVisible = !emailVisible }
                )

                Spacer(modifier = Modifier.height(28.dp))

                // Submit Button
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .then(if (colors.effect.enableGlow) Modifier.neonGlow(color = colors.loginSheet.submitButton, alpha = 0.6f, glowRadius = 16.dp, borderRadius = 24.dp) else Modifier)
                            .background(colors.bg.page, CircleShape)
                            .border(2.dp, colors.loginSheet.submitButton, CircleShape)
                            .clickable { onSubmit(idNumber, mobile, email) },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Send,
                            contentDescription = stringResource(R.string.find_app_submit_desc),
                            tint = colors.loginSheet.submitButton,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun FindAppInputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    trailingIcon: ImageVector,
    trailingIconDesc: String,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardType: KeyboardType = KeyboardType.Text,
    onTrailingClick: (() -> Unit)? = null
) {
    val colors = LocalAppColors.current
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            color = Color.White,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(bottom = 6.dp)
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(40.dp)
                    .then(if (colors.effect.enableGlow) Modifier.neonGlow(color = colors.selector.border, alpha = 0.8f, glowRadius = 12.dp, borderRadius = 20.dp) else Modifier)
                    .background(darkCharcoal, RoundedCornerShape(20.dp))
                    .border(1.5.dp, colors.selector.border, RoundedCornerShape(20.dp))
                    .padding(horizontal = 14.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                BasicTextField(
                    value = value,
                    onValueChange = onValueChange,
                    singleLine = true,
                    visualTransformation = visualTransformation,
                    keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
                    textStyle = TextStyle(color = Color.White, fontSize = 14.sp),
                    cursorBrush = SolidColor(colors.selector.border),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Box(
                modifier = Modifier
                    .size(40.dp)
                    .then(if (colors.effect.enableGlow) Modifier.neonGlow(color = colors.selector.border, alpha = 0.8f, glowRadius = 12.dp, borderRadius = 20.dp) else Modifier)
                    .background(darkCharcoal, CircleShape)
                    .border(1.5.dp, colors.selector.border, CircleShape)
                    .then(
                        if (onTrailingClick != null) Modifier.clickable { onTrailingClick() }
                        else Modifier
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = trailingIcon,
                    contentDescription = trailingIconDesc,
                    tint = colors.selector.border,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

@Preview(name = "Neon", showBackground = true, backgroundColor = 0xFF030F1B)
@Composable
private fun FindAppDialogPreviewNeon() {
    AppTheme(colors = NeonColors) {
        Box(modifier = Modifier.fillMaxSize()) {
            FindAppDialog(onDismiss = {}, onSubmit = { _, _, _ -> })
        }
    }
}

@Preview(name = "Black Gold", showBackground = true, backgroundColor = 0xFF050505)
@Composable
private fun FindAppDialogPreviewBlackGold() {
    AppTheme(colors = BlackGoldColors) {
        Box(modifier = Modifier.fillMaxSize()) {
            FindAppDialog(onDismiss = {}, onSubmit = { _, _, _ -> })
        }
    }
}
