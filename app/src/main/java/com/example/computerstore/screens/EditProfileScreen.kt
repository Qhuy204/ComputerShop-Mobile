package com.example.computerstore.screens

import android.app.DatePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.computerstore.R
import com.example.computerstore.data.model.User
import com.example.computerstore.screens.components.CustomTopBar
import com.example.computerstore.screens.components.CustomTopBarProfile
import com.example.computerstore.viewmodel.UserViewModel
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    navController: NavController,
    userViewModel: UserViewModel = viewModel()
) {
    val auth = FirebaseAuth.getInstance()
    val uid = auth.currentUser?.uid ?: ""
    val user by userViewModel.currentUser.collectAsState()

    LaunchedEffect(uid) {
        if (uid.isNotEmpty()) {
            userViewModel.loadUser(uid)
        }
    }

    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }
    var birthday by remember { mutableStateOf<Date?>(null) }
    var showValidation by remember { mutableStateOf(false) }

    LaunchedEffect(user) {
        user?.let {
            name = it.full_name ?: ""
            phone = it.phone_number ?: ""
            gender = it.gender ?: ""
            birthday = it.birthday?.toDate()
        }
    }

    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val horizontalPadding = ((configuration.screenWidthDp * 0.05f).coerceIn(8f, 20f)).dp
    val dateFormat = remember { SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) }
    val birthdayText = remember(birthday) {
        birthday?.let { dateFormat.format(it) } ?: ""
    }

    val calendar = remember { Calendar.getInstance() }
    val initialCalendar = remember(birthday) {
        val cal = Calendar.getInstance()
        birthday?.let { cal.time = it }
        cal
    }
    val datePicker = remember {
        DatePickerDialog(
            context,
            { _, year, month, day ->
                calendar.set(year, month, day)
                birthday = calendar.time
            },
            initialCalendar.get(Calendar.YEAR),
            initialCalendar.get(Calendar.MONTH),
            initialCalendar.get(Calendar.DAY_OF_MONTH)
        )
    }

    // Validation
    val isNameValid = name.isNotBlank()
    val isPhoneValid = phone.matches(Regex("^0[0-9]{9}$"))

    Scaffold(
        topBar = {
            CustomTopBar(
                title = "Thông tin cá nhân",
                iconRes = R.drawable.leftarrow,
                onBackClick = { navController.popBackStack() }
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5F5F5))
                .padding(padding)
                .imePadding()
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(Modifier.height(12.dp))

            // Form Section
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = horizontalPadding),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Họ và tên
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            "Họ và tên",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF333333)
                        )
                        OutlinedTextField(
                            value = name,
                            onValueChange = { name = it },
                            placeholder = { Text("Nhập họ và tên", color = Color(0xFFAAAAAA)) },
                            leadingIcon = {
                                Icon(
                                    Icons.Default.Person,
                                    contentDescription = null,
                                    tint = Color(0xFF757575)
                                )
                            },
                            isError = showValidation && !isNameValid,
                            supportingText = {
                                if (showValidation && !isNameValid) {
                                    Text("Vui lòng nhập họ tên", color = Color(0xFFDC2626))
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFFDC2626),
                                unfocusedBorderColor = Color(0xFFE0E0E0)
                            ),
                            singleLine = true,
                        )
                    }

                    // Email (read-only)
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            "Email",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF333333)
                        )
                        OutlinedTextField(
                            value = user?.email ?: auth.currentUser?.email ?: "",
                            onValueChange = {},
                            readOnly = true,
                            enabled = false,
                            leadingIcon = {
                                Icon(
                                    Icons.Default.Email,
                                    contentDescription = null,
                                    tint = Color(0xFF757575)
                                )
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                disabledBorderColor = Color(0xFFE0E0E0),
                                disabledTextColor = Color(0xFF757575)
                            ),
                            singleLine = true,
                            supportingText = { Text("") }
                        )
                    }

                    // Số điện thoại
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            "Số điện thoại",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF333333)
                        )
                        OutlinedTextField(
                            value = phone,
                            onValueChange = { if (it.length <= 10) phone = it },
                            placeholder = { Text("Nhập số điện thoại", color = Color(0xFFAAAAAA)) },
                            leadingIcon = {
                                Icon(
                                    Icons.Default.Phone,
                                    contentDescription = null,
                                    tint = Color(0xFF757575)
                                )
                            },
                            isError = showValidation && phone.isNotEmpty() && !isPhoneValid,
                            supportingText = {
                                if (showValidation && phone.isNotEmpty() && !isPhoneValid) {
                                    Text("Số điện thoại không hợp lệ", color = Color(0xFFDC2626))
                                }
                            },
                            keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                                keyboardType = KeyboardType.Phone
                            ),
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFFDC2626),
                                unfocusedBorderColor = Color(0xFFE0E0E0)
                            ),
                            singleLine = true
                        )
                    }

                    // Ngày sinh
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            "Ngày sinh",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF333333)
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { datePicker.show() }
                        ) {
                            OutlinedTextField(
                                value = birthdayText,
                                onValueChange = {},
                                placeholder = { Text("Chọn ngày sinh", color = Color(0xFFAAAAAA)) },
                                readOnly = true,
                                enabled = false,
                                leadingIcon = {
                                    Icon(
                                        Icons.Default.CalendarToday,
                                        contentDescription = null,
                                        tint = Color(0xFF757575)
                                    )
                                },
                                trailingIcon = {
                                    Icon(
                                        Icons.Default.ArrowDropDown,
                                        contentDescription = "Chọn ngày",
                                        tint = Color(0xFF757575)
                                    )
                                },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(8.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    disabledBorderColor = Color(0xFFE0E0E0),
                                    disabledTextColor = Color(0xFF757575),
                                    focusedBorderColor = Color(0xFFDC2626),
                                    unfocusedBorderColor = Color(0xFFE0E0E0)
                                ),
                                singleLine = true,
                                supportingText = { Text("") }
                            )
                        }
                    }

                    // Giới tính
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            "Giới tính",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF333333)
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            listOf("Nam", "Nữ", "Khác").forEach { option ->
                                Row(
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(48.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .border(
                                            width = 1.dp,
                                            color = if (gender == option) Color(0xFFDC2626) else Color(0xFFE0E0E0),
                                            shape = RoundedCornerShape(8.dp)
                                        )
                                        .background(
                                            if (gender == option) Color(0xFFFEF2F2) else Color.White
                                        )
                                        .clickable { gender = option }
                                        .padding(start = 2.dp, end = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Start
                                ) {
                                    RadioButton(
                                        selected = gender == option,
                                        onClick = { gender = option },
                                        colors = RadioButtonDefaults.colors(
                                            selectedColor = Color(0xFFDC2626),
                                            unselectedColor = Color(0xFFBDBDBD)
                                        )
                                    )
                                    Spacer(Modifier.width(4.dp))
                                    Text(
                                        option,
                                        fontSize = 14.sp,
                                        color = if (gender == option) Color(0xFFDC2626) else Color(0xFF757575),
                                        fontWeight = if (gender == option) FontWeight.Medium else FontWeight.Normal
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.height(24.dp))

            // Action Buttons
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = horizontalPadding),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = {
                        showValidation = true
                        if (isNameValid) {
                            val updatedUser = User(
                                user_id = uid,
                                full_name = name,
                                phone_number = phone.ifBlank { null },
                                gender = gender.ifBlank { null },
                                birthday = birthday?.let { Timestamp(it) },
                                email = user?.email ?: auth.currentUser?.email,
                                registration_date = user?.registration_date ?: Timestamp.now()
                            )

                            if (user == null) userViewModel.addUser(updatedUser)
                            else userViewModel.updateUser(updatedUser)

                            navController.popBackStack()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFDC2626)
                    ),
                    shape = RoundedCornerShape(8.dp),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
                ) {
                    Icon(
                        Icons.Default.Save,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        "Lưu thay đổi",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                OutlinedButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color(0xFF757575)
                    ),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE0E0E0)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        "Hủy bỏ",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(Modifier.height(24.dp))
        }
    }
}