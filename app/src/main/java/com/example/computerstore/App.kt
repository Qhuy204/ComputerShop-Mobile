package com.example.computerstore

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.example.computerstore.screens.LoginScreen
import com.example.computerstore.screens.SignupScreen
import com.example.computerstore.screens.VerificationScreen

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun App() {
    var showSignup by remember { mutableStateOf(false) }

    Surface(color = MaterialTheme.colorScheme.background) {
        Box {
            // Luôn luôn hiển thị SignIn (nền)
//            LoginScreen(
//                onSignupClick = { showSignup = true }
//            )
//
//            // SignUp overlay
//            AnimatedVisibility(
//                visible = showSignup,
//                enter = slideInHorizontally(
//                    initialOffsetX = { it }, // trượt từ phải sang
//                    animationSpec = tween(700, easing = LinearOutSlowInEasing)
//                ),
//                exit = slideOutHorizontally(
//                    targetOffsetX = { it }, // trượt ra phải
//                    animationSpec = tween(700, easing = LinearOutSlowInEasing)
//                )
//            ) {
//                SignupScreen(
//                    onClose = { showSignup = false },
//                )
//            }
            VerificationScreen()
        }
    }
}
