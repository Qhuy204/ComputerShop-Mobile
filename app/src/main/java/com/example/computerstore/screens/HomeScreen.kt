package com.example.computerstore.screens

import ProductRegion
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.overscroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.computerstore.data.model.Product
import com.example.computerstore.screens.components.BannerSection
import com.example.computerstore.screens.components.BannerSection1
import com.example.computerstore.screens.components.HeaderSection
import com.example.computerstore.screens.components.HorizontalItem
import com.example.computerstore.screens.components.ItemList
import com.example.computerstore.screens.components.ViewedProduct
import com.example.computerstore.screens.components.ViewedProductsSection
import com.example.computerstore.viewmodel.ProductImageViewModel
import com.example.computerstore.viewmodel.ProductViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun HomeScreen(
    onLogout: () -> Unit,
    listState: LazyListState,
    navController: androidx.navigation.NavController,
    productViewModel: ProductViewModel = viewModel(),
    productImageViewModel: ProductImageViewModel = viewModel()
) {
    val user = FirebaseAuth.getInstance().currentUser
    val products by productViewModel.products.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current
    val images by productImageViewModel.productImages.collectAsState()

    LaunchedEffect(Unit) {
        productViewModel.loadAllProducts()
        productImageViewModel.loadAllProductImages()
    }

    if (user != null) {
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.LightGray)
                .overscroll(overscrollEffect = null)
        ) {
            // Header (search bar + user button)
            item {
                HeaderSection(
                    searchQuery = "",
                    onSearchChange = {},
                    focusManager = LocalFocusManager.current,
                    navController = navController
                )
            }
            item {
                BannerSection1(
                    onCheckNowClick = { /* TODO: navigate to promotion */ }
                )
            }

            // Multi Banner
            item {
                val banners = listOf(
                    "https://file.hstatic.net/200000722513/file/gearvn-laptop-van-phong-t8-header-pc-fix.png",
                    "https://file.hstatic.net/200000722513/file/gearvn-back-to-school-2025-header-pc_658a24f226a146f69278f2c4905c3b22.png",
                    "https://file.hstatic.net/200000722513/file/gearvn-acer-back-to-school-2025-slider.jpg"
                )

                BannerSection(banners) { clickedUrl ->
                    // TODO: mở link khuyến mãi
                }
            }

            item{
                val viewedProducts = listOf(
                    ViewedProduct("PC Gaming i9 + RTX 4090", "https://pcmarket.vn/media/product/250_11929_msi_geforce_rtx_5090_32g_gaming.jpg", "70.000.000đ", "65.000.000đ", "7%"),
                    ViewedProduct("PC Gaming i9 + RTX 4090", "https://pcmarket.vn/media/product/250_11929_msi_geforce_rtx_5090_32g_gaming.jpg", "70.000.000đ", "65.000.000đ", "7%"),
                    ViewedProduct("PC Gaming i9 + RTX 4090", "https://pcmarket.vn/media/product/250_11929_msi_geforce_rtx_5090_32g_gaming.jpg", "70.000.000đ", "65.000.000đ", "7%"),
                    ViewedProduct("PC Gaming i9 + RTX 4090", "https://pcmarket.vn/media/product/250_11929_msi_geforce_rtx_5090_32g_gaming.jpg", "70.000.000đ", "65.000.000đ", "7%"),
                    ViewedProduct("PC Gaming i9 + RTX 4090", "https://pcmarket.vn/media/product/250_11929_msi_geforce_rtx_5090_32g_gaming.jpg", "70.000.000đ", "65.000.000đ", "7%"),

                    )

                ViewedProductsSection(
                    products = viewedProducts,
                    onProductClick = { product ->
                        // Demo gọi function khi click
                        Log.d("ProductClick", "Clicked: ${product.name}")
                        // TODO: điều hướng sang ProductDetailScreen(product)
                    }
                )
            }

            item{
                Spacer(modifier = Modifier.height(8.dp))
            }

            item{
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
//                        .padding(bottom = 8.dp)
//                        .clip(RoundedCornerShape(6.dp))
                        .background(Color.White)
//                        .border(0.dp, Color.LightGray, RoundedCornerShape(6.dp))
                        .wrapContentHeight()
                ) {
                    val brands = listOf(
                        HorizontalItem("Amazon", "https://logo.clearbit.com/amazon.com"),
                        HorizontalItem("Walmart", "https://logo.clearbit.com/walmart.com"),
                        HorizontalItem("Tire Rack", "https://logo.clearbit.com/tirerack.com"),
                        HorizontalItem("Zales", "https://logo.clearbit.com/zales.com"),
                        HorizontalItem("Kay Jewelers", "https://logo.clearbit.com/kay.com"),
                        HorizontalItem("Jomashop", "https://logo.clearbit.com/jomashop.com"),
                        HorizontalItem("Nectar", "https://logo.clearbit.com/nectar.com"),
                        HorizontalItem("DJI", "https://logo.clearbit.com/dji.com"),
                        HorizontalItem("Amazon", "https://logo.clearbit.com/amazon.com"),
                        HorizontalItem("Walmart", "https://logo.clearbit.com/walmart.com"),
                        HorizontalItem("Tire Rack", "https://logo.clearbit.com/tirerack.com"),
                        HorizontalItem("Zales", "https://logo.clearbit.com/zales.com"),
                        HorizontalItem("Kay Jewelers", "https://logo.clearbit.com/kay.com"),
                        HorizontalItem("Jomashop", "https://logo.clearbit.com/jomashop.com"),
                        HorizontalItem("Nectar", "https://logo.clearbit.com/nectar.com"),
                        HorizontalItem("DJI", "https://logo.clearbit.com/dji.com"),
                    )

                    ItemList(title = "Thương hiệu", items = brands) { clicked ->
                        println("Clicked: ${clicked.name}")
                        // hoặc navController.navigate("brand/${clicked.name}")
                    }

                }
            }

            item{
                Spacer(modifier = Modifier.height(8.dp))
            }

            item {
                ProductRegion(
                    title = "Laptop bán chạy",
                    categories = listOf(),
                    products = products.filter { it.category_id == 1 },
                    images = images,
                    onViewAllClick = { /* TODO: navigate to list */ },
                    onProductClick = { product ->
                        navController.navigate("product/${product.product_id}")
                    }
                )
            }


            item { Spacer(modifier = Modifier.height(8.dp)) }

            item {
                ProductRegion(
                    title = "PC bán chạy",
                    categories = listOf(),
                    products = products.filter { it.category_id == 2 },
                    onViewAllClick = { /* TODO: navigate list */ },
                    images = images,
                    onProductClick = { product ->
                        navController.navigate("product/${product.product_id}")
                    }
                )
            }

            item { Spacer(modifier = Modifier.height(8.dp)) }

            item {
                ProductRegion(
                    title = "Màn hình bán chạy",
                    categories = listOf(),
                    products = products.filter { it.category_id == 3 },
                    onViewAllClick = { /* TODO: navigate list */ },
                    images = images,
                    onProductClick = { product ->
                        navController.navigate("product/${product.product_id}")
                    }
                )
            }

            item { Spacer(modifier = Modifier.height(8.dp)) }

            item {
                ProductRegion(
                    title = "Bàn phím bán chạy",
                    categories = listOf(),
                    products = products.filter { it.category_id == 4 },
                    onViewAllClick = { /* TODO: navigate list */ },
                    images = images,
                    onProductClick = { product ->
                        navController.navigate("product/${product.product_id}")
                    }
                )
            }
            item { Spacer(modifier = Modifier.height(8.dp)) }

            item {
                ProductRegion(
                    title = "Chuột bán chạy",
                    categories = listOf(),
                    products = products.filter { it.category_id == 5 },
                    onViewAllClick = { /* TODO: navigate list */ },
                    images = images,
                    onProductClick = { product ->
                        navController.navigate("product/${product.product_id}")
                    }
                )
            }
            item { Spacer(modifier = Modifier.height(8.dp)) }

            item {
                ProductRegion(
                    title = "Tai nghe bán chạy",
                    categories = listOf(),
                    products = products.filter { it.category_id == 6 },
                    onViewAllClick = { /* TODO: navigate list */ },
                    images = images,
                    onProductClick = { product ->
                        navController.navigate("product/${product.product_id}")
                    }
                )
            }
            item { Spacer(modifier = Modifier.height(8.dp)) }

            item {
                ProductRegion(
                    title = "Loa bán chạy",
                    categories = listOf(),
                    products = products.filter { it.category_id == 7 },
                    onViewAllClick = { /* TODO: navigate list */ },
                    images = images,
                    onProductClick = { product ->
                        navController.navigate("product/${product.product_id}")
                    }
                )
            }

        }
    } else {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "No user logged in")
        }
    }
}



@Composable
fun ProductItem(
    product: Product,
    imageUrl: String? = null,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = imageUrl ?: "https://via.placeholder.com/150",
                contentDescription = product.product_name,
                modifier = Modifier
                    .size(80.dp)
                    .background(Color.LightGray, RoundedCornerShape(8.dp))
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(product.product_name ?: "Sản phẩm", fontWeight = FontWeight.Bold)
                Text("Giá: ${"%,.0f".format(product.base_price)} đ", color = Color.Red)
            }
        }
    }
}
