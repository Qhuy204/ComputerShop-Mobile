//package com.example.computerstore.screens
//
//import androidx.compose.foundation.background
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.*
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.unit.dp
//import androidx.lifecycle.viewmodel.compose.viewModel
//import androidx.navigation.NavController
//import com.example.computerstore.data.model.UserAddress
//import com.example.computerstore.viewmodel.UserAddressViewModel
//import com.google.firebase.auth.FirebaseAuth
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun ManageAddressScreen(
//    navController: NavController,
//    addressViewModel: UserAddressViewModel = viewModel()
//) {
//    val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return
//    val addresses by addressViewModel.userAddresses.collectAsState()
//
//    LaunchedEffect(uid) {
//        addressViewModel.loadAddressesByUser(uid)
//    }
//
//    Scaffold(
//        topBar = {
//            TopAppBar(
//                title = { Text("Quản lý địa chỉ") },
//                navigationIcon = {
//                    IconButton(onClick = { navController.popBackStack() }) {
//                        Icon(Icons.Default.ArrowBack, contentDescription = null)
//                    }
//                },
//                actions = {
//                    IconButton(onClick = {
//                        addressViewModel.addUserAddress(
//                            UserAddress(
//                                address_id = "",
//                                user_id = uid,
//                                recipient_name = "Người nhận mới",
//                                phone_number = "0000000000",
//                                address = "Địa chỉ mới",
//                                city = "Thành phố",
//                                province = "Tỉnh",
//                                is_default = 0
//                            )
//                        )
//                    }) {
//                        Icon(Icons.Default.Add, contentDescription = "Thêm địa chỉ")
//                    }
//                },
//                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFFDC2626), titleContentColor = Color.White)
//            )
//        }
//    ) { padding ->
//        LazyColumn(
//            modifier = Modifier
//                .fillMaxSize()
//                .background(Color(0xFFF9FAFB))
//                .padding(padding)
//                .padding(16.dp),
//            verticalArrangement = Arrangement.spacedBy(12.dp)
//        ) {
//            items(addresses.size) { i ->
//                val addr = addresses[i]
//                Surface(
//                    color = Color.White,
//                    shadowElevation = 2.dp,
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .clickable {
//                            addressViewModel.setDefaultAddress(addr.address_id, uid)
//                        }
//                ) {
//                    Column(Modifier.padding(12.dp)) {
//                        addr.recipient_name?.let { Text(it, fontWeight = FontWeight.Bold) }
//                        addr.phone_number?.let { Text(it, color = Color.Gray) }
//                        Text("${addr.address}, ${addr.city}, ${addr.province}")
//                        if (addr.is_default == 1) {
//                            Text("Địa chỉ mặc định", color = Color(0xFFDC2626), style = MaterialTheme.typography.labelSmall)
//                        }
//                    }
//                }
//            }
//        }
//    }
//}
