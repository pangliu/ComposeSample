package com.example.newproject.ui.scanpay

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.newproject.ui.components.QrMode
import com.example.newproject.ui.components.QrModeTabSelector
import com.example.newproject.ui.theme.welcomeBackground

@Composable
fun ScanPayScreen() {
    var selectedMode by rememberSaveable { mutableStateOf(QrMode.MY_QR) }
    val qrCodeUrl = "http://xcash.xxxx.cpu.ttw"
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(welcomeBackground),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        QrModeTabSelector(
            selectedMode = selectedMode,
            onModeChange = { selectedMode = it },
            modifier = Modifier.padding(horizontal = 32.dp)
        )
        Spacer(modifier = Modifier.height(15.dp))
        when (selectedMode) {
            QrMode.SCAN_QR -> ScanQrContent()
            QrMode.MY_QR   -> MyQrContent(qrCodeUrl = qrCodeUrl)
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0B1327)
@Composable
private fun ScanPayScreenPreview() {
    MaterialTheme {
        ScanPayScreen()
    }
}
