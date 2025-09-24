package com.example.computerstore

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.computerstore.navigation.BottomBar
import com.example.computerstore.navigation.BottomBarScreen
import com.example.computerstore.screens.HomeScreen
import com.example.computerstore.screens.NewsDetailsScreen
import com.example.computerstore.screens.NewsScreen
import com.example.computerstore.screens.ProfileScreen


@Composable
fun MainScaffold(onLogout: () -> Unit) {
    val navController = rememberNavController()
    val homeListState = rememberLazyListState()

    Scaffold(
        bottomBar = {
            BottomBar(navController = navController)
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = BottomBarScreen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(BottomBarScreen.Home.route) {
                HomeScreen(onLogout = onLogout, listState = homeListState)
            }

            composable(BottomBarScreen.News.route) {
                NewsScreen(
                    onBlogClick = { blog ->
                        navController.navigate("newsDetails/${blog.blog_id}")
                    }
                )
            }

            composable(
                route = "newsDetails/{blogId}",
                arguments = listOf(
                    navArgument("blogId") {
                        type = NavType.IntType
                    }
                )
            ) { backStackEntry ->
                val blogId = backStackEntry.arguments?.getInt("blogId") ?: 0
                NewsDetailsScreen(
                    blogId = blogId,
                    onBackClick = { navController.popBackStack() }
                )
            }

            composable(BottomBarScreen.Cart.route) {
                // CartScreen placeholder
            }

            composable(BottomBarScreen.Profile.route) {
                ProfileScreen()
            }
        }
    }
}