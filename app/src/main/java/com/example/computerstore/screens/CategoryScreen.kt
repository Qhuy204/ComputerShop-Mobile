package com.example.computerstore.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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

    // fallback mock data nếu Firestore chưa có ảnh
    val mockCategories = listOf(
        Category(1, "Laptop", "https://images.unsplash.com/photo-1517336714731-489689fd1ca8"),
        Category(2, "Card đồ họa", "https://images.unsplash.com/photo-1610465299996-9d846b9a5f5d"),
        Category(3, "Mainboard", "https://images.unsplash.com/photo-1618401471353-b98afee0b2eb"),
        Category(4, "CPU", "https://images.unsplash.com/photo-1610521544038-6c61c96f4e36"),
        Category(5, "Máy tính bàn", "https://images.unsplash.com/photo-1587202372775-98927b2d57a7"),
        Category(6, "Màn hình", "https://images.unsplash.com/photo-1587829741301-dc798b83add3"),
        Category(7, "Bàn phím", "https://images.unsplash.com/photo-1517336714731-489689fd1ca8?ixlib=rb-4.0.3&auto=format&fit=crop&w=800&q=60"),
        Category(8, "Chuột", "https://images.unsplash.com/photo-1587825140708-6a1f59f7b6f8"),
        Category(9, "Tai nghe", "https://images.unsplash.com/photo-1511367461989-f85a21fda167"),
        Category(10, "Loa", "https://images.unsplash.com/photo-1585386959984-a4155224a1a5"),
        Category(11, "Ổ cứng", "https://images.unsplash.com/photo-1587202372585-99a0a01bbf10"),
        Category(12, "RAM", "https://images.unsplash.com/photo-1587202373080-4c80d9f2b82d"),
    )

    val displayCategories = if (categories.isEmpty()) mockCategories else categories

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFf6f6f6))
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
                        onClick = {
                            navController.navigate("category/${category.category_id}/${category.category_name}")
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
    onClick: () -> Unit
) {
    // map category_id sang ảnh demo
    val imageMap = mapOf(
        1 to "https://images.unsplash.com/photo-1517336714731-489689fd1ca8", // Laptop
        2 to "https://images.unsplash.com/photo-1610465299996-9d846b9a5f5d", // Card đồ họa
        3 to "https://images.unsplash.com/photo-1618401471353-b98afee0b2eb", // Mainboard
        4 to "https://images.unsplash.com/photo-1610521544038-6c61c96f4e36", // CPU
        5 to "https://images.unsplash.com/photo-1587202372775-98927b2d57a7", // Máy tính bàn
        6 to "https://images.unsplash.com/photo-1587829741301-dc798b83add3", // Màn hình
        7 to "https://images.unsplash.com/photo-1517336714731-489689fd1ca8?ixlib=rb-4.0.3&auto=format&fit=crop&w=800&q=60", // Bàn phím
        8 to "https://images.unsplash.com/photo-1587825140708-6a1f59f7b6f8", // Chuột
        9 to "https://images.unsplash.com/photo-1511367461989-f85a21fda167", // Tai nghe
        10 to "https://images.unsplash.com/photo-1585386959984-a4155224a1a5", // Loa
        11 to "https://images.unsplash.com/photo-1587202372585-99a0a01bbf10", // Ổ cứng
        12 to "https://images.unsplash.com/photo-1587202373080-4c80d9f2b82d"  // RAM
    )

    val imageUrl = imageMap[category.category_id] ?: "https://via.placeholder.com/150"

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
                        text = category.description!!,
                        fontSize = 14.sp,
                        color = Color.Gray,
                        maxLines = 2
                    )
                }
            }
        }
    }
}

