package com.example.computerstore.screens

import android.util.Log
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.overscroll
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.computerstore.data.model.ProductImage
import com.example.computerstore.data.model.ProductSpecification
import com.example.computerstore.data.model.ProductVariant
import com.example.computerstore.screens.buttons.BackButton
import com.example.computerstore.screens.components.BottomBarShopee
import com.example.computerstore.screens.components.PromoSection
import com.example.computerstore.viewmodel.*
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay
import androidx.compose.runtime.collectAsState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailsScreen(
    productId: Int,
    navController: NavController,
    productViewModel: ProductViewModel = viewModel(),
    variantViewModel: ProductVariantViewModel = viewModel(),
    specViewModel: ProductSpecificationViewModel = viewModel(),
    imageViewModel: ProductImageViewModel = viewModel(),
    cartViewModel: CartViewModel = viewModel(),
    productImageViewModel: ProductImageViewModel = viewModel(),
    categoryViewMoel: CategoryViewModel = viewModel(),
    onBackClick: () -> Unit = {}
) {
    val product by productViewModel.currentProduct.collectAsState()
    val variants by variantViewModel.variants.collectAsState()
    val specs by specViewModel.specs.collectAsState()
    val images by imageViewModel.productImages.collectAsState()

    // biến thể mặc định
    val defaultVariant = variants.firstOrNull { it.is_default == 1 } ?: variants.firstOrNull()
    var selectedVariant by remember { mutableStateOf(defaultVariant) }
    var quantity by remember { mutableStateOf(1) }

    val auth = FirebaseAuth.getInstance()
    val userId = auth.currentUser?.uid.toString()

    // Load dữ liệu khi mở màn
    LaunchedEffect(productId) {
        productViewModel.loadProduct(productId)
        variantViewModel.loadVariantsByProduct(productId)
        specViewModel.loadSpecificationsByProduct(productId)
        imageViewModel.loadProductImagesByProduct(productId)
        productViewModel.loadAllProducts()
        productImageViewModel.loadAllProductImages()
        categoryViewMoel.loadAllCategories()
    }

    Log.d("ProductDetailsScreen", "🟢 Product: $product")
    Log.d("ProductDetailsScreen", "🟢 Variant: $selectedVariant")
    Log.d("ProductDetailsScreen", "🟢 Specs: $specs")
    Log.d("ProductDetailsScreen", "🟢 Images: $images")
    Log.d("ProductDetailsScreen", "🟢 UserID: $userId")

    val scope = rememberCoroutineScope()

    Scaffold(
        bottomBar = {
            BottomBarShopee(
                onChat = { /* TODO mở chat */ },
                onAddToCart = {
                    if (product != null && selectedVariant != null) {
                        scope.launch {
                            cartViewModel.addOrUpdateCart(
                                product = product!!,
                                variant = selectedVariant!!,
                                quantity = quantity,
                                userId = userId
                            )
                        }
                    }
                },
                onBuyNow = {
                    if (product != null && selectedVariant != null) {
                        scope.launch {
                            val addedCartId = cartViewModel.addOrUpdateCart(
                                product = product!!,
                                variant = selectedVariant!!,
                                quantity = quantity,
                                userId = userId
                            )
                            // ✅ Tick sản phẩm vừa thêm
                            cartViewModel.selectCartItem(addedCartId)
                            // ✅ Điều hướng sang màn giỏ hàng
                            navController.navigate("cart") {
                                launchSingleTop = true
                            }
                        }
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(padding),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            // --- Ảnh sản phẩm ---
            item {
                val filteredImages = images.filter { it.product_id?.toInt() == productId }

                ProductImageSliderShopee(
                    productName = product?.product_name ?: "",
                    variant = selectedVariant,
                    images = filteredImages,
                    onBackClick = onBackClick
                )
            }

            // --- Tên + Giá ---
            item {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .padding(12.dp)
                ) {
                    Text(
                        text = product?.product_name ?: "",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(Modifier.height(8.dp))

                    if (product != null && selectedVariant != null) {
                        PriceDisplayShopee(product!!.base_price.toDouble(), selectedVariant!!)
                    }
                }
            }

            // --- Danh sách biến thể (Variants) ---
            item {
                if (variants.isNotEmpty()) {
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .background(Color.White)
                            .padding(horizontal = 12.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = "Phiên bản / Dung lượng:",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.Black
                        )
                        Spacer(Modifier.height(8.dp))

                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            items(variants) { variant ->
                                val isSelected = variant.variant_id == selectedVariant?.variant_id

                                Box(
                                    modifier = Modifier
                                        .border(
                                            width = if (isSelected) 2.dp else 1.dp,
                                            color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Gray.copy(alpha = 0.4f),
                                            shape = RoundedCornerShape(8.dp)
                                        )
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(if (isSelected) Color(0xFFE3F2FD) else Color.White)
                                        .clickable { selectedVariant = variant }
                                        .padding(horizontal = 12.dp, vertical = 8.dp)
                                ) {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text(
                                            text = variant.variant_sku ?: "Phiên bản ${variant.variant_id}",
                                            color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Black,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                            fontSize = 13.sp
                                        )
                                        if (variant.price_adjustment != null) {
                                            Text(
                                                text = "${"%,.0f".format(product?.base_price?.plus(variant.price_adjustment ?: 0.0) ?: 0.0)} đ",
                                                fontSize = 12.sp,
                                                color = Color.Gray
                                            )
                                        }
                                    }
                                }
                            }
                        }

//                        Spacer(Modifier.height(8.dp))
//                        Text(
//                            text = "Kho: ${selectedVariant?.stock_quantity ?: 0} sản phẩm",
//                            fontSize = 13.sp,
//                            color = Color.Gray
//                        )
                    }
                }
            }

            // --- Khuyến mãi ---
            item {
                PromoSection()
            }

            // --- Sản phẩm tương tự ---
            item {
                val relatedProducts = productViewModel.products.value
                    .filter { it.category_id == product?.category_id && it.product_id != product?.product_id }
                    .take(10)

                val relatedImages = imageViewModel.productImages.value.filter { img ->
                    relatedProducts.any { it.product_id == img.product_id?.toInt() }
                }

                if (relatedProducts.isNotEmpty()) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White)
                            .padding(vertical = 12.dp)
                    ) {
                        Text(
                            text = "Sản phẩm tương tự",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            modifier = Modifier.padding(start = 12.dp, bottom = 8.dp)
                        )

                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 12.dp)
                                .overscroll(overscrollEffect = null)
                        ) {
                            items(relatedProducts) { related ->
                                val imageUrl = relatedImages.find {
                                    it.product_id?.toInt() == related.product_id && it.is_primary == 1
                                }?.image_url

                                ProductCard2(
                                    product = related,
                                    imageUrl = imageUrl,
                                    specs = specViewModel.specs.value.filter { it.product_id == related.product_id },
                                    onClick = {
                                        navController.navigate("product/${related.product_id}")
                                    }
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            }

            // --- Mô tả sản phẩm ---
            item {
                Column(
                    Modifier
                        .background(Color.White)
                        .padding(12.dp)
                ) {
                    Text("Mô tả sản phẩm", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(8.dp))
                    DescriptionHtml(product?.description ?: "")
                }
            }

            // --- Thông số kỹ thuật ---
            item {
                Column(
                    Modifier
                        .background(Color.White)
                        .padding(12.dp)
                ) {
                    Text("Thông số kỹ thuật", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    SpecTable(specs ?: emptyList())
                }
            }
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ProductImageSliderShopee(
    productName: String,
    variant: ProductVariant?,
    images: List<ProductImage>,
    onBackClick: (() -> Unit)? = null
) {
    val allImages = buildList {
        if (!variant?.image_url.isNullOrEmpty())
            add(ProductImage(image_url = variant.image_url!!))
        addAll(images)
    }

    if (allImages.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(260.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0xFFE0E0E0)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Không có ảnh sản phẩm",
                color = Color.DarkGray,
                fontStyle = FontStyle.Italic
            )
        }
        return
    }

    val pagerState = rememberPagerState { allImages.size }

    // Auto scroll mỗi 4 giây
    LaunchedEffect(pagerState) {
        while (true) {
            delay(4000)
            val nextPage = (pagerState.currentPage + 1) % allImages.size
            pagerState.animateScrollToPage(nextPage)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
            .clip(RoundedCornerShape(8.dp))
    ) {
        // --- Ảnh trượt ---
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize(),
            pageSpacing = 8.dp
        ) { page ->
            AsyncImage(
                model = allImages[page].image_url,
                contentDescription = productName,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(8.dp))
            )
        }

        // --- Nút Back ở góc trái trên ảnh ---
        if (onBackClick != null) {
            BackButton(
                onClick = onBackClick,
                backgroundColor = Color.Black.copy(alpha = 0.4f),
                iconTint = Color.White,
                size = 44,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(10.dp)
            )
        }

        // --- Indicator dots (dưới giữa) ---
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(allImages.size) { index ->
                val isSelected = pagerState.currentPage == index
                Box(
                    modifier = Modifier
                        .padding(3.dp)
                        .size(if (isSelected) 10.dp else 8.dp)
                        .clip(CircleShape)
                        .background(
                            if (isSelected)
                                MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                        )
                        .animateContentSize()
                )
            }
        }

        // --- Số trang ảnh (x / tổng) ---
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(10.dp)
                .clip(RoundedCornerShape(50.dp))
                .background(Color.Black.copy(alpha = 0.5f))
                .padding(horizontal = 10.dp, vertical = 4.dp)
        ) {
            Text(
                text = "${pagerState.currentPage + 1} / ${allImages.size}",
                color = Color.White,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}


@Composable
fun PriceDisplayShopee(basePrice: Double, variant: ProductVariant) {
    val finalPrice = basePrice + (variant.price_adjustment ?: 0.0)

    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = "${"%,.0f".format(finalPrice)} đ",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Red
        )
        Spacer(Modifier.width(8.dp))

        Text(
            text = "${"%,.0f".format(basePrice)} đ",
            fontSize = 14.sp,
            color = Color.Gray,
            textDecoration = TextDecoration.LineThrough
        )

        Spacer(Modifier.width(8.dp))

        val discountPercent = if (basePrice > 0) {
            ((basePrice - finalPrice) / basePrice * 100).toInt()
        } else 0

        if (discountPercent > 0) {
            Text(
                text = "-$discountPercent%",
                color = Color.Red,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun QuantitySelectorShopee(quantity: Int, onQuantityChange: (Int) -> Unit) {
    Row(
        Modifier
            .background(Color.White)
            .padding(12.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("Số lượng: ")
        Spacer(Modifier.width(8.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Button(
                onClick = { if (quantity > 1) onQuantityChange(quantity - 1) },
                contentPadding = PaddingValues(0.dp),
                modifier = Modifier.size(32.dp)
            ) { Text("-") }
            Text("$quantity", Modifier.padding(horizontal = 12.dp), fontSize = 16.sp)
            Button(
                onClick = { onQuantityChange(quantity + 1) },
                contentPadding = PaddingValues(0.dp),
                modifier = Modifier.size(32.dp)
            ) { Text("+") }
        }
    }
}

@Composable
fun DescriptionHtml(htmlContent: String) {
    AndroidView(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(8.dp),
        factory = { context ->
            WebView(context).apply {
                settings.javaScriptEnabled = false
                settings.domStorageEnabled = true
                settings.cacheMode = WebSettings.LOAD_NO_CACHE
                webViewClient = WebViewClient()

                // CSS để fit màn hình
                val styledHtml = """
                    <html>
                    <head>
                      <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
                      <style>
                        body { 
                          font-family: sans-serif; 
                          font-size: 14px; 
                          padding: 8px; 
                          text-align: justify; 
                          margin:0;
                        }
                        img { 
                          max-width: 100% !important; 
                          height: auto !important; 
                          border-radius: 6px; 
                          display: block;
                          margin: 8px auto;
                        }
                        table {
                          width: 100% !important;
                          border-collapse: collapse;
                        }
                        td {
                          border: 1px solid #ddd;
                          padding: 6px;
                        }
                      </style>
                    </head>
                    <body>$htmlContent</body>
                    </html>
                """.trimIndent()

                loadDataWithBaseURL(null, styledHtml, "text/html", "UTF-8", null)
            }
        },
        update = { webView ->
            val styledHtml = """
                <html>
                <head>
                  <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
                  <style>
                    body { font-family: sans-serif; font-size: 14px; padding: 8px; text-align: justify; margin:0; }
                    img { max-width: 100% !important; height: auto !important; border-radius: 6px; display: block; margin: 8px auto; }
                    table { width: 100% !important; border-collapse: collapse; }
                    td { border: 1px solid #ddd; padding: 6px; }
                  </style>
                </head>
                <body>$htmlContent</body>
                </html>
            """.trimIndent()
            webView.loadDataWithBaseURL(null, styledHtml, "text/html", "UTF-8", null)
        }
    )
}


@Composable
fun SpecTable(
    specs: List<ProductSpecification>,
    modifier: Modifier = Modifier
) {
    if (specs.isEmpty()) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Chưa có thông số kỹ thuật",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = Color.Gray,
                    fontStyle = FontStyle.Italic
                )
            )
        }
    } else {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(12.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color.White)
                .padding(vertical = 8.dp)
        ) {
            specs.forEachIndexed { index, spec ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = spec.spec_name,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface
                        ),
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = spec.spec_value,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.primary
                        ),
                        textAlign = TextAlign.End,
                        modifier = Modifier.weight(1f)
                    )
                }

                if (index < specs.lastIndex) {
                    Divider(
                        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                        thickness = 0.6.dp,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
            }
        }
    }
}

