package com.example.newproject.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.newproject.ui.main.MainScreen
import com.example.newproject.ui.login.LoginScreen
import com.example.newproject.ui.login.LoginViewModel

import com.example.newproject.ui.welcome.WelcomeScreen
import com.example.newproject.ui.welcome.WelcomeViewModel

@Composable
fun AppNavigation(
    appViewModel: AppViewModel = hiltViewModel()
) {
    val navController = rememberNavController()

    // 🎯 監聽全局的登出事件
    LaunchedEffect(Unit) {
        appViewModel.logoutEvent.collect {
            // 已在登入頁（例如帳密錯誤觸發的 1005）不重複導向，避免重建畫面
            if (navController.currentDestination?.route != "login") {
                navController.navigate("login") {
                    popUpTo(0) { inclusive = true }
                }
            }
        }
    }

    NavHost(navController = navController, startDestination = "welcome") {
        
        composable("welcome") {
            val welcomeViewModel: WelcomeViewModel = hiltViewModel()
            WelcomeScreen(
                viewModel = welcomeViewModel,
                onNavigateToHome = {
                    navController.navigate("main") {
                        popUpTo("welcome") { inclusive = true }
                    }
                },
                onNavigateToLogin = {
                    navController.navigate("login") {
                        popUpTo("welcome") { inclusive = true }
                    }
                }
            )
        }
        
        composable("login") {
            val loginViewModel: LoginViewModel = hiltViewModel()
            LoginScreen(
                viewModel = loginViewModel,
                onNavigateToHome = {
                    navController.navigate("main") {
                        // 轉跳後把 login 頁面從 back stack 中清掉，確保按返回鍵不會回到登入頁
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }

        composable("main") {
            MainScreen()
        }

    }
}
