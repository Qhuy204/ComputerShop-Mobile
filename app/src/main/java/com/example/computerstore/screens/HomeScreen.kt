package com.example.computerstore.screens

import ProductRegion
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.overscroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.computerstore.data.model.Product
import com.example.computerstore.ui.components.CustomTextField
import com.example.computerstore.viewmodel.ProductViewModel
import com.google.firebase.auth.FirebaseAuth
import com.example.computerstore.R
import com.example.computerstore.screens.buttons.CustomButton
import com.example.computerstore.screens.components.BannerSection
import com.example.computerstore.screens.components.HorizontalItem
import com.example.computerstore.screens.components.ItemList
import com.example.computerstore.screens.components.ViewedProduct
import com.example.computerstore.screens.components.ViewedProductsSection

@Composable
fun HomeScreen(
    onLogout: () -> Unit,
    listState: LazyListState,
    productViewModel: ProductViewModel = viewModel()
) {
    val user = FirebaseAuth.getInstance().currentUser
    val products by productViewModel.products.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    LaunchedEffect(Unit) {
        productViewModel.loadAllProducts()
    }

    if (user != null) {
        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize().background(color = Color.LightGray).overscroll(overscrollEffect  = null)
        ) {
            // Header (search bar + user button)
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp)
                        .background(Color.Black)
                        .padding(top = 60.dp, start = 16.dp, end = 16.dp)
                ) {
                    CustomTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = "Search for a product",
                        iconRes = R.drawable.ic_search,
                        focusManager = focusManager,
                        isLastField = true,
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(30.dp))
                            .height(56.dp)
                    )

                    Spacer(modifier = Modifier.width(10.dp))

                    CustomButton(
                        onClick = { /* add something */ },
                        iconRes = R.drawable.ic_user,
                        backgroundColor = Color.Black,
                        iconTint = Color.White,
                        size = 56.dp,
                        modifier = Modifier
                            .border(
                                width = 0.5.dp,
                                color = Color.LightGray,
                                shape = RoundedCornerShape(30.dp)
                            )
                    )
                }
            }

            // Banner + Check Now section
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(130.dp)
                        .background(Color.Black)
                        .padding(horizontal = 16.dp)
                ) {
                    Text(
                        text = "Power Up Your Next Upgrade",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    Text(
                        text = "Top gear, best performance, trusted by gamers.",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.LightGray
                    )

                    Button(
                        onClick = { /* add something */ },
                        modifier = Modifier
                            .width(200.dp)
                            .padding(top = 16.dp),
                        shape = RoundedCornerShape(5.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFE30000),
                            contentColor = Color.White
                        )
                    ) {
                        Text("Check Now", fontWeight = FontWeight.Bold)
                    }
                }
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

            item{
                ProductRegion(
                    title = "PC bán chạy",
                    categories = listOf("PC I3", "PC I5", "PC I7", "PC I9"),
                    products = products.filter { it.category_id == 1 }, // ví dụ lọc theo category_id
                    onViewAllClick = { /* TODO: navigate list */ },
                    onProductClick = { product ->
                        // TODO: navController.navigate("productDetail/${product.product_id}")
                    }
                )
            }
            item{
                Spacer(modifier = Modifier.height(8.dp))
            }


            // Logout button
//            item {
//                Spacer(modifier = Modifier.height(32.dp))
//                Button(onClick = {
//                    FirebaseAuth.getInstance().signOut()
//                    onLogout()
//                }) {
//                    Text("Logout")
//                }
//                Spacer(modifier = Modifier.height(16.dp))
//            }
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
fun ProductItem(product: Product) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Product ID: ${product.product_id}", fontSize = 16.sp)
            Text(text = "Name: ${product.product_name}", fontSize = 16.sp)
            Text(text = "Price: $${product.base_price}", fontSize = 16.sp)
        }
    }
}


