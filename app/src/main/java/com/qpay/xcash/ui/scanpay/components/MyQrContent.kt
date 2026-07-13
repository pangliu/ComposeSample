package com.qpay.xcash.ui.scanpay.components

import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview as CameraXPreview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import com.google.zxing.BinaryBitmap
import com.google.zxing.DecodeHintType
import com.google.zxing.MultiFormatReader
import com.google.zxing.NotFoundException
import com.google.zxing.PlanarYUVLuminanceSource
import com.google.zxing.common.HybridBinarizer
import java.util.concurrent.Executors
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.qpay.xcash.R
import com.qpay.xcash.ui.components.QrMode
import com.qpay.xcash.ui.components.neonGlow
import com.qpay.xcash.ui.profile.transaction.formatAmount
import com.qpay.xcash.ui.theme.AppTheme
import com.qpay.xcash.ui.theme.BlackGoldAssets
import com.qpay.xcash.ui.theme.BlackGoldColors
import com.qpay.xcash.ui.theme.LocalAppAssets
import com.qpay.xcash.ui.theme.LocalAppColors
import com.qpay.xcash.ui.theme.NeonColors
import com.qpay.xcash.ui.theme.lemonYellow
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

@Composable
internal fun MyQrContent(
    qrCodeUrl: String,
    selectedMode: QrMode,
    userName: String = "",
    nickName: String = "",
    balance: Double = 0.0,
    onQrCodeScanned: (String) -> Unit = {}
) {
    val colors = LocalAppColors.current
    val assets = LocalAppAssets.current
    val context = LocalContext.current
    val hasCameraPermission = remember {
        ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) ==
                PackageManager.PERMISSION_GRANTED
    }
    var isBalanceVisible by remember { mutableStateOf(false) }
    val qrPainter = rememberQrCodePainter(
        data = qrCodeUrl,
        shapes = QrShapes(
            ball = QrBallShape.circle(),
            darkPixel = QrPixelShape.roundCorners(),
            frame = QrFrameShape.roundCorners(.25f)
        ),
        colors = QrColors(
            dark = QrBrush.solid(colors.scanPay.qrCodeColor),
            light = QrBrush.solid(Color.Transparent)
        ),
        errorCorrectionLevel = QrErrorCorrectionLevel.High
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // ── QR Code Section ──────────────────────────────────────────────────
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val leftDecor = assets.qrSectionLeftDecor
            if (leftDecor != null) {
                Image(
                    modifier = Modifier
                        .weight(0.2f)
                        .fillMaxHeight(),
                    painter = painterResource(leftDecor),
                    contentScale = ContentScale.Crop,
                    alignment = Alignment.CenterEnd,
                    contentDescription = stringResource(R.string.scan_pay_my_qr_left_qr_code_desc),
                )
            } else {
                Spacer(modifier = Modifier.weight(0.2f))
            }

            Box(
                modifier = Modifier
                    .testTag("qrcode_camera_box")
                    .align(Alignment.CenterVertically)
                    .weight(0.65f)
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(10.dp))
            ) {
                when (selectedMode) {
                    QrMode.MY_QR -> {
                        // 底色墊最底層（原本掛在外框圖上，不透明色會蓋住下層的內層底圖）
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    color = colors.scanPay.qrFrameBackground,
                                    shape = RoundedCornerShape(10.dp)
                                )
                        )
                        // 墊在外框圖下層的內層底圖（Black Gold 專用）
                        assets.myQrPanelBg?.let { panelBg ->
                            Image(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .align(Alignment.Center),
                                contentScale = ContentScale.FillBounds,
                                painter = painterResource(panelBg),
                                contentDescription = null
                            )
                        }
                        Image(
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight()
                                .align(Alignment.Center),
                            contentScale = ContentScale.FillBounds,
                            painter = painterResource(assets.qrCodeBorder),
                            contentDescription = stringResource(R.string.scan_pay_my_qr_qr_code_desc)
                        )
                        Text(
                            modifier = Modifier
                                .align(Alignment.TopCenter)
                                .padding(top = 5.dp),
                            text = "@$nickName",
                            color = colors.scanPay.qrNickNameText,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .padding(bottom = 5.dp),
                            text = userName,
                            color = colors.scanPay.qrUserNameText,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Box(
                            modifier = Modifier
                                .testTag("qrcode_box")
                                .fillMaxWidth(0.8f)
                                .aspectRatio(1f)
                                .background(
                                    color = Color.Transparent,
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .align(Alignment.Center)
                                .padding(8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = qrPainter,
                                contentDescription = stringResource(R.string.scan_pay_my_qr_qr_code_desc),
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }

                    QrMode.SCAN_QR -> {

                        if (hasCameraPermission) {
                            CameraPreviewView(
                                modifier = Modifier.fillMaxSize(),
                                onQrCodeScanned = onQrCodeScanned
                            )
                        } else {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(Color.Black.copy(alpha = 0.3f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = stringResource(R.string.scan_pay_camera_permission_required),
                                    color = colors.text.body,
                                    fontSize = 12.sp,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(horizontal = 12.dp)
                                )
                            }
                        }
                        Image(
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight()
//                                .background(color = colors.scanPay.qrFrameBackground, shape = RoundedCornerShape(10.dp))
                                .align(Alignment.Center),
                            contentScale = ContentScale.FillBounds,
                            painter = painterResource(assets.qrCodeBorder),
                            contentDescription = stringResource(R.string.scan_pay_my_qr_qr_code_desc)
                        )
                    }
                }
            }

            val rightDecor = assets.qrSectionRightDecor
            if (rightDecor != null) {
                Image(
                    modifier = Modifier
                        .weight(0.2f)
                        .fillMaxHeight(),
                    painter = painterResource(rightDecor),
                    contentScale = ContentScale.Crop,
                    alignment = Alignment.CenterStart,
                    contentDescription = stringResource(R.string.scan_pay_my_qr_right_qr_code_desc)
                )
            } else {
                Spacer(modifier = Modifier.weight(0.2f))
            }
        }

        Spacer(Modifier.height(20.dp))

        // ── Balance Section ──────────────────────────────────────────────────
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 0.dp)
        ) {
            Image(
                painter = painterResource(assets.myQrLeftDecorIcon),
                contentDescription = null,
                modifier = Modifier
                    .offset(0.dp,20.dp)
                    .size(80.dp)
                    .then(
                        if (colors.effect.enableGlow)
                            Modifier.neonGlow(
                                color = colors.accent.secondary,
                                alpha = 0.25f,
                                glowRadius = 30.dp
                            )
                        else Modifier
                    )
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .align(Alignment.CenterVertically)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = stringResource(R.string.scan_pay_my_qr_balance),
                        color = colors.text.body,
                        fontSize = 14.sp,
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isBalanceVisible) "PHP ${formatAmount(balance)}" else "••••",
                        color = colors.scanPay.balanceAmountText,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        imageVector = if (isBalanceVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = stringResource(R.string.balance_toggle_desc),
                        tint = colors.scanPay.balanceToggleIcon,
                        modifier = Modifier
                            .size(18.dp)
                            .clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }
                            ) { isBalanceVisible = !isBalanceVisible }
                    )
                }
                Text(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    text = stringResource(R.string.scan_pay_my_qr_x_points),
                    color = colors.text.body,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Image(
                painter = painterResource(assets.myQrRightDecorIcon),
                contentDescription = null,
                modifier = Modifier
                    .offset(0.dp,20.dp)
                    .size(60.dp)
                    .then(
                        if (colors.effect.enableGlow)
                            Modifier.neonGlow(color = lemonYellow, alpha = 0.3f, glowRadius = 30.dp)
                        else Modifier
                    )
                    .align(Alignment.CenterVertically)
            )
        }

        Spacer(Modifier.height(40.dp))

        // ── Action Buttons ───────────────────────────────────────────────────
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            MyQrActionButton(
                iconRes = R.mipmap.ic_gift,
                label = stringResource(R.string.scan_pay_my_qr_generate_ang_pao_btn),
                modifier = Modifier.weight(1f)
            )
            MyQrActionButton(
                iconRes = R.mipmap.ic_money,
                label = stringResource(R.string.scan_pay_my_qr_payment_history_btn),
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(Modifier.height(10.dp))

        // ── Daily Quests Card ────────────────────────────────────────────────
        val questCardBg = assets.myQrQuestCardBg
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        ) {
            if (questCardBg != null) {
                // Black Gold：無邊框，背景圖填滿；matchParentSize 不參與量測，高度由內容決定
                Image(
                    painter = painterResource(questCardBg),
                    contentDescription = null,
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier.matchParentSize()
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .then(
                        if (questCardBg == null)
                            Modifier
                                .then(
                                    if (colors.effect.enableGlow)
                                        Modifier.neonGlow(
                                            color = colors.accent.primary,
                                            alpha = 0.5f,
                                            glowRadius = 8.dp,
                                            borderRadius = 12.dp
                                        )
                                    else Modifier
                                )
                                .border(
                                    width = 1.5.dp,
                                    color = colors.accent.primary.copy(alpha = 0.7f),
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .background(
                                    color = colors.scanPay.questCardBackground,
                                    shape = RoundedCornerShape(12.dp)
                                )
                        else Modifier
                    )
                    .padding(horizontal = 12.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = stringResource(R.string.scan_pay_my_qr_daily_quest_title),
                        color = colors.scanPay.questTitleText,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.width(5.dp))
                    Text(
                        text = stringResource(R.string.scan_pay_my_qr_daily_quest_progress),
                        color = colors.scanPay.questProgressText,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(Modifier.height(4.dp))
                LinearProgressIndicator(
                    progress = 0f,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp)
                        .clip(RoundedCornerShape(50)),
                    color = colors.scanPay.questProgressIndicator,
                    trackColor = colors.scanPay.questProgressTrack
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = stringResource(R.string.scan_pay_my_qr_daily_quest_progress),
                    color = colors.scanPay.questProgressText,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }
                Spacer(Modifier.width(10.dp))
                Image(
                    painter = painterResource(assets.myQrQuestCardIcon),
                    contentDescription = null,
                    modifier = Modifier.size(60.dp)
                )
            }
        }

        Spacer(Modifier.height(24.dp))
    }
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
        // 反色 QR code（亮色模組 + 深色背景，如 neonCyan on dark）
        try {
            reader.decode(BinaryBitmap(HybridBinarizer(source.invert())))?.text
        } catch (e2: NotFoundException) {
            null
        }
    }
}

@Composable
internal fun MyQrActionButton(
    iconRes: Int,
    label: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    val colors = LocalAppColors.current
    val actionButtonBg = LocalAppAssets.current.myQrActionButtonBg
    Box(modifier = modifier) {
        if (actionButtonBg != null) {
            // Black Gold：無邊框，背景圖填滿；matchParentSize 不參與量測，尺寸由內容決定
            Image(
                painter = painterResource(actionButtonBg),
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .matchParentSize()
                    .clip(RoundedCornerShape(12.dp))
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .then(
                    if (actionButtonBg == null)
                        Modifier
                            .border(
                                width = 1.5.dp,
                                color = colors.accent.secondary.copy(alpha = 0.6f),
                                shape = RoundedCornerShape(12.dp)
                            )
                            .then(
                                if (colors.effect.enableGlow)
                                    Modifier.neonGlow(
                                        color = colors.accent.secondary,
                                        alpha = 0.3f,
                                        glowRadius = 8.dp,
                                        borderRadius = 12.dp
                                    )
                                else Modifier
                            )
                            .background(
                                color = colors.bg.page.copy(alpha = 0.7f),
                                shape = RoundedCornerShape(12.dp)
                            )
                    else Modifier
                )
                .padding(vertical = 8.dp, horizontal = 8.dp)
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) { onClick() }
        ) {
        Box(
            modifier = Modifier
                .border(
                    color = colors.scanPay.actionButtonIconBorder,
                    width = 1.5.dp,
                    shape = RoundedCornerShape(50.dp)
                )
                .then(
                    if (actionButtonBg == null)
                        Modifier
                            .then(
                                if (colors.effect.enableGlow)
                                    Modifier.neonGlow(
                                        color = colors.accent.primary,
                                        alpha = 0.7f,
                                        glowRadius = 50.dp,
                                        borderRadius = 50.dp
                                    )
                                else Modifier
                            )
                            .background(
                                color = colors.bg.page.copy(alpha = 0.3f),
                                shape = RoundedCornerShape(50.dp)
                            )
                    else Modifier
                )
                .padding(10.dp)
        ) {
            Icon(
                painter = painterResource(iconRes),
                contentDescription = null,
                tint = colors.scanPay.actionButtonIconTint,
                modifier = Modifier.size(24.dp)
            )
        }
            Spacer(Modifier.width(5.dp))
            Text(
                text = label,
                color = colors.scanPay.actionButtonText,
                fontSize = 11.sp,
                textAlign = TextAlign.Center,
                lineHeight = 15.sp
            )
        }
    }
}

@Preview(name = "Neon", showBackground = true, backgroundColor = 0xFF0B1327)
@Composable
private fun MyQrContentPreviewNeon() {
    AppTheme(colors = NeonColors) {
        MyQrContent(qrCodeUrl = "https://example.com", selectedMode = QrMode.MY_QR)
    }
}

@Preview(name = "Black Gold", showBackground = true, backgroundColor = 0xFF050505)
@Composable
private fun MyQrContentPreviewBlackGold() {
    AppTheme(colors = BlackGoldColors, assets = BlackGoldAssets) {
        MyQrContent(qrCodeUrl = "https://example.com", selectedMode = QrMode.MY_QR)
    }
}
