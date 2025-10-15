package com.example.computerstore.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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

data class HorizontalItem(val name: String, val imageUrl: String)
data class PriceRange(val label: String, val min: Double, val max: Double)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryDetailScreen(
    navController: NavController,
    categoryId: Int,
    categoryName: String,
    productViewModel: ProductViewModel = viewModel(),
    productImageViewModel: ProductImageViewModel = viewModel(),
    productSpecViewModel: ProductSpecificationViewModel = viewModel()
) {
    val products by productViewModel.products.collectAsState()
    val images by productImageViewModel.productImages.collectAsState()
    val specs by productSpecViewModel.productSpecifications.collectAsState()

    var sortOption by remember { mutableStateOf("Phổ biến") }
    var selectedBrand by remember { mutableStateOf<String?>(null) }
    var selectedPriceRange by remember { mutableStateOf<PriceRange?>(null) }

    val brands = listOf(
        HorizontalItem("Dell", "https://logo.clearbit.com/dell.com"),
        HorizontalItem("HP", "https://logo.clearbit.com/hp.com"),
        HorizontalItem("Asus", "https://logo.clearbit.com/asus.com"),
        HorizontalItem("Acer", "https://logo.clearbit.com/acer.com"),
        HorizontalItem("Lenovo", "https://logo.clearbit.com/lenovo.com"),
        HorizontalItem("MSI", "https://storage-asset.msi.com/frontend/imgs/logo.png"),
        HorizontalItem("Apple", "https://logo.clearbit.com/apple.com"),
        HorizontalItem("Samsung", "https://logo.clearbit.com/samsung.com"),
        HorizontalItem("LG", "https://logo.clearbit.com/lg.com"),
        HorizontalItem("Logitech", "https://logo.clearbit.com/logitech.com"),
        HorizontalItem("Corsair", "https://logo.clearbit.com/corsair.com"),
        HorizontalItem("Razer", "https://logo.clearbit.com/razer.com"),
        HorizontalItem("HyperX", "https://logo.clearbit.com/hyperx.com"),
        HorizontalItem("Intel", "https://logo.clearbit.com/intel.com"),
        HorizontalItem("AMD", "https://logo.clearbit.com/amd.com"),
        HorizontalItem("NVIDIA", "https://logo.clearbit.com/nvidia.com"),
        HorizontalItem("GIGABYTE", "https://logo.clearbit.com/gigabyte.com"),
        HorizontalItem("Kingston", "https://logo.clearbit.com/kingston.com")
    )

    val priceRanges = listOf(
        PriceRange("Dưới 5 triệu", 0.0, 5000000.0),
        PriceRange("5 - 10 triệu", 5000000.0, 10000000.0),
        PriceRange("10 - 15 triệu", 10000000.0, 15000000.0),
        PriceRange("15 - 20 triệu", 15000000.0, 20000000.0),
        PriceRange("20 - 30 triệu", 20000000.0, 30000000.0),
        PriceRange("Trên 30 triệu", 30000000.0, Double.MAX_VALUE)
    )

    // Filter and sort products
    val filteredProducts = remember(products, sortOption, selectedBrand, selectedPriceRange) {
        var list = products.filter { it.category_id == categoryId }

        // Filter by brand
        selectedBrand?.let { brand ->
            list = list.filter { product ->
                product.product_name.contains(brand, ignoreCase = true) ||
                        product.brand_name?.contains(brand, ignoreCase = true) == true
            }
        }

        // Filter by price range
        selectedPriceRange?.let { range ->
            list = list.filter { it.base_price in range.min..range.max }
        }

        // Sort
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
                title = categoryName,
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
            // Filter chips row
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
                        listOf("Phổ biến", "Mới nhất", "Giá tăng dần", "Giá giảm dần", "Tên (A-Z)", "Tên (Z-A)").forEach { option ->
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

                // Brand dropdown
                var brandExpanded by remember { mutableStateOf(false) }

                Box {
                    FilterChip(
                        selected = selectedBrand != null,
                        onClick = { brandExpanded = true },
                        label = {
                            Text(
                                selectedBrand ?: "Thương hiệu",
                                fontSize = 13.sp
                            )
                        },
                        trailingIcon = {
                            if (selectedBrand != null) {
                                Icon(
                                    Icons.Default.Close,
                                    contentDescription = "Clear",
                                    modifier = Modifier
                                        .size(18.dp)
                                        .clickable {
                                            selectedBrand = null
                                        }
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
                        expanded = brandExpanded,
                        onDismissRequest = { brandExpanded = false },
                        modifier = Modifier.heightIn(max = 400.dp)
                    ) {
                        brands.forEach { brand ->
                            DropdownMenuItem(
                                text = { Text(brand.name) },
                                onClick = {
                                    selectedBrand = brand.name
                                    brandExpanded = false
                                },
                                leadingIcon = {
                                    Image(
                                        painter = rememberAsyncImagePainter(brand.imageUrl),
                                        contentDescription = brand.name,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                            )
                        }
                    }
                }

                // Price range dropdown
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
                                        .clickable {
                                            selectedPriceRange = null
                                        }
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

            // Products count
            Text(
                text = "${filteredProducts.size} sản phẩm",
                fontSize = 14.sp,
                color = Color.Gray,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
            )

            // Product grid
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