package com.example.newproject.ui.main

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.newproject.ui.cards.CardsScreen
import com.example.newproject.ui.cards.CardsViewModel
import com.example.newproject.ui.home.HomeScreen
import com.example.newproject.ui.main.nvaTab.CustomBottomNavigation
import com.example.newproject.ui.profile.ProfileScreen
import com.example.newproject.ui.profile.ProfileViewModel
import com.example.newproject.ui.quests.QuestsScreen
import com.example.newproject.ui.scanpay.ScanPayScreen
import com.example.newproject.ui.scanpay.ScanPayViewModel
import com.example.newproject.ui.theme.welcomeBackground

@Composable
fun MainScreen(
    scanPayViewModel: ScanPayViewModel,
    onNavigate: (String) -> Unit = {}
) {
    var selectedIndex by rememberSaveable { mutableStateOf(0) }

    Scaffold(
        containerColor = welcomeBackground,
        contentColor = Color.White,
        bottomBar = {
            CustomBottomNavigation(
                selectedIndex = selectedIndex,
                onTabSelected = { selectedIndex = it },
                onScanPayClick = { selectedIndex = 4 }
            )
        }
    ) { paddingValues ->
        AnimatedContent(
            targetState = selectedIndex,
            transitionSpec = {
                if (targetState > initialState) {
                    slideInHorizontally { it } togetherWith slideOutHorizontally { -it }
                } else {
                    slideInHorizontally { -it } togetherWith slideOutHorizontally { it }
                }
            },
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            label = "tab_content"
        ) { index ->
            Box(modifier = Modifier.fillMaxSize()) {
                when (index) {
                    0 -> HomeScreen()
                    1 -> {
                        val cardsViewModel: CardsViewModel = hiltViewModel()
                        CardsScreen(viewModel = cardsViewModel, onNavigate = onNavigate)
                    }
                    2 -> QuestsScreen()
                    3 -> {
                        val profileViewModel: ProfileViewModel = hiltViewModel()
                        ProfileScreen(viewModel = profileViewModel, onNavigate = onNavigate)
                    }
                    4 -> ScanPayScreen(viewModel = scanPayViewModel, onNavigate = onNavigate)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MainScreenPreview() {
    var selectedIndex by remember { mutableStateOf(0) }
    MaterialTheme {
        Scaffold(
            containerColor = welcomeBackground,
            contentColor = Color.White,
            bottomBar = {
                CustomBottomNavigation(
                    selectedIndex = selectedIndex,
                    onTabSelected = { selectedIndex = it }
                )
            }
        ) { paddingValues ->
            Box(modifier = Modifier.fillMaxSize().padding(paddingValues))
        }
    }
}
