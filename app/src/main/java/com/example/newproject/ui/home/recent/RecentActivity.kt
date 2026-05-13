package com.example.newproject.ui.home.recent

import android.graphics.Color
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.newproject.ui.components.neonGlow
import com.example.newproject.ui.theme.DarkBackground
import com.example.newproject.ui.theme.NeonCyan
import com.example.newproject.ui.theme.NeonCyanLight
import com.example.newproject.ui.theme.NeonPurple
import com.example.newproject.ui.theme.ThemeWhite
import com.example.newproject.ui.theme.WelcomeBackground


@Composable
fun RecentActivity() {
    Column(
        modifier = Modifier.fillMaxWidth()
    ){
        Text(
            text = "Recent Activity",
            color = ThemeWhite,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(10.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp) // 固定高度
                .neonGlow(
                    color = NeonCyanLight,
                    alpha = 0.6f,
                    glowRadius = 18.dp,
                    borderRadius = 18.dp,
                )
                .border(
                    width = 2.dp,
                    color = NeonCyanLight, // 你剛才轉好的洋紅色
                    shape = RoundedCornerShape(18.dp)
                )
                .background(
                    color = WelcomeBackground,
                    shape = RoundedCornerShape(18.dp)
                )

        ) {

        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0A0E1A)
@Composable
fun RecentActivityPreview() {
    MaterialTheme {
        Box(
            modifier = Modifier.Companion
                .fillMaxWidth()
                .background(DarkBackground)
                .padding(16.dp)
        ) {
            RecentActivity()
        }
    }
}