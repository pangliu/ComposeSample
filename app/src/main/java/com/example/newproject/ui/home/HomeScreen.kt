package com.example.newproject.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.newproject.model.Post

@Composable
fun HomeScreen(viewModel: HomeViewModel) {
    val state by viewModel.homeState.collectAsState()

    HomeScreenContent(state = state)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenContent(state: HomeState) {
    Scaffold(
        containerColor = androidx.compose.ui.graphics.Color.Black,
        contentColor = androidx.compose.ui.graphics.Color.White
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (val s = state) {
                is HomeState.Loading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                is HomeState.Error -> {
                    Text(
                        text = "Error: ${s.message}", 
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                is HomeState.Success -> {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("取得的使用者資料 (UserInfo API):", style = MaterialTheme.typography.titleMedium)
                        Text(text = s.userInfo, style = MaterialTheme.typography.bodySmall)
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        HorizontalDivider()
                        Spacer(modifier = Modifier.height(16.dp))

                        Text    ("貼文資料:", style = MaterialTheme.typography.titleMedium)
                        Spacer(modifier = Modifier.height(8.dp))
                        LazyColumn(modifier = Modifier.fillMaxSize()) {
                            items(s.posts) { post ->
                                PostCard(post)
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    MaterialTheme {
        HomeScreenContent(
            state = HomeState.Success(
                userInfo = "{ name: \"Test User\" }",
                posts = listOf(
                    Post(userId = 1, id = 1, title = "Test Post 1", body = "Hello World"),
                    Post(userId = 1, id = 2, title = "Test Post 2", body = "Compose is awesome")
                )
            )
        )
    }
}

@Composable
fun PostCard(post: Post) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = androidx.compose.ui.graphics.Color.DarkGray,
            contentColor = androidx.compose.ui.graphics.Color.White
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = post.title, style = MaterialTheme.typography.titleSmall)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = post.body, style = MaterialTheme.typography.bodySmall)
        }
    }
}
