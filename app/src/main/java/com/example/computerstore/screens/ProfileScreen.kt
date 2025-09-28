package com.example.computerstore.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.computerstore.data.model.Order
import com.example.computerstore.data.model.User
import com.example.computerstore.data.model.UserAddress
import com.example.computerstore.viewmodel.OrderViewModel
import com.example.computerstore.viewmodel.UserAddressViewModel
import com.example.computerstore.viewmodel.UserViewModel
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    orderViewModel: OrderViewModel = viewModel(),
    addressViewModel: UserAddressViewModel = viewModel(),
    userViewModel: UserViewModel = viewModel(),
    onLogout: () -> Unit = {}
) {
    val authUser = FirebaseAuth.getInstance().currentUser
    val user by userViewModel.currentUser.collectAsState()
    val orders by orderViewModel.orders.collectAsState()
    val addresses by addressViewModel.userAddresses.collectAsState()

    val uid = authUser?.uid
//    LaunchedEffect(uid) {
//        uid?.let {
//            userViewModel.loadUser(it)
//            orderViewModel.loadAllOrders() // hoặc loadOrdersByUser(it) nếu có
//            addressViewModel.loadAddressesByUser(it)
//        }
//    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Hồ sơ cá nhân") }) },
        bottomBar = {
            Button(
                onClick = {
                    FirebaseAuth.getInstance().signOut()
                    onLogout()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
            ) {
                Icon(Icons.Default.ExitToApp, contentDescription = null, tint = Color.White)
                Spacer(Modifier.width(8.dp))
                Text("Đăng xuất", color = Color.White)
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Thông tin người dùng
            item {
                if (user != null) {
                    ProfileInfo(user!!)
                } else {
                    Text("Đang tải thông tin người dùng...")
                }
            }

            // Địa chỉ giao hàng
            item { Text("Địa chỉ giao hàng", fontWeight = FontWeight.Bold) }
            items(addresses) { addr ->
                AddressCardProfile(addr)
            }

            // Đơn hàng
            item { Text("Đơn hàng của bạn", fontWeight = FontWeight.Bold) }
            items(orders) { order ->
                OrderCard(order)
            }
        }
    }
}

@Composable
fun ProfileInfo(user: User) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(8.dp))
            .padding(16.dp)
    ) {
        Text(user.full_name ?: user.username ?: "Người dùng", fontWeight = FontWeight.Bold)
        Text("Email: ${user.email ?: ""}")
        Text("SĐT: ${user.phone_number ?: ""}")
        Text("Giới tính: ${user.gender ?: ""}")
        user.birthday?.let { Text("Ngày sinh: $it") }
        user.registration_date?.let { Text("Ngày đăng ký: $it") }
        user.last_login?.let { Text("Đăng nhập gần nhất: $it") }
    }
}

@Composable
fun AddressCardProfile(address: UserAddress) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(8.dp))
            .padding(12.dp)
    ) {
        Text(address.recipient_name, fontWeight = FontWeight.Bold)
        Text("SĐT: ${address.phone_number}")
        Text("${address.address}, ${address.city}, ${address.province}")
    }
}

@Composable
fun OrderCard(order: Order) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(8.dp))
            .padding(12.dp)
    ) {
        Text("Mã đơn: ${order.order_id}", fontWeight = FontWeight.Bold)
        Text("Tổng tiền: ${"%,.0f".format(order.total_amount)} đ", color = Color.Red)
        Text("Trạng thái: ${order.status}")
    }
}
