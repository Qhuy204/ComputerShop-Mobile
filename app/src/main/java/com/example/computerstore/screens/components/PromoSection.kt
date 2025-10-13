package com.example.computerstore.screens.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PromoSection() {
    Column(
        Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(Color.White)
                .border(BorderStroke(1.dp, Color(0xFFFFC0C0)))
                .padding(12.dp)
        ) {
            Text(
                text = "🎁 Quà tặng khuyến mãi",
                color = Color.Red,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            Spacer(Modifier.height(8.dp))
            Text("① Tặng ngay 1 voucher giảm ngay 50% tối đa 100k vào lần mua tiếp theo")
        }

        Spacer(Modifier.height(12.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(Color(0xFFFFF8F0))
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("🎓 HSSV giảm ngay 30% tối đa 100k", color = Color.Black, fontWeight = FontWeight.Medium)
                Spacer(Modifier.width(12.dp))
            }
            Button(
                onClick = { /* TODO: chi tiết ưu đãi */ },
                colors = ButtonDefaults.buttonColors(Color(0xFFFF6F00)),
                contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
                shape = RoundedCornerShape(50.dp)
            ) {
                Text("Chi tiết", color = Color.White, fontSize = 13.sp)
            }
        }

        Spacer(Modifier.height(12.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = { /* TODO: Mua ngay */ },
                colors = ButtonDefaults.buttonColors(Color(0xFFE53935)),
                modifier = Modifier
                    .weight(1f)
                    .height(80.dp),
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(0.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        "MUA NGAY",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                    Text(
                        "Giao tận nơi / Nhận tại cửa hàng",
                        color = Color.White,
                        fontSize = 10.sp
                    )
                }
            }

            OutlinedButton(
                onClick = { /* TODO: Tư vấn ngay */ },
                border = BorderStroke(1.dp, Color(0xFF0066FF)),
                modifier = Modifier
                    .weight(1f)
                    .height(80.dp),
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(0.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        "💬 TƯ VẤN NGAY",
                        color = Color(0xFF0066FF),
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                    Text(
                        "Chọn laptop \"hợp gu\" trong 1 phút!",
                        color = Color(0xFF0066FF),
                        fontSize = 10.sp
                    )
                }
            }
        }


        Spacer(Modifier.height(12.dp))

        // --- Chính sách ---
        Column(Modifier.padding(horizontal = 8.dp)) {
            Text("✔ Bảo hành chính hãng 24 tháng.", color = Color.Black)
            Text("✔ Hỗ trợ đổi mới trong 7 ngày.", color = Color.Black)
            Text("✔ Miễn phí giao hàng toàn quốc.", color = Color.Black)
        }
    }
}
