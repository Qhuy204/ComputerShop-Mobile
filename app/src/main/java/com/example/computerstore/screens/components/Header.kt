package com.example.computerstore.screens.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.example.computerstore.R
import com.example.computerstore.navigation.BottomBarScreen
import com.example.computerstore.screens.buttons.CustomButton
import com.example.computerstore.ui.components.CustomTextField

@Composable
fun HeaderSection(
    searchQuery: String,
    onSearchChange: (String) -> Unit,
    focusManager: FocusManager,
    navController: NavController
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .background(Color(0xFFE30000))
            .padding(top = 22.dp, start = 16.dp, end = 16.dp)
    ) {
        // Logo → quay lại Home
        Box(
            modifier = Modifier
                .size(56.dp)
                .clickable {
                    navController.navigate(BottomBarScreen.Home.route) {
                        launchSingleTop = true
                        restoreState = true
                    }
                }
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data("https://file.hstatic.net/200000636033/file/logo-mobile_1e5b7fc485b24cf985b3d63cfa1f88be.svg")
                    .decoderFactory(SvgDecoder.Factory())
                    .build(),
                contentDescription = "Logo",
                modifier = Modifier.fillMaxSize()
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        // ✅ Ô nhập tìm kiếm (có thể nhập text bình thường)
        CustomTextField(
            value = searchQuery,
            onValueChange = onSearchChange,
            placeholder = "Tìm kiếm sản phẩm...",
            iconRes = R.drawable.search,
            focusManager = focusManager,
            isLastField = true,
            modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(30.dp))
                .height(56.dp)
        )

        Spacer(modifier = Modifier.width(10.dp))

        // Nút tìm kiếm
        CustomButton(
            onClick = {
                if (searchQuery.isNotBlank()) {
                    navController.navigate("search?query=${searchQuery}")
                    focusManager.clearFocus()
                }
            },
            iconRes = R.drawable.search,
            backgroundColor = Color.Black,
            iconTint = Color.White,
            size = 56.dp,
            modifier = Modifier
                .border(
                    width = 1.dp,
                    color = Color.LightGray,
                    shape = RoundedCornerShape(30.dp)
                )
        )
    }
}
