package com.example.computerstore.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.computerstore.R
import com.example.computerstore.screens.components.CustomCheckbox
import com.example.computerstore.ui.components.CustomTextField

@Composable
fun LoginScreen(onSignupClick: () -> Unit) {
    // State variables for the text fields and checkbox
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var rememberMe by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .fillMaxWidth()
            .background(Color(0xFFE8F0FF))
            .clickable(
            indication = null,
            interactionSource = remember { MutableInteractionSource() }
        ) {
            focusManager.clearFocus()
        }


    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(350.dp)
        ){
            Image(
                painter = painterResource(id = R.drawable.loginbackground),
                contentDescription = "Login background",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = "Hello!",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "Securely log in with your\nemail and password.",
                    fontSize = 16.sp,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(32.dp))
            }
        }

        // Main Content Column
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(0.dp),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.Start
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 0.dp)
                    .height(600.dp),
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White // màu nền card
                )
            ) {
                Column(
                    modifier = Modifier
                        .padding(24.dp)
                ) {
                    Text(
                        text = "Sign in",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    // Email Input Field
                    CustomTextField(
                        value = email,
                        onValueChange = { email = it },
                        placeholder = "Enter your email",
                        iconRes = R.drawable.mail,
                        focusManager = focusManager,
                        isLastField = false
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Password Input Field
                    CustomTextField(
                        value = password,
                        onValueChange = { password = it },
                        placeholder = "Enter your password",
                        iconRes = R.drawable.lock,
                        focusManager = focusManager,
                        isPasswordField = true,
                        isLastField = true
                    )

                    Spacer(modifier = Modifier.height(6.dp))


                    // Checkbox and Forgot Password Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            CustomCheckbox(
                                modifier = Modifier
                                    .offset(x = 3.dp, y = (0).dp),
                                checked = rememberMe,
                                size = 24.dp,
                                uncheckedBorderColor = Color.Transparent,
                                uncheckedBackgroundColor = Color(0xFFE5E5E5),
                                onCheckedChange = { rememberMe = it }
                            )

                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Remember me")
                        }

                        TextButton(
                            onClick = { /* Handle forgot password */ }) {
                            Text(text ="Forgot password?",
                                color = Color(0xFF3F7DE8))
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Sign-in Button
                    Button(
                        onClick = { /* Handle sign-in logic */ },
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF3F7DE8)
                        ),
                    ) {
                        Text("Sign in", fontSize = 16.sp)
                    }
                    // Sign-up Link at the bottom
                    Box(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.BottomCenter)
                                .padding(8.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Don't have an account?", color = Color.DarkGray)
                            TextButton(onClick = onSignupClick) {
                                Text("Sign up", color = Color(0xFF3F7DE8))
                            }

                        }
                    }
                }
            }


        }
    }
}