package com.example.computerstore.screens.components

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

@Composable
fun Footer() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
    ) {
        ExpandableFooterSection(
            title = "VỀ GEARVN",
            items = listOf("Giới thiệu", "Tuyển dụng", "Liên hệ")
        )
        ExpandableFooterSection(
            title = "CHÍNH SÁCH",
            items = listOf("Chính sách bảo hành", "Chính sách giao hàng", "Chính sách bảo mật")
        )
        ExpandableFooterSection(
            title = "THÔNG TIN",
            items = listOf(
                "Hệ thống cửa hàng",
                "Hướng dẫn mua hàng",
                "Hướng dẫn thanh toán",
                "Hướng dẫn trả góp",
                "Tra cứu địa chỉ bảo hành",
                "Build PC"
            )
        )
        ExpandableFooterSection(
            title = "TỔNG ĐÀI HỖ TRỢ (8:00 - 21:00)",
            items = listOf(
                "Mua hàng: 1900.5301",
                "Bảo hành: 1900.5325",
                "Khiếu nại: 1800.6173",
                "Email: cskh@gearvn.com"
            ),
            isContact = true
        )
        ExpandableFooterSection(
            title = "ĐƠN VỊ VẬN CHUYỂN",
            items = listOf("GHN Express", "EMS Việt Nam", "GVN Logistic", "Phương Trang", "Bưu Điện")
        )
        ExpandableFooterSection(
            title = "CÁCH THỨC THANH TOÁN",
            items = listOf("Internet Banking", "Visa", "MasterCard", "JCB", "ZaloPay", "Tiền mặt", "Trả góp 0%")
        )
        ExpandableFooterSectionWithIcons(
            title = "KẾT NỐI VỚI CHÚNG TÔI",
            icons = listOf(
                "https://file.hstatic.net/200000636033/file/facebook_1_0e31d70174824ea184c759534430deec.png", // Facebook
                "https://file.hstatic.net/200000722513/file/tiktok-logo_fe1e020f470a4d679064cec31bc676e4.png", // TikTok
                "https://file.hstatic.net/200000636033/file/youtube_1_d8de1f41ca614424aca55aa0c2791684.png", // YouTube
                "https://file.hstatic.net/200000722513/file/icon_zalo__1__f5d6f273786c4db4a3157f494019ab1e.png", // Zalo
                "https://theme.hstatic.net/200000722513/1001090675/14/logo-bct.png?v=9844", // Bộ Công Thương
            )
        )
    }
}

@Composable
fun ExpandableFooterSection(
    title: String,
    items: List<String>,
    isContact: Boolean = false
) {
    var expanded by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = !expanded }
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(title, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Icon(
                imageVector = if (expanded) Icons.Default.Remove else Icons.Default.Add,
                contentDescription = null,
                tint = Color.Gray
            )
        }

        AnimatedVisibility(visible = expanded) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 24.dp, bottom = 8.dp)
            ) {
                items.forEach { item ->
                    Text(
                        text = item,
                        fontSize = 13.sp,
                        color = Color.DarkGray,
                        modifier = Modifier
                            .padding(vertical = 6.dp)
                            .clickable {
                                if (isContact) {
                                    handleContactClick(context, item)
                                }
                            }
                    )
                }
            }
        }

        Divider(color = Color.LightGray, thickness = 0.5.dp)
    }
}

@Composable
fun ExpandableFooterSectionWithIcons(
    title: String,
    icons: List<String>
) {
    var expanded by remember { mutableStateOf(false) }

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = !expanded }
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(title, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Icon(
                imageVector = if (expanded) Icons.Default.Remove else Icons.Default.Add,
                contentDescription = null,
                tint = Color.Gray
            )
        }

        AnimatedVisibility(visible = expanded) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                icons.forEach { url ->
                    AsyncImage(
                        model = url,
                        contentDescription = null,
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color.White)
                    )
                }
            }
        }

        Divider(color = Color.LightGray, thickness = 0.5.dp)
    }
}

fun handleContactClick(context: Context, item: String) {
    when {
        item.contains("1900.5301") -> openDialer(context, "19005301")
        item.contains("1900.5325") -> openDialer(context, "19005325")
        item.contains("1800.6173") -> openDialer(context, "18006173")
        item.contains("cskh@gearvn.com") -> openEmail(context, "cskh@gearvn.com")
    }
}

fun openDialer(context: Context, phoneNumber: String) {
    val intent = Intent(Intent.ACTION_DIAL).apply {
        data = Uri.parse("tel:$phoneNumber")
    }
    context.startActivity(intent)
}

fun openEmail(context: Context, email: String) {
    val intent = Intent(Intent.ACTION_SENDTO).apply {
        data = Uri.parse("mailto:$email")
    }
    context.startActivity(intent)
}
