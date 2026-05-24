package com.example.newproject.ui

import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.newproject.ui.main.MainScreen
import com.example.newproject.ui.login.LoginScreen
import com.example.newproject.ui.login.LoginViewModel
import com.example.newproject.ui.cards.select.SelectCardTypeScreen
import com.example.newproject.ui.cards.add.AddNewCardScreen
import com.example.newproject.ui.cards.select.SelectCardTypeScreen
import com.example.newproject.ui.profile.security.SecurityCenterScreen
import com.example.newproject.ui.welcome.WelcomeScreen
import com.example.newproject.ui.welcome.WelcomeViewModel

@Composable
fun AppNavigation(
    appViewModel: AppViewModel = hiltViewModel()
) {
    val navController = rememberNavController()

    LaunchedEffect(Unit) {
        appViewModel.logoutEvent.collect {
            if (navController.currentDestination?.route != Routes.LOGIN) {
                navController.navigate(Routes.LOGIN) {
                    popUpTo(0) { inclusive = true }
                }
            }
        }
    }

    NavHost(navController = navController, startDestination = Routes.WELCOME) {

        composable(Routes.WELCOME) {
            val welcomeViewModel: WelcomeViewModel = hiltViewModel()
            WelcomeScreen(
                viewModel = welcomeViewModel,
                onNavigateToHome = {
                    navController.navigate(Routes.MAIN) {
                        popUpTo(Routes.WELCOME) { inclusive = true }
                    }
                },
                onNavigateToLogin = {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.WELCOME) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.LOGIN) {
            val loginViewModel: LoginViewModel = hiltViewModel()
            LoginScreen(
                viewModel = loginViewModel,
                onNavigateToHome = {
                    navController.navigate(Routes.MAIN) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                }
            )
        }

        composable(
            route = Routes.MAIN,
            exitTransition = {
                when (targetState.destination.route) {
                    Routes.SECURITY_CENTER -> slideOutHorizontally { -it }
                    Routes.SELECT_CARD_TYPE -> slideOutHorizontally { -it }
                    else -> null
                }
            },
            popEnterTransition = {
                when (initialState.destination.route) {
                    Routes.SECURITY_CENTER -> slideInHorizontally { -it }
                    Routes.SELECT_CARD_TYPE -> slideInHorizontally { -it }
                    else -> null
                }
            }
        ) {
            MainScreen(onNavigate = { navController.navigate(it) })
        }

        // ── Profile sub-pages ──────────────────────────────────────────────
        composable(
            route = Routes.SECURITY_CENTER,
            enterTransition = { slideInHorizontally { it } },
            popExitTransition = { slideOutHorizontally { it } }
        ) {
            SecurityCenterScreen(onBack = { navController.popBackStack() })
        }

        // ── Cards sub-pages ────────────────────────────────────────────────
        composable(
            route = Routes.SELECT_CARD_TYPE,
            enterTransition = { slideInHorizontally { it } },
            popExitTransition = { slideOutHorizontally { it } },
            exitTransition = { slideOutHorizontally { -it } },
            popEnterTransition = { slideInHorizontally { -it } }
        ) {
            SelectCardTypeScreen(
                onBack = { navController.popBackStack() },
                onNavigate = { navController.navigate(it) }
            )
        }

        composable(
            route = Routes.ADD_NEW_CARD,
            enterTransition = { slideInHorizontally { it } },
            popExitTransition = { slideOutHorizontally { it } }
        ) {
            AddNewCardScreen(onBack = { navController.popBackStack() })
        }

    }
}
