package com.example.computerstore.screens.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun CustomTopBarProfile(
    title: String,
    navController: NavController,
    backgroundColor: Color = Color(0xFFDC2626),
    contentColor: Color = Color.White
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .background(backgroundColor)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically // căn giữa theo chiều dọc
    ) {
        // Icon back bên trái
        IconButton(onClick = { navController.popBackStack() }) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Quay lại",
                tint = contentColor
            )
        }

        // Text tiêu đề căn theo chiều dọc, không căn giữa ngang
        Text(
            text = title,
            color = contentColor,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        )
    }
}
