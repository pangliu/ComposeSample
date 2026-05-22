package com.example.newproject.ui.login.dialog

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.newproject.R
import com.example.newproject.ui.components.neonGlow
import com.example.newproject.ui.theme.neonCyan
import com.example.newproject.ui.theme.neonPurpleLight
import com.example.newproject.ui.theme.welcomeBackground

@Composable
fun BiometricEnrollDialog(
    onEnroll: () -> Unit,
    onSkip: () -> Unit
) {
    Dialog(
        onDismissRequest = onSkip,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnClickOutside = false
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.88f)
                .neonGlow(color = neonPurpleLight, alpha = 0.6f, glowRadius = 16.dp, borderRadius = 20.dp)
                .background(welcomeBackground, RoundedCornerShape(20.dp))
                .border(2.dp, neonPurpleLight, RoundedCornerShape(20.dp))
                .padding(horizontal = 28.dp, vertical = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.Fingerprint,
                contentDescription = null,
                tint = neonCyan,
                modifier = Modifier.size(64.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(R.string.biometric_enroll_title),
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.biometric_enroll_subtitle),
                color = Color.LightGray,
                fontSize = 13.sp,
                textAlign = TextAlign.Center,
                lineHeight = 20.sp
            )
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = onEnroll,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(46.dp)
                    .neonGlow(color = neonCyan, alpha = 0.5f, glowRadius = 15.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = neonCyan.copy(alpha = 0.15f),
                    contentColor = neonCyan
                ),
                border = BorderStroke(1.5.dp, neonCyan),
                shape = RoundedCornerShape(25.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Fingerprint,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(stringResource(R.string.biometric_enroll_enable_btn), fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(8.dp))
            TextButton(onClick = onSkip) {
                Text(
                    text = stringResource(R.string.biometric_enroll_skip_btn),
                    color = Color.Gray,
                    fontSize = 13.sp
                )
            }
        }
    }
}

@Preview
@Composable
fun BiometricEnrollDialogPreview() {
    MaterialTheme {
        BiometricEnrollDialog(onEnroll = {}, onSkip = {})
    }
}
