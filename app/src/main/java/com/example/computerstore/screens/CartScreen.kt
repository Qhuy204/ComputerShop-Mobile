package com.example.computerstore.screens

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.example.computerstore.data.model.Cart
import com.example.computerstore.data.model.Product
import com.example.computerstore.data.model.ProductVariant
import com.example.computerstore.data.model.ProductImage
import com.example.computerstore.screens.components.CustomTopBar
import com.example.computerstore.viewmodel.CartViewModel
import com.example.computerstore.viewmodel.ProductViewModel
import com.example.computerstore.viewmodel.ProductVariantViewModel
import com.example.computerstore.viewmodel.ProductImageViewModel
import com.google.firebase.auth.FirebaseAuth
import com.example.computerstore.R
import com.example.computerstore.screens.components.CustomCheckbox

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    navController: NavController,
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

    val selectedIds = remember { mutableStateListOf<String>() }

    val totalPrice = carts.filter { selectedIds.contains(it.cart_id ?: "") }.sumOf { cart ->
        val product = products.find { it.product_id == cart.product_id }
        val variant = variants.find { it.variant_id == cart.variant_id }
        val price = (product?.base_price ?: 0.0) + (variant?.price_adjustment ?: 0.0)
        price * cart.quantity
    }

    val userId = FirebaseAuth.getInstance().currentUser?.uid
    LaunchedEffect(userId) {
        userId?.let { cartViewModel.loadCartsByUser(it) }
        productViewModel.loadAllProducts()
        variantViewModel.loadAllProductVariants()
        imageViewModel.loadAllProductImages()
    }

    Log.d("CartScreen", "Carts: $carts")

    Scaffold(
        topBar = {
            CustomTopBar(
                title = "Giỏ hàng",
                iconRes = R.drawable.leftarrow,
                onBackClick = { navController.popBackStack() }
            )
        },
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
                        CustomCheckbox(
                            checked = selectedIds.size == carts.size,
                            onCheckedChange = { checked ->
                                if (checked) {
                                    selectedIds.clear()
                                    selectedIds.addAll(carts.mapNotNull { it.cart_id })
                                } else selectedIds.clear()
                            }
                        )
                        Spacer(Modifier.width(8.dp))
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
                    .background(Color(0xFFf6f6f6))
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
                        onIncrease = { cartViewModel.increaseQuantity(cart) },
                        onDecrease = { cartViewModel.decreaseQuantity(cart) },
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
                        images = images,
                        onItemClick = { productId ->
                            navController.navigate("productDetails/$productId")
                        }
                    )
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

@Composable
fun ConfirmDeleteDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = Color.White,
            border = BorderStroke(1.dp, Color.LightGray)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .width(300.dp)
            ) {
                Text(
                    text = "Xác nhận",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = "Bạn chắc chắn muốn bỏ sản phẩm này?",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Không")
                    }
                    Spacer(Modifier.width(8.dp))
                    TextButton(onClick = onConfirm) {
                        Text("Đồng ý", color = Color.Red)
                    }
                }
            }
        }
    }
}
