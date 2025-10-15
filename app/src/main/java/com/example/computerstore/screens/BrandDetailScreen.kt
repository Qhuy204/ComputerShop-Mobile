package com.example.computerstore.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.computerstore.R
import com.example.computerstore.screens.components.CustomTopBar
import com.example.computerstore.viewmodel.ProductImageViewModel
import com.example.computerstore.viewmodel.ProductSpecificationViewModel
import com.example.computerstore.viewmodel.ProductViewModel

//data class PriceRange(val label: String, val min: Double, val max: Double)
data class BrandLogo(val name: String, val imageUrl: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BrandDetailScreen(
    navController: NavController,
    brandName: String,
    productViewModel: ProductViewModel = viewModel(),
    productImageViewModel: ProductImageViewModel = viewModel(),
    productSpecViewModel: ProductSpecificationViewModel = viewModel()
) {
    val products by productViewModel.products.collectAsState()
    val images by productImageViewModel.productImages.collectAsState()
    val specs by productSpecViewModel.productSpecifications.collectAsState()

    var sortOption by remember { mutableStateOf("Phổ biến") }
    var selectedPriceRange by remember { mutableStateOf<PriceRange?>(null) }

    val priceRanges = listOf(
        PriceRange("Dưới 5 triệu", 0.0, 5_000_000.0),
        PriceRange("5 - 10 triệu", 5_000_000.0, 10_000_000.0),
        PriceRange("10 - 15 triệu", 10_000_000.0, 15_000_000.0),
        PriceRange("15 - 20 triệu", 15_000_000.0, 20_000_000.0),
        PriceRange("20 - 30 triệu", 20_000_000.0, 30_000_000.0),
        PriceRange("Trên 30 triệu", 30_000_000.0, Double.MAX_VALUE)
    )

    // Lọc & sắp xếp
    val filteredProducts = remember(products, sortOption, selectedPriceRange) {
        var list = products.filter { it.brand_name?.contains(brandName, ignoreCase = true) == true }

        selectedPriceRange?.let { range ->
            list = list.filter { it.base_price in range.min..range.max }
        }

        when (sortOption) {
            "Phổ biến" -> list
            "Mới nhất" -> list.sortedByDescending { it.product_id }
            "Giá tăng dần" -> list.sortedBy { it.base_price }
            "Giá giảm dần" -> list.sortedByDescending { it.base_price }
            "Tên (A-Z)" -> list.sortedBy { it.product_name }
            "Tên (Z-A)" -> list.sortedByDescending { it.product_name }
            else -> list
        }
    }

    LaunchedEffect(Unit) {
        productViewModel.loadAllProducts()
        productImageViewModel.loadAllProductImages()
        productSpecViewModel.loadAllProductSpecifications()
    }

    Scaffold(
        topBar = {
            CustomTopBar(
                title = brandName,
                iconRes = R.drawable.leftarrow,
                onBackClick = { navController.popBackStack() }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5F5F5))
                .padding(paddingValues)
        ) {
            // Filter Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Sort dropdown
                var sortExpanded by remember { mutableStateOf(false) }

                Box {
                    FilterChip(
                        selected = false,
                        onClick = { sortExpanded = true },
                        label = { Text(sortOption, fontSize = 13.sp) },
                        trailingIcon = {
                            Icon(
                                Icons.Default.KeyboardArrowDown,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    )

                    DropdownMenu(
                        expanded = sortExpanded,
                        onDismissRequest = { sortExpanded = false }
                    ) {
                        listOf("Phổ biến", "Mới nhất", "Giá tăng dần", "Giá giảm dần", "Tên (A-Z)", "Tên (Z-A)")
                            .forEach { option ->
                                DropdownMenuItem(
                                    text = { Text(option) },
                                    onClick = {
                                        sortOption = option
                                        sortExpanded = false
                                    }
                                )
                            }
                    }
                }

                // Price filter dropdown
                var priceExpanded by remember { mutableStateOf(false) }

                Box {
                    FilterChip(
                        selected = selectedPriceRange != null,
                        onClick = { priceExpanded = true },
                        label = {
                            Text(
                                selectedPriceRange?.label ?: "Giá",
                                fontSize = 13.sp
                            )
                        },
                        trailingIcon = {
                            if (selectedPriceRange != null) {
                                Icon(
                                    Icons.Default.Close,
                                    contentDescription = "Clear",
                                    modifier = Modifier
                                        .size(18.dp)
                                        .clickable { selectedPriceRange = null }
                                )
                            } else {
                                Icon(
                                    Icons.Default.KeyboardArrowDown,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    )

                    DropdownMenu(
                        expanded = priceExpanded,
                        onDismissRequest = { priceExpanded = false }
                    ) {
                        priceRanges.forEach { range ->
                            DropdownMenuItem(
                                text = { Text(range.label) },
                                onClick = {
                                    selectedPriceRange = range
                                    priceExpanded = false
                                }
                            )
                        }
                    }
                }
            }

            // Count
            Text(
                text = "${filteredProducts.size} sản phẩm",
                fontSize = 14.sp,
                color = Color.Gray,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
            )

            // Product Grid
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(filteredProducts) { product ->
                    val imageUrl = images.find { it.product_id?.toInt() == product.product_id }?.image_url
                    val productSpecs = specs.filter { it.product_id == product.product_id }

                    ProductCard2(
                        product = product,
                        imageUrl = imageUrl,
                        specs = productSpecs,
                        onClick = {
                            navController.navigate("product/${product.product_id}")
                        }
                    )
                }
            }
        }
    }
}
