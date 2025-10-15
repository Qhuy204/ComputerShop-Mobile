package com.example.computerstore.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.computerstore.R
import com.example.computerstore.data.model.Category
import com.example.computerstore.screens.components.CustomTopBar
import com.example.computerstore.viewmodel.CategoryViewModel

@Composable
fun CategoryScreen(
    navController: NavController,
    categoryId: Int = 0,
    categoryName: String = "Danh mục",
    categoryViewModel: CategoryViewModel = viewModel()
) {
    val categories by categoryViewModel.categories.collectAsState()

    LaunchedEffect(Unit) {
        categoryViewModel.loadAllCategories()
    }

    // Map category_name → ảnh minh họa
    val imageMap = mapOf(
        "Laptop" to "https://file.hstatic.net/200000636033/file/icon1_ce115f32db874a8e9b5af39517176e96.png",
        "Máy tính bàn" to "https://file.hstatic.net/200000636033/file/icon3_5c59c1dc52ec4b81a94a3edba293e895.png",
        "Màn hình" to "https://product.hstatic.net/200000722513/product/asus_pg27aqdm_gearvn_53c46bd0ca1f40f1a7abfb0246800081_e341bb95b0724bee845ba8f093678245_master.jpg",
        "Bàn phím" to "https://file.hstatic.net/200000722513/file/ban_phim_93a4d3cefd8345dfac23829818a3c5d4.jpg",
        "Chuột" to "https://file.hstatic.net/200000722513/file/chuot_aa348bf0177b4795a39ab66d51e62ed7.jpg",
        "Tai nghe" to "https://file.hstatic.net/200000722513/file/tai_nghe_ed3b4f52172f40929e1d3ab493099b73.jpg",
        "Loa" to "https://file.hstatic.net/200000636033/file/icon10_bfdf42150dbf45cfbcdf990b26f59691.png",
        "Ổ cứng" to "https://file.hstatic.net/200000636033/file/icon11_2f0ea4c77ae3482f906591cec8f24cea.png",
        "RAM" to "https://file.hstatic.net/200000636033/file/icon13_708c31c3ba56430dbec3f4cc7e1b14f0.png",
        "Card đồ họa" to "https://file.hstatic.net/200000722513/file/asus-rog-strix-rtx4090-o24g-gaming-03_c948a4c2a9cf4adcbd522319bfcd4846.jpg",
        "Mainboard" to "https://file.hstatic.net/200000636033/file/icon5_71200675c9e64c32a11730486ba04b32.png",
        "CPU" to "https://file.hstatic.net/200000636033/file/icon6_056974287cd84e0d82eac05809b7e5d5.png"
    )

    // Nếu chưa có danh mục từ Firestore, fallback theo map
    val mockCategories = imageMap.keys.mapIndexed { index, name ->
        Category(index + 1, name, "Danh mục $name")
    }

    val displayCategories = if (categories.isEmpty()) mockCategories else categories

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF6F6F6))
    ) {
        CustomTopBar(
            title = categoryName,
            iconRes = R.drawable.leftarrow,
            onBackClick = { navController.popBackStack() }
        )

        if (displayCategories.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Không có danh mục nào")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(displayCategories, key = { it.category_id }) { category ->
                    CategoryItem(
                        category = category,
                        imageMap = imageMap,
                        onClick = {
                            navController.navigate(
                                "categoryDetail/${category.category_id}/${category.category_name}"
                            )
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun CategoryItem(
    category: Category,
    imageMap: Map<String, String>,
    onClick: () -> Unit
) {
    val imageUrl = imageMap[category.category_name] ?: "https://via.placeholder.com/150"

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = imageUrl,
                contentDescription = category.category_name,
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.LightGray)
            )

            Spacer(Modifier.width(12.dp))

            Column {
                Text(
                    text = category.category_name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
                if (!category.description.isNullOrEmpty()) {
                    Text(
                        text = category.description ?: "",
                        fontSize = 13.sp,
                        color = Color.Gray,
                        maxLines = 2
                    )
                }
            }
        }
    }
}
