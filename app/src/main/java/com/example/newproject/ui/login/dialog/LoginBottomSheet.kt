package com.example.newproject.ui.login.dialog

import com.example.newproject.ui.components.neonGlow
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
import com.example.newproject.R
import com.example.newproject.ui.theme.neonCyan
import com.example.newproject.ui.theme.neonPurple
import com.example.newproject.ui.theme.welcomeBackground
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
import com.example.newproject.ui.theme.neonCyanLight
import com.example.newproject.ui.theme.neonMint
import com.example.newproject.ui.theme.neonPurpleLight
import com.example.newproject.ui.theme.sendPink
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
                enter = slideInVertically(initialOffsetY = { it }, animationSpec = tween(300)) + fadeIn(animationSpec = tween(300)),
                exit = slideOutVertically(targetOffsetY = { it }, animationSpec = tween(300)) + fadeOut(animationSpec = tween(300))
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
    var mobileNumber by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    val inputBorderColor = if (errorMessage != null) sendPink else neonCyanLight

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
                .background(welcomeBackground, RoundedCornerShape(32.dp))
                .border(2.dp, neonPurpleLight, RoundedCornerShape(32.dp))
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
                        .neonGlow(color = inputBorderColor, alpha = 0.5f, glowRadius = 15.dp, borderRadius = 25.dp)
                        .background(welcomeBackground, RoundedCornerShape(25.dp))
                        .border(2.dp, inputBorderColor, RoundedCornerShape(25.dp))
                        .padding(horizontal = 16.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    BasicTextField(
                        value = mobileNumber,
                        onValueChange = { mobileNumber = it },
                        textStyle = TextStyle(color = Color.White, fontSize = 14.sp),
                        cursorBrush = SolidColor(neonCyan),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Box(
                    modifier = Modifier
                        .size(35.dp)
                        .neonGlow(color = inputBorderColor, alpha = 0.5f, glowRadius = 15.dp, borderRadius = 17.5.dp)
                        .border(2.dp, inputBorderColor, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Visibility,
                        contentDescription = stringResource(id = R.string.visibility_desc),
                        tint = Color.LightGray,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
            if (errorMessage != null) {
                Text(
                    text = errorMessage,
                    color = sendPink,
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
                        .neonGlow(color = inputBorderColor, alpha = 0.5f, glowRadius = 15.dp, borderRadius = 25.dp)
                        .background(welcomeBackground, RoundedCornerShape(25.dp))
                        .border(2.dp, inputBorderColor, RoundedCornerShape(25.dp))
                        .padding(horizontal = 16.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    BasicTextField(
                        value = password,
                        onValueChange = { password = it },
                        textStyle = TextStyle(color = Color.White, fontSize = 16.sp),
                        cursorBrush = SolidColor(neonCyan),
                        singleLine = true,
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Box(
                    modifier = Modifier
                        .size(35.dp)
                        .neonGlow(color = inputBorderColor, alpha = 0.5f, glowRadius = 15.dp, borderRadius = 17.5.dp)
                        .border(2.dp, inputBorderColor, CircleShape)
                        .clickable { passwordVisible = !passwordVisible },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = stringResource(id = R.string.toggle_visibility_desc),
                        tint = Color.LightGray,
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
                Text(stringResource(id = R.string.password_hint_length), color = Color.White, fontSize = 12.sp)
                Spacer(modifier = Modifier.width(3.dp))
                Text(stringResource(id = R.string.login_hint_separator), color = neonMint, fontSize = 12.sp)
                Spacer(modifier = Modifier.width(3.dp))
                Text(stringResource(id = R.string.password_hint_uppercase), color = neonMint, fontSize = 12.sp)
                Spacer(modifier = Modifier.width(3.dp))
                Text(stringResource(id = R.string.login_hint_separator), color = neonMint, fontSize = 12.sp)
                Spacer(modifier = Modifier.width(3.dp))
                Text(stringResource(id = R.string.password_hint_number), color = neonMint, fontSize = 12.sp)
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
                    .neonGlow(color = neonMint, alpha = 0.6f, glowRadius = 20.dp, borderRadius = 25.dp)
                    .border(2.dp, neonMint, CircleShape)
                    .clickable { onLoginSubmit(mobileNumber, password) },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Send,
                    contentDescription = stringResource(id = R.string.submit_login_desc),
                    tint = neonMint,
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
                        tint = neonCyan,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = stringResource(id = R.string.biometric_login_btn),
                        color = neonCyan,
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

@Preview(showBackground = true)
@Composable
fun LoginBottomSheetPreview() {
    MaterialTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black),
            contentAlignment = Alignment.BottomCenter
        ) {
            LoginBottomSheetContent(onLoginSubmit = { _, _ -> })
        }
    }
}
