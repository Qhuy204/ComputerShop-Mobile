package com.example.computerstore

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.computerstore.navigation.BottomBar
import com.example.computerstore.navigation.BottomBarScreen
import com.example.computerstore.screens.HomeScreen
// import com.example.computerstore.screens.CartScreen
// import com.example.computerstore.screens.ProfileScreen

@Composable
fun MainScaffold(onLogout: () -> Unit) {
    val navController = rememberNavController()
    val listState = rememberLazyListState()
    var bottomBarVisible by remember { mutableStateOf(true) }

    // 🔹 Theo dõi hướng scroll để ẩn/hiện bottom bar
    LaunchedEffect(listState) {
        var lastOffset = 0
        val threshold = 20 // px để tránh nhấp nháy

        snapshotFlow { listState.firstVisibleItemIndex to listState.firstVisibleItemScrollOffset }
            .collect { (index, offset) ->
                val current = index * 10000 + offset
                when {
                    current - lastOffset > threshold -> bottomBarVisible = false // scroll xuống
                    lastOffset - current > threshold -> bottomBarVisible = true  // scroll lên
                }
                lastOffset = current
            }
    }

    Scaffold(
        bottomBar = {
            AnimatedVisibility(
                visible = bottomBarVisible,
                enter = slideInVertically(
                    initialOffsetY = { it },
                    animationSpec = tween(durationMillis = 250) // mượt hơn
                ),
                exit = slideOutVertically(
                    targetOffsetY = { it },
                    animationSpec = tween(durationMillis = 250)
                )
            ) {
                BottomBar(navController = navController)
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = BottomBarScreen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(BottomBarScreen.Home.route) {
                HomeScreen(onLogout = onLogout, listState = listState)
            }
            composable(BottomBarScreen.Cart.route) {
                // CartScreen(listState = listState)
            }
            composable(BottomBarScreen.Profile.route) {
                // ProfileScreen(listState = listState)
            }
        }
    }
}
