package com.example.newproject.ui

import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.newproject.ui.cards.add.AddNewCardScreen
import com.example.newproject.ui.cards.detail.CardDetailScreen
import com.example.newproject.ui.cards.select.SelectCardTypeScreen
import com.example.newproject.ui.login.LoginScreen
import com.example.newproject.ui.login.LoginViewModel
import com.example.newproject.ui.main.MainScreen
import com.example.newproject.ui.profile.security.SecurityCenterScreen
import com.example.newproject.ui.scanpay.ConfirmPaymentScreen
import com.example.newproject.ui.scanpay.InputAmountScreen
import com.example.newproject.ui.scanpay.ScanPayViewModel
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
                    Routes.CARD_DETAIL -> slideOutHorizontally { -it }
                    Routes.SCAN_PAY_INPUT_AMOUNT -> slideOutHorizontally { -it }
                    else -> null
                }
            },
            popEnterTransition = {
                when (initialState.destination.route) {
                    Routes.SECURITY_CENTER -> slideInHorizontally { -it }
                    Routes.SELECT_CARD_TYPE -> slideInHorizontally { -it }
                    Routes.CARD_DETAIL -> slideInHorizontally { -it }
                    Routes.SCAN_PAY_INPUT_AMOUNT -> slideInHorizontally { -it }
                    else -> null
                }
            }
        ) { backStackEntry ->
            val scanPayViewModel = hiltViewModel<ScanPayViewModel>(backStackEntry)
            MainScreen(
                scanPayViewModel = scanPayViewModel,
                onNavigate = { navController.navigate(it) }
            )
        }

        // ── Profile sub-pages ──────────────────────────────────────────────
        composable(
            route = Routes.SECURITY_CENTER,
            enterTransition = { slideInHorizontally { it } },
            popExitTransition = { slideOutHorizontally { it } }
        ) {
            SecurityCenterScreen(onBack = { navController.popBackStack() })
        }

        // ── ScanPay sub-pages (ScanPayViewModel scoped to MAIN) ──────────
        composable(
            route = Routes.SCAN_PAY_INPUT_AMOUNT,
            enterTransition = { slideInHorizontally { it } },
            popExitTransition = { slideOutHorizontally { it } },
            exitTransition = { slideOutHorizontally { -it } },
            popEnterTransition = { slideInHorizontally { -it } }
        ) { backStackEntry ->
            val mainEntry = remember(backStackEntry) {
                navController.getBackStackEntry(Routes.MAIN)
            }
            val viewModel = hiltViewModel<ScanPayViewModel>(mainEntry)
            InputAmountScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onNavigate = { navController.navigate(it) }
            )
        }

        composable(
            route = Routes.SCAN_PAY_CONFIRM_PAYMENT,
            enterTransition = { slideInHorizontally { it } },
            popExitTransition = { slideOutHorizontally { it } }
        ) { backStackEntry ->
            val mainEntry = remember(backStackEntry) {
                navController.getBackStackEntry(Routes.MAIN)
            }
            val viewModel = hiltViewModel<ScanPayViewModel>(mainEntry)
            ConfirmPaymentScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
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

        composable(
            route = Routes.CARD_DETAIL,
            arguments = listOf(navArgument("cardId") { type = NavType.IntType }),
            enterTransition = { slideInHorizontally { it } },
            popExitTransition = { slideOutHorizontally { it } }
        ) {
            CardDetailScreen(onBack = { navController.popBackStack() })
        }

    }
}
