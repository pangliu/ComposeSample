package com.qpay.xcash.ui.avatar

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview as CameraXPreview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
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
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import coil.compose.AsyncImage
import com.qpay.xcash.R
import com.qpay.xcash.ui.theme.AppTheme
import com.qpay.xcash.ui.theme.BlackGoldColors
import com.qpay.xcash.ui.theme.LocalAppColors
import com.qpay.xcash.ui.theme.NeonColors
import java.io.File
import java.util.concurrent.Executors

@Composable
fun CameraScreen(
    onCancel: () -> Unit,
    onPhotoCaptured: (Uri) -> Unit
) {
    val colors = LocalAppColors.current
    val context = LocalContext.current
    val isPreview = LocalInspectionMode.current
    val hasCameraPermission = remember {
        ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) ==
            PackageManager.PERMISSION_GRANTED
    }
    var lastCapturedUri by remember { mutableStateOf<Uri?>(null) }
    var imageCapture by remember { mutableStateOf<ImageCapture?>(null) }
    val mainExecutor = remember { ContextCompat.getMainExecutor(context) }

    fun takePhoto() {
        val capture = imageCapture ?: return
        val file = File(context.cacheDir, "camera_capture_${System.currentTimeMillis()}.jpg")
        val outputOptions = ImageCapture.OutputFileOptions.Builder(file).build()
        capture.takePicture(
            outputOptions,
            mainExecutor,
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    lastCapturedUri = Uri.fromFile(file)
                }

                override fun onError(exception: ImageCaptureException) {
                    exception.printStackTrace()
                }
            }
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth(0.78f)
                .aspectRatio(1f)
                .clip(CircleShape)
        ) {
            if (hasCameraPermission && !isPreview) {
                CameraLensPreview(
                    modifier = Modifier.fillMaxSize(),
                    onImageCaptureReady = { imageCapture = it }
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(colors.camera.thumbnailPlaceholderBackground),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.camera_permission_required),
                        color = colors.camera.permissionText,
                        fontSize = 13.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 24.dp)
                    )
                }
            }
        }

        Icon(
            imageVector = Icons.Default.Close,
            contentDescription = stringResource(R.string.camera_cancel_desc),
            tint = colors.camera.cancelIconTint,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(top = 32.dp, start = 20.dp)
                .size(28.dp)
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() },
                    onClick = onCancel
                )
        )

        CameraControlsBar(
            modifier = Modifier.align(Alignment.BottomCenter),
            lastCapturedUri = lastCapturedUri,
            onCapture = { takePhoto() },
            onThumbnailClick = { lastCapturedUri?.let(onPhotoCaptured) }
        )
    }
}

@Composable
private fun CameraControlsBar(
    modifier: Modifier = Modifier,
    lastCapturedUri: Uri? = null,
    onCapture: () -> Unit = {},
    onThumbnailClick: () -> Unit = {}
) {
    val colors = LocalAppColors.current
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 40.dp, vertical = 32.dp)
    ) {
        Icon(
            imageVector = Icons.Default.AutoAwesome,
            contentDescription = null,
            tint = colors.camera.bottomIconTint,
            modifier = Modifier
                .align(Alignment.CenterStart)
                .size(44.dp)
                .border(1.5.dp, colors.camera.bottomIconBorder, RoundedCornerShape(12.dp))
                .padding(10.dp)
        )

        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .size(64.dp)
                .border(
                    width = 1.5.dp,
                    color = colors.camera.shutterBorder,
                    shape = CircleShape)
                .padding(5.dp)
                .background(
                    color = colors.camera.shutterFill,
                    shape = CircleShape)
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() },
                    onClick = onCapture
                )
        )

        Box(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .size(44.dp)
                .clip(RoundedCornerShape(10.dp))
//                .border(1.5.dp, colors.camera.thumbnailBorder, RoundedCornerShape(10.dp))
                .background(colors.camera.thumbnailPlaceholderBackground)
                .then(
                    if (lastCapturedUri != null) {
                        Modifier.clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() },
                            onClick = onThumbnailClick
                        )
                    } else Modifier
                )
        ) {
            lastCapturedUri?.let { uri ->
                AsyncImage(
                    model = uri,
                    contentDescription = stringResource(R.string.camera_thumbnail_desc),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

@Composable
private fun CameraLensPreview(
    modifier: Modifier = Modifier,
    onImageCaptureReady: (ImageCapture) -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val providerRef = remember { arrayOfNulls<ProcessCameraProvider>(1) }
    val cameraExecutor = remember { Executors.newSingleThreadExecutor() }
    val onReadyRef = rememberUpdatedState(onImageCaptureReady)

    DisposableEffect(Unit) {
        onDispose {
            providerRef[0]?.unbindAll()
            cameraExecutor.shutdown()
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
                    val imageCapture = ImageCapture.Builder().build()
                    try {
                        provider.unbindAll()
                        provider.bindToLifecycle(
                            lifecycleOwner,
                            CameraSelector.DEFAULT_BACK_CAMERA,
                            preview,
                            imageCapture
                        )
                        onReadyRef.value(imageCapture)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }, ContextCompat.getMainExecutor(ctx))
            }
        },
        modifier = modifier
    )
}

@Preview(name = "Controls - Neon", showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun CameraControlsBarPreviewNeon() {
    AppTheme(colors = NeonColors) {
        CameraControlsBar()
    }
}

@Preview(name = "Controls - Black Gold", showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun CameraControlsBarPreviewBlackGold() {
    AppTheme(colors = BlackGoldColors) {
        CameraControlsBar()
    }
}

@Preview(name = "Neon", showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun CameraScreenPreviewNeon() {
    AppTheme(colors = NeonColors) {
        CameraScreen(onCancel = {}, onPhotoCaptured = {})
    }
}

@Preview(name = "Black Gold", showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun CameraScreenPreviewBlackGold() {
    AppTheme(colors = BlackGoldColors) {
        CameraScreen(onCancel = {}, onPhotoCaptured = {})
    }
}
