package com.example.computerstore.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.computerstore.data.model.Order
import com.example.computerstore.data.model.User
import com.example.computerstore.data.model.UserAddress
import com.example.computerstore.viewmodel.OrderViewModel
import com.example.computerstore.viewmodel.UserAddressViewModel
import com.example.computerstore.viewmodel.UserViewModel
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.*

// Light Theme Color Scheme
private val RedPrimary = Color(0xFFDC2626)
private val RedDark = Color(0xFF991B1B)
private val WhiteBg = Color(0xFFFFFFFF)
private val WhiteCard = Color(0xFFF9FAFB)
private val GrayLight = Color(0xFFE5E7EB)
private val BlackText = Color(0xFF1F2937)
private val GrayText = Color(0xFF6B7280)

// Hằng số TAG cho Logcat
private const val TAG = "ProfileScreen"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    orderViewModel: OrderViewModel = viewModel(),
    addressViewModel: UserAddressViewModel = viewModel(),
    userViewModel: UserViewModel = viewModel(),
    onLogout: () -> Unit = {},
    // Thêm callback điều hướng đến màn hình chỉnh sửa hồ sơ
    onNavigateToEditProfile: () -> Unit = {}
) {
    val authUser = FirebaseAuth.getInstance().currentUser
    val uid = authUser?.uid

    val user by userViewModel.currentUser.collectAsState()
    val orders by orderViewModel.orders.collectAsState()
    val addresses by addressViewModel.userAddresses.collectAsState()

    // 1. Logic tải dữ liệu và Log trạng thái Auth/UID
    // Đã loại bỏ DisposableEffect(Unit) và tích hợp log vào LaunchedEffect(uid) để phản ứng với thay đổi UID
    LaunchedEffect(uid) {
        if (uid != null) {
            // Log khi UID đã có và bắt đầu tải dữ liệu
            Log.d("Profile", "Auth Status: UID $uid is available. Starting data load.")
            userViewModel.loadUser(uid)
            orderViewModel.loadOrdersByUser(uid)
            addressViewModel.loadAddressesByUser(uid)
        } else {
            // Log khi UID là null, có thể do chưa đăng nhập hoặc trạng thái đang chờ
            Log.w("Profile", "Auth Status: UID is null. Cannot load user data.")
        }
    }

    // 2. Log thông tin người dùng mỗi khi dữ liệu được tải hoặc cập nhật
    LaunchedEffect(user) {
        if (user != null) {
            Log.d("Profile", "--- User Data Loaded ---")
            Log.d("Profile", "User ID: ${user!!.user_id}")
            Log.d("Profile", "Full Name: ${user!!.full_name}")
            Log.d("Profile", "Email: ${user!!.email}")
            Log.d("Profile", "Phone Number: ${user!!.phone_number}")
            Log.d("Profile", "Gender: ${user!!.gender}")
            Log.d("Profile", "Birthday: ${user!!.birthday}")
            Log.d("Profile", "Registration Date: ${user!!.registration_date}")
            Log.d("Profile", "--------------------------")
        } else if (uid != null) {
            Log.d("Profile", "User Load Status: Waiting for user data or data is null for UID: $uid")
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(WhiteBg)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(top = 80.dp, bottom = 100.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header với avatar và tên
            item {
                user?.let {
                    ProfileHeader(it)
                } ?: run {
                    DefaultProfilePlaceholder()
                }
            }

            // --- Nội dung chính chỉ hiển thị khi có dữ liệu User ---
            if (user != null) {
                // Stats Cards
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        StatCard(
                            icon = Icons.Default.ShoppingBag,
                            value = "${orders.size}",
                            label = "Đơn hàng",
                            modifier = Modifier.weight(1f)
                        )
                        StatCard(
                            icon = Icons.Default.LocationOn,
                            value = "${addresses.size}",
                            label = "Địa chỉ",
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                // Thông tin cá nhân
                item {
                    SectionHeaderWithAction(
                        title = "Thông tin cá nhân",
                        icon = Icons.Default.Person,
                        actionIcon = Icons.Default.Edit,
                        onActionClick = onNavigateToEditProfile // Gán callback điều hướng
                    )
                }

                item {
                    user?.let { ProfileInfo(it) }
                }

                // Địa chỉ giao hàng
                item {
                    SectionHeaderWithAction(
                        title = "Địa chỉ giao hàng",
                        icon = Icons.Default.LocationOn,
                        actionIcon = Icons.Default.Add,
                        onActionClick = {
                            // TODO: Triển khai navigation đến AddNewAddressScreen (Chức năng thêm địa chỉ)
                            Log.d(TAG, "Navigate to Add New Address Screen clicked.")
                        }
                    )
                }

                if (addresses.isEmpty()) {
                    item {
                        EmptyState(
                            icon = Icons.Default.LocationOn,
                            message = "Chưa có địa chỉ nào được lưu"
                        )
                    }
                } else {
                    items(addresses) { addr -> AddressCardProfile(addr) }
                }

                // Đơn hàng
                item {
                    SectionHeader(title = "Lịch sử đơn hàng", icon = Icons.Default.ShoppingBag)
                }

                if (orders.isEmpty()) {
                    item {
                        EmptyState(
                            icon = Icons.Default.ShoppingBag,
                            message = "Bạn chưa có đơn hàng nào"
                        )
                    }
                } else {
                    items(orders.sortedByDescending { it.order_date }) { order ->
                        OrderCard(order)
                    }
                }
            } else {
                // Hiển thị loading cho các phần còn lại nếu user vẫn là null
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentAlignment = Alignment.TopCenter
                    ) {
                        CircularProgressIndicator(color = RedPrimary)
                    }
                }
            }
        }

        // Floating Logout Button
        Button(
            onClick = {
                FirebaseAuth.getInstance().signOut()
                onLogout()
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(16.dp)
                .height(56.dp)
                .shadow(8.dp, RoundedCornerShape(28.dp)),
            colors = ButtonDefaults.buttonColors(
                containerColor = RedPrimary
            ),
            shape = RoundedCornerShape(28.dp)
        ) {
            Icon(Icons.Default.ExitToApp, contentDescription = null, tint = WhiteBg)
            Spacer(Modifier.width(8.dp))
            Text(
                "Đăng xuất",
                color = WhiteBg,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}


@Composable
fun DefaultProfilePlaceholder() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(RedDark, RedPrimary)
                ),
                shape = RoundedCornerShape(20.dp)
            )
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(Color.White)
                .border(3.dp, Color.White, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Avatar Mặc định",
                modifier = Modifier.size(70.dp),
                tint = RedDark
            )
        }

        Spacer(Modifier.height(16.dp))

        CircularProgressIndicator(
            modifier = Modifier.size(24.dp),
            color = WhiteBg,
            strokeWidth = 2.dp
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = "Đang tải thông tin...",
            fontSize = 14.sp,
            color = Color.White.copy(alpha = 0.7f)
        )
    }
}

@Composable
fun ProfileHeader(user: User) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(RedDark, RedPrimary)
                ),
                shape = RoundedCornerShape(20.dp)
            )
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(Color.White)
                .border(3.dp, Color.White, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Avatar Mặc định",
                modifier = Modifier.size(70.dp),
                tint = RedDark
            )
        }

        Spacer(Modifier.height(16.dp))

        Text(
            text = user.full_name ?: user.username ?: "Người dùng",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = WhiteBg
        )

        user.email?.let {
            Text(
                text = it,
                fontSize = 14.sp,
                color = Color.White.copy(alpha = 0.7f),
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

@Composable
fun SectionHeader(title: String, icon: ImageVector) {
    // Phiên bản cũ, giữ lại nếu cần
    SectionHeaderWithAction(title, icon, null, null)
}

@Composable
fun SectionHeaderWithAction(title: String, icon: ImageVector, actionIcon: ImageVector? = null, onActionClick: (() -> Unit)? = null) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = RedPrimary,
            modifier = Modifier.size(24.dp)
        )
        Spacer(Modifier.width(8.dp))
        Text(
            text = title,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = BlackText,
            modifier = Modifier.weight(1f) // Đẩy nút action về bên phải
        )

        // Nút hành động (Edit/Add)
        actionIcon?.let {
            IconButton(onClick = onActionClick ?: {}) {
                Icon(
                    imageVector = it,
                    contentDescription = "Thao tác",
                    tint = RedPrimary // Đổi màu nút action thành RedPrimary
                )
            }
        }
    }
}


@Composable
fun ProfileInfo(user: User) {
    val formatter = remember { SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(WhiteCard, RoundedCornerShape(16.dp))
            .border(1.dp, GrayLight, RoundedCornerShape(16.dp))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Phone Number
        val phoneNumberValue = user.phone_number.let {
            if (it.isNullOrEmpty()) "Chưa cập nhật" else it
        }
        InfoRow(Icons.Default.Phone, "Số điện thoại", phoneNumberValue)

        // Gender
        val genderValue = user.gender.let {
            if (it.isNullOrEmpty()) "Chưa cập nhật" else it
        }
        InfoRow(Icons.Default.Person, "Giới tính", genderValue)

        // Birthday
        val birthdayValue = user.birthday?.let {
            formatter.format(it.toDate())
        } ?: "Chưa cập nhật"
        InfoRow(Icons.Default.Cake, "Ngày sinh", birthdayValue)

        HorizontalDivider(color = GrayLight.copy(alpha = 0.5f), thickness = 1.dp)

        // Registration Date (Có thể null từ Firestore)
        val registrationDateValue = user.registration_date?.let {
            formatter.format(it.toDate())
        } ?: "N/A (Dữ liệu hệ thống)"
        InfoRow(Icons.Default.CalendarToday, "Ngày đăng ký", registrationDateValue)

        // Last Login (Có thể null từ Firestore)
        val lastLoginValue = user.last_login?.let {
            formatter.format(it.toDate())
        } ?: "N/A (Dữ liệu hệ thống)"
        InfoRow(Icons.Default.Login, "Đăng nhập gần nhất", lastLoginValue)
    }
}

@Composable
fun InfoRow(icon: ImageVector, label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = GrayText,
            modifier = Modifier.size(20.dp)
        )
        Spacer(Modifier.width(12.dp))
        Column {
            Text(
                text = label,
                fontSize = 12.sp,
                color = GrayText
            )
            Text(
                text = value,
                fontSize = 14.sp,
                color = if (value == "Chưa cập nhật") GrayText else BlackText, // Giảm độ đậm nếu là placeholder
                fontWeight = if (value == "Chưa cập nhật") FontWeight.Normal else FontWeight.Medium
            )
        }
    }
}


@Composable
fun StatCard(
    icon: ImageVector,
    value: String,
    label: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .background(WhiteCard, RoundedCornerShape(16.dp))
            .border(1.dp, GrayLight, RoundedCornerShape(16.dp))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = RedPrimary,
            modifier = Modifier.size(32.dp)
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = value,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = BlackText
        )
        Text(
            text = label,
            fontSize = 12.sp,
            color = GrayText
        )
    }
}

@Composable
fun AddressCardProfile(address: UserAddress) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(WhiteCard, RoundedCornerShape(16.dp))
            .border(
                width = if (address.is_default == 1) 2.dp else 1.dp,
                color = if (address.is_default == 1) RedPrimary else GrayLight,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = address.recipient_name,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = BlackText
            )
            if (address.is_default == 1) {
                Surface(
                    color = RedPrimary.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "Mặc định",
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = RedPrimary
                    )
                }
            }
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.Phone,
                contentDescription = null,
                tint = GrayText,
                modifier = Modifier.size(16.dp)
            )
            Spacer(Modifier.width(6.dp))
            Text(
                text = address.phone_number,
                fontSize = 14.sp,
                color = GrayText
            )
        }

        Row(verticalAlignment = Alignment.Top) {
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = null,
                tint = GrayText,
                modifier = Modifier.size(16.dp).padding(top = 2.dp)
            )
            Spacer(Modifier.width(6.dp))
            Text(
                text = "${address.address}, ${address.city}, ${address.province}",
                fontSize = 14.sp,
                color = GrayText,
                lineHeight = 20.sp
            )
        }
    }
}

@Composable
fun OrderCard(order: Order) {
    val statusColor = when (order.status.lowercase()) {
        "đã giao", "hoàn thành" -> Color(0xFF10B981)
        "đang giao" -> Color(0xFFF59E0B)
        "đã hủy" -> Color(0xFFEF4444)
        else -> GrayText
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(WhiteCard, RoundedCornerShape(16.dp))
            .border(1.dp, GrayLight, RoundedCornerShape(16.dp))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Mã đơn: ${order.order_id}",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = BlackText
            )
            Surface(
                color = statusColor.copy(alpha = 0.2f),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = order.status,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = statusColor
                )
            }
        }

        HorizontalDivider(color = GrayLight, thickness = 1.dp)

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Tổng tiền",
                fontSize = 14.sp,
                color = GrayText
            )
            Text(
                text = "${"%,.0f".format(order.total_amount)} đ",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = RedPrimary
            )
        }

        Row(verticalAlignment = Alignment.Top) {
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = null,
                tint = GrayText,
                modifier = Modifier.size(16.dp).padding(top = 2.dp)
            )
            Spacer(Modifier.width(6.dp))
            Text(
                text = order.shipping_address,
                fontSize = 12.sp,
                color = GrayText,
                lineHeight = 18.sp
            )
        }
    }
}

@Composable
fun EmptyState(icon: ImageVector, message: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(WhiteCard, RoundedCornerShape(16.dp))
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = GrayLight,
            modifier = Modifier.size(48.dp)
        )
        Spacer(Modifier.height(12.dp))
        Text(
            text = message,
            fontSize = 14.sp,
            color = GrayText,
            fontWeight = FontWeight.Medium
        )
    }
}
