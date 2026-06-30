package com.example.newproject.ui.login

import android.graphics.SurfaceTexture
import android.net.Uri
import android.view.Surface
import android.view.TextureView
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.ui.layout.ContentScale
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.FragmentActivity
import com.example.newproject.R
import com.example.newproject.ui.UiEvent
import com.example.newproject.ui.login.components.DrawerMenuContent
import com.example.newproject.ui.login.components.LanguageSelector
import com.example.newproject.ui.login.dialog.AccountStatusDialog
import com.example.newproject.ui.login.dialog.BiometricEnrollDialog
import com.example.newproject.ui.login.dialog.LoginBottomSheet
import com.example.newproject.ui.login.dialog.VerifyMobileDialog
import com.example.newproject.ui.components.LoadingDialog
import com.example.newproject.ui.components.neonGlow
import com.example.newproject.ui.theme.AppTheme
import com.example.newproject.ui.theme.BlackGoldColors
import com.example.newproject.ui.theme.BlackGoldAssets
import com.example.newproject.ui.theme.LocalAppAssets
import com.example.newproject.ui.theme.LocalAppColors
import com.example.newproject.ui.theme.NeonAssets
import com.example.newproject.ui.theme.NeonColors
import com.example.newproject.utils.BiometricHelper
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    onNavigateToHome: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val hasSavedCredentials by viewModel.hasSavedCredentials.collectAsState()
    val context = LocalContext.current

    var showVerifyDialog by remember { mutableStateOf(false) }
    var verifyPhone by remember { mutableStateOf("") }
    var showBiometricEnrollDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.navigationEvent.collect { event ->
            when (event) {
                is LoginNavigationEvent.Success -> onNavigateToHome()
                is LoginNavigationEvent.PromptBiometricEnroll -> showBiometricEnrollDialog = true
                is LoginNavigationEvent.NeedsVerification -> {
                    verifyPhone = event.phone
                    showVerifyDialog = true
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.eventFlow.collect { event ->
            when (event) {
                is UiEvent.ShowToast -> Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                is UiEvent.ShowDialog -> Toast.makeText(context, event.message, Toast.LENGTH_LONG).show()
            }
        }
    }

    LoginScreenContent(
        uiState = uiState,
        hasSavedCredentials = hasSavedCredentials,
        showVerifyDialog = showVerifyDialog,
        verifyPhone = verifyPhone,
        showBiometricEnrollDialog = showBiometricEnrollDialog,
        onLoginClick = { phone, pwd -> viewModel.login(phone, pwd) },
        onResetState = { viewModel.resetState() },
        onVerifyOtp = { otp -> viewModel.verifyOtp(verifyPhone, otp) },
        onDismissVerifyDialog = { showVerifyDialog = false; viewModel.resetState() },
        onDismissBiometricEnrollDialog = { showBiometricEnrollDialog = false },
        onBiometricEnrollSuccess = { viewModel.enrollBiometric() },
        onBiometricEnrollSkip = { viewModel.skipBiometricEnroll() },
        onShowBiometricPromptForLogin = {
            BiometricHelper.showPrompt(
                activity = context as FragmentActivity,
                title = context.getString(R.string.biometric_prompt_login_title),
                subtitle = context.getString(R.string.biometric_prompt_login_subtitle),
                negativeText = context.getString(R.string.biometric_prompt_negative),
                onSuccess = { viewModel.loginWithStoredCredentials() }
            )
        },
        onShowBiometricPromptForEnroll = {
            BiometricHelper.showPrompt(
                activity = context as FragmentActivity,
                title = context.getString(R.string.biometric_prompt_enroll_title),
                subtitle = context.getString(R.string.biometric_prompt_enroll_subtitle),
                negativeText = context.getString(R.string.biometric_prompt_negative),
                onSuccess = { viewModel.enrollBiometric() },
                onError = { viewModel.skipBiometricEnroll() }
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreenContent(
    uiState: LoginUiState,
    hasSavedCredentials: Boolean = false,
    showVerifyDialog: Boolean = false,
    verifyPhone: String = "",
    showBiometricEnrollDialog: Boolean = false,
    onLoginClick: (String, String) -> Unit,
    onResetState: () -> Unit = {},
    onVerifyOtp: (String) -> Unit = {},
    onDismissVerifyDialog: () -> Unit = {},
    onDismissBiometricEnrollDialog: () -> Unit = {},
    onBiometricEnrollSuccess: () -> Unit = {},
    onBiometricEnrollSkip: () -> Unit = {},
    onShowBiometricPromptForLogin: () -> Unit = {},
    onShowBiometricPromptForEnroll: () -> Unit = {}
) {
    val colors = LocalAppColors.current
    val assets = LocalAppAssets.current
    val context = LocalContext.current
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var showLoginSheet by remember { mutableStateOf(false) }
    var showAccountStatusDialog by remember { mutableStateOf(false) }

    var selectedLanguage by remember { mutableStateOf("EN") }
    val languages = listOf("EN", "CN", "JP", "AU")

    val showBiometricButton = hasSavedCredentials && BiometricHelper.isAvailable(context)

    LaunchedEffect(showVerifyDialog, showBiometricEnrollDialog) {
        if (showVerifyDialog || showBiometricEnrollDialog) {
            showLoginSheet = false
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        scrimColor = Color.Black.copy(alpha = 0.75f),
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = Color.Transparent, // 讓抽屜本身透明，我們自己畫背景
                modifier = Modifier.fillMaxWidth(0.9f)
            ) {
                DrawerMenuContent(
                    onClose = { scope.launch { drawerState.close() } },
                    onAccountStatusClick = {
                        scope.launch {
                            drawerState.close()
                            showAccountStatusDialog = true
                        }
                    }
                )
            }
        }
    ) {
        Scaffold(
            containerColor = colors.bg.page,
            contentColor = Color.White
        ) { paddingValues ->
            LoadingDialog(isShowing = uiState.isLoading)

            Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
                assets.loginBackground?.let { resId ->
                    Image(
                        painter = painterResource(resId),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp, vertical = 24.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // ================== Top Bar ==================
                Box(modifier = Modifier.fillMaxWidth()) {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = stringResource(id = R.string.menu_desc),
                        tint = colors.accent.secondary,
                        modifier = Modifier
                            .size(28.dp)
                            .align(Alignment.CenterStart)
                            .clickable { scope.launch { drawerState.open() } }
                            .then(if (colors.effect.enableGlow)
                                Modifier.neonGlow(color = colors.accent.secondary, alpha = 0.8f, glowRadius = 15.dp, borderRadius = 15.dp)
                            else Modifier)
                    )
                    
                    Icon(
                        painter = painterResource(id = assets.xcashWordmark),
                        contentDescription = stringResource(id = R.string.xcash_logo_desc),
                        modifier = Modifier.height(30.dp).align(Alignment.Center),
                        tint = Color.Unspecified
                    )
                    
                    LanguageSelector(
                        selectedLanguage = selectedLanguage,
                        languages = languages,
                        onLanguageSelected = { selectedLanguage = it },
                        modifier = Modifier.align(Alignment.CenterEnd)
                    )
                }

                // ================== Center Logo ==================
                Box(
                    modifier = Modifier.weight(1f).fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    val isPreview = androidx.compose.ui.platform.LocalInspectionMode.current
                    if (isPreview || assets.centerLogoImage != null) {
                        Image(
                            painter = painterResource(assets.centerLogoImage ?: R.drawable.ic_xcash_logo),
                            contentDescription = stringResource(R.string.center_neon_logo_desc),
                            modifier = Modifier.fillMaxWidth(0.9f)
                        )
                    } else {
                        val exoPlayer = remember(context) {
                            ExoPlayer.Builder(context).build().apply {
                                val uri = Uri.parse("android.resource://${context.packageName}/${assets.centerLogoVideo}")
                                setMediaItem(MediaItem.fromUri(uri))
                                repeatMode = ExoPlayer.REPEAT_MODE_OFF
                                volume = 0f
                                prepare()
                                playWhenReady = true
                            }
                        }
                        var videoAspectRatio by remember { mutableStateOf(1f) }
                        DisposableEffect(exoPlayer) {
                            val listener = object : androidx.media3.common.Player.Listener {
                                override fun onVideoSizeChanged(videoSize: androidx.media3.common.VideoSize) {
                                    if (videoSize.height > 0) {
                                        videoAspectRatio = videoSize.width.toFloat() / videoSize.height.toFloat()
                                    }
                                }
                            }
                            exoPlayer.addListener(listener)
                            onDispose {
                                exoPlayer.removeListener(listener)
                                exoPlayer.release()
                            }
                        }
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(0.9f)
                                .aspectRatio(videoAspectRatio)
                        ) {
                            AndroidView(
                                factory = { ctx ->
                                    TextureView(ctx).apply {
                                        surfaceTextureListener = object : TextureView.SurfaceTextureListener {
                                            private var surface: Surface? = null
                                            override fun onSurfaceTextureAvailable(st: SurfaceTexture, w: Int, h: Int) {
                                                surface = Surface(st).also { exoPlayer.setVideoSurface(it) }
                                            }
                                            override fun onSurfaceTextureSizeChanged(st: SurfaceTexture, w: Int, h: Int) {}
                                            override fun onSurfaceTextureDestroyed(st: SurfaceTexture): Boolean {
                                                exoPlayer.setVideoSurface(null)
                                                surface?.release()
                                                surface = null
                                                return true
                                            }
                                            override fun onSurfaceTextureUpdated(st: SurfaceTexture) {}
                                        }
                                    }
                                },
                                modifier = Modifier.fillMaxSize()
                            )
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
//                                    .background(
//                                        Brush.radialGradient(
//                                            colorStops = arrayOf(
//                                                0.5f to Color.Transparent,
//                                                1.0f to welcomeBackground
//                                            )
//                                        )
//                                    )
                            )
                        }
                    }
                }

                // ================== Bottom Section ==================
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
//                    if (state is LoginState.Error) {
//                        Text(text = state.message, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(bottom = 8.dp))
//                    }

                    Button(
                        onClick = { showLoginSheet = true },
                        enabled = !uiState.isLoading,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .then(if (colors.effect.enableGlow)
                                Modifier.neonGlow(color = colors.button.loginBorder.takeIf { it != Color.Transparent }
                                    ?: colors.button.loginBackground, alpha = 0.9f, glowRadius = 20.dp)
                            else Modifier),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colors.button.loginBackground,
                            contentColor = colors.button.loginText
                        ),
                        border = BorderStroke(width = 1.5.dp, color = colors.button.loginBorder),
                        shape = RoundedCornerShape(25.dp)
                    ) {
                        Text(if (uiState.isLoading) stringResource(id = R.string.logging_in) else stringResource(id = R.string.login_btn), fontSize = 16.sp)
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {},
                        enabled = !uiState.isLoading,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .then(if (colors.effect.enableGlow) Modifier.neonGlow(color = colors.button.telegramBorder, alpha = 0.7f, glowRadius = 20.dp) else Modifier),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colors.button.telegramBackground,
                            contentColor = colors.button.telegramText
                        ),
                        border = BorderStroke(width = 1.dp, color = colors.button.telegramBorder),
                        shape = RoundedCornerShape(25.dp)
                    ){
                        Text(if (uiState.isLoading) stringResource(id = R.string.logging_in) else stringResource(id = R.string.sign_up_with_telegram), fontSize = 16.sp)
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = stringResource(id = R.string.login_or), color = Color.LightGray, fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(3.dp))
                    Text(
                        text = buildAnnotatedString {
                            append(stringResource(id = R.string.dont_have_account))
                            withStyle(SpanStyle(color = colors.accent.secondary)) { append(stringResource(id = R.string.sign_up)) }
                        },
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
//                            imageVector = Icons.Default.Info,
                            painter = painterResource(R.mipmap.ic_shield),
                            contentDescription = stringResource(id = R.string.shield_desc),
                            modifier = Modifier.size(14.dp),
                            tint = Color.Unspecified
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(stringResource(id = R.string.regulated_by_bsp), color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
            } // Box
        }
    }

    if (showAccountStatusDialog) {
        AccountStatusDialog(onDismiss = { showAccountStatusDialog = false })
    }

    if (showLoginSheet) {
        LoginBottomSheet(
            onDismissRequest = {
                showLoginSheet = false
                onResetState()
            },
            onLoginSubmit = { mobileNumber, password ->
                onLoginClick(mobileNumber, password)
            },
            errorMessage = uiState.loginError,
            showBiometricButton = showBiometricButton,
            onBiometricLogin = { onShowBiometricPromptForLogin() }
        )
    }

    if (showBiometricEnrollDialog) {
        BiometricEnrollDialog(
            onEnroll = { onShowBiometricPromptForEnroll() },
            onSkip = { onBiometricEnrollSkip(); onDismissBiometricEnrollDialog() }
        )
    }

    if (showVerifyDialog) {
        VerifyMobileDialog(
            initialPhone = verifyPhone,
            onDismiss = onDismissVerifyDialog,
            onSubmit = { otp -> onVerifyOtp(otp) }
        )
    }
}

@Preview(name = "Neon", showBackground = true, backgroundColor = 0xFF030F1B)
@Composable
private fun LoginPreviewNeon() {
    AppTheme(colors = NeonColors, assets = NeonAssets) {
        LoginScreenContent(uiState = LoginUiState(), onLoginClick = { _, _ -> })
    }
}

@Preview(name = "Black Gold", showBackground = true, backgroundColor = 0xFF050505)
@Composable
private fun LoginPreviewBlackGold() {
    AppTheme(colors = BlackGoldColors, assets = BlackGoldAssets) {
        LoginScreenContent(uiState = LoginUiState(), onLoginClick = { _, _ -> })
    }
}

