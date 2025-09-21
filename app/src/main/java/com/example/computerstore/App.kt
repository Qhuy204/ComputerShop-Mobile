package com.example.computerstore

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.example.computerstore.screens.LoginScreen
import com.example.computerstore.screens.SignupScreen
import com.example.computerstore.screens.HomeScreen
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun App() {
    val navController = rememberAnimatedNavController()
    val auth = FirebaseAuth.getInstance()
    val startDestination = if (auth.currentUser != null) "home" else "login"

    Surface(color = MaterialTheme.colorScheme.background) {
        AnimatedNavHost(
            navController = navController,
            startDestination = startDestination
        ) {
            composable("login") {
                LoginScreen(
                    navController = navController,
                    onSignupClick = { navController.navigate("signup") }
                )
            }
            composable("signup") {
                SignupScreen(onClose = { navController.popBackStack() })
            }
            composable("home") {
                HomeScreen(onLogout = {
                    auth.signOut()
                    navController.navigate("login") {
                        popUpTo("home") { inclusive = true }
                    }
                })
            }
        }
    }
}
