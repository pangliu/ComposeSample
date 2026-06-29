package com.example.newproject.ui.home.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.LightGray
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.newproject.R
import com.example.newproject.ui.components.neonGlow
import com.example.newproject.ui.theme.darkBackground
import com.example.newproject.ui.theme.neonPurple
import com.example.newproject.ui.theme.welcomeBackground

@Composable
fun QuestCard() {
    Box(
        modifier = Modifier
//            .padding(top = 20.dp) // 給上方預留一點空間讓人物露出來
            .fillMaxWidth()
    ) {
        Text(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(top = 0.dp),
            text = "Quest Card",
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        // 1. 底層的紫色邊框卡片 (Row 所在的容器)
        Column(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .fillMaxWidth()
                .height(120.dp) // 固定高度
                .neonGlow(
                    color = neonPurple, 
                    alpha = 0.6f, 
                    glowRadius = 15.dp, 
                    borderRadius = 18.dp,
                    blurStyle = android.graphics.BlurMaskFilter.Blur.OUTER
                )
                .background(
                    color = welcomeBackground,
                    shape = RoundedCornerShape(18.dp)
                )
                .border(
                    width = 2.dp,
                    color = neonPurple, // 你剛才轉好的洋紅色
                    shape = RoundedCornerShape(18.dp)
                )
                .padding(horizontal = 16.dp, vertical = 8.dp) // 內部元件距離邊框的距離
        ) {
            Text(
                text = "Stack your points now",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            Text(
                text = "with breathing cyan light effect",
                color = Color.White,
                fontSize = 10.sp,
            )
        }
        Image(
            painter = painterResource(id = R.mipmap.bg_quest_card),
            contentDescription = "Quest Card",
            modifier = Modifier
                .size(150.dp)
                .align(Alignment.BottomEnd) // 關鍵：設定在 Box 中的對齊方式為右下角
                .offset(x = 3.dp, y = 0.dp)
        )
    }
}


@Preview(showBackground = true, backgroundColor = 0xFF0A0E1A)
@Composable
fun QuestCardPreview() {
    MaterialTheme {
        Box(
            modifier = Modifier.Companion
                .fillMaxWidth()
                .background(darkBackground)
                .padding(16.dp)
        ) {
            QuestCard()
        }
    }
}