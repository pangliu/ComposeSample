package com.qpay.xcash.ui.scanpay

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.qpay.xcash.ui.Routes
import com.qpay.xcash.ui.components.QrMode
import com.qpay.xcash.ui.components.QrModeTabSelector
import com.qpay.xcash.ui.components.XcashQrUrl
import com.qpay.xcash.ui.components.neonGlow
import com.qpay.xcash.ui.scanpay.components.MyQrContent
import com.qpay.xcash.ui.theme.AppTheme
import com.qpay.xcash.ui.theme.BlackGoldAssets
import com.qpay.xcash.ui.theme.BlackGoldColors
import com.qpay.xcash.ui.theme.LocalAppAssets
import com.qpay.xcash.ui.theme.LocalAppColors
import com.qpay.xcash.ui.theme.NeonColors
import com.qpay.xcash.ui.theme.lemonYellow

@Composable
fun ScanPayScreen(
    viewModel: ScanPayViewModel = hiltViewModel(),
    onNavigate: (String) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    ScanPayContent(
        uiState = uiState,
        qrCodeUrl = XcashQrUrl.FAKE_STORE,
        onQrCodeScanned = { url ->
            Log.e("tag", "onQrCodeScanned: $url")
            when {
                url.contains(XcashQrUrl.STORE_PREFIX) -> {
                    val (account, nickName, name) = parseXcashQrCode(url)
                    viewModel.setRecipientInfo(account = account, nickName = nickName, name = name)
                    onNavigate(Routes.SCAN_PAY_INPUT_AMOUNT)
                }
                url.contains(XcashQrUrl.PERSONAL_PREFIX) -> {
                    val (countryCode, phoneNumber) = parseXcashPersonalQrCode(url)
                    if (countryCode.isNotEmpty() && phoneNumber.isNotEmpty()) {
                        onNavigate(Routes.generalTransfer(countryCode = countryCode, phoneNumber = phoneNumber))
                    }
                }
            }
        }
    )
}

@Composable
private fun ScanPayContent(
    uiState: ScanPayUiState,
    qrCodeUrl: String,
    onQrCodeScanned: (String) -> Unit = {}
) {
    val colors = LocalAppColors.current
    val assets = LocalAppAssets.current
    var selectedMode by rememberSaveable { mutableStateOf(QrMode.MY_QR) }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.bg.page)
    ) {
        assets.scanPayBackground?.let { resId ->
            Image(
                painter = painterResource(resId),
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.fillMaxSize()
            )
        }
        Column(
            modifier = Modifier.fillMaxSize(),
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
            Spacer(modifier = Modifier.weight(1f))
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                assets.scanPayYellowStarDecor?.let { resId ->
                    Image(
                        painter = painterResource(resId),
                        contentDescription = null,
                        modifier = Modifier
                            .then(
                                if (colors.effect.enableGlow)
                                    Modifier.neonGlow(color = lemonYellow, alpha = 0.1f, glowRadius = 30.dp)
                                else Modifier
                            )
                    )
                }
                assets.scanPayTreeDecor?.let { resId ->
                    Image(
                        painter = painterResource(resId),
                        contentDescription = null,
                        modifier = Modifier
                            .size(80.dp)
                            .then(
                                if (colors.effect.enableGlow)
                                    Modifier.neonGlow(color = lemonYellow, alpha = 0.2f, glowRadius = 30.dp)
                                else Modifier
                            )
                    )
                }
            }
        }
    }
}

/**
 * 解析商家 QR code（http://xcash_store）URL，取出收款人資訊。
 *
 * URL 格式：
 *   http://xcash_store.io/pay?account={account}&to={nickName}&name={name}
 *
 * 範例：
 *   http://xcash_store.io/pay?account=bruceb_001&to=bruceb&name=Bruce+Banner
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

/**
 * 解析個人轉帳 QR code（http://xcash_personal）URL。
 *
 * URL 格式：
 *   http://xcash_personal.io/pay?country_code={countryCode}&phone_number={phoneNumber}
 *
 * @return Pair(countryCode, phoneNumber)，解析失敗時兩個值皆為空字串
 */
private fun parseXcashPersonalQrCode(url: String): Pair<String, String> {
    return try {
        val uri = Uri.parse(url)
        Pair(uri.getQueryParameter("country_code") ?: "", uri.getQueryParameter("phone_number") ?: "")
    } catch (e: Exception) {
        Pair("", "")
    }
}

@Preview(name = "Neon", showBackground = true, backgroundColor = 0xFF0B1327)
@Composable
private fun ScanPayScreenPreviewNeon() {
    AppTheme(colors = NeonColors) {
        ScanPayContent(
            uiState = ScanPayUiState(myUserName = "Hank Liu", myNickName = "Hank"),
            qrCodeUrl = XcashQrUrl.FAKE_PERSONAL
        )
    }
}

@Preview(name = "Black Gold", showBackground = true, backgroundColor = 0xFF050505)
@Composable
private fun ScanPayScreenPreviewBlackGold() {
    AppTheme(colors = BlackGoldColors, assets = BlackGoldAssets) {
        ScanPayContent(
            uiState = ScanPayUiState(myUserName = "Hank Liu", myNickName = "Hank"),
            qrCodeUrl = XcashQrUrl.FAKE_PERSONAL
        )
    }
}
