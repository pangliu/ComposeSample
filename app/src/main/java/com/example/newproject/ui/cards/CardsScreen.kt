package com.example.newproject.ui.cards

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.newproject.R
import com.example.newproject.ui.theme.NeonCyan
import com.example.newproject.ui.theme.WelcomeBackground

@Composable
fun CardsScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.List,
                contentDescription = null,
                tint = NeonCyan.copy(alpha = 0.4f),
                modifier = Modifier.size(64.dp)
            )
            Text(
                text = stringResource(R.string.cards_coming_soon_title),
                color = NeonCyan,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = stringResource(R.string.coming_soon),
                color = Color.Gray,
                fontSize = 14.sp
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0A1F37)
@Composable
fun CardsScreenPreview() {
    MaterialTheme {
        CardsScreen()
    }
}
