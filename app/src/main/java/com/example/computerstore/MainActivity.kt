package com.example.computerstore

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.view.WindowCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.computerstore.screens.LoginScreen
import com.example.computerstore.screens.SignupScreen
import com.example.computerstore.ui.theme.ComputerStoreTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.firebase.auth.FirebaseAuth

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            ComputerStoreTheme {
                val systemUiController = rememberSystemUiController()
                SideEffect {
                    systemUiController.setStatusBarColor(
                        color = Color(0xFFDC2626),
                        darkIcons = true
                    )
                }

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.White
                ) {
                    val navController = rememberNavController()
                    var isLoggedIn by remember { mutableStateOf(FirebaseAuth.getInstance().currentUser != null) }

                    if (isLoggedIn) {
                        MainScaffold(
                            onLogout = {
                                FirebaseAuth.getInstance().signOut()
                                isLoggedIn = false
                            }
                        )
                    } else {
                        NavHost(
                            navController = navController,
                            startDestination = "login"
                        ) {
                            composable("login") {
                                LoginScreen(
                                    navController = navController,
                                    onSignupClick = {
                                        navController.navigate("signup")
                                    },
                                    onLoginSuccess = {
                                        isLoggedIn = true
                                    }
                                )
                            }

                            composable("signup") {
                                SignupScreen(
                                    onClose = {
                                        navController.navigate("login") {
                                            popUpTo("signup") { inclusive = true }
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
