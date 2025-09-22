package com.example.computerstore.screens.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.overscroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

data class ViewedProduct(
    val name: String,
    val imageUrl: String,
    val oldPrice: String,
    val newPrice: String,
    val discountPercent: String
)

@Composable
fun ViewedProductsSection(
    title: String = "Sản phẩm đã xem",
    products: List<ViewedProduct>,
    onProductClick: (ViewedProduct) -> Unit // callback khi click vào card
) {
    Column(modifier = Modifier.fillMaxWidth().background(Color.White).height(250.dp)) {
        Text(
            text = title,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(8.dp)
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth().padding(start = 8.dp, end = 8.dp).overscroll(overscrollEffect  = null)
        ) {
            items(products) { product ->
                ProductCard(product, onClick = { onProductClick(product) })
            }
        }
    }
}

@Composable
fun ProductCard(product: ViewedProduct, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .width(150.dp)
            .height(200.dp)
            .border(0.5.dp, Color.LightGray, RoundedCornerShape(8.dp))
            .clickable { onClick() }, // handle click ở đây
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
//        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            AsyncImage(
                model = product.imageUrl,
                contentDescription = product.name,
                modifier = Modifier
                    .height(80.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(6.dp))
                    .background(Color.White)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = product.name,
                fontSize = 13.sp,
                maxLines = 2,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = product.oldPrice,
                fontSize = 12.sp,
                color = Color.Gray,
                textDecoration = TextDecoration.LineThrough
            )

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = product.newPrice,
                    fontSize = 14.sp,
                    color = Color(0xFFE30000),
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(4.dp))
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(Color(0xFFFFEBEE))
                        .padding(horizontal = 4.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "-${product.discountPercent}",
                        fontSize = 12.sp,
                        color = Color(0xFFE30000),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
