package com.example.newproject.ui.login

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.newproject.ui.components.LoadingDialog

@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    onNavigateToHome: () -> Unit
) {
    val state by viewModel.loginState.collectAsState()

    // 監聽有沒有成功登入
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
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("登入頁面") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    ) { paddingValues ->
        // 🌟 把它放在 UI 畫面上層，Dialog 會自動蓋住後面的元件並阻擋點擊
        LoadingDialog(isShowing = state is LoginState.Loading)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when (state) {
                is LoginState.Error -> {
                    Text(text = state.message, color = MaterialTheme.colorScheme.error)
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = onLoginClick) {
                        Text("重試登入")
                    }
                }
                else -> {
                    // Idle 或是 Loading 狀態時，都先顯示這個按鈕畫面
                    // 因為上方已經有 LoadingDialog 遮罩會禁止使用者的點擊了
                    Text("請點擊下方按鈕登入", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(16.dp))
                    // 為了避免重複點擊，可以在 Loading 時把按鈕停用 (enabled = false)
                    Button(onClick = onLoginClick, enabled = state !is LoginState.Loading) {
                        Text(if (state is LoginState.Loading) "登入中..." else "登入 (Login API)")
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    MaterialTheme {
        LoginScreenContent(
            state = LoginState.Idle,
            onLoginClick = {}
        )
    }
}
