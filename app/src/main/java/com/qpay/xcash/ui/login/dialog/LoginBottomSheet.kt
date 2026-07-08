package com.qpay.xcash.ui.login.dialog

import com.qpay.xcash.ui.components.neonGlow
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.automirrored.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.qpay.xcash.R
import com.qpay.xcash.ui.theme.AppTheme
import com.qpay.xcash.ui.theme.BlackGoldColors
import com.qpay.xcash.ui.theme.LocalAppColors
import com.qpay.xcash.ui.theme.NeonColors
import com.qpay.xcash.ui.theme.dustyRed
import com.qpay.xcash.ui.theme.neonCyanLight
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.activity.compose.BackHandler
import androidx.compose.ui.graphics.Shadow
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun LoginBottomSheet(
    onDismissRequest: () -> Unit,
    onLoginSubmit: (String, String) -> Unit,
    errorMessage: String? = null,
    showBiometricButton: Boolean = false,
    onBiometricLogin: () -> Unit = {}
) {
    var isVisible by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        isVisible = true
    }

    fun dismissWithAnimation() {
        isVisible = false
        scope.launch {
            delay(300)
            onDismissRequest()
        }
    }

    Dialog(
        onDismissRequest = { dismissWithAnimation() },
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = false,
            dismissOnBackPress = false,
            dismissOnClickOutside = false
        )
    ) {
        BackHandler(enabled = isVisible) {
            dismissWithAnimation()
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.6f))
                .imePadding()
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = { dismissWithAnimation() }
                ),
            contentAlignment = Alignment.BottomCenter
        ) {
            AnimatedVisibility(
                visible = isVisible,
                enter = slideInVertically(
                    initialOffsetY = { it },
                    animationSpec = tween(300)
                ) + fadeIn(animationSpec = tween(300)),
                exit = slideOutVertically(
                    targetOffsetY = { it },
                    animationSpec = tween(300)
                ) + fadeOut(animationSpec = tween(300))
            ) {
                LoginBottomSheetContent(
                    onLoginSubmit = onLoginSubmit,
                    errorMessage = errorMessage,
                    showBiometricButton = showBiometricButton,
                    onBiometricLogin = onBiometricLogin
                )
            }
        }
    }
}

@Composable
fun LoginBottomSheetContent(
    onLoginSubmit: (String, String) -> Unit,
    errorMessage: String? = null,
    showBiometricButton: Boolean = false,
    onBiometricLogin: () -> Unit = {}
) {
    val colors = LocalAppColors.current
    var mobileNumber by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    val inputBorderColor = if (errorMessage != null) dustyRed else colors.login.loginSheet.inputAccent

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 16.dp)
    ) {
        // 頂部的標籤，位於框線上方
        Text(
            text = stringResource(id = R.string.login_your_account),
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = {} // 攔截點擊，避免關閉對話框
                )
                .background(colors.bg.page, RoundedCornerShape(32.dp))
                .border(2.dp, colors.login.loginSheet.outerBorder, RoundedCornerShape(32.dp))
                .padding(horizontal = 32.dp, vertical = 25.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Mobile Number Label
            Text(
                text = stringResource(id = R.string.mobile_number),
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )

            // Mobile Input Row
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(35.dp)
                        .then(
                            if (colors.effect.enableGlow) Modifier.neonGlow(
                                color = inputBorderColor,
                                alpha = 0.5f,
                                glowRadius = 15.dp,
                                borderRadius = 25.dp
                            ) else Modifier
                        )
                        .background(colors.bg.page, RoundedCornerShape(25.dp))
                        .border(2.dp, inputBorderColor, RoundedCornerShape(25.dp))
                        .padding(horizontal = 16.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    BasicTextField(
                        value = mobileNumber,
                        onValueChange = { mobileNumber = it },
                        textStyle = TextStyle(color = Color.White, fontSize = 14.sp),
                        cursorBrush = SolidColor(colors.accent.primary),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Box(
                    modifier = Modifier
                        .size(35.dp)
                        .then(
                            if (colors.effect.enableGlow) Modifier.neonGlow(
                                color = colors.login.loginSheet.inputAccent,
                                alpha = 0.5f,
                                glowRadius = 15.dp,
                                borderRadius = 17.5.dp
                            ) else Modifier
                        )
                        .border(2.dp, colors.login.loginSheet.inputAccent, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Visibility,
                        contentDescription = stringResource(id = R.string.visibility_desc),
                        tint = colors.login.loginSheet.inputAccent,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
            if (errorMessage != null) {
                Text(
                    text = errorMessage,
                    color = dustyRed,
                    fontSize = 12.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 6.dp)
                )
            }
            Spacer(modifier = Modifier.height(10.dp))

            // Password Label
            Text(
                text = stringResource(id = R.string.password),
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )

            // Password Input Row
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(35.dp)
                        .then(
                            if (colors.effect.enableGlow) Modifier.neonGlow(
                                color = inputBorderColor,
                                alpha = 0.5f,
                                glowRadius = 15.dp,
                                borderRadius = 25.dp
                            ) else Modifier
                        )
                        .background(colors.bg.page, RoundedCornerShape(25.dp))
                        .border(2.dp, inputBorderColor, RoundedCornerShape(25.dp))
                        .padding(horizontal = 16.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    BasicTextField(
                        value = password,
                        onValueChange = { password = it },
                        textStyle = TextStyle(color = Color.White, fontSize = 16.sp),
                        cursorBrush = SolidColor(colors.accent.primary),
                        singleLine = true,
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Box(
                    modifier = Modifier
                        .size(35.dp)
                        .then(
                            if (colors.effect.enableGlow) Modifier.neonGlow(
                                color = colors.login.loginSheet.inputAccent,
                                alpha = 0.5f,
                                glowRadius = 15.dp,
                                borderRadius = 17.5.dp
                            ) else Modifier
                        )
                        .border(2.dp, colors.login.loginSheet.inputAccent, CircleShape)
                        .clickable { passwordVisible = !passwordVisible },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = stringResource(id = R.string.toggle_visibility_desc),
                        tint = colors.login.loginSheet.inputAccent,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            // Password Hints
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    stringResource(id = R.string.password_hint_length),
                    color = Color.White,
                    fontSize = 12.sp
                )
                Spacer(modifier = Modifier.width(3.dp))
                Text(
                    stringResource(id = R.string.login_hint_separator),
                    color = colors.login.loginSheet.hint,
                    fontSize = 12.sp
                )
                Spacer(modifier = Modifier.width(3.dp))
                Text(
                    stringResource(id = R.string.password_hint_uppercase),
                    color = colors.login.loginSheet.hint,
                    fontSize = 12.sp
                )
                Spacer(modifier = Modifier.width(3.dp))
                Text(
                    stringResource(id = R.string.login_hint_separator),
                    color = colors.login.loginSheet.hint,
                    fontSize = 12.sp
                )
                Spacer(modifier = Modifier.width(3.dp))
                Text(
                    stringResource(id = R.string.password_hint_number),
                    color = colors.login.loginSheet.hint,
                    fontSize = 12.sp
                )
            }

            // Forgot Password
            Text(
                text = stringResource(id = R.string.forgot_password),
                color = neonCyanLight,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                modifier = Modifier
                    .fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Send Button
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .then(
                        if (colors.effect.enableGlow) Modifier.neonGlow(
                            color = colors.login.loginSheet.submitButton,
                            alpha = 0.4f,
                            glowRadius = 20.dp,
                            borderRadius = 25.dp
                        ) else Modifier
                    )
                    .border(2.dp, colors.login.loginSheet.submitButton, CircleShape)
                    .clickable { onLoginSubmit(mobileNumber, password) },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Send,
                    contentDescription = stringResource(id = R.string.submit_login_desc),
                    tint = colors.login.loginSheet.submitButton,
                    modifier = Modifier
                        .size(25.dp)
                        .rotate(-40f)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            if (showBiometricButton) {
                HorizontalDivider(color = Color.White.copy(alpha = 0.15f))
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = onBiometricLogin
                        ),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Fingerprint,
                        contentDescription = null,
                        tint = colors.accent.primary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = stringResource(id = R.string.biometric_login_btn),
                        color = colors.accent.primary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
            } else {
                Spacer(modifier = Modifier.height(4.dp))
            }
        }
    }
}

@Preview(name = "Neon", showBackground = true, backgroundColor = 0xFF030F1B)
@Composable
private fun LoginBottomSheetPreviewNeon() {
    AppTheme(colors = NeonColors) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {
            LoginBottomSheetContent(onLoginSubmit = { _, _ -> })
        }
    }
}

@Preview(name = "Black Gold", showBackground = true, backgroundColor = 0xFF050505)
@Composable
private fun LoginBottomSheetPreviewBlackGold() {
    AppTheme(colors = BlackGoldColors) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {
            LoginBottomSheetContent(onLoginSubmit = { _, _ -> })
        }
    }
}
