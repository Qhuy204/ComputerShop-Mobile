package com.example.computerstore.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.computerstore.data.model.Product
import com.example.computerstore.viewmodel.ProductImageViewModel
import com.example.computerstore.viewmodel.ProductViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchResultScreen(
    query: String,
    navController: androidx.navigation.NavController,
    productViewModel: ProductViewModel = viewModel(),
    imageViewModel: ProductImageViewModel = viewModel()
) {
    val products by productViewModel.products.collectAsState()
    val images by imageViewModel.productImages.collectAsState()

    // Lọc sản phẩm theo tên
    val filtered = remember(products, query) {
        products.filter {
            it.product_name?.contains(query, ignoreCase = true) == true
        }
    }

    LaunchedEffect(Unit) {
        productViewModel.loadAllProducts()
        imageViewModel.loadAllProductImages()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Kết quả cho \"$query\"") },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFFE30000), titleContentColor = Color.White)
            )
        }
    ) { padding ->
        if (filtered.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("Không tìm thấy sản phẩm phù hợp")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .background(Color.White)
            ) {
                items(filtered) { product ->
                    val imageUrl = images.find {
                        it.product_id?.toInt() == product.product_id && it.is_primary == 1
                    }?.image_url ?: "https://via.placeholder.com/150"

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .clickable {
                                navController.navigate("product/${product.product_id}")
                            },
                        shape = RoundedCornerShape(8.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            AsyncImage(
                                model = imageUrl,
                                contentDescription = product.product_name,
                                modifier = Modifier
                                    .size(80.dp)
                                    .background(Color(0xFFF5F5F5), RoundedCornerShape(8.dp))
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(product.product_name ?: "", fontWeight = FontWeight.Bold)
                                Text(
                                    "${"%,.0f".format(product.base_price)} đ",
                                    color = Color(0xFFE30000),
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
