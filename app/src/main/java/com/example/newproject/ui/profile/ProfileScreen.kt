package com.example.newproject.ui.profile

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.newproject.ui.components.LoadingDialog

@Composable
fun ProfileScreen(viewModel: ProfileViewModel) {
    val state by viewModel.profileState.collectAsState()

    LoadingDialog(isShowing = state is ProfileState.Loading)

    ProfileScreenContent(
        state = state,
        onLogout = { viewModel.logout() },
        onDismissError = { viewModel.resetState() }
    )
}

@Composable
fun ProfileScreenContent(
    state: ProfileState = ProfileState.Idle,
    onLogout: () -> Unit = {},
    onDismissError: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (state is ProfileState.Error) {
                Text(
                    text = state.message,
                    color = MaterialTheme.colorScheme.error
                )
            }

            Button(
                onClick = onLogout,
                enabled = state !is ProfileState.Loading,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFE53935),
                    contentColor = Color.White
                )
            ) {
                Text(text = "Logout", modifier = Modifier.padding(vertical = 8.dp))
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0A1F37)
@Composable
fun ProfileScreenPreview() {
    MaterialTheme {
        ProfileScreenContent()
    }
}

