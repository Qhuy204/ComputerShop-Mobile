package com.example.computerstore.screens

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
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.computerstore.data.model.Product
import com.example.computerstore.screens.components.BannerSection
import com.example.computerstore.screens.components.HeaderSection
import com.example.computerstore.screens.components.HorizontalItem
import com.example.computerstore.screens.components.ItemList
import com.example.computerstore.screens.components.ViewedProduct
import com.example.computerstore.screens.components.ViewedProductsSection
import com.example.computerstore.viewmodel.ProductImageViewModel
import com.example.computerstore.viewmodel.ProductViewModel
import com.google.firebase.auth.FirebaseAuth
import androidx.compose.ui.res.stringResource
import com.example.computerstore.R
import com.example.computerstore.screens.components.CategoryList
import com.example.computerstore.screens.components.NewsList
import com.example.computerstore.viewmodel.CategoryViewModel
import com.example.computerstore.viewmodel.BlogViewModel
import com.example.computerstore.data.model.Blog
import com.example.computerstore.screens.components.Footer

@Composable
fun HomeScreen(
    onLogout: () -> Unit,
    listState: LazyListState,
    navController: androidx.navigation.NavController,
    productViewModel: ProductViewModel = viewModel(),
    productImageViewModel: ProductImageViewModel = viewModel(),
    categoryViewMoel: CategoryViewModel = viewModel()
) {
    val user = FirebaseAuth.getInstance().currentUser
    val products by productViewModel.products.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current
    val images by productImageViewModel.productImages.collectAsState()
    val category by categoryViewMoel.categories.collectAsState()
    val blogViewModel: BlogViewModel = viewModel()
    val blogs by blogViewModel.blogs.collectAsState()

    LaunchedEffect(Unit) {
        productViewModel.loadAllProducts()
        productImageViewModel.loadAllProductImages()
        categoryViewMoel.loadAllCategories()
        blogViewModel.loadAllBlogs()
    }

    Log.d("HomeScreen", "products: $products")
    Log.d("HomeScreen", "Image: $images")
    Log.d("HomeScreen", "Category: $category")
    Log.d("HomeScreen", "Blogs: $blogs")


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
//            item {
//                BannerSection1(
//                    onCheckNowClick = { /* TODO: navigate to promotion */ }
//                )
//            }

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
                        .background(Color.White)
                        .wrapContentHeight()
                ) {
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
                        HorizontalItem("Edifier", "https://logo.clearbit.com/edifier.com"),
                        HorizontalItem("Aula", "https://www.aulastar.com/uploads/allimg/20230921/1-230921155F0X4.png"), // ít phổ biến, dùng domain chính thức nếu có
                        HorizontalItem("Viewsonic", "https://logo.clearbit.com/viewsonic.com"),
                        HorizontalItem("GVN", "https://sstc.tech/cdn/shop/files/SSTC_Logo_-_480-100.png?v=1693578888&width=140"), // GearVN custom
                        HorizontalItem("Steelseries", "https://logo.clearbit.com/steelseries.com"),
                        HorizontalItem("DareU", "https://logo.clearbit.com/dareu.com"),
                        HorizontalItem("AKKO", "https://logo.clearbit.com/akkogear.com"),
                        HorizontalItem("NVIDIA", "https://logo.clearbit.com/nvidia.com"),
                        HorizontalItem("GIGABYTE", "https://logo.clearbit.com/gigabyte.com"),
                        HorizontalItem("Durgod", "https://logo.clearbit.com/durgod.com"),
                        HorizontalItem("Rapoo", "https://logo.clearbit.com/rapoo.com"),
                        HorizontalItem("Kingston", "https://logo.clearbit.com/kingston.com"),
                        HorizontalItem("SSTC", "https://sstc.tech/cdn/shop/files/SSTC_Logo_-_480-100.png?v=1693578888&width=140"), // storage brand (taiwan domain)
                        HorizontalItem("SOUNARC", "https://logo.clearbit.com/sounarc.com"),
                        HorizontalItem("PNY", "https://logo.clearbit.com/pny.com")
                    )


                    ItemList(title = stringResource(R.string.brand), items = brands) { clicked ->
                        println("Clicked: ${clicked.name}")
                        // hoặc navController.navigate("brand/${clicked.name}")
                    }

                }
            }

            item{
                Spacer(modifier = Modifier.height(8.dp))
            }

            val categoryIds = listOf(1,2,3,4,5,6,7,8,9,10,11,12)

            items(categoryIds) { id ->
                ProductRegion(
                    title = stringResource(R.string.category_ads, categoryNameRes(id)),
                    categories = listOf(),
                    products = products.filter { it.category_id == id },
                    images = images,
                    onViewAllClick = { /* TODO */ },
                    onProductClick = { product ->
                        navController.navigate("product/${product.product_id}")
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .wrapContentHeight()
                ) {
                    // Chuyển category (từ ViewModel) sang HorizontalItem để truyền vào ItemList
                    val items = category.map {
                        HorizontalItem(
                            name = it.category_name,
                            iconUrl = when (it.category_name) {
                                "Laptop" -> "https://file.hstatic.net/200000636033/file/icon1_ce115f32db874a8e9b5af39517176e96.png"
                                "Máy tính bàn" -> "https://file.hstatic.net/200000636033/file/icon3_5c59c1dc52ec4b81a94a3edba293e895.png"
                                "Màn hình" -> "https://product.hstatic.net/200000722513/product/asus_pg27aqdm_gearvn_53c46bd0ca1f40f1a7abfb0246800081_e341bb95b0724bee845ba8f093678245_master.jpg"
                                "Bàn phím" -> "https://file.hstatic.net/200000722513/file/ban_phim_93a4d3cefd8345dfac23829818a3c5d4.jpg"
                                "Chuột" -> "https://file.hstatic.net/200000722513/file/chuot_aa348bf0177b4795a39ab66d51e62ed7.jpg"
                                "Tai nghe" -> "https://file.hstatic.net/200000722513/file/tai_nghe_ed3b4f52172f40929e1d3ab493099b73.jpg"
                                "Loa" -> "https://file.hstatic.net/200000636033/file/icon10_bfdf42150dbf45cfbcdf990b26f59691.png"
                                "Ổ cứng" -> "https://file.hstatic.net/200000636033/file/icon11_2f0ea4c77ae3482f906591cec8f24cea.png"
                                "RAM" -> "https://file.hstatic.net/200000636033/file/icon13_708c31c3ba56430dbec3f4cc7e1b14f0.png"
                                "Card đồ họa" -> "https://file.hstatic.net/200000722513/file/asus-rog-strix-rtx4090-o24g-gaming-03_c948a4c2a9cf4adcbd522319bfcd4846.jpg"
                                "Mainboard" -> "https://file.hstatic.net/200000636033/file/icon5_71200675c9e64c32a11730486ba04b32.png"
                                "CPU" -> "https://file.hstatic.net/200000636033/file/icon6_056974287cd84e0d82eac05809b7e5d5.png"
                                else -> "https://via.placeholder.com/150"
                            }
                        )
                    }
                    ItemList(
                        title = stringResource(R.string.category),
                        items = items
                    ) { clicked ->
                        println("Clicked: ${clicked.name}")
                        // navController.navigate("category/${clicked.name}")
                    }
                }
            }

            item{
                Spacer(modifier = Modifier.height(8.dp))
            }

            item {
                NewsList(
                    title = "Tin tức công nghệ",
                    blogs = blogs.take(4),
                    onBlogClick = { blog ->
                        navController.navigate("newsDetails/${blog.blog_id}")
                    },
                    onViewAllClick = {
                        navController.navigate("news")
                    }
                )
            }

            item{
                Spacer(modifier = Modifier.height(8.dp))
            }

            item {
                Footer()
            }

            item{
                Spacer(modifier = Modifier.height(8.dp))
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
fun categoryNameRes(categoryId: Int): String {
    val context = LocalContext.current
    val resId = context.resources.getIdentifier(
        "cat_$categoryId", // tên resource: cat_1, cat_2, ...
        "string",
        context.packageName
    )
    return if (resId != 0) stringResource(resId) else "Unknown"
}
