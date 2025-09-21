package com.example.computerstore.screens.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun OtpInput(
    otpLength: Int = 4,
    otpValue: String,
    onOtpChange: (String) -> Unit,
    size: Dp = 60.dp,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        repeat(otpLength) { index ->
            Box(
                modifier = Modifier
                    .size(size)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFFE8E9EE)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = otpValue.getOrNull(index)?.toString() ?: "-",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (otpValue.getOrNull(index) != null) Color.Black else Color.Gray,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
