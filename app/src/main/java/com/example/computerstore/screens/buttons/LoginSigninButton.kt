package com.example.computerstore.screens.buttons

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.unit.dp


@Composable
fun LoginButton(
    email: String,
    password: String,
    onLogin: (String, String) -> Unit,
    onNavigateToRegister: () -> Unit,
    modifier: Modifier = Modifier,
    focusRequester: FocusRequester = remember { FocusRequester() }
){
    Button(
        onClick = { onLogin(email, password) },
        modifier = modifier
            .fillMaxWidth()
            .focusRequester(focusRequester)
    ) {
        Text(text = "Login")
    }

    Spacer(modifier = Modifier.height(16.dp))

    TextButton(
        onClick = onNavigateToRegister
    ) {
        Text(text = "Don't have an account? Register")
    }
}


@Composable
fun SignupButton(
    email: String,
    password: String,
    onSignup: (String, String) -> Unit,
    onNavigateToSignin: () -> Unit,
    modifier: Modifier = Modifier,
    focusRequester: FocusRequester = remember { FocusRequester() }
){
    Button(
        onClick = { onSignup(email, password) },
        modifier = modifier
            .fillMaxWidth()
            .focusRequester(focusRequester)
    ) {
        Text(text = "Register")
    }

    Spacer(modifier = Modifier.height(16.dp))

    TextButton(
        onClick = onNavigateToSignin
    ) {
        Text(text = "Have an account? Login")
    }
}
