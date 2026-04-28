package com.example.newproject.ui.login

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.newproject.R
import com.example.newproject.ui.components.LoadingDialog
import com.example.newproject.ui.theme.NeonCyan
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
        onLoginClick = { viewModel.login() }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreenContent(
    state: LoginState,
    onLoginClick: () -> Unit
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

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
                        contentDescription = "Menu",
                        tint = NeonPurple,
                        modifier = Modifier
                            .size(28.dp)
                            .align(Alignment.CenterStart)
                            .clickable { scope.launch { drawerState.open() } }
                    )
                    
                    Image(
                        painter = painterResource(id = R.drawable.ic_xcash),
                        contentDescription = "xCash Logo",
                        modifier = Modifier.height(40.dp).align(Alignment.Center)
                    )
                    
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.align(Alignment.CenterEnd)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "Language",
                            tint = NeonPurple,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("EN", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    }
                }

                // ================== Center Logo ==================
                Box(
                    modifier = Modifier.weight(1f).fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_xcash_logo),
                        contentDescription = "Center Neon Logo",
                        modifier = Modifier.fillMaxWidth(0.9f)
                    )
                }

                // ================== Bottom Section ==================
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (state is LoginState.Error) {
                        Text(text = state.message, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(bottom = 8.dp))
                    }

                    Button(
                        onClick = onLoginClick,
                        enabled = state !is LoginState.Loading,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .shadow(elevation = 20.dp, spotColor = NeonPurple, shape = RoundedCornerShape(25.dp)),
                        colors = ButtonDefaults.buttonColors(containerColor = NeonPurple, contentColor = Color.White),
                        shape = RoundedCornerShape(25.dp)
                    ) {
                        Text(if (state is LoginState.Loading) "Logging in..." else "Login", fontSize = 16.sp)
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(text = "or", color = Color.LightGray, fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = buildAnnotatedString {
                            append("Don't have an account? ")
                            withStyle(SpanStyle(color = NeonPurple)) { append("Sign Up") }
                        },
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(32.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Info, contentDescription = "Shield", modifier = Modifier.size(14.dp), tint = Color.White)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Regulated by BSP", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    MaterialTheme {
        LoginScreenContent(state = LoginState.Idle, onLoginClick = {})
    }
}
