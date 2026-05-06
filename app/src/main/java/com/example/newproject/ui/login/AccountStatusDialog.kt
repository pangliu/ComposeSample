package com.example.newproject.ui.login

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.res.stringResource
import com.example.newproject.R
import com.example.newproject.ui.theme.NeonBlue
import com.example.newproject.ui.theme.NeonCyan
import com.example.newproject.ui.theme.NeonPurple
import com.example.newproject.ui.theme.WelcomeBackground

@Composable
fun AccountStatusDialog(onDismiss: () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(WelcomeBackground, RoundedCornerShape(24.dp))
                .border(2.dp, NeonCyan, RoundedCornerShape(24.dp))
                .padding(24.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Title
                Text(
                    text = stringResource(id = R.string.select_action),
                    color = NeonCyan,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    style = TextStyle(
                        shadow = Shadow(color = NeonCyan, blurRadius = 15f)
                    )
                )
                
                Spacer(modifier = Modifier.height(32.dp))
                
                // Button 1: Check Application Progress
                ActionDialogButton(
                    icon = Icons.Default.Search,
                    text = stringResource(id = R.string.check_application_progress),
                    borderColor = NeonPurple,
                    onClick = { 
                        // TODO: 處理點擊事件
                        onDismiss()
                    }
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Button 2: Verify My Identity
                ActionDialogButton(
                    icon = Icons.Default.Person,
                    text = stringResource(id = R.string.verify_my_identity),
                    borderColor = NeonBlue,
                    onClick = { 
                        // TODO: 處理點擊事件
                        onDismiss()
                    }
                )
                
                Spacer(modifier = Modifier.height(32.dp))
                
                // Close Button
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .border(2.dp, NeonCyan, RoundedCornerShape(24.dp))
                        .clickable { onDismiss() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = stringResource(id = R.string.close),
                        tint = Color.White
                    )
                }
            }
        }
    }
}

@Composable
fun ActionDialogButton(icon: ImageVector, text: String, borderColor: Color, onClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .background(WelcomeBackground, RoundedCornerShape(16.dp))
            .border(2.dp, borderColor, RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 16.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = NeonCyan,
            modifier = Modifier.size(32.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = text,
            color = Color.White,
            fontSize = 15.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.weight(1f)
        )
        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = stringResource(id = R.string.go),
            tint = borderColor
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AccountStatusDialogPreview() {
    MaterialTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black), // 黑色背景可以讓霓虹特效更明顯
            contentAlignment = Alignment.Center
        ) {
            AccountStatusDialog(onDismiss = {})
        }
    }
}
