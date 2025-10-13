package com.example.computerstore.screens.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun BottomBarShopee(
    onChat: () -> Unit,
    onAddToCart: () -> Unit,
    onBuyNow: () -> Unit
) {
    Row(
        Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onChat, modifier = Modifier.weight(0.5f)) {
            Icon(Icons.Default.Chat, contentDescription = "Chat ngay", tint = Color.Red)
        }
        IconButton(onClick = onAddToCart, modifier = Modifier.weight(0.5f)) {
            Icon(Icons.Default.ShoppingCart, contentDescription = "Thêm giỏ hàng", tint = Color.Red)
        }
        Button(
            onClick = onBuyNow,
            modifier = Modifier.weight(2f),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
        ) {
            Text("Mua ngay", fontWeight = FontWeight.Bold, color = Color.White)
        }
    }
}