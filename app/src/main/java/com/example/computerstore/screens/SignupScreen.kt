package com.example.computerstore.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.computerstore.R
import com.example.computerstore.screens.components.CustomCheckbox
import com.example.computerstore.ui.components.CustomTextField
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import com.google.firebase.auth.FirebaseAuth
import android.widget.Toast
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
@Composable
fun SignupScreen(onClose: () -> Unit) {
    // State variables for the text fields and checkbox
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("")}
    var rememberMe by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()


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
                contentDescription = "Signup background",
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
                    text = "Create Account",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "Register your account today\nusing a valid email and password.",
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
                        text = "Sign up",
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
                        isLastField = false
                    )


                    Spacer(modifier = Modifier.height(8.dp))

                    // Confirm Password Input Field
                    CustomTextField(
                        value = confirmPassword,
                        onValueChange = { confirmPassword = it },
                        placeholder = "Confirm your password",
                        iconRes = R.drawable.lock,
                        focusManager = focusManager,
                        isPasswordField = true,
                        isLastField = true
                    )

                    Spacer(modifier = Modifier.height(16.dp))



//                     Checkbox and Forgot Password Row
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


                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Spacer(modifier = Modifier.width(8.dp))

                                PrivacyPolicyText(
                                    onPrivacyClick = { /* TODO: mở Privacy */ },
                                    onPolicyClick = { /* TODO: mở Policy */ }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Sign-up Button
                    Button(
                        onClick = {
                            if (password == confirmPassword && email.isNotEmpty() && password.length >= 6) {
                                scope.launch {
                                    try {
                                        val result = auth.createUserWithEmailAndPassword(email, password).await()
                                        val user = result.user
                                        user?.let {
                                            val db = FirebaseFirestore.getInstance()
                                            val profile = hashMapOf(
                                                "user_id" to it.uid,
                                                "email" to it.email,
                                                "full_name" to "User",
                                                "gender" to "male",
                                                "phone_number" to "",
                                                "birthday" to null,
                                                "is_admin" to 0,
                                                "created_at" to FieldValue.serverTimestamp()
                                            )
                                            db.collection("users").document(it.uid).set(profile)
                                                .addOnSuccessListener {
                                                    Toast.makeText(context, "✅ Signup success: ${user.email}", Toast.LENGTH_SHORT).show()
                                                }
                                                .addOnFailureListener { e ->
                                                    Toast.makeText(context, "⚠️ Failed to save profile: ${e.message}", Toast.LENGTH_SHORT).show()
                                                }

                                        }
                                    } catch (e: Exception) {
                                        Toast.makeText(context, "❌ Error: ${e.message}", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            } else {
                                Toast.makeText(context, "⚠️ Check your inputs!", Toast.LENGTH_SHORT).show()
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF3F7DE8)
                        ),
                    ) {
                        Text("Sign up", fontSize = 16.sp)
                    }

                    // Sign-in Link at the bottom
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
                            Text("Already have an account?", color = Color.DarkGray)
                            TextButton(onClick = onClose) {
                                Text("Sign in here",  color = Color(0xFF3F7DE8))
                            }
                        }
                    }
                }
            }


        }
    }
}


@Composable
fun PrivacyPolicyText(
    onPrivacyClick: () -> Unit,
    onPolicyClick: () -> Unit
) {
    val annotatedText = buildAnnotatedString {
        append("I agree with ")

        pushStringAnnotation(tag = "PRIVACY", annotation = "privacy")
        withStyle(style = SpanStyle(color = Color(0xFF3F7DE8))) {
            append("privacy")
        }
        pop()

        append(" and ")

        pushStringAnnotation(tag = "POLICY", annotation = "policy")
        withStyle(style = SpanStyle(color = Color(0xFF3F7DE8))) {
            append("policy")
        }
        pop()
    }

    ClickableText(
        text = annotatedText,
        onClick = { offset ->
            annotatedText.getStringAnnotations(start = offset, end = offset)
                .firstOrNull()?.let { annotation ->
                    when (annotation.tag) {
                        "PRIVACY" -> onPrivacyClick()
                        "POLICY" -> onPolicyClick()
                    }
                }
        }
    )
}
