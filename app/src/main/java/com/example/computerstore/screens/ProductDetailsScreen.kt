package com.example.computerstore.screens

import android.content.Context
import android.util.Log
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
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
import com.example.computerstore.viewmodel.*
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailsScreen(
    productId: Int,
    navController: NavController,
    productViewModel: ProductViewModel = viewModel(),
    variantViewModel: ProductVariantViewModel = viewModel(),
    specViewModel: ProductSpecificationViewModel = viewModel(),
    imageViewModel: ProductImageViewModel = viewModel(),
    cartViewModel: CartViewModel = viewModel()
) {
    val product by productViewModel.currentProduct.collectAsState()
    val variants by variantViewModel.variants.collectAsState()
    val specs by specViewModel.specs.collectAsState()
    val images by imageViewModel.productImages.collectAsState()

    val selectedVariant = variants.firstOrNull { it.is_default == 1 } ?: variants.firstOrNull()

    var quantity by remember { mutableStateOf(1) }

    val auth = FirebaseAuth.getInstance()
    val userId = auth.currentUser?.uid.toString()

    // Load dữ liệu
    LaunchedEffect(productId) {
        productViewModel.loadProduct(productId)
        variantViewModel.loadVariantsByProduct(productId)
        specViewModel.loadSpecificationsByProduct(productId)
        imageViewModel.loadProductImagesByProduct(productId)
    }

    Log.d("ProductDetailsScreen", "Product: $product")
    Log.d("ProductDetailsScreen", "Variant: $selectedVariant")
    Log.d("ProductDetailsScreen", "Specs: $specs")
    Log.d("ProductDetailsScreen", "Images: $images")
    Log.d("ProductDetailsScreen", "UserID: $userId")

    Scaffold(
        bottomBar = {
            BottomBarShopee(
                onChat = { /* TODO mở chat */ },
                onAddToCart = {
                    if (product != null && selectedVariant != null) {
                        cartViewModel.addCart(product!!, selectedVariant, quantity, userId = userId)
                    }
                },
                onBuyNow = {
                    if (product != null && selectedVariant != null) {
                        cartViewModel.addCart(product!!, selectedVariant, quantity, userId = userId)
                        navController.navigate("checkout")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(Color(0xFFF5F5F5)),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Ảnh sản phẩm
            item {
                ProductImageSliderShopee(
                    productName = product?.product_name ?: "",
                    variant = selectedVariant,
                    images = images
                )
            }

            // Tên + Giá
            item {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .padding(12.dp)
                ) {
                    Text(product?.product_name ?: "", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
                    Spacer(Modifier.height(8.dp))

                    if (product != null && selectedVariant != null) {
                        PriceDisplayShopee(product!!.base_price.toDouble(), selectedVariant)
                        Text(
                            text = "Kho: ${selectedVariant.stock_quantity ?: 0}",
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    }
                }
            }

            // Số lượng
            item {
                QuantitySelectorShopee(
                    quantity = quantity,
                    onQuantityChange = { quantity = it },
                )
            }

            Log.d("DescriptionHtml", "Description: ${product?.description ?: "null"}")

            // Mô tả
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

            // Thông số kỹ thuật
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

@Composable
fun ProductImageSliderShopee(
    productName: String,
    variant: ProductVariant?,
    images: List<ProductImage>
) {
    val allImages = buildList {
        if (!variant?.image_url.isNullOrEmpty()) add(ProductImage(image_url = variant.image_url!!))
        addAll(images)
    }

    if (allImages.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .background(Color.LightGray),
            contentAlignment = Alignment.Center
        ) {
            Text("Không có ảnh sản phẩm", color = Color.White)
        }
    } else {
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(allImages) { img ->
                AsyncImage(
                    model = img.image_url,
                    contentDescription = productName,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillParentMaxWidth()
                        .clip(RoundedCornerShape(0.dp))
                )
            }
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
fun BottomBarShopee(
    onChat: () -> Unit,
    onAddToCart: () -> Unit,
    onBuyNow: () -> Unit
) {
    Row(
        Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onChat, modifier = Modifier.weight(0.5f)) {
            Icon(Icons.Default.Chat, contentDescription = "Chat ngay", tint = Color.Red)
        }
        IconButton(onClick = onAddToCart, modifier = Modifier.weight(0.5f)) {
            Icon(Icons.Default.ShoppingCart, contentDescription = "Thêm giỏ hàng", tint = Color.Red)
        }
        Button(
            onClick = onBuyNow,
            modifier = Modifier.weight(2f),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
        ) {
            Text("Mua ngay", fontWeight = FontWeight.Bold, color = Color.White)
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
fun SpecTable(specs: List<ProductSpecification>) {
    if (specs.isEmpty()) {
        Text("Chưa có thông số kỹ thuật", Modifier.padding(12.dp).fillMaxWidth())
    } else {
        Column(Modifier.padding(12.dp)) {
            specs.forEach {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(it.spec_name, fontWeight = FontWeight.SemiBold)
                    Text(it.spec_value)
                }
                Divider()
            }
        }
    }
}
