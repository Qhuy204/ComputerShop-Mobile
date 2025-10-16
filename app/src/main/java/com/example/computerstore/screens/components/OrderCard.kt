package com.example.computerstore.screens.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.computerstore.data.model.Order
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun OrderCard(
    order: Order,
    onClick: (() -> Unit)? = null
) {
    val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    val formattedDate = order.order_date?.let { dateFormat.format(it.toDate()) } ?: "N/A"
    val totalAmount = order.total_amount ?: 0.0
    val statusColor = when (order.status?.lowercase()) {
        "pending" -> Color(0xFFF59E0B)
        "completed" -> Color(0xFF10B981)
        "cancelled" -> Color(0xFFEF4444)
        else -> Color(0xFF6B7280)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(12.dp))
            .border(1.dp, Color(0xFFE5E7EB), RoundedCornerShape(12.dp))
            .clickable(enabled = onClick != null) { onClick?.invoke() }
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Mã đơn: ${order.order_id ?: "Không có"}",
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
                color = Color(0xFF111827)
            )
            Text(
                text = order.status ?: "Không xác định",
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                color = statusColor
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "Ngày đặt: $formattedDate",
            fontSize = 14.sp,
            color = Color(0xFF6B7280),
            overflow = TextOverflow.Ellipsis
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "Tổng tiền: %,d₫".format(totalAmount.toInt()),
            fontSize = 15.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF111827)
        )
    }
}
