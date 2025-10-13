package com.example.computerstore.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import com.example.computerstore.data.model.User
import com.example.computerstore.viewmodel.UserViewModel
import com.google.firebase.Timestamp
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

// Màu sắc
private val RedPrimary = Color(0xFFDC2626)
private val WhiteBg = Color(0xFFFFFFFF)
private val WhiteCard = Color(0xFFF9FAFB)
private val GrayLight = Color(0xFFE5E7EB)
private val BlackText = Color(0xFF1F2937)
private val GrayText = Color(0xFF6B7280)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    userViewModel: UserViewModel = viewModel(),
    onBack: () -> Unit,
    showSnackbar: (String) -> Unit // Cơ chế hiển thị thông báo
) {
    val currentUser by userViewModel.currentUser.collectAsState()
    val scope = rememberCoroutineScope()

    // State cho các trường cần chỉnh sửa
    var fullName by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }
    var birthday by remember { mutableStateOf<Date?>(null) }
    var isLoading by remember { mutableStateOf(false) }

    // Cập nhật state khi currentUser thay đổi lần đầu hoặc khi màn hình được tải
    LaunchedEffect(currentUser) {
        currentUser?.let { user ->
            fullName = user.full_name.orEmpty()
            phoneNumber = user.phone_number.orEmpty()
            gender = user.gender.orEmpty()
            birthday = user.birthday?.toDate()
        }
    }

    // Format ngày sinh hiển thị
    val dateFormatter = remember { SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) }
    val birthdayText = birthday?.let { dateFormatter.format(it) } ?: "Chạm để chọn ngày sinh"

    fun showDatePicker() {
        showSnackbar("Chức năng chọn ngày tháng sẽ được triển khai chi tiết hơn trên môi trường Android thực!")
        // Tạm thời gán một ngày sinh mẫu để demo chức năng cập nhật
        birthday = Date()
    }

    fun handleSave() {
        val user = currentUser
        if (user == null || user.user_id == null) {
            showSnackbar("Lỗi: Không tìm thấy ID người dùng.")
            return
        }

        // Tạo bản sao của đối tượng User hiện tại và cập nhật các trường mới
        val updatedUser = user.copy(
            full_name = fullName,
            phone_number = phoneNumber,
            gender = gender,
            // Chuyển Date sang Timestamp
            birthday = birthday?.let { Timestamp(it) }
        )

        // Kiểm tra xem có bất kỳ thay đổi nào không (để tránh gọi API không cần thiết)
        val hasChanges = updatedUser.full_name != user.full_name ||
                updatedUser.phone_number != user.phone_number ||
                updatedUser.gender != user.gender ||
                updatedUser.birthday != user.birthday

        if (!hasChanges) {
            showSnackbar("Không có thông tin nào được thay đổi.")
            return
        }

        isLoading = true

        scope.launch {
            try {
                // Gọi hàm updateUser(user: User) từ UserViewModel
                userViewModel.updateUser(updatedUser)
                // Giả định rằng updateUser() sẽ tự động kích hoạt loadUser trong repository
                // và cập nhật currentUser flow. Nếu không, bạn có thể gọi:
                // userViewModel.loadUser(user.user_id!!)

                showSnackbar("Cập nhật thông tin cá nhân thành công!")
                onBack() // Quay lại màn hình Profile sau khi lưu thành công
            } catch (e: Exception) {
                showSnackbar("Cập nhật thất bại: ${e.message}")
                println("Error during update: $e")
            } finally {
                isLoading = false
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Chỉnh sửa Hồ sơ", fontWeight = FontWeight.Bold, color = BlackText) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Quay lại")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = WhiteBg)
            )
        },
        bottomBar = {
            Button(
                onClick = { if (!isLoading) handleSave() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(56.dp),
                enabled = !isLoading && currentUser != null,
                colors = ButtonDefaults.buttonColors(containerColor = RedPrimary),
                shape = RoundedCornerShape(12.dp)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(color = WhiteBg, modifier = Modifier.size(24.dp))
                } else {
                    Text("Lưu Thay đổi", fontSize = 16.sp, color = WhiteBg, fontWeight = FontWeight.Bold)
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(WhiteCard)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Cập nhật các thông tin cơ bản của bạn.",
                fontSize = 14.sp,
                color = BlackText.copy(alpha = 0.7f),
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Full Name Input
            OutlinedTextField(
                value = fullName,
                onValueChange = { fullName = it },
                label = { Text("Họ và Tên") },
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            // Phone Number Input
            OutlinedTextField(
                value = phoneNumber,
                onValueChange = { phoneNumber = it },
                label = { Text("Số điện thoại") },
                leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            // Gender Selection
            GenderDropdown(
                selectedGender = gender,
                onGenderSelected = { gender = it }
            )

            // Birthday Input (Chỉ cho phép click để chọn)
            OutlinedTextField(
                value = birthdayText,
                onValueChange = {},
                label = { Text("Ngày sinh") },
                leadingIcon = { Icon(Icons.Default.Cake, contentDescription = null) },
                readOnly = true,
                trailingIcon = {
                    IconButton(onClick = { showDatePicker() }) {
                        Icon(Icons.Default.CalendarToday, contentDescription = "Chọn ngày")
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            // Email (Non-editable)
            OutlinedTextField(
                value = currentUser?.email.orEmpty(),
                onValueChange = {},
                label = { Text("Email (Không thể thay đổi)") },
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                readOnly = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    disabledLeadingIconColor = BlackText.copy(alpha = 0.6f),
                    disabledLabelColor = GrayLight,
                    disabledTextColor = GrayText
                ),
                enabled = false
            )

            Spacer(Modifier.height(32.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenderDropdown(selectedGender: String, onGenderSelected: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    val genders = listOf("Nam", "Nữ", "Khác", "Không rõ")

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = Modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = selectedGender.ifEmpty { "Chọn giới tính" },
            onValueChange = {},
            readOnly = true,
            label = { Text("Giới tính") },
            leadingIcon = { Icon(Icons.Default.Wc, contentDescription = null) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier.menuAnchor().fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            genders.forEach { selectionOption ->
                DropdownMenuItem(
                    text = { Text(selectionOption) },
                    onClick = {
                        onGenderSelected(selectionOption)
                        expanded = false
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                )
            }
        }
    }
}
