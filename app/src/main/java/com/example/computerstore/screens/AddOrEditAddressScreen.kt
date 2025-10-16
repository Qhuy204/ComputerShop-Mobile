package com.example.computerstore.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.computerstore.data.model.UserAddress
import com.example.computerstore.screens.components.CustomTopBarProfile
import com.example.computerstore.viewmodel.UserAddressViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.net.URL

data class Province(val name: String, val code: Int)
data class District(val name: String, val code: Int, val province_code: Int)
data class Ward(val name: String, val code: Int, val district_code: Int)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddOrEditAddressScreen(
    navController: NavController,
    userId: String,
    addressId: String? = null,
    addressViewModel: UserAddressViewModel = viewModel(),
    isEdit: Boolean = false
) {
    val context = LocalContext.current

    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var addressDetail by remember { mutableStateOf("") }
    var addressType by remember { mutableStateOf("Nhà riêng") }

    var provinces by remember { mutableStateOf<List<Province>>(emptyList()) }
    var districts by remember { mutableStateOf<List<District>>(emptyList()) }
    var wards by remember { mutableStateOf<List<Ward>>(emptyList()) }

    var selectedProvince by remember { mutableStateOf<Province?>(null) }
    var selectedDistrict by remember { mutableStateOf<District?>(null) }
    var selectedWard by remember { mutableStateOf<Ward?>(null) }

    var expandedProvince by remember { mutableStateOf(false) }
    var expandedDistrict by remember { mutableStateOf(false) }
    var expandedWard by remember { mutableStateOf(false) }

    // Theme colors - Light Mode
    val primaryRed = Color(0xFFD32F2F)
    val lightBg = Color(0xFFFFFFFF)
    val cardBg = Color(0xFFF8F8F8)
    val textPrimary = Color(0xFF212121)
    val textSecondary = Color.Black
    val accentGlow = Color(0xFFFF5252)

    // --- Load tỉnh ---
    LaunchedEffect(Unit) {
        try {
            val json = withContext(Dispatchers.IO) {
                URL("https://provinces.open-api.vn/api/v1/p/").readText()
            }
            val array = JSONArray(json)
            provinces = (0 until array.length()).map {
                val obj = array.getJSONObject(it)
                Province(obj.getString("name"), obj.getInt("code"))
            }.sortedBy { it.name }
            Log.d("AddressScreen", "Loaded ${provinces.size} provinces")
        } catch (e: Exception) {
            Toast.makeText(context, "Lỗi tải danh sách tỉnh: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    // --- Load quận/huyện ---
    LaunchedEffect(selectedProvince) {
        selectedProvince?.let { prov ->
            try {
                val json = withContext(Dispatchers.IO) {
                    URL("https://provinces.open-api.vn/api/v1/p/${prov.code}?depth=2").readText()
                }
                val obj = JSONObject(json)
                val array = obj.getJSONArray("districts")
                districts = (0 until array.length()).map { i ->
                    val d = array.getJSONObject(i)
                    District(d.getString("name"), d.getInt("code"), d.getInt("province_code"))
                }.sortedBy { it.name }
                Log.d("AddressScreen", "Loaded ${districts.size} districts for ${prov.name}")
            } catch (e: Exception) {
                Toast.makeText(context, "Lỗi tải danh sách quận/huyện: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // --- Load phường/xã ---
    LaunchedEffect(selectedDistrict) {
        selectedDistrict?.let { dist ->
            try {
                val json = withContext(Dispatchers.IO) {
                    URL("https://provinces.open-api.vn/api/v1/d/${dist.code}?depth=2").readText()
                }
                val obj = JSONObject(json)
                val array = obj.getJSONArray("wards")
                wards = (0 until array.length()).map { i ->
                    val w = array.getJSONObject(i)
                    Ward(w.getString("name"), w.getInt("code"), w.getInt("district_code"))
                }.sortedBy { it.name }
                Log.d("AddressScreen", "Loaded ${wards.size} wards for ${dist.name}")
            } catch (e: Exception) {
                Toast.makeText(context, "Lỗi tải danh sách phường/xã: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    Scaffold(
        topBar = {
            CustomTopBarProfile(
                title = if (isEdit) "Chỉnh sửa địa chỉ" else "Địa chỉ mới",
                navController = navController
            )
        },
        containerColor = lightBg
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            // Modern Section Label
            @Composable
            fun SectionLabel(text: String) {
                Text(
                    text = text.uppercase(),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = primaryRed,
                    letterSpacing = 1.2.sp
                )
            }

            // Modern Input Field
            @Composable
            fun ModernTextField(
                value: String,
                onValueChange: (String) -> Unit,
                label: String,
                placeholder: String,
                keyboardType: KeyboardType = KeyboardType.Text,
                maxLength: Int? = null,
                minLines: Int = 1
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    SectionLabel(label)
                    OutlinedTextField(
                        value = value,
                        onValueChange = {
                            if (maxLength == null || it.length <= maxLength) {
                                onValueChange(it)
                            }
                        },
                        placeholder = {
                            Text(placeholder, color = Color(0xFF9E9E9E), fontSize = 15.sp)
                        },
                        minLines = minLines,
                        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = textPrimary,
                            unfocusedTextColor = textPrimary,
                            focusedBorderColor = primaryRed,
                            unfocusedBorderColor = Color(0xFFE0E0E0),
                            focusedContainerColor = cardBg,
                            unfocusedContainerColor = lightBg,
                            cursorColor = primaryRed
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(
                                width = 1.dp,
                                brush = if (value.isNotEmpty()) {
                                    Brush.horizontalGradient(
                                        listOf(primaryRed.copy(alpha = 0.3f), accentGlow.copy(alpha = 0.3f))
                                    )
                                } else Brush.linearGradient(listOf(Color.Transparent, Color.Transparent)),
                                shape = RoundedCornerShape(12.dp)
                            )
                    )
                }
            }

            // Modern Dropdown
            @Composable
            fun ModernDropdown(
                value: String,
                label: String,
                placeholder: String,
                expanded: Boolean,
                onExpandedChange: (Boolean) -> Unit,
                items: List<String>,
                onItemSelected: (String) -> Unit
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    SectionLabel(label)
                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { onExpandedChange(it) }
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (expanded) cardBg else lightBg)
                                .border(
                                    width = 1.5.dp,
                                    color = if (expanded) primaryRed else Color(0xFFE0E0E0),
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .clickable { onExpandedChange(!expanded) }
                                .menuAnchor()
                                .padding(horizontal = 16.dp, vertical = 18.dp)
                        ) {
                            Text(
                                text = value.ifEmpty { placeholder },
                                color = if (value.isEmpty()) Color(0xFF9E9E9E) else textPrimary,
                                fontSize = 15.sp
                            )
                            Icon(
                                imageVector = Icons.Default.KeyboardArrowDown,
                                contentDescription = null,
                                tint = if (expanded) primaryRed else Color(0xFF757575),
                                modifier = Modifier.align(Alignment.CenterEnd)
                            )
                        }

                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { onExpandedChange(false) },
                            modifier = Modifier.background(lightBg)
                        ) {
                            items.forEach { item ->
                                DropdownMenuItem(
                                    text = {
                                        Text(item, color = textPrimary, fontSize = 14.sp)
                                    },
                                    onClick = {
                                        onItemSelected(item)
                                        onExpandedChange(false)
                                    },
                                    colors = MenuDefaults.itemColors(
                                        textColor = textPrimary
                                    )
                                )
                            }
                        }
                    }
                }
            }

            // Header với gradient accent
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        Brush.horizontalGradient(
                            listOf(primaryRed.copy(alpha = 0.08f), accentGlow.copy(alpha = 0.05f))
                        )
                    )
                    .border(
                        width = 1.dp,
                        color = primaryRed.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(16.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = if (isEdit) "Cập nhật thông tin" else "Thêm địa chỉ giao hàng",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = textPrimary
                    )
                    Text(
                        text = "Điền đầy đủ thông tin để đơn hàng được giao nhanh chóng",
                        fontSize = 13.sp,
                        color = Color(0xFF616161)
                    )
                }
            }

            // --- Họ tên ---
            ModernTextField(
                value = name,
                onValueChange = { name = it },
                label = "Họ và tên người nhận",
                placeholder = "Nhập họ tên"
            )

            // --- Số điện thoại ---
            ModernTextField(
                value = phone,
                onValueChange = { phone = it },
                label = "Số điện thoại",
                placeholder = "Nhập số điện thoại",
                keyboardType = KeyboardType.Phone,
                maxLength = 10
            )

            // Divider
            Spacer(modifier = Modifier.height(4.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(
                        Brush.horizontalGradient(
                            listOf(
                                Color.Transparent,
                                Color(0xFFE0E0E0),
                                Color.Transparent
                            )
                        )
                    )
            )
            Spacer(modifier = Modifier.height(4.dp))

            // --- Dropdown chọn Tỉnh ---
            ModernDropdown(
                value = selectedProvince?.name ?: "",
                label = "Tỉnh/Thành phố",
                placeholder = "Chọn Tỉnh/Thành phố",
                expanded = expandedProvince,
                onExpandedChange = { expandedProvince = it },
                items = provinces.map { it.name },
                onItemSelected = { selectedName ->
                    selectedProvince = provinces.find { it.name == selectedName }
                    selectedDistrict = null
                    selectedWard = null
                }
            )

            // --- Dropdown chọn Quận/Huyện ---
            AnimatedVisibility(
                visible = selectedProvince != null,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                ModernDropdown(
                    value = selectedDistrict?.name ?: "",
                    label = "Quận/Huyện",
                    placeholder = "Chọn Quận/Huyện",
                    expanded = expandedDistrict,
                    onExpandedChange = {
                        if (districts.isNotEmpty()) expandedDistrict = it
                    },
                    items = districts.map { it.name },
                    onItemSelected = { selectedName ->
                        selectedDistrict = districts.find { it.name == selectedName }
                        selectedWard = null
                    }
                )
            }

            // --- Dropdown chọn Phường/Xã ---
            AnimatedVisibility(
                visible = selectedDistrict != null,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                ModernDropdown(
                    value = selectedWard?.name ?: "",
                    label = "Phường/Xã",
                    placeholder = "Chọn Phường/Xã",
                    expanded = expandedWard,
                    onExpandedChange = {
                        if (wards.isNotEmpty()) expandedWard = it
                    },
                    items = wards.map { it.name },
                    onItemSelected = { selectedName ->
                        selectedWard = wards.find { it.name == selectedName }
                    }
                )
            }

            // --- Địa chỉ cụ thể ---
            ModernTextField(
                value = addressDetail,
                onValueChange = { addressDetail = it },
                label = "Địa chỉ cụ thể",
                placeholder = "Số nhà, tên đường...",
                minLines = 2
            )

            // --- Loại địa chỉ ---
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                SectionLabel("Loại địa chỉ")
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    listOf("Nhà riêng", "Văn phòng").forEach { type ->
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(
                                    if (addressType == type) {
                                        Brush.horizontalGradient(
                                            listOf(primaryRed.copy(alpha = 0.1f), accentGlow.copy(alpha = 0.05f))
                                        )
                                    } else {
                                        Brush.linearGradient(listOf(lightBg, lightBg))
                                    }
                                )
                                .border(
                                    width = if (addressType == type) 2.dp else 1.dp,
                                    color = if (addressType == type) primaryRed else Color(0xFFE0E0E0),
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .clickable { addressType = type }
                                .padding(vertical = 16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = type.uppercase(),
                                color = if (addressType == type) primaryRed else Color(0xFF757575),
                                fontSize = 13.sp,
                                fontWeight = if (addressType == type) FontWeight.Bold else FontWeight.Medium,
                                letterSpacing = 0.8.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // --- Nút Lưu ---
            Button(
                onClick = {
                    val newAddress = UserAddress(
                        address_id = addressId ?: "",
                        user_id = userId,
                        recipient_name = name,
                        phone_number = phone,
                        address = addressDetail,
                        district = selectedDistrict?.name ?: "",
                        city = selectedWard?.name ?: "",
                        province = selectedProvince?.name ?: "",
                        country = "Việt Nam",
                        address_type = addressType,
                        is_default = 0
                    )
                    if (isEdit) addressViewModel.updateUserAddress(newAddress)
                    else addressViewModel.addUserAddress(newAddress)

                    Toast.makeText(context, "✓ Lưu địa chỉ thành công", Toast.LENGTH_SHORT).show()
                    navController.popBackStack()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .border(
                        width = 1.dp,
                        brush = Brush.horizontalGradient(
                            listOf(primaryRed, accentGlow)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ),
                colors = ButtonDefaults.buttonColors(
                    containerColor = primaryRed
                ),
                shape = RoundedCornerShape(12.dp),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 4.dp,
                    pressedElevation = 8.dp
                )
            ) {
                Text(
                    text = if (isEdit) "CẬP NHẬT ĐỊA CHỈ" else "LƯU ĐỊA CHỈ",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    letterSpacing = 1.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}