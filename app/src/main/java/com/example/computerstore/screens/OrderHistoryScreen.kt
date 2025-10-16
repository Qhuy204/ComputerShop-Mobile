package com.example.computerstore.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.computerstore.viewmodel.OrderViewModel
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderHistoryScreen(
    navController: NavController,
    orderViewModel: OrderViewModel = viewModel()
) {
    val uid = FirebaseAuth.getInstance().currentUser?.uid
    val orders by orderViewModel.orders.collectAsState()
    val isLoading by orderViewModel.isLoading.collectAsState()

    LaunchedEffect(uid) {
        if (!uid.isNullOrEmpty()) {
            orderViewModel.loadOrdersByUser(uid)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Lịch sử đơn hàng") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Quay lại",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFDC2626),
                    titleContentColor = Color.White
                )
            )
        }
    ) { padding ->

        when {
            // ✅ Hiển thị nếu chưa đăng nhập
            uid == null -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFFF9FAFB)),
                    contentAlignment = Alignment.Center
                ) {
                    EmptyState(Icons.Default.ShoppingBag, "Vui lòng đăng nhập để xem đơn hàng của bạn")
                }
            }

            // ✅ Trạng thái loading
            isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFFF9FAFB)),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color(0xFFDC2626))
                }
            }

            // ✅ Không có đơn hàng
            orders.isEmpty() -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFFF9FAFB)),
                    contentAlignment = Alignment.Center
                ) {
                    EmptyState(Icons.Default.ShoppingBag, "Bạn chưa có đơn hàng nào")
                }
            }

            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFFF9FAFB))
                        .padding(padding)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(orders.sortedByDescending { it.order_date }) { order ->
                        OrderCard(order)
                    }
                }
            }
        }
    }
}
