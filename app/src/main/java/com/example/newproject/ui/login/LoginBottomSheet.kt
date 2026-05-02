package com.example.newproject.ui.login

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
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
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
import com.example.newproject.ui.theme.NeonCyan
import com.example.newproject.ui.theme.NeonPurple
import com.example.newproject.ui.theme.WelcomeBackground

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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun LoginBottomSheet(
    onDismissRequest: () -> Unit,
    onLoginSubmit: (String, String) -> Unit
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

    BackHandler(enabled = isVisible) {
        dismissWithAnimation()
    }

    Dialog(
        onDismissRequest = { dismissWithAnimation() },
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = false
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
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
                LoginBottomSheetContent(onLoginSubmit = onLoginSubmit)
            }
        }
    }
}

@Composable
fun LoginBottomSheetContent(
    onLoginSubmit: (String, String) -> Unit
) {
    var mobileNumber by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 16.dp)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = {} // 攔截點擊，避免關閉對話框
                        )
                        .background(WelcomeBackground, RoundedCornerShape(32.dp))
                        .border(2.dp, NeonPurple, RoundedCornerShape(32.dp))
                        .padding(horizontal = 32.dp, vertical = 32.dp)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
            // Mobile Number Label
            Text(
                text = stringResource(id = R.string.mobile_number),
                color = NeonCyan,
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
                        .background(Color(0xFF2C2C2C), RoundedCornerShape(25.dp))
                        .border(2.dp, NeonCyan, RoundedCornerShape(25.dp))
                        .padding(horizontal = 16.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    BasicTextField(
                        value = mobileNumber,
                        onValueChange = { mobileNumber = it },
                        textStyle = TextStyle(color = Color.White, fontSize = 14.sp),
                        cursorBrush = SolidColor(NeonCyan),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Box(
                    modifier = Modifier
                        .size(30.dp)
                        .border(2.dp, NeonCyan, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    // 暫時使用 Info 替代眼睛圖示，若要使用 Visibility，需加入 material-icons-extended 依賴
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = stringResource(id = R.string.visibility_desc),
                        tint = Color.LightGray,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            
            // Password Label
            Text(
                text = stringResource(id = R.string.password),
                color = NeonCyan,
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
                        .background(Color(0xFF2C2C2C), RoundedCornerShape(25.dp))
                        .border(2.dp, NeonCyan, RoundedCornerShape(25.dp))
                        .padding(horizontal = 16.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    BasicTextField(
                        value = password,
                        onValueChange = { password = it },
                        textStyle = TextStyle(color = Color.White, fontSize = 16.sp),
                        cursorBrush = SolidColor(NeonCyan),
                        singleLine = true,
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Box(
                    modifier = Modifier
                        .size(30.dp)
                        .border(2.dp, NeonCyan, CircleShape)
                        .clickable { passwordVisible = !passwordVisible },
                    contentAlignment = Alignment.Center
                ) {
                    // 暫時使用 Info 與 Lock 替代眼睛圖示，若要使用 Visibility/VisibilityOff，需加入 material-icons-extended 依賴
                    Icon(
                        imageVector = if (passwordVisible) Icons.Default.Info else Icons.Default.Lock,
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
                Text(" | ", color = Color.Gray, fontSize = 12.sp)
                Text(stringResource(id = R.string.password_hint_uppercase), color = NeonCyan, fontSize = 12.sp)
                Text(" | ", color = Color.Gray, fontSize = 12.sp)
                Text(stringResource(id = R.string.password_hint_number), color = NeonCyan, fontSize = 12.sp)
            }
            
            // Forgot Password
            Text(
                text = stringResource(id = R.string.forgot_password),
                color = NeonPurple,
                fontSize = 12.sp,
                modifier = Modifier
                    .fillMaxWidth()
            )
            
            Spacer(modifier = Modifier.height(10.dp))
            
            // Send Button
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .border(2.dp, NeonPurple, CircleShape)
                    .clickable { onLoginSubmit(mobileNumber, password) },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Send,
                    contentDescription = stringResource(id = R.string.submit_login_desc),
                    tint = NeonPurple,
                    modifier = Modifier
                        .size(24.dp)
                        .rotate(-45f)
                )
            }
            
            Spacer(modifier = Modifier.height(10.dp))
            
            // Biometrics
            Text(
                text = stringResource(id = R.string.login_biometrics),
                color = NeonCyan,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                style = TextStyle(shadow = Shadow(color = NeonCyan, blurRadius = 20f))
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(horizontalArrangement = Arrangement.Center) {
                Icon(imageVector = Icons.Default.Face, contentDescription = stringResource(id = R.string.face_id_desc), tint = Color.White, modifier = Modifier.size(24.dp))
                Spacer(modifier = Modifier.width(12.dp))
                Icon(imageVector = Icons.Default.Lock, contentDescription = stringResource(id = R.string.fingerprint_desc), tint = Color.White, modifier = Modifier.size(24.dp))
            }
            
            Spacer(modifier = Modifier.height(24.dp))
        }
    }

@Preview(showBackground = true)
@Composable
fun LoginBottomSheetPreview() {
    MaterialTheme {
        Box(modifier = Modifier.fillMaxSize().background(Color.Black), contentAlignment = Alignment.BottomCenter) {
            LoginBottomSheetContent(onLoginSubmit = { _, _ -> })
        }
    }
}
