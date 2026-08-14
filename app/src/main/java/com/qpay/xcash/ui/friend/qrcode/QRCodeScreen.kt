package com.qpay.xcash.ui.friend.qrcode

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview as CameraXPreview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.IosShare
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.google.zxing.BinaryBitmap
import com.google.zxing.DecodeHintType
import com.google.zxing.MultiFormatReader
import com.google.zxing.NotFoundException
import com.google.zxing.PlanarYUVLuminanceSource
import com.google.zxing.common.HybridBinarizer
import com.qpay.xcash.R
import com.qpay.xcash.ui.components.QrMode
import com.qpay.xcash.ui.components.QrModeTabSelector
import com.qpay.xcash.ui.components.neonGlow
import com.qpay.xcash.ui.friend.components.FriendTopBar
import com.qpay.xcash.ui.theme.AppTheme
import com.qpay.xcash.ui.theme.BlackGoldColors
import com.qpay.xcash.ui.theme.LocalAppColors
import com.qpay.xcash.ui.theme.NeonColors
import io.github.alexzhirkevich.qrose.options.QrBallShape
import io.github.alexzhirkevich.qrose.options.QrBrush
import io.github.alexzhirkevich.qrose.options.QrColors
import io.github.alexzhirkevich.qrose.options.QrErrorCorrectionLevel
import io.github.alexzhirkevich.qrose.options.QrFrameShape
import io.github.alexzhirkevich.qrose.options.QrPixelShape
import io.github.alexzhirkevich.qrose.options.QrShapes
import io.github.alexzhirkevich.qrose.options.circle
import io.github.alexzhirkevich.qrose.options.roundCorners
import io.github.alexzhirkevich.qrose.options.solid
import io.github.alexzhirkevich.qrose.rememberQrCodePainter
import java.util.concurrent.Executors

@Composable
fun QRCodeScreen(
    onBack: () -> Unit,
    viewModel: QRCodeViewModel = hiltViewModel()
) {
    val colors = LocalAppColors.current
    val uiState by viewModel.uiState.collectAsState()
    Scaffold(
        containerColor = colors.bg.page,
        contentColor = Color.White
    ) { paddingValues ->
        QRCodeContent(
            paddingValues = paddingValues,
            uiState = uiState,
            onBack = onBack
        )
    }
}

@Composable
private fun QRCodeContent(
    paddingValues: PaddingValues,
    uiState: QRCodeUiState = QRCodeUiState(),
    onBack: () -> Unit = {}
) {
    val colors = LocalAppColors.current.qrCode
    val context = LocalContext.current
    val isPreview = LocalInspectionMode.current
    val comingSoon = stringResource(R.string.friend_feature_coming_soon)
    var selectedMode by rememberSaveable { mutableStateOf(QrMode.SCAN_QR) }
    var hasCameraPermission by remember { mutableStateOf(hasCameraPermission(context)) }

    // 從系統設定頁返回時重新檢查相機權限
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                hasCameraPermission = hasCameraPermission(context)
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        FriendTopBar(title = stringResource(R.string.qr_code_title), onBack = onBack)

        Spacer(Modifier.height(24.dp))

        QrModeTabSelector(
            selectedMode = selectedMode,
            onModeChange = { selectedMode = it },
            modifier = Modifier.padding(horizontal = 24.dp),
            borderBrush = colors.tabBorder,
            selectedFillBrush = colors.tabSelectedFill,
            backgroundColor = colors.tabBackground
        )

        Spacer(Modifier.height(32.dp))

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            when (selectedMode) {
                QrMode.SCAN_QR -> {
                    QrFrame(
                        modifier = Modifier
                            .fillMaxWidth(0.7f)
                            .aspectRatio(1f),
                        cornerBrush = colors.frameCorner
                    ) {
                        if (hasCameraPermission && !isPreview) {
                            CameraPreviewView(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(RoundedCornerShape(4.dp)),
                                onQrCodeScanned = {
                                    Toast.makeText(context, comingSoon, Toast.LENGTH_SHORT).show()
                                }
                            )
                        } else {
                            Column(
                                modifier = Modifier.padding(horizontal = 24.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = stringResource(R.string.qr_code_permission_required),
                                    color = colors.permissionText,
                                    fontSize = 14.sp,
                                    textAlign = TextAlign.Center
                                )
                                Spacer(Modifier.height(20.dp))
                                GoToSettingButton(onClick = {
                                    context.startActivity(
                                        Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                                            data =
                                                Uri.fromParts("package", context.packageName, null)
                                        }
                                    )
                                })
                            }
                        }
                    }
                }

                QrMode.MY_QR -> {
                    MyQrCard(
                        modifier = Modifier
                            .fillMaxWidth(0.8f),
                        qrCodeUrl = "http://xcash.io/pay?account=${Uri.encode(uiState.userName)}" +
                                "&to=${Uri.encode(uiState.nickName)}&name=${Uri.encode(uiState.userName)}",
                        phoneNumber = uiState.userPhone
                    )
                }
            }
        }

        if (selectedMode == QrMode.SCAN_QR && !hasCameraPermission) {
            Spacer(Modifier.height(16.dp))
            Text(
                text = stringResource(R.string.qr_code_check_setting_hint),
                color = colors.permissionHintText,
                fontSize = 13.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(Modifier.height(40.dp))

        ShareProfileButton(
            modifier = Modifier.padding(horizontal = 24.dp),
            onClick = {
                val shareText = context.getString(
                    R.string.qr_code_share_text,
                    uiState.nickName.ifEmpty { uiState.userName }
                )
                context.startActivity(
                    Intent.createChooser(
                        Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(Intent.EXTRA_TEXT, shareText)
                        },
                        null
                    )
                )
            }
        )
    }
}

@Composable
private fun QrFrame(
    modifier: Modifier = Modifier,
    cornerBrush: Brush,
    content: @Composable () -> Unit
) {
    val colors = LocalAppColors.current
    Box(
        modifier = modifier
            .then(
                if (colors.effect.enableGlow)
                    Modifier.neonGlow(
                        color = colors.accent.primary,
                        alpha = 0.35f,
                        glowRadius = 20.dp,
                        borderRadius = 4.dp
                    )
                else Modifier
            )
            .background(color = colors.qrCode.frameBackground, shape = RoundedCornerShape(4.dp)),
        contentAlignment = Alignment.Center
    ) {
        content()
        QrFrameCorners(
            modifier = Modifier.fillMaxSize(),
            cornerBrush = cornerBrush
        )
    }
}

@Composable
private fun QrFrameCorners(
    modifier: Modifier = Modifier,
    cornerBrush: Brush,
    cornerLength: Dp = 26.dp,
    strokeWidth: Dp = 2.5.dp
) {
    Canvas(modifier = modifier) {
        val len = cornerLength.toPx()
        val stroke = strokeWidth.toPx()
        val w = size.width
        val h = size.height

        // 左上
        drawLine(cornerBrush, Offset(0f, 0f), Offset(len, 0f), stroke, StrokeCap.Round)
        drawLine(cornerBrush, Offset(0f, 0f), Offset(0f, len), stroke, StrokeCap.Round)
        // 右上
        drawLine(cornerBrush, Offset(w, 0f), Offset(w - len, 0f), stroke, StrokeCap.Round)
        drawLine(cornerBrush, Offset(w, 0f), Offset(w, len), stroke, StrokeCap.Round)
        // 左下
        drawLine(cornerBrush, Offset(0f, h), Offset(len, h), stroke, StrokeCap.Round)
        drawLine(cornerBrush, Offset(0f, h), Offset(0f, h - len), stroke, StrokeCap.Round)
        // 右下
        drawLine(cornerBrush, Offset(w, h), Offset(w - len, h), stroke, StrokeCap.Round)
        drawLine(cornerBrush, Offset(w, h), Offset(w, h - len), stroke, StrokeCap.Round)
    }
}

@Composable
private fun GoToSettingButton(onClick: () -> Unit) {
    val colors = LocalAppColors.current.qrCode
    Box(
        modifier = Modifier
            .background(brush = colors.goToSettingBackground, shape = RoundedCornerShape(25.dp))
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick = onClick
            )
            .padding(horizontal = 28.dp, vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(R.string.qr_code_go_to_setting),
            color = colors.goToSettingText,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun MyQrCard(
    modifier: Modifier = Modifier,
    qrCodeUrl: String,
    phoneNumber: String
) {
    val colors = LocalAppColors.current.qrCode
    val qrPainter = rememberQrCodePainter(
        data = qrCodeUrl,
        shapes = QrShapes(
            ball = QrBallShape.circle(),
            darkPixel = QrPixelShape.roundCorners(),
            frame = QrFrameShape.roundCorners(.25f)
        ),
        colors = QrColors(
            dark = QrBrush.solid(colors.qrCodeColor),
            light = QrBrush.solid(Color.Transparent)
        ),
        errorCorrectionLevel = QrErrorCorrectionLevel.High
    )
    Column(
        modifier = modifier
            .aspectRatio(1f)
            .background(
                brush = colors.myQrCardBackground,
                shape = RoundedCornerShape(28.dp))
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .aspectRatio(1f)
                    .background(
                        color = colors.qrCodeBackground,
                        shape = RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                Image(
                    painter = qrPainter,
                    contentDescription = stringResource(R.string.qr_code_desc),
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
        Spacer(Modifier.height(16.dp))
        Text(
            text = phoneNumber,
            color = colors.myQrPhoneText,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun ShareProfileButton(modifier: Modifier = Modifier, onClick: () -> Unit) {
    val colors = LocalAppColors.current.qrCode
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(color = colors.shareProfileBackground, shape = RoundedCornerShape(16.dp))
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick = onClick
            )
            .padding(vertical = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Outlined.IosShare,
            contentDescription = null,
            tint = colors.shareProfileIconTint,
            modifier = Modifier.size(26.dp)
        )
        Spacer(Modifier.height(10.dp))
        Text(
            text = stringResource(R.string.qr_code_share_profile),
            color = colors.shareProfileText,
            fontSize = 15.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

private fun hasCameraPermission(context: android.content.Context): Boolean {
    return ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) ==
            PackageManager.PERMISSION_GRANTED
}

@Composable
private fun CameraPreviewView(
    modifier: Modifier = Modifier,
    onQrCodeScanned: (String) -> Unit = {}
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val providerRef = remember { arrayOfNulls<ProcessCameraProvider>(1) }
    val analysisExecutor = remember { Executors.newSingleThreadExecutor() }
    val onScannedRef = rememberUpdatedState(onQrCodeScanned)
    val lastScannedRef = remember { arrayOfNulls<String>(1) }

    DisposableEffect(Unit) {
        onDispose {
            providerRef[0]?.unbindAll()
            analysisExecutor.shutdown()
        }
    }

    AndroidView(
        factory = { ctx ->
            PreviewView(ctx).apply {
                implementationMode = PreviewView.ImplementationMode.COMPATIBLE
                val future = ProcessCameraProvider.getInstance(ctx)
                future.addListener({
                    val provider = future.get().also { providerRef[0] = it }
                    val preview = CameraXPreview.Builder().build().also {
                        it.setSurfaceProvider(surfaceProvider)
                    }
                    val imageAnalysis = ImageAnalysis.Builder()
                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                        .build().also { analysis ->
                            val mainExecutor = ContextCompat.getMainExecutor(ctx)
                            analysis.setAnalyzer(analysisExecutor) { imageProxy ->
                                val result = decodeQrFromProxy(imageProxy)
                                imageProxy.close()
                                if (result != null && lastScannedRef[0] != result) {
                                    lastScannedRef[0] = result
                                    mainExecutor.execute { onScannedRef.value(result) }
                                }
                            }
                        }
                    try {
                        provider.unbindAll()
                        provider.bindToLifecycle(
                            lifecycleOwner,
                            CameraSelector.DEFAULT_BACK_CAMERA,
                            preview,
                            imageAnalysis
                        )
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }, ContextCompat.getMainExecutor(ctx))
            }
        },
        modifier = modifier
    )
}

private fun decodeQrFromProxy(imageProxy: ImageProxy): String? {
    val plane = imageProxy.planes[0]
    val rowStride = plane.rowStride
    val buffer = plane.buffer
    val bytes = ByteArray(buffer.remaining())
    buffer.get(bytes)
    val source = PlanarYUVLuminanceSource(
        bytes,
        rowStride, imageProxy.height,
        0, 0,
        imageProxy.width, imageProxy.height,
        false
    )
    val hints = mapOf(DecodeHintType.TRY_HARDER to true)
    val reader = MultiFormatReader().also { it.setHints(hints) }
    return try {
        reader.decode(BinaryBitmap(HybridBinarizer(source)))?.text
    } catch (e: NotFoundException) {
        try {
            reader.decode(BinaryBitmap(HybridBinarizer(source.invert())))?.text
        } catch (e2: NotFoundException) {
            null
        }
    }
}

private val previewUiState = QRCodeUiState(
    userName = "hank_liu",
    nickName = "hank",
    userPhone = "09174798166"
)

@Preview(
    name = "Black Gold - Scan QR (No Permission)",
    showBackground = true,
    backgroundColor = 0xFF050505
)
@Composable
private fun QRCodeScreenPreviewBlackGoldScanNoPermission() {
    AppTheme(colors = BlackGoldColors) {
        QRCodeContent(paddingValues = PaddingValues(), uiState = previewUiState)
    }
}

@Preview(name = "Black Gold - My QR", showBackground = true, backgroundColor = 0xFF050505)
@Composable
private fun QRCodeScreenPreviewBlackGoldMyQr() {
    AppTheme(colors = BlackGoldColors) {
        Column {
            FriendTopBar(title = stringResource(R.string.qr_code_title), onBack = {})
            QrModeTabSelector(
                selectedMode = QrMode.MY_QR,
                onModeChange = {},
                modifier = Modifier.padding(horizontal = 24.dp),
                borderBrush = LocalAppColors.current.qrCode.tabBorder,
                selectedFillBrush = LocalAppColors.current.qrCode.tabSelectedFill,
                backgroundColor = LocalAppColors.current.qrCode.tabBackground
            )
            Spacer(Modifier.height(32.dp))
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                MyQrCard(
                    modifier = Modifier.fillMaxWidth(0.8f),
                    qrCodeUrl = "http://xcash.io/pay?account=hank_liu&to=hank&name=hank_liu",
                    phoneNumber = previewUiState.userPhone
                )
            }
            Spacer(Modifier.height(40.dp))
            ShareProfileButton(modifier = Modifier.padding(horizontal = 24.dp), onClick = {})
        }
    }
}

@Preview(
    name = "Neon - Scan QR (No Permission)",
    showBackground = true,
    backgroundColor = 0xFF030F1B
)
@Composable
private fun QRCodeScreenPreviewNeonScanNoPermission() {
    AppTheme(colors = NeonColors) {
        QRCodeContent(paddingValues = PaddingValues(), uiState = previewUiState)
    }
}

@Preview(name = "Neon - My QR", showBackground = true, backgroundColor = 0xFF030F1B)
@Composable
private fun QRCodeScreenPreviewNeonMyQr() {
    AppTheme(colors = NeonColors) {
        Column {
            FriendTopBar(title = stringResource(R.string.qr_code_title), onBack = {})
            QrModeTabSelector(
                selectedMode = QrMode.MY_QR,
                onModeChange = {},
                modifier = Modifier.padding(horizontal = 24.dp),
                borderBrush = LocalAppColors.current.qrCode.tabBorder,
                selectedFillBrush = LocalAppColors.current.qrCode.tabSelectedFill,
                backgroundColor = LocalAppColors.current.qrCode.tabBackground
            )
            Spacer(Modifier.height(32.dp))
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                MyQrCard(
                    modifier = Modifier.fillMaxWidth(0.8f),
                    qrCodeUrl = "http://xcash.io/pay?account=hank_liu&to=hank&name=hank_liu",
                    phoneNumber = previewUiState.userPhone
                )
            }
            Spacer(Modifier.height(40.dp))
            ShareProfileButton(modifier = Modifier.padding(horizontal = 24.dp), onClick = {})
        }
    }
}
