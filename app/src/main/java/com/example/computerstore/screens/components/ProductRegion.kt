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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.computerstore.data.model.Product

@Composable
fun ProductRegion(
    title: String,
    categories: List<String>,
    products: List<Product>,
    onViewAllClick: () -> Unit,
    onProductClick: (Product) -> Unit
) {
    var selectedCategory by remember { mutableStateOf(categories.firstOrNull() ?: "") }

    Card(
        modifier = Modifier
            .fillMaxWidth(),
//            .padding(8.dp), // cách viền ngoài 8dp
        shape = RoundedCornerShape(0.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
//        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Xem tất cả",
                    fontSize = 14.sp,
                    color = Color(0xFFE30000),
                    modifier = Modifier.clickable { onViewAllClick() }
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Tabs filter
            if (categories.isNotEmpty()) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    categories.forEach { category ->
                        FilterChip(
                            selected = selectedCategory == category,
                            onClick = { selectedCategory = category },
                            label = { Text(category) }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Product cards
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth().overscroll(overscrollEffect  = null)
            ) {
                items(products) { product ->
                    ProductCard2(
                        product = product,
                        onClick = { onProductClick(product) }
                    )
                }
            }
        }
    }
}

@Composable
fun ProductCard2(product: Product, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .width(170.dp)
            .border(0.5.dp, Color.LightGray, RoundedCornerShape(8.dp))
            .clickable { onClick() },
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            AsyncImage(
                model = product.description ?: "", // tạm dùng description chứa link ảnh nếu có
                contentDescription = product.product_name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(Color.White)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                product.product_name,
                maxLines = 2,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "${"%,.0f".format(product.base_price)} đ",
                fontSize = 14.sp,
                color = Color(0xFFE30000),
                fontWeight = FontWeight.Bold
            )
        }
    }
}

