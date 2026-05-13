package com.example.newproject.ui.login.dialog

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.res.stringResource
import com.example.newproject.R
import com.example.newproject.ui.components.neonGlow
import com.example.newproject.ui.theme.NeonBlue
import com.example.newproject.ui.theme.NeonCyan
import com.example.newproject.ui.theme.NeonCyanLight
import com.example.newproject.ui.theme.NeonPurple
import com.example.newproject.ui.theme.NeonPurpleLight
import com.example.newproject.ui.theme.WelcomeBackground

@Composable
fun AccountStatusDialog(onDismiss: () -> Unit) {
    var showFindAppDialog by remember { mutableStateOf(false) }

    if (showFindAppDialog) {
        FindAppDialog(
            onDismiss = { showFindAppDialog = false },
            onSubmit = { _, _, _ -> showFindAppDialog = false }
        )
        return
    }

    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .neonGlow(
                    color = NeonCyanLight,
                    alpha = 0.6f,
                    glowRadius = 16.dp,
                    borderRadius = 24.dp
                )
                .background(WelcomeBackground, RoundedCornerShape(24.dp))
                .border(2.dp, NeonCyanLight, RoundedCornerShape(24.dp))
                .padding(24.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Title
                Text(
                    text = stringResource(id = R.string.select_action),
                    color = NeonCyanLight,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    style = TextStyle(
                        shadow = Shadow(color = NeonCyan, blurRadius = 15f)
                    )
                )
                
                Spacer(modifier = Modifier.height(32.dp))
                
                // Button 1: Check Application Progress
                ActionDialogButton(
                    icon = R.mipmap.ic_check_progress,
                    text = stringResource(id = R.string.check_application_progress),
                    borderColor = NeonPurpleLight,
                    onClick = { showFindAppDialog = true }
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Button 2: Verify My Identity
                ActionDialogButton(
                    icon = R.mipmap.ic_verify_id,
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
                        .neonGlow(
                            color = NeonCyanLight,
                            alpha = 0.6f,
                            glowRadius = 16.dp,
                            borderRadius = 24.dp
                        )
                        .border(2.dp, NeonCyanLight, RoundedCornerShape(24.dp))
                        .background(
                            color = WelcomeBackground,
                            shape = RoundedCornerShape(24.dp)
                        )
                        .clickable { onDismiss() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = stringResource(id = R.string.close),
                        tint = NeonCyanLight
                    )
                }
            }
        }
    }
}

@Composable
fun ActionDialogButton(icon: Int, text: String, borderColor: Color, onClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .neonGlow(
                color = borderColor,
                alpha = 0.6f,
                glowRadius = 16.dp,
                borderRadius = 16.dp
            )
            .background(WelcomeBackground, RoundedCornerShape(16.dp))
            .border(2.dp, borderColor, RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .padding(start = 10.dp, top = 20.dp, end = 3.dp, bottom = 20.dp)
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = null,
            tint = borderColor,
            modifier = Modifier.size(32.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            color = Color.White,
            fontSize = 14.sp,
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
        ) {
            AccountStatusDialog(onDismiss = {})
        }
    }
}
