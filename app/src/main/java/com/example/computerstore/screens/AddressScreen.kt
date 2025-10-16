package com.example.computerstore.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.computerstore.data.model.UserAddress
import com.example.computerstore.screens.components.CustomTopBarProfile
import com.example.computerstore.viewmodel.UserAddressViewModel
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddressScreen(
    navController: NavController,
    addressViewModel: UserAddressViewModel = viewModel()
) {
    val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return
    val addresses by addressViewModel.userAddresses.collectAsState()

    LaunchedEffect(uid) {
        addressViewModel.loadAddressesByUser(uid)
    }

    Scaffold(
        topBar = {
            CustomTopBarProfile(title = "Danh sách địa chỉ", navController = navController)
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return@FloatingActionButton
                    navController.navigate("add_or_edit_address?userId=$uid")
                },
                containerColor = Color(0xFFDC2626),
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Add, "Thêm địa chỉ")
            }
        }
    ) { padding ->
        if (addresses.isEmpty()) {
            EmptyAddressState(navController)
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFF5F5F5))
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(addresses.size) { i ->
                    val addr = addresses[i]
                    AddressCard(
                        navController = navController,
                        address = addr,
                        onDelete = { addressViewModel.deleteUserAddress(addr.address_id) },
                        onSetDefault = { addressViewModel.setDefaultAddress(addr.address_id, uid) }
                    )
                }
                item { Spacer(Modifier.height(72.dp)) } // Space for FAB
            }
        }
    }
}

@Composable
fun EmptyAddressState(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                Icons.Default.LocationOn,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = Color(0xFFBDBDBD)
            )
            Text(
                "Chưa có địa chỉ nào",
                fontSize = 16.sp,
                color = Color(0xFF757575)
            )
            Button(
                onClick = {
                    val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return@Button
                    navController.navigate("add_or_edit_address?userId=$uid")
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC2626))
            ) {
                Icon(Icons.Default.Add, null, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(4.dp))
                Text("Thêm địa chỉ mới")
            }
        }
    }
}

@Composable
fun AddressCard(
    navController: NavController,
    address: UserAddress,
    onDelete: () -> Unit,
    onSetDefault: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        address.recipient_name?.let {
                            Text(
                                it,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF212121)
                            )
                        }
                        if (address.is_default == 1) {
                            Surface(
                                color = Color(0xFFFEF2F2),
                                shape = RoundedCornerShape(4.dp),
                                border = BorderStroke(1.dp, Color(0xFFDC2626))
                            ) {
                                Text(
                                    "Mặc định",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color(0xFFDC2626),
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }

                    Spacer(Modifier.height(4.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.Phone,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp),
                            tint = Color(0xFF757575)
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(
                            address.phone_number,
                            fontSize = 14.sp,
                            color = Color(0xFF757575)
                        )
                    }
                }

                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    IconButton(
                        onClick = {
                            val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return@IconButton
                            navController.navigate("add_or_edit_address?addressId=${address.address_id}&userId=$uid")
                        },
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            Icons.Default.Edit,
                            contentDescription = "Sửa",
                            tint = Color(0xFF2563EB),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    IconButton(
                        onClick = onDelete,
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            Icons.Default.Delete,
                            contentDescription = "Xóa",
                            tint = Color(0xFFDC2626),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            Divider(color = Color(0xFFE0E0E0))

            // Address Details
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                if (!address.address_type.isNullOrBlank()) {
                    Row(verticalAlignment = Alignment.Top) {
                        Icon(
                            Icons.Default.Home,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = Color(0xFF757575)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            address.address_type!!,
                            fontSize = 13.sp,
                            color = Color(0xFF757575),
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                Row(verticalAlignment = Alignment.Top) {
                    Icon(
                        Icons.Default.LocationOn,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = Color(0xFF757575)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        buildString {
                            append(address.address)
                            if (!address.district.isNullOrBlank()) append(", ${address.district}")
                            if (!address.city.isNullOrBlank()) append(", ${address.city}")
                            if (!address.province.isNullOrBlank()) append(", ${address.province}")
                        },
                        fontSize = 14.sp,
                        color = Color(0xFF424242),
                        lineHeight = 20.sp
                    )
                }
            }

            // Set Default Button
            if (address.is_default != 1) {
                Divider(color = Color(0xFFE0E0E0))

                TextButton(
                    onClick = onSetDefault,
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = Color(0xFFDC2626)
                    )
                ) {
                    Icon(
                        Icons.Default.CheckCircle,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(Modifier.width(4.dp))
                    Text("Đặt làm địa chỉ mặc định", fontSize = 14.sp)
                }
            }
        }
    }
}
