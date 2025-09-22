package com.example.computerstore.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.computerstore.screens.*

@Composable
fun MainNavHost() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = { BottomBar(navController = navController) }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = BottomBarScreen.Home.route,
            modifier = androidx.compose.ui.Modifier.padding(paddingValues)
        ) {
            composable(BottomBarScreen.Home.route) { HomeScreen(onLogout = {}, listState = rememberLazyListState()) }
            composable(BottomBarScreen.Cart.route) { Text(text = "Cart Screen") }
            composable(BottomBarScreen.Profile.route) { Text(text = "Profile Screen") }
            composable("login") { LoginScreen(
                navController = navController,
                onSignupClick = { navController.navigate("signup") }
            ) }
            composable("register") { SignupScreen(onClose = { navController.popBackStack() }) }
        }
    }
}
