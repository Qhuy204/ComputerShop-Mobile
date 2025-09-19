package com.example.computerstore.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.computerstore.R

@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    iconRes: Int,
    focusManager: FocusManager,
    isPasswordField: Boolean = false,   // thêm để phân biệt password
    isLastField: Boolean = false,
    modifier: Modifier = Modifier
) {
    var passwordVisible by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
        placeholder = {
            Text(
                text = placeholder,
                color = Color.Gray,
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal
            )
        },
        leadingIcon = {
            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = placeholder
            )
        },
        trailingIcon = {
            if (isPasswordField) {
                val visibilityIcon =
                    if (passwordVisible) R.drawable.ic_eye_open else R.drawable.ic_eye_closed
                Icon(
                    painter = painterResource(id = visibilityIcon),
                    contentDescription = if (passwordVisible) "Hide password" else "Show password",
                    modifier = Modifier.clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
                        passwordVisible = !passwordVisible
                    }
                )
            }
        },
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        textStyle = TextStyle(fontSize = 14.sp),
        visualTransformation = if (isPasswordField && !passwordVisible) {
            PasswordVisualTransformation()
        } else {
            VisualTransformation.None
        },
        keyboardOptions = KeyboardOptions(
            imeAction = if (isLastField) ImeAction.Done else ImeAction.Next
        ),
        keyboardActions = KeyboardActions(
            onNext = { focusManager.moveFocus(FocusDirection.Down) },
            onDone = {
                if (isLastField) {
                    focusManager.clearFocus()
                }
            }
        ),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color(0xFFEEEEEE),
            unfocusedBorderColor = Color(0xFFEEEEEE),
            disabledBorderColor = Color(0xFFEEEEEE),
            errorBorderColor = Color(0xFFEEEEEE),
            focusedContainerColor = Color(0xFFF6F8FE),
            unfocusedContainerColor = Color(0xFFF6F8FE),
            disabledContainerColor = Color(0xFFF6F8FE)
        )
    )
}
