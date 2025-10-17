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
import com.example.computerstore.R
import com.example.computerstore.screens.components.CustomTopBar
import com.example.computerstore.screens.components.CustomTopBarProfile
import com.example.computerstore.screens.components.OrderCard
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
            CustomTopBar(
                title = "Danh sách đơn hàng",
                iconRes = R.drawable.leftarrow,
                onBackClick = { navController.popBackStack() }
            )
        },
    ) { padding ->

        when {
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
                        OrderCard(
                            order = order,
                            onClick = { navController.navigate("order_detail/${order.order_id}") }
                        )

                    }
                }
            }
        }
    }
}
