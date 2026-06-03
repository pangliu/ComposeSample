package com.example.newproject.ui.profile.security

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.newproject.R
import com.example.newproject.ui.components.SubPageTopBar
import com.example.newproject.ui.theme.welcomeBackground

@Composable
fun SecurityCenterScreen(onBack: () -> Unit) {
    SecurityCenterContent(onBack = onBack)
}

@Composable
fun SecurityCenterContent(onBack: () -> Unit = {}) {
    Scaffold(
        containerColor = welcomeBackground,
        contentColor = Color.White
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            SubPageTopBar(
                title = stringResource(R.string.profile_security_center),
                onBack = onBack
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0E1422)
@Composable
private fun SecurityCenterPreview() {
    MaterialTheme {
        SecurityCenterContent()
    }
}
