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
import com.example.newproject.ui.cards.linked_success.LinkedSuccessScreen
import com.example.newproject.ui.cards.select.SelectCardTypeScreen
import com.example.newproject.ui.home.transaction_detail.TransactionDetailScreen
import com.example.newproject.ui.home.transaction_detail.TransactionDetailViewModel
import com.example.newproject.ui.home.update_log.UpdateLogScreen
import com.example.newproject.ui.home.update_log.UpdateLogViewModel
import com.example.newproject.ui.login.LoginScreen
import com.example.newproject.ui.login.LoginViewModel
import com.example.newproject.ui.main.MainScreen
import com.example.newproject.ui.home.setting.SettingScreen
import com.example.newproject.ui.profile.edit.ProfileEditScreen
import com.example.newproject.ui.profile.security.SecurityCenterScreen
import com.example.newproject.ui.profile.transaction.TransactionHistoryScreen
import com.example.newproject.ui.profile.verification.VerificationStatusScreen
import com.example.newproject.ui.scanpay.ScanPayViewModel
import com.example.newproject.ui.scanpay.confirm.ConfirmPaymentScreen
import com.example.newproject.ui.scanpay.input.InputAmountScreen
import com.example.newproject.ui.scanpay.success.TransactionSuccessfulScreen
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
                    navController.navigate(Routes.mainAtTab(0)) {
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
                    navController.navigate(Routes.mainAtTab(0)) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                }
            )
        }

        composable(
            route = Routes.MAIN,
            arguments = listOf(navArgument("tab") { type = NavType.IntType; defaultValue = 0 }),
            exitTransition = {
                when (targetState.destination.route) {
                    Routes.PROFILE_EDIT -> slideOutHorizontally { -it }
                    Routes.SECURITY_CENTER -> slideOutHorizontally { -it }
                    Routes.TRANSACTION_HISTORY -> slideOutHorizontally { -it }
                    Routes.VERIFICATION_STATUS -> slideOutHorizontally { -it }
                    Routes.SELECT_CARD_TYPE -> slideOutHorizontally { -it }
                    Routes.CARD_DETAIL -> slideOutHorizontally { -it }
                    Routes.SCAN_PAY_INPUT_AMOUNT -> slideOutHorizontally { -it }
                    Routes.SETTINGS -> slideOutHorizontally { -it }
                    Routes.CARD_LINKED_SUCCESS -> slideOutHorizontally { -it }
                    Routes.TRANSACTION_DETAIL -> slideOutHorizontally { -it }
                    Routes.UPDATE_LOG -> slideOutHorizontally { -it }
                    else -> null
                }
            },
            popEnterTransition = {
                when (initialState.destination.route) {
                    Routes.PROFILE_EDIT -> slideInHorizontally { -it }
                    Routes.SECURITY_CENTER -> slideInHorizontally { -it }
                    Routes.TRANSACTION_HISTORY -> slideInHorizontally { -it }
                    Routes.VERIFICATION_STATUS -> slideInHorizontally { -it }
                    Routes.SELECT_CARD_TYPE -> slideInHorizontally { -it }
                    Routes.CARD_DETAIL -> slideInHorizontally { -it }
                    Routes.SCAN_PAY_INPUT_AMOUNT -> slideInHorizontally { -it }
                    Routes.SETTINGS -> slideInHorizontally { -it }
                    Routes.CARD_LINKED_SUCCESS -> slideInHorizontally { -it }
                    Routes.TRANSACTION_DETAIL -> slideInHorizontally { -it }
                    Routes.UPDATE_LOG -> slideInHorizontally { -it }
                    else -> null
                }
            }
        ) { backStackEntry ->
            val scanPayViewModel = hiltViewModel<ScanPayViewModel>(backStackEntry)
            val initialTab = backStackEntry.arguments?.getInt("tab") ?: 0
            MainScreen(
                scanPayViewModel = scanPayViewModel,
                initialTab = initialTab,
                onNavigate = { navController.navigate(it) }
            )
        }

        // ── Profile sub-pages ──────────────────────────────────────────────
        composable(
            route = Routes.PROFILE_EDIT,
            enterTransition = { slideInHorizontally { it } },
            popExitTransition = { slideOutHorizontally { it } }
        ) {
            ProfileEditScreen(onBack = { navController.popBackStack() })
        }

        composable(
            route = Routes.SECURITY_CENTER,
            enterTransition = { slideInHorizontally { it } },
            popExitTransition = { slideOutHorizontally { it } }
        ) {
            val viewModel = hiltViewModel<com.example.newproject.ui.profile.security.SecurityCenterViewModel>()
            SecurityCenterScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Routes.TRANSACTION_HISTORY,
            enterTransition = { slideInHorizontally { it } },
            popExitTransition = { slideOutHorizontally { it } }
        ) {
            val viewModel = hiltViewModel<com.example.newproject.ui.profile.transaction.TransactionHistoryViewModel>()
            TransactionHistoryScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Routes.VERIFICATION_STATUS,
            enterTransition = { slideInHorizontally { it } },
            popExitTransition = { slideOutHorizontally { it } }
        ) {
            val viewModel = hiltViewModel<com.example.newproject.ui.profile.verification.VerificationStatusViewModel>()
            VerificationStatusScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }

        // ── Home sub-pages ────────────────────────────────────────────────
        composable(
            route = Routes.SETTINGS,
            enterTransition = { slideInHorizontally { it } },
            popExitTransition = { slideOutHorizontally { it } }
        ) {
            val viewModel = hiltViewModel<com.example.newproject.ui.home.setting.SettingViewModel>()
            SettingScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onNavigate = { navController.navigate(it) }
            )
        }

        composable(
            route = Routes.UPDATE_LOG,
            enterTransition = { slideInHorizontally { it } },
            popExitTransition = { slideOutHorizontally { it } }
        ) {
            val viewModel = hiltViewModel<UpdateLogViewModel>()
            UpdateLogScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
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
                onBack = { navController.popBackStack() },
                onNavigate = { navController.navigate(it) }
            )
        }

        composable(
            route = Routes.SCAN_PAY_TRANSACTION_SUCCESSFUL,
            enterTransition = { slideInHorizontally { it } },
            popExitTransition = { slideOutHorizontally { it } }
        ) { backStackEntry ->
            val mainEntry = remember(backStackEntry) {
                navController.getBackStackEntry(Routes.MAIN)
            }
            val viewModel = hiltViewModel<ScanPayViewModel>(mainEntry)
            TransactionSuccessfulScreen(
                viewModel = viewModel,
                onBack = {
                    navController.navigate(Routes.mainAtTab(0)) {
                        popUpTo(Routes.MAIN) { inclusive = true }
                    }
                }
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
            popExitTransition = { slideOutHorizontally { it } },
            exitTransition = { slideOutHorizontally { -it } }
        ) {
            AddNewCardScreen(
                onBack = { navController.popBackStack() },
                onNavigate = { navController.navigate(it) }
            )
        }

        composable(
            route = Routes.CARD_LINKED_SUCCESS,
            enterTransition = { slideInHorizontally { it } },
            popExitTransition = { slideOutHorizontally { it } }
        ) {
            LinkedSuccessScreen(
                onSetupPrimary = {
                    navController.navigate(Routes.mainAtTab(0)) {
                        popUpTo(Routes.MAIN) { inclusive = true }
                    }
                },
                onNotNow = {
                    navController.navigate(Routes.mainAtTab(1)) {
                        popUpTo(Routes.MAIN) { inclusive = true }
                    }
                }
            )
        }

        composable(
            route = Routes.CARD_DETAIL,
            arguments = listOf(navArgument("cardId") { type = NavType.IntType }),
            enterTransition = { slideInHorizontally { it } },
            popExitTransition = { slideOutHorizontally { it } }
        ) {
            CardDetailScreen(onBack = { navController.popBackStack() })
        }

        composable(
            route = Routes.TRANSACTION_DETAIL,
            arguments = listOf(navArgument("orderId") { type = NavType.StringType }),
            enterTransition = { slideInHorizontally { it } },
            popExitTransition = { slideOutHorizontally { it } }
        ) {
            val viewModel = hiltViewModel<TransactionDetailViewModel>()
            TransactionDetailScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }

    }
}
