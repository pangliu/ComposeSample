package com.example.newproject.ui.scanpay

import android.net.Uri
import android.util.Log
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
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.newproject.ui.Routes
import com.example.newproject.ui.components.QrMode
import com.example.newproject.ui.components.QrModeTabSelector
import com.example.newproject.ui.theme.welcomeBackground

@Composable
fun ScanPayScreen(
    viewModel: ScanPayViewModel = hiltViewModel(),
    onNavigate: (String) -> Unit = {}
) {
    var selectedMode by rememberSaveable { mutableStateOf(QrMode.MY_QR) }
    val qrCodeUrl = "http://xcash.io/pay?to=hank&name=hank+liu"
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
        MyQrContent(
            qrCodeUrl = qrCodeUrl,
            selectedMode = selectedMode,
            onQrCodeScanned = { url ->
                Log.e("tag", "onQrCodeScanned: $url")
                if (url.contains("http://xcash")) {
                    val (username, name) = parseXcashQrCode(url)
                    viewModel.setRecipientInfo(username, name)
                    onNavigate(Routes.SCAN_PAY_INPUT_AMOUNT)
                }
            }
        )
    }
}

/**
 * 解析 xcash QR code URL，取出收款人 username 與 name。
 * 預期格式：http://xcash.io/pay?to=bruceb&name=Bruce+Banner
 * 若 URL 不含這些參數則回傳空字串。
 */
private fun parseXcashQrCode(url: String): Pair<String, String> {
    return try {
        val uri = Uri.parse(url)
        val username = uri.getQueryParameter("to") ?: ""
        val name = uri.getQueryParameter("name") ?: ""
        Pair(username, name)
    } catch (e: Exception) {
        Pair("", "")
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0B1327)
@Composable
private fun ScanPayScreenPreview() {
    MaterialTheme {
        ScanPayScreen()
    }
}
