package com.example.computerstore.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.computerstore.screens.buttons.BackButton
import com.example.computerstore.screens.components.NumberPad
import com.example.computerstore.screens.components.OtpInput

@Composable
fun VerificationScreen(
    onBackClick: () -> Unit = {}
) {
    var verifyCode by remember { mutableStateOf("") }
    val otpLength = 4
    val yourEmail = "Qhuy204@gmail.com"

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
            .padding(start = 18.dp, top = 42.dp, end = 18.dp, bottom = 42.dp),
    ) {
        // Back button
        BackButton(
            onClick = onBackClick,
            backgroundColor = Color(0xFFE8EAE9),
            iconTint = Color.Black,
            size = 48,
            modifier = Modifier.align(Alignment.TopStart)
        )

        // Main content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 70.dp),
        ) {
            Text(
                text = "Verify Code",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Enter the code we just sent",
                fontSize = 16.sp,
                color = Color.DarkGray,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = yourEmail,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(40.dp))

            // Hiển thị OTP
            OtpInput(
                otpLength = otpLength,
                otpValue = verifyCode,
                onOtpChange = { verifyCode = it },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(40.dp))

            Text("Didn't get OTP?", color = Color.Gray, modifier = Modifier.align(Alignment.CenterHorizontally))
            Text(
                "Resend code",
                color = Color(0xFF0066FF),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Bàn phím số
            Box(
                modifier = Modifier
                    .padding(12.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(Color(0xFFe8e9ee))
                    .align(Alignment.CenterHorizontally)
            ) {
                NumberPad(
                    onNumberClick = { digit ->
                        if (verifyCode.length < otpLength && digit.all { it.isDigit() }) {
                            verifyCode += digit
                            if (verifyCode.length == otpLength) {
                                println("OTP nhập đủ: $verifyCode")
                            }
                        }
                    },
                    onDeleteClick = {
                        if (verifyCode.isNotEmpty()) {
                            verifyCode = verifyCode.dropLast(1)
                        }
                    }
                )
            }
            Spacer(modifier = Modifier.height(40.dp))
            Button(
                onClick = { /* Handle sign-in logic */ },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF3F7DE8)
                ),
            ) {
                Text("Verify", fontSize = 16.sp)
            }
        }
    }
}
