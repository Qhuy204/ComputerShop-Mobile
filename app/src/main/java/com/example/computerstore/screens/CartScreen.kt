package com.example.computerstore.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.computerstore.data.model.Cart
import com.example.computerstore.data.model.Product
import com.example.computerstore.data.model.ProductVariant
import com.example.computerstore.data.model.ProductImage
import com.example.computerstore.viewmodel.CartViewModel
import com.example.computerstore.viewmodel.ProductViewModel
import com.example.computerstore.viewmodel.ProductVariantViewModel
import com.example.computerstore.viewmodel.ProductImageViewModel
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    cartViewModel: CartViewModel = viewModel(),
    productViewModel: ProductViewModel = viewModel(),
    variantViewModel: ProductVariantViewModel = viewModel(),
    imageViewModel: ProductImageViewModel = viewModel(),
    onCheckoutClick: () -> Unit = {}
) {
    val carts by cartViewModel.carts.collectAsState()
    val products by productViewModel.products.collectAsState()
    val variants by variantViewModel.productVariants.collectAsState()
    val images by imageViewModel.productImages.collectAsState()

    // cart_id giờ là String
    val selectedIds = remember { mutableStateListOf<String>() }

    // Tổng tiền chỉ tính cho sản phẩm được chọn
    val totalPrice = carts.filter { selectedIds.contains(it.cart_id ?: "") }.sumOf { cart ->
        val product = products.find { it.product_id == cart.product_id }
        val variant = variants.find { it.variant_id == cart.variant_id }
        val price = (product?.base_price ?: 0.0) + (variant?.price_adjustment ?: 0.0)
        price * cart.quantity
    }

    // Lấy user hiện tại
    val userId = FirebaseAuth.getInstance().currentUser?.uid
    LaunchedEffect(userId) {
        userId?.let { cartViewModel.loadCartsByUser(it) }
        productViewModel.loadAllProducts()
        variantViewModel.loadAllProductVariants()
        imageViewModel.loadAllProductImages()
    }

    Log.d("CartScreen", "Carts: $carts")

    Scaffold(
        topBar = { TopAppBar(title = { Text("Giỏ hàng") }) },
        bottomBar = {
            if (carts.isNotEmpty()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(
                            checked = selectedIds.size == carts.size,
                            onCheckedChange = { checked ->
                                if (checked) {
                                    selectedIds.clear()
                                    selectedIds.addAll(carts.mapNotNull { it.cart_id })
                                } else selectedIds.clear()
                            }
                        )
                        Text("Tất cả")
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "Tổng: ${"%,.0f".format(totalPrice)} đ",
                            fontWeight = FontWeight.Bold,
                            color = Color.Red
                        )
                        Button(
                            onClick = onCheckoutClick,
                            modifier = Modifier.height(40.dp),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                        ) {
                            Text("Mua hàng (${selectedIds.size})", color = Color.White)
                        }
                    }
                }
            }
        }
    ) { padding ->
        if (carts.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) { Text("Giỏ hàng trống") }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                items(carts, key = { it.cart_id ?: "" }) { cart ->
                    CartItemRowShopee(
                        cart = cart,
                        selected = selectedIds.contains(cart.cart_id),
                        onSelect = { checked ->
                            cart.cart_id?.let { id ->
                                if (checked) selectedIds.add(id)
                                else selectedIds.remove(id)
                            }
                        },
                        onIncrease = {
                            cart.cart_id?.let { id ->
                                cartViewModel.increaseQuantity(cart)
                            }
                        },
                        onDecrease = {
                            cart.cart_id?.let { id ->
                                cartViewModel.decreaseQuantity(cart)
                            }
                        },
                        onDelete = {
                            cart.cart_id?.let { id ->
                                val uid = FirebaseAuth.getInstance().currentUser?.uid
                                if (uid != null) {
                                    cartViewModel.deleteCart(id, uid)
                                    selectedIds.remove(id)
                                }
                            }
                        },
                        products = products,
                        variants = variants,
                        images = images
                    )
                    Divider()
                }
            }
        }
    }
}

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
    images: List<ProductImage>
) {
    val product = products.find { it.product_id == cart.product_id }
    val variant = variants.find { it.variant_id == cart.variant_id }
    val image = images.find { it.product_id?.toInt() == cart.product_id && it.is_primary == 1 }

    val price = (product?.base_price ?: 0.0) + (variant?.price_adjustment ?: 0.0)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(checked = selected, onCheckedChange = onSelect)

        AsyncImage(
            model = image?.image_url ?: variant?.image_url ?: "",
            contentDescription = product?.product_name ?: "",
            modifier = Modifier
                .size(80.dp)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )

        Spacer(Modifier.width(8.dp))

        Column(Modifier.weight(1f)) {
            Text(product?.product_name ?: "Sản phẩm", fontWeight = FontWeight.SemiBold)
            variant?.let { Text(it.variant_sku, style = MaterialTheme.typography.bodySmall, color = Color.Gray) }
            Text("${"%,.0f".format(price)} đ", color = Color.Red, fontWeight = FontWeight.Bold)

            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onDecrease) { Icon(Icons.Default.Remove, contentDescription = "Giảm") }
                Text("${cart.quantity}")
                IconButton(onClick = onIncrease) { Icon(Icons.Default.Add, contentDescription = "Tăng") }
            }
        }

        IconButton(onClick = onDelete) { Icon(Icons.Default.Delete, contentDescription = "Xóa") }
    }
}
