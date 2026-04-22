package com.example.newproject.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.newproject.ui.home.HomeScreen
import com.example.newproject.ui.home.HomeViewModel
import com.example.newproject.ui.login.LoginScreen
import com.example.newproject.ui.login.LoginViewModel

@Composable
fun AppNavigation(
    appViewModel: AppViewModel = hiltViewModel()
) {
    val navController = rememberNavController()

    // 🎯 監聽全局的登出事件
    LaunchedEffect(Unit) {
        appViewModel.logoutEvent.collect {
            // 當接收到登出事件時，清空所有的返回堆疊，並強迫導向登入頁
            navController.navigate("login") {
                popUpTo(0) { inclusive = true }
            }
        }
    }

    NavHost(navController = navController, startDestination = "login") {
        
        composable("login") {
            val loginViewModel: LoginViewModel = hiltViewModel()
            LoginScreen(
                viewModel = loginViewModel,
                onNavigateToHome = {
                    navController.navigate("home") {
                        // 轉跳後把 login 頁面從 back stack 中清掉，確保按返回鍵不會回到登入頁
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }

        composable("home") {
            val homeViewModel: HomeViewModel = hiltViewModel()
            HomeScreen(viewModel = homeViewModel)
        }

    }
}
