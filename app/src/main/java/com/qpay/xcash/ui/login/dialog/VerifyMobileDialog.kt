package com.qpay.xcash.ui.login.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.qpay.xcash.R
import kotlinx.coroutines.delay
import com.qpay.xcash.ui.components.neonGlow
import com.qpay.xcash.ui.theme.AppTheme
import com.qpay.xcash.ui.theme.BlackGoldColors
import com.qpay.xcash.ui.theme.LocalAppColors
import com.qpay.xcash.ui.theme.NeonColors
import com.qpay.xcash.ui.theme.darkCharcoal

@Composable
fun VerifyMobileDialog(
    initialPhone: String,
    onDismiss: () -> Unit,
    onSubmit: (String) -> Unit
) {
    val colors = LocalAppColors.current
    val cancelButtonColor = if (colors.effect.enableGlow) colors.loginSheet.inputAccent else Color.White
    var phone by remember { mutableStateOf(initialPhone) }
    var otpCode by remember { mutableStateOf("") }
    var timeLeft by remember { mutableIntStateOf(60) }

    LaunchedEffect(Unit) {
        while (timeLeft > 0) {
            delay(1000L)
            timeLeft--
        }
    }

    val minutes = String.format("%02d", timeLeft / 60)
    val seconds = String.format("%02d", timeLeft % 60)
    val timerText = "$minutes:$seconds"

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = true,
            dismissOnClickOutside = false
        )
    ) {
        Column(
            modifier = Modifier
                .wrapContentHeight()
                .padding(top = 20.dp, bottom = 20.dp)
        ) {
            // 頂部的標籤，位於框線上方
            Text(
                text = stringResource(R.string.verify_mobile_title),
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 5.dp, bottom = 3.dp)
            )

            // 主要背景卡片
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .then(if (colors.effect.enableGlow) Modifier.neonGlow(color = colors.loginSheet.outerBorder, alpha = 0.6f, glowRadius = 16.dp, borderRadius = 20.dp) else Modifier)
                    .background(colors.bg.page, RoundedCornerShape(20.dp))
                    .border(2.dp, colors.loginSheet.outerBorder, RoundedCornerShape(20.dp))
                    .padding(horizontal = 25.dp, vertical = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
//                Spacer(modifier = Modifier.height(16.dp))

                // Phone number 標籤
                Text(
                    text = stringResource(R.string.verify_mobile_phone_label),
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Start
                )

                Spacer(modifier = Modifier.height(3.dp))

                // Phone Input + Send Button + Resend Text
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // 輸入框 (縮短長度且唯讀)
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(35.dp)
                            .then(if (colors.effect.enableGlow) Modifier.neonGlow(color = colors.loginSheet.inputAccent, alpha = 0.5f, glowRadius = 15.dp, borderRadius = 25.dp) else Modifier)
                            .background(darkCharcoal, RoundedCornerShape(25.dp))
                            .border(2.dp, colors.loginSheet.inputAccent, RoundedCornerShape(25.dp))
                            .padding(horizontal = 16.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Text(
                            text = phone,
                            color = colors.loginSheet.inputAccent,
                            fontSize = 14.sp,
                            maxLines = 1
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    // Send 按鈕
                    Box(
                        modifier = Modifier
                            .size(35.dp)
                            .then(if (colors.effect.enableGlow) Modifier.neonGlow(color = colors.loginSheet.inputAccent, alpha = 0.5f, glowRadius = 15.dp, borderRadius = 15.dp) else Modifier)
                            .background(darkCharcoal, CircleShape)
                            .border(2.dp, colors.loginSheet.inputAccent, CircleShape)
//                            .rotate(-45f)
                            .clickable { /* TODO: 發送驗證碼邏輯 */ },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Send,
                            contentDescription = stringResource(R.string.verify_mobile_send_desc),
                            tint = colors.loginSheet.inputAccent,
                            modifier = Modifier.size(15.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = stringResource(R.string.verify_mobile_resend_timer, timerText),
                    color = colors.accent.secondary,
                    fontSize = 12.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 4.dp),
                    textAlign = TextAlign.Start
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = stringResource(R.string.verify_mobile_enter_code),
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                    textAlign = TextAlign.Start
                )

                // 6 碼 OTP 輸入區塊
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    // 底層顯示的 6 個方塊
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        for (i in 0 until 6) {
                            val char = otpCode.getOrNull(i)?.toString() ?: ""
                            Box(
                                modifier = Modifier
                                    .size(35.dp)
                                    .then(if (colors.effect.enableGlow) Modifier.neonGlow(color = colors.loginSheet.inputAccent, alpha = 0.5f, glowRadius = 15.dp, borderRadius = 8.dp) else Modifier)
                                    .background(darkCharcoal, RoundedCornerShape(8.dp))
                                    .border(2.dp, colors.loginSheet.inputAccent, RoundedCornerShape(8.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = char,
                                    color = Color.White,
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    // 疊在最上層的全透明輸入框，負責接收所有的點擊與鍵盤事件
                    BasicTextField(
                        value = otpCode,
                        onValueChange = { newValue ->
                            val digitsOnly = newValue.filter { it.isDigit() }
                            if (digitsOnly.length <= 6) {
                                otpCode = digitsOnly
                                if (otpCode.length == 6) {
                                    onSubmit(otpCode)
                                }
                            }
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                        modifier = Modifier.matchParentSize(),
                        textStyle = TextStyle(color = Color.Transparent),
                        cursorBrush = androidx.compose.ui.graphics.SolidColor(Color.Transparent)
                    )
                }

                Spacer(modifier = Modifier.height(40.dp))

                // 關閉按鈕
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .then(if (colors.effect.enableGlow) Modifier.neonGlow(color = cancelButtonColor, alpha = 0.5f, glowRadius = 15.dp, borderRadius = 25.dp) else Modifier)
                        .background(colors.bg.page, CircleShape)
                        .border(2.dp, cancelButtonColor, CircleShape)
                        .clickable { onDismiss() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = stringResource(R.string.verify_mobile_close_desc),
                        tint = cancelButtonColor,
                        modifier = Modifier.size(30.dp)
                    )
                }
            }

        }
    }
}

@Preview(name = "Neon", showBackground = true, backgroundColor = 0xFF030F1B)
@Composable
private fun VerifyMobileDialogPreviewNeon() {
    AppTheme(colors = NeonColors) {
        Box(
            modifier = Modifier.fillMaxSize().padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            VerifyMobileDialog(initialPhone = "0912345678", onDismiss = {}, onSubmit = {})
        }
    }
}

@Preview(name = "Black Gold", showBackground = true, backgroundColor = 0xFF050505)
@Composable
private fun VerifyMobileDialogPreviewBlackGold() {
    AppTheme(colors = BlackGoldColors) {
        Box(
            modifier = Modifier.fillMaxSize().padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            VerifyMobileDialog(initialPhone = "0912345678", onDismiss = {}, onSubmit = {})
        }
    }
}
