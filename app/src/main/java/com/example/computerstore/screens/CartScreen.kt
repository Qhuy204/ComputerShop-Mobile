package com.example.computerstore.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.computerstore.R
import com.example.computerstore.screens.components.CartItemRowShopee
import com.example.computerstore.screens.components.CustomCheckbox
import com.example.computerstore.screens.components.CustomTopBar
import com.example.computerstore.viewmodel.CartViewModel
import com.example.computerstore.viewmodel.ProductImageViewModel
import com.example.computerstore.viewmodel.ProductVariantViewModel
import com.example.computerstore.viewmodel.ProductViewModel
import com.google.firebase.auth.FirebaseAuth

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
    val selectedCartItemId by cartViewModel.selectedCartItemId.collectAsState()

    // ✅ Danh sách các ID được chọn
    val selectedIds = remember { mutableStateListOf<String>() }

    // ✅ Tự động tick item được chọn khi “Buy Now”
    LaunchedEffect(selectedCartItemId, carts) {
        selectedIds.clear()
        selectedCartItemId?.let { id ->
            if (carts.any { it.cart_id == id }) {
                selectedIds.add(id)
            }
        }
    }

    // ✅ Tính tổng tiền
    val totalPrice = carts.filter { selectedIds.contains(it.cart_id ?: "") }.sumOf { cart ->
        val product = products.find { it.product_id == cart.product_id }
        val variant = variants.find { it.variant_id == cart.variant_id }
        val price = (product?.base_price ?: 0.0) + (variant?.price_adjustment ?: 0.0)
        price * cart.quantity
    }

    // ✅ Load dữ liệu khi mở màn hình
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
                    // --- Checkbox chọn tất cả ---
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        CustomCheckbox(
                            checked = selectedIds.size == carts.size && carts.isNotEmpty(),
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

                    // --- Tổng tiền + Nút mua hàng ---
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "Tổng: ${"%,.0f".format(totalPrice)} đ",
                            fontWeight = FontWeight.Bold,
                            color = Color.Red
                        )
                        Button(
                            onClick = {
                                if (selectedIds.isNotEmpty()) {
                                    val joinedIds = selectedIds.joinToString(",")
                                    navController.navigate("checkout/$joinedIds")
                                }
                            },
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
            // --- Giỏ hàng trống ---
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Giỏ hàng trống")
            }
        } else {
            // --- Danh sách sản phẩm trong giỏ ---
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
                            navController.navigate("product/$productId")
                        }
                    )
                }
            }
        }
    }
}
