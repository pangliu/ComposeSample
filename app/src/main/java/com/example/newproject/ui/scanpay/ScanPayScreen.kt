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
import androidx.compose.runtime.collectAsState
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
import com.example.newproject.ui.scanpay.components.MyQrContent
import com.example.newproject.ui.theme.LocalAppColors

@Composable
fun ScanPayScreen(
    viewModel: ScanPayViewModel = hiltViewModel(),
    onNavigate: (String) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    ScanPayContent(
        uiState = uiState,
        onQrCodeScanned = { url ->
            Log.e("tag", "onQrCodeScanned: $url")
            if (url.contains("http://xcash")) {
                val (account, nickName, name) = parseXcashQrCode(url)
                viewModel.setRecipientInfo(account = account, nickName = nickName, name = name)
                onNavigate(Routes.SCAN_PAY_INPUT_AMOUNT)
            }
        }
    )
}

@Composable
private fun ScanPayContent(
    uiState: ScanPayUiState,
    onQrCodeScanned: (String) -> Unit = {}
) {
    val colors = LocalAppColors.current
    var selectedMode by rememberSaveable { mutableStateOf(QrMode.MY_QR) }
    val qrCodeUrl = "http://xcash.io/pay?account=hank_001&to=hank&name=hank+liu"
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.bg.page),
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
            userName = uiState.myUserName,
            nickName = uiState.myNickName,
            balance = uiState.balance,
            onQrCodeScanned = onQrCodeScanned
        )
    }
}

/**
 * 解析 XCash QR code URL，取出收款人資訊。
 *
 * URL 格式：
 *   http://xcash.io/pay?account={account}&to={nickName}&name={name}
 *
 * 範例：
 *   http://xcash.io/pay?account=bruceb_001&to=bruceb&name=Bruce+Banner
 *
 * Query 參數對應：
 *   account → 收款人帳號（用於實際付款，例如 "bruceb_001"）
 *   to      → 收款人暱稱（顯示用 @handle，例如 "bruceb"）
 *   name    → 收款人全名（顯示用，例如 "Bruce Banner"）
 *
 * @return Triple(account, nickName, name)，解析失敗時三個值皆為空字串
 */
private fun parseXcashQrCode(url: String): Triple<String, String, String> {
    return try {
        val uri = Uri.parse(url)
        val account = uri.getQueryParameter("account") ?: ""
        val nickName = uri.getQueryParameter("to") ?: ""
        val name = uri.getQueryParameter("name") ?: ""
        Triple(account, nickName, name)
    } catch (e: Exception) {
        Triple("", "", "")
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0B1327)
@Composable
private fun ScanPayScreenPreview() {
    MaterialTheme {
        ScanPayContent(uiState = ScanPayUiState(myUserName = "Hank Liu", myNickName = "Hank"))
    }
}
