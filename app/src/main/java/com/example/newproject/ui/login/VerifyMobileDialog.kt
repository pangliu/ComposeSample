package com.example.newproject.ui.login

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.newproject.R
import com.example.newproject.ui.theme.DarkOverlay
import com.example.newproject.ui.theme.InputFieldDark
import com.example.newproject.ui.theme.NeonCyan
import com.example.newproject.ui.theme.NeonPurple
import com.example.newproject.ui.theme.WelcomeBackground

@Composable
fun VerifyMobileDialog(
    initialPhone: String,
    onDismiss: () -> Unit,
    onSubmit: (String) -> Unit
) {
    var phone by remember { mutableStateOf(initialPhone) }
    var otpCode by remember { mutableStateOf("") }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnClickOutside = false
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .wrapContentHeight()
                .padding(top = 20.dp, bottom = 20.dp) // 給 title 留空間
        ) {
            // 主要背景卡片
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(WelcomeBackground, RoundedCornerShape(20.dp))
                    .border(2.dp, NeonPurple, RoundedCornerShape(20.dp))
                    .padding(horizontal = 10.dp, vertical = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
//                Spacer(modifier = Modifier.height(16.dp))

                // Phone number 標籤
                Text(
                    text = "Phone number",
                    color = NeonCyan,
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
                            .width(130.dp)
                            .height(30.dp)
                            .shadow(elevation = 10.dp, spotColor = NeonCyan, shape = RoundedCornerShape(25.dp))
                            .background(InputFieldDark, RoundedCornerShape(25.dp))
                            .border(2.dp, NeonCyan, RoundedCornerShape(25.dp))
                            .padding(horizontal = 16.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Text(
                            text = phone,
                            color = Color.LightGray,
                            fontSize = 14.sp,
                            maxLines = 1
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    // Send 按鈕
                    Box(
                        modifier = Modifier
                            .size(30.dp)
                            .shadow(elevation = 10.dp, spotColor = NeonCyan, shape = CircleShape)
                            .background(InputFieldDark, CircleShape)
                            .border(2.dp, NeonCyan, CircleShape)
                            .rotate(-45f)
                            .clickable { /* TODO: 發送驗證碼邏輯 */ },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Send,
                            contentDescription = "Send",
                            tint = Color.LightGray,
                            modifier = Modifier.size(15.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Text(
                        text = "Resend after 00:32 sec.",
                        color = NeonPurple,
                        fontSize = 10.sp,
                        maxLines = 1,
                        softWrap = false
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

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
                                    .size(45.dp)
                                    .shadow(elevation = 8.dp, spotColor = NeonCyan, shape = RoundedCornerShape(8.dp))
                                    .background(InputFieldDark, RoundedCornerShape(8.dp))
                                    .border(2.dp, NeonCyan, RoundedCornerShape(8.dp)),
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
                        .shadow(elevation = 15.dp, spotColor = NeonCyan, shape = CircleShape)
                        .background(WelcomeBackground, CircleShape)
                        .border(2.dp, NeonCyan, CircleShape)
                        .clickable { onDismiss() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = NeonCyan,
                        modifier = Modifier.size(30.dp)
                    )
                }
            }

            // 頂部的 "Verify Mobile" 標籤，壓在邊框上
            Box(
                modifier = Modifier
                    .padding(start = 32.dp)
                    .offset(y = (-15).dp) // 往上偏移壓住邊框
                    .background(DarkOverlay) // 類似全黑，為了遮住邊框
                    .padding(horizontal = 8.dp)
            ) {
                Text(
                    text = "Verify Mobile",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Preview
@Composable
fun VerifyMobileDialogPreview() {
    MaterialTheme {
        VerifyMobileDialog(initialPhone = "0912345678", onDismiss = {}, onSubmit = {})
    }
}
