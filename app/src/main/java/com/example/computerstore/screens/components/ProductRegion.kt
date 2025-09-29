package com.example.computerstore.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.overscroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.computerstore.R
import com.example.computerstore.data.model.Product
import com.example.computerstore.data.model.ProductSpecification
import com.example.computerstore.viewmodel.CartViewModel
import com.example.computerstore.viewmodel.ProductImageViewModel
import com.example.computerstore.viewmodel.ProductSpecificationViewModel
import com.example.computerstore.viewmodel.ProductVariantViewModel
import com.example.computerstore.viewmodel.ProductViewModel

// map spec_name -> icon drawable
val specIconsByName = mapOf(
    "Màu sắc" to R.drawable.color,
    "Dung lượng RAM" to R.drawable.ram,
    "Dung lượng SSD" to R.drawable.ssd,
    "Kích thước màn hình" to R.drawable.monitor_1,
    "Độ phân giải" to R.drawable.monitor_2,
    "Loại switch" to R.drawable.resource_switch,
    "Loại kết nối" to R.drawable.connection,
    "CPU" to R.drawable.cpu,
    "Card đồ họa" to R.drawable.gpu,
    "Hệ điều hành" to R.drawable.os,
    "Vật liệu" to R.drawable.materials,
    "Tần số quét" to R.drawable.refresh
)

@Composable
fun ProductRegion(
    title: String,
    categories: List<String>,
    products: List<Product>,
    images: List<com.example.computerstore.data.model.ProductImage>,
    onViewAllClick: () -> Unit,
    onProductClick: (Product) -> Unit,
    productViewModel: ProductViewModel = viewModel(),
    variantViewModel: ProductVariantViewModel = viewModel(),
    specViewModel: ProductSpecificationViewModel = viewModel(),
    imageViewModel: ProductImageViewModel = viewModel(),
    cartViewModel: CartViewModel = viewModel()
) {
    val specs by specViewModel.specs.collectAsState()
    val productImages by imageViewModel.productImages.collectAsState()

    LaunchedEffect(Unit) {
        specViewModel.loadAllProductSpecifications()
        imageViewModel.loadAllProductImages()
    }

    Log.d("ProductRegion", "Categories: $categories")
    Log.d("ProductRegion", "Specs: $specs")

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(0.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(title, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Text(
                    text = "Xem tất cả",
                    fontSize = 14.sp,
                    color = Color(0xFFE30000),
                    modifier = Modifier.clickable { onViewAllClick() }
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Product cards
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .overscroll(overscrollEffect = null)
            ) {
                items(products) { product ->
                    val imageUrl = productImages.find {
                        it.product_id?.toInt() == product.product_id && it.is_primary == 1
                    }?.image_url

                    ProductCard2(
                        product = product,
                        imageUrl = imageUrl,
                        specs = specs.filter { it.product_id == product.product_id },
                        onClick = { onProductClick(product) }
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))

        }
    }
}

@Composable
fun ProductCard2(
    product: Product,
    imageUrl: String?,
    specs: List<ProductSpecification>,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(180.dp)
            .border(1.dp, Color.LightGray, RoundedCornerShape(8.dp))
            .clickable { onClick() },
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            // image
            AsyncImage(
                model = imageUrl ?: "https://via.placeholder.com/150",
                contentDescription = product.product_name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(Color.White)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // name
            Text(
                text = product.product_name ?: "",
                maxLines = 2,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(6.dp))

            // specs
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(6.dp))
                    .background(Color(0xFFf5f5f5))
                    .padding(6.dp)
            ) {
                val shownSpecs = specs.take(4)

                // Nếu ít hơn 2 -> bắt buộc hiển thị 2
                val targetCount = if (shownSpecs.size <= 2) 2 else if (shownSpecs.size < 4) 3 else 4
                val missing = targetCount - shownSpecs.size

                shownSpecs.forEach { spec ->
                    val icon = specIconsByName[spec.spec_name] ?: R.drawable.ic_notification_2
                    SpecRow(icon, spec.spec_value)
                }

                repeat(missing) {
                    Spacer(modifier = Modifier.height(20.dp)) // chèn slot trống
                }

            }

            Spacer(modifier = Modifier.height(6.dp))

            // price
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    if (product.base_price > 0) {
                        Text(
                            text = "${"%,.0f".format(product.base_price * 1.15)} đ",
                            fontSize = 12.sp,
                            color = Color.Gray,
                            textDecoration = TextDecoration.LineThrough
                        )
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${"%,.0f".format(product.base_price)} đ",
                        fontSize = 14.sp,
                        color = Color(0xFFE30000),
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "-15%",
                        fontSize = 12.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .background(Color(0xFFE30000), RoundedCornerShape(4.dp))
                            .padding(horizontal = 4.dp, vertical = 2.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun SpecRow(iconRes: Int, text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 2.dp)
    ) {
        Icon(
            painter = painterResource(id = iconRes),
            contentDescription = null,
            tint = Color.Unspecified,
            modifier = Modifier.size(14.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = text,
            fontSize = 12.sp,
            color = Color.Black,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}
