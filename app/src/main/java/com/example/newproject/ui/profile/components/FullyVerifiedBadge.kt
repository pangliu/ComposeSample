package com.example.newproject.ui.profile.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.newproject.R
import com.example.newproject.ui.theme.LocalAppColors
import com.example.newproject.ui.theme.neonPink

private val CardBackground = Color(0xFF0E1A2E)

@Composable
fun FullyVerifiedBadge(
    text: String,
    modifier: Modifier = Modifier
) {
    val colors = LocalAppColors.current
    val iconSize = 32.dp
    Box(
        modifier = modifier,
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            modifier = Modifier
                .padding(start = iconSize / 2)
                .border(
                    width = 1.5.dp,
                    brush = Brush.horizontalGradient(
                        listOf(colors.secondary.copy(alpha = 0.7f), neonPink.copy(alpha = 0.9f))
                    ),
                    shape = RoundedCornerShape(50)
                )
                .padding(start = iconSize / 2 + 10.dp, end = 12.dp, top = 5.dp, bottom = 5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = text,
                color = colors.secondary,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
        }
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(CardBackground, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(R.mipmap.ic_shield_check),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier.size(iconSize)
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0E1A2E)
@Composable
private fun FullyVerifiedBadgePreview() {
    MaterialTheme {
        FullyVerifiedBadge(text = "FULLY VERIFIED", modifier = Modifier.padding(16.dp))
    }
}
