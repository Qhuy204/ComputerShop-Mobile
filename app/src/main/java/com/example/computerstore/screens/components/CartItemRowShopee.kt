package com.example.computerstore.screens.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.computerstore.R
import com.example.computerstore.data.model.Cart
import com.example.computerstore.data.model.Product
import com.example.computerstore.data.model.ProductImage
import com.example.computerstore.data.model.ProductVariant
import com.example.computerstore.screens.dialog.ConfirmDeleteDialog

@Composable
fun CartItemRowShopee(
    cart: Cart,
    selected: Boolean,
    onSelect: (Boolean) -> Unit,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit,
    onDelete: () -> Unit,
    products: List<Product>,
    variants: List<ProductVariant>,
    images: List<ProductImage>,
    onItemClick: (Int) -> Unit
) {
    val product = products.find { it.product_id == cart.product_id }
    val variant = variants.find { it.variant_id == cart.variant_id }
    val image = images.find { it.product_id?.toInt() == cart.product_id && it.is_primary == 1 }

    val price = (product?.base_price ?: 0.0) + (variant?.price_adjustment ?: 0.0)

    var showDeleteDialog by remember { mutableStateOf(false) }

    if (showDeleteDialog) {
        ConfirmDeleteDialog(
            onDismiss = { showDeleteDialog = false },
            onConfirm = {
                showDeleteDialog = false
                onDelete()
            }
        )
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White)
            .clickable { product?.product_id?.let { onItemClick(it) } },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(Modifier.width(8.dp))

        CustomCheckbox(checked = selected, onCheckedChange = onSelect)

        Spacer(Modifier.width(8.dp))

        AsyncImage(
            model = image?.image_url ?: variant?.image_url ?: "",
            contentDescription = product?.product_name ?: "",
            modifier = Modifier
                .size(100.dp)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )

        Spacer(Modifier.width(8.dp))

        Column(Modifier.weight(1f)) {
            Text(
                text = product?.product_name ?: "Sản phẩm",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(top = 4.dp)
            )
            variant?.let { Text(it.variant_sku, style = MaterialTheme.typography.bodySmall, color = Color.Gray) }
            Text("${"%,.0f".format(price)} đ", color = Color.Red, fontWeight = FontWeight.Bold)

            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .border(width = 0.5.dp, color = Color.LightGray, shape = RoundedCornerShape(8.dp))
                    .background(Color(0xFFf6f6f6))
                    .height(30.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {
                        if (cart.quantity <= 1) {
                            showDeleteDialog = true
                        } else {
                            onDecrease()
                        }
                    },
                    modifier = Modifier
                        .border(width = 0.5.dp, color = Color.LightGray)
                        .width(30.dp)
                ) { Icon(Icons.Default.Remove, contentDescription = "Giảm") }

                Text(
                    text = "${cart.quantity}",
                    modifier = Modifier.width(40.dp),
                    textAlign = TextAlign.Center
                )

                IconButton(
                    onClick = onIncrease,
                    modifier = Modifier
                        .border(width = 0.5.dp, color = Color.LightGray)
                        .width(30.dp)
                ) { Icon(Icons.Default.Add, contentDescription = "Tăng") }
            }
            Spacer(Modifier.height(8.dp))
        }

        IconButton(onClick = { showDeleteDialog = true }) {
            Icon(
                painter = painterResource(id = R.drawable.recycle_bin),
                contentDescription = "Xóa",
                modifier = Modifier.size(20.dp),
                tint = Color.Red
            )
        }

    }
}

