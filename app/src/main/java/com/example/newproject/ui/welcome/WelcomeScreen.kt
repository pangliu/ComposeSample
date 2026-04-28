package com.example.newproject.ui.welcome

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.newproject.ui.theme.NeonCyan
import com.example.newproject.ui.theme.NeonDivider
import com.example.newproject.ui.theme.NeonPurple
import com.example.newproject.ui.theme.WelcomeBackground

@Composable
fun WelcomeScreen(
    viewModel: WelcomeViewModel,
    onNavigateToHome: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
    val navigationEvent by viewModel.navigationEvent.collectAsState()

    // 監聽 Viewmodel 決定出來的路由方向
    LaunchedEffect(navigationEvent) {
        when (navigationEvent) {
            is WelcomeNavigationEvent.GoToHome -> onNavigateToHome()
            is WelcomeNavigationEvent.GoToLogin -> onNavigateToLogin()
            else -> {} // Idle 狀態，停留在目前畫面
        }
    }

    WelcomeScreenContent()
}

@Composable
fun WelcomeScreenContent() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(WelcomeBackground), // 引用統一色碼
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            // "xcash" 文字，帶有 Neon 螢光陰影
            Text(
                text = buildAnnotatedString {
                    withStyle(
                        SpanStyle(
                            color = NeonCyan,
                            shadow = Shadow(color = NeonCyan, blurRadius = 20f)
                        )
                    ) {
                        append("x")
                    }
                    withStyle(
                        SpanStyle(
                            color = NeonPurple,
                            shadow = Shadow(color = NeonPurple, blurRadius = 20f)
                        )
                    ) {
                        append("cash")
                    }
                },
                fontSize = 32.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 1.sp
            )
            
            Spacer(modifier = Modifier.width(16.dp))

            // 中央的分隔線 | 
            Box(
                modifier = Modifier
                    .width(2.dp)
                    .height(34.dp)
                    .background(NeonDivider)
            )

            Spacer(modifier = Modifier.width(16.dp))

            // "Tara!" 文字，帶有白青色輝光效果
            Text(
                text = "Tara!",
                color = Color.White,
                fontSize = 42.sp,
                fontWeight = FontWeight.Bold,
                style = TextStyle(
                    shadow = Shadow(color = NeonCyan.copy(alpha = 0.53f), blurRadius = 25f)
                ),
                letterSpacing = 1.sp
            )
        }
    }
}

@androidx.compose.ui.tooling.preview.Preview(showBackground = true)
@Composable
fun WelcomeScreenPreview() {
    MaterialTheme {
        WelcomeScreenContent()
    }
}
