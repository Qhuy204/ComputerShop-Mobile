package com.example.computerstore

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.computerstore.screens.LoginScreen
import com.example.computerstore.screens.SignupScreen
import com.google.firebase.auth.FirebaseAuth

@Composable
fun App() {
    val navController = rememberNavController()
    val auth = FirebaseAuth.getInstance()
    val startDestination = if (auth.currentUser != null) "main" else "login"

    Surface(color = MaterialTheme.colorScheme.background) {
        NavHost(
            navController = navController,
            startDestination = startDestination,
        ) {
            composable("login") {
                LoginScreen(
                    navController = navController,
                    onSignupClick = { navController.navigate("signup") },
                    onLoginSuccess = {
                        navController.navigate("main") {
                            popUpTo("login") { inclusive = true }
                        }
                    }
                )
            }
            composable("signup") {
                SignupScreen(onClose = { navController.popBackStack() })
            }
            composable("main") {
                MainScaffold(
                    onLogout = {
                        auth.signOut()
                        navController.navigate("login") {
                            popUpTo("main") { inclusive = true }
                        }
                    }
                )
            }
        }
    }
}