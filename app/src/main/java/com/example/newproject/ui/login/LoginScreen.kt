package com.example.newproject.ui.login

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.GifDecoder
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
import com.example.newproject.R
import com.example.newproject.ui.login.dialog.LoginBottomSheet
import com.example.newproject.ui.login.dialog.VerifyMobileDialog
import com.example.newproject.ui.components.LoadingDialog
import com.example.newproject.ui.components.neonGlow
import com.example.newproject.ui.theme.NeonGreen
import com.example.newproject.ui.theme.NeonGreenLight
import com.example.newproject.ui.theme.NeonPurple
import com.example.newproject.ui.theme.WelcomeBackground
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    onNavigateToHome: () -> Unit
) {
    val state by viewModel.loginState.collectAsState()

    LaunchedEffect(state) {
        if (state is LoginState.Success) {
            onNavigateToHome()
        }
    }

    LoginScreenContent(
        state = state,
        onLoginClick = { phone, pwd ->
            viewModel.login(phone, pwd)
        },
        onResetState = {
            viewModel.resetState()
        },
        onVerifyOtp = { phone, otp, onSuccess, onError ->
            viewModel.verifyOtp(phone, otp, onSuccess, onError)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreenContent(
    state: LoginState,
    onLoginClick: (String, String) -> Unit,
    onResetState: () -> Unit = {},
    onVerifyOtp: (String,
                  String,
                  () -> Unit,
                  (String) -> Unit
            ) -> Unit = { _, _, _, _ -> }
    /**
     * 為了預覽方便，給予預設值在後面 preview function 就不用加 onVerifyOtp = { _, _, _, _ -> }
     */
) {
    val context = LocalContext.current
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var showLoginSheet by remember { mutableStateOf(false) }

    LaunchedEffect(state) {
        if (state is LoginState.Success || state is LoginState.NeedsVerification) {
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
                DrawerMenuContent(onClose = { scope.launch { drawerState.close() } })
            }
        }
    ) {
        Scaffold(
            containerColor = WelcomeBackground,
            contentColor = Color.White
        ) { paddingValues ->
            LoadingDialog(isShowing = state is LoginState.Loading)

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 24.dp, vertical = 24.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // ================== Top Bar ==================
                Box(modifier = Modifier.fillMaxWidth()) {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = stringResource(id = R.string.menu_desc),
                        tint = NeonPurple,
                        modifier = Modifier
                            .size(28.dp)
                            .align(Alignment.CenterStart)
                            .clickable { scope.launch { drawerState.open() } }
                            .neonGlow(color = NeonPurple, alpha = 0.8f, glowRadius = 15.dp, borderRadius = 15.dp)
//                            .neonGlow(color = NeonPurple, alpha = 0.7f, glowRadius = 20.dp),
                    )
                    
                    Image(
                        painter = painterResource(id = R.drawable.ic_xcash),
                        contentDescription = stringResource(id = R.string.xcash_logo_desc),
                        modifier = Modifier.height(30.dp).align(Alignment.Center)
                    )
                    
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.height(40.dp).align(Alignment.CenterEnd)
                    ) {
                        Icon(
//                            imageVector = Icons.Default.Public,
                            painter = painterResource(R.mipmap.ic_global),
                            contentDescription = stringResource(id = R.string.language_desc),
                            tint = NeonPurple,
                            modifier = Modifier
                                .size(20.dp)
                                .neonGlow(color = NeonPurple, alpha = 0.8f, glowRadius = 15.dp, borderRadius = 10.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(stringResource(id = R.string.language_en), color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    }
                }

                // ================== Center Logo ==================
                Box(
                    modifier = Modifier.weight(1f).fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    val gifLoader = remember(context) {
                        ImageLoader.Builder(context)
                            .components { add(GifDecoder.Factory()) }
                            .build()
                    }
                    AsyncImage(
                        model = R.drawable.xcash_logo_type2,
                        imageLoader = gifLoader,
                        contentDescription = stringResource(id = R.string.center_neon_logo_desc),
                        modifier = Modifier.fillMaxWidth(0.9f)
                    )
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
                        enabled = state !is LoginState.Loading,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .neonGlow(color = NeonPurple, alpha = 0.7f, glowRadius = 20.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = NeonPurple, contentColor = Color.White),
                        shape = RoundedCornerShape(25.dp)
                    ) {
                        Text(if (state is LoginState.Loading) stringResource(id = R.string.logging_in) else stringResource(id = R.string.login_btn), fontSize = 16.sp)
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {},
                        enabled = state !is LoginState.Loading,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .neonGlow(color = NeonGreen, alpha = 0.7f, glowRadius = 20.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = NeonGreen, contentColor = Color.White),
                        border = BorderStroke(width = 1.dp, color = NeonGreenLight),
                        shape = RoundedCornerShape(25.dp)
                    ){
                        Text(if (state is LoginState.Loading) stringResource(id = R.string.logging_in) else stringResource(id = R.string.sign_up_with_telegram), fontSize = 16.sp)
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = stringResource(id = R.string.login_or), color = Color.LightGray, fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(3.dp))
                    Text(
                        text = buildAnnotatedString {
                            append(stringResource(id = R.string.dont_have_account))
                            withStyle(SpanStyle(color = NeonPurple)) { append(stringResource(id = R.string.sign_up)) }
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
        }
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
            errorMessage = (state as? LoginState.Error)?.message
        )
    }

    if (state is LoginState.NeedsVerification) {
        VerifyMobileDialog(
            initialPhone = state.phone,
            onDismiss = { onResetState() },
            onSubmit = { otp -> 
                onVerifyOtp(
                    state.phone,
                    otp,
                    { /* 成功時由 LaunchedEffect(state) 自動導向 home */ },
                    { errorMsg ->
                        Toast.makeText(context, errorMsg, Toast.LENGTH_LONG).show()
                    }
                )
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    MaterialTheme {
        LoginScreenContent(state = LoginState.Idle, onLoginClick = { _, _ -> })
    }
}
