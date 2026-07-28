package com.qpay.xcash.ui.avatar

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material.icons.filled.Cameraswitch
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
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
    var lensFacing by remember { mutableIntStateOf(CameraSelector.LENS_FACING_BACK) }
    var imageCapture by remember { mutableStateOf<ImageCapture?>(null) }
    val mainExecutor = remember { ContextCompat.getMainExecutor(context) }
    val onPhotoCapturedState = rememberUpdatedState(onPhotoCaptured)

    val albumLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri -> uri?.let { onPhotoCapturedState.value(it) } }

    fun takePhoto() {
        val capture = imageCapture ?: return
        val file = File(context.cacheDir, "camera_capture_${System.currentTimeMillis()}.jpg")
        val outputOptions = ImageCapture.OutputFileOptions.Builder(file).build()
        capture.takePicture(
            outputOptions,
            mainExecutor,
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    onPhotoCapturedState.value(Uri.fromFile(file))
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
                    lensFacing = lensFacing,
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
            onFlipCamera = {
                lensFacing = if (lensFacing == CameraSelector.LENS_FACING_BACK) {
                    CameraSelector.LENS_FACING_FRONT
                } else {
                    CameraSelector.LENS_FACING_BACK
                }
            },
            onCapture = { takePhoto() },
            onThumbnailClick = {
                albumLauncher.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                )
            }
        )
    }
}

@Composable
private fun CameraControlsBar(
    modifier: Modifier = Modifier,
    onFlipCamera: () -> Unit = {},
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
            painter = painterResource(R.drawable.ic_turn_camera),
            contentDescription = stringResource(R.string.camera_flip_desc),
            tint = Color.Unspecified,
            modifier = Modifier
                .align(Alignment.CenterStart)
                .size(42.dp)
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() },
                    onClick = onFlipCamera
                )
        )

        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .size(64.dp)
                .border(
                    width = 3.dp,
                    color = colors.camera.shutterBorder,
                    shape = CircleShape
                )
                .padding(7.dp)
                .background(
                    color = colors.camera.shutterFill,
                    shape = CircleShape
                )
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() },
                    onClick = onCapture
                )
        )

        Icon(
            painter = painterResource(R.drawable.ic_photo),
            contentDescription = stringResource(R.string.camera_thumbnail_desc),
            tint = Color.Unspecified,
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .size(42.dp)
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() },
                    onClick = onThumbnailClick
                )
        )
    }
}

@Composable
private fun CameraLensPreview(
    modifier: Modifier = Modifier,
    lensFacing: Int,
    onImageCaptureReady: (ImageCapture) -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val providerRef = remember { arrayOfNulls<ProcessCameraProvider>(1) }
    val cameraExecutor = remember { Executors.newSingleThreadExecutor() }
    val onReadyRef = rememberUpdatedState(onImageCaptureReady)
    val lensFacingRef = rememberUpdatedState(lensFacing)
    val previewView = remember {
        PreviewView(context).apply {
            implementationMode = PreviewView.ImplementationMode.COMPATIBLE
        }
    }

    fun bindCamera(provider: ProcessCameraProvider) {
        val preview = CameraXPreview.Builder().build().also {
            it.setSurfaceProvider(previewView.surfaceProvider)
        }
        val imageCapture = ImageCapture.Builder().build()
        val selector = CameraSelector.Builder().requireLensFacing(lensFacingRef.value).build()
        try {
            provider.unbindAll()
            provider.bindToLifecycle(lifecycleOwner, selector, preview, imageCapture)
            onReadyRef.value(imageCapture)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    DisposableEffect(Unit) {
        val future = ProcessCameraProvider.getInstance(context)
        future.addListener({
            val provider = future.get().also { providerRef[0] = it }
            bindCamera(provider)
        }, ContextCompat.getMainExecutor(context))

        onDispose {
            providerRef[0]?.unbindAll()
            cameraExecutor.shutdown()
        }
    }

    LaunchedEffect(lensFacing) {
        providerRef[0]?.let { bindCamera(it) }
    }

    AndroidView(factory = { previewView }, modifier = modifier)
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
