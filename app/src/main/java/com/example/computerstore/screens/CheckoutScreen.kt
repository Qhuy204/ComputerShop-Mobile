package com.example.computerstore.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.SemanticsActions.OnClick
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.computerstore.R
import com.example.computerstore.data.model.*
import com.example.computerstore.screens.components.CustomTopBar
import com.example.computerstore.viewmodel.*
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(
    navController: NavController,
    selectedCartIds: List<String>,
    cartViewModel: CartViewModel = viewModel(),
    productViewModel: ProductViewModel = viewModel(),
    variantViewModel: ProductVariantViewModel = viewModel(),
    imageViewModel: ProductImageViewModel = viewModel(),
    addressViewModel: UserAddressViewModel = viewModel(),
    paymentViewModel: PaymentMethodViewModel = viewModel(),
    orderViewModel: OrderViewModel = viewModel(),
    orderItemViewModel: OrderItemViewModel = viewModel(),
    onOrderPlaced: (String) -> Unit = {},
    onClick: (() -> Unit)? = null
) {
    val userId = FirebaseAuth.getInstance().currentUser?.uid
    val carts by cartViewModel.carts.collectAsState()
    val products by productViewModel.products.collectAsState()
    val variants by variantViewModel.productVariants.collectAsState()
    val images by imageViewModel.productImages.collectAsState()
    val addresses by addressViewModel.userAddresses.collectAsState()
    val paymentMethods by paymentViewModel.paymentMethods.collectAsState()

    var selectedAddress by remember { mutableStateOf<UserAddress?>(null) }
    var selectedPayment by remember { mutableStateOf<PaymentMethod?>(null) }
    var showAllPayments by remember { mutableStateOf(false) }

    // lọc chỉ những cart được tick
    val selectedCarts = carts.filter { selectedCartIds.contains(it.cart_id ?: "") }

    // load dữ liệu
    LaunchedEffect(userId) {
        if (userId != null) {
            cartViewModel.loadCartsByUser(userId)
            addressViewModel.loadAddressesByUser(userId)
        }
        productViewModel.loadAllProducts()
        variantViewModel.loadAllProductVariants()
        imageViewModel.loadAllProductImages()
        paymentViewModel.loadAllPaymentMethods()
    }

    // chọn địa chỉ mặc định
    LaunchedEffect(addresses) {
        selectedAddress = addresses.firstOrNull { it.is_default == 1 } ?: addresses.firstOrNull()
    }

    val totalPrice = selectedCarts.sumOf { cart ->
        val product = products.find { it.product_id == cart.product_id }
        val variant = variants.find { it.variant_id == cart.variant_id }
        ((product?.base_price ?: 0.0) + (variant?.price_adjustment ?: 0.0)) * cart.quantity
    }

    Scaffold(
        topBar = {
            CustomTopBar(
                title = "Thanh toán",
                iconRes = R.drawable.leftarrow,
                onBackClick = { navController.popBackStack() }
            )
        },
        bottomBar = {
            if (selectedCarts.isNotEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .padding(16.dp)
                ) {
                    Text(
                        "Tổng cộng: ${"%,.0f".format(totalPrice)} đ",
                        fontWeight = FontWeight.Bold,
                        color = Color.Red
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = {
                            if (userId != null && selectedAddress != null && selectedPayment != null) {
                                val order = Order(
                                    user_id = userId,
                                    guest_name = selectedAddress!!.recipient_name,
                                    guest_phone = selectedAddress!!.phone_number,
                                    shipping_address = selectedAddress!!.address,
                                    total_amount = totalPrice,
                                    payment_method_id = selectedPayment!!.payment_method_id,
                                    status = "Pending",
                                    payment_status = "Pending",
                                    order_date = Timestamp.now()
                                )

                                orderViewModel.addOrder(
                                    order,
                                    onSuccess = { newOrderId ->
                                        // Log xác nhận Order đã tạo
                                        android.util.Log.d("CheckoutScreen", "✅ Order created: $newOrderId")

                                        // Tạo OrderItem tuần tự
                                        orderItemViewModel.viewModelScope.launch {
                                            try {
                                                for (cart in selectedCarts) {
                                                    val product = products.find { it.product_id == cart.product_id }
                                                    val variant = variants.find { it.variant_id == cart.variant_id }
                                                    val price =
                                                        (product?.base_price ?: 0.0) + (variant?.price_adjustment ?: 0.0)

                                                    val orderItem = OrderItem(
                                                        order_id = newOrderId,
                                                        product_id = cart.product_id,
                                                        variant_id = cart.variant_id,
                                                        variant_sku = cart.variant_sku,
                                                        quantity = cart.quantity,
                                                        price_at_time = price
                                                    )

                                                    android.util.Log.d(
                                                        "CheckoutScreen",
                                                        "🛒 Adding item: ${product?.product_name} (x${cart.quantity})"
                                                    )
                                                    orderItemViewModel.addOrderItem(orderItem)
                                                }

                                                // Xóa cart sau khi đã thêm order item
                                                for (cart in selectedCarts) {
                                                    cart.cart_id?.let { cartViewModel.deleteCart(it, userId) }
                                                }

                                                android.util.Log.d(
                                                    "CheckoutScreen",
                                                    "🎉 Checkout done for order $newOrderId"
                                                )

                                                onOrderPlaced(newOrderId)
                                            } catch (e: Exception) {
                                                android.util.Log.e(
                                                    "CheckoutScreen",
                                                    "Failed to add order items: ${e.message}"
                                                )
                                            }
                                        }
                                    },
                                    onError = { e ->
                                        android.util.Log.e("CheckoutScreen", "Failed to create order: ${e.message}")
                                    }
                                )
                            }
                        },
                        enabled = selectedAddress != null && selectedPayment != null,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                    ) {
                        Text("Đặt hàng (${selectedCarts.size})", color = Color.White)
                    }

                }
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 65.dp, start = 16.dp, end = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Địa chỉ
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White, RoundedCornerShape(12.dp))
                        .padding(10.dp)
                ) {
                    // Tiêu đề + Icon
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.location),
                            contentDescription = "Location",
                            tint = Color.Red,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(Modifier.width(6.dp))
                        Text(
                            "Địa chỉ giao hàng",
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    }

                    Spacer(Modifier.height(6.dp))

                    if (addresses.isEmpty()) {
                        Text(
                            "Chưa có địa chỉ. Vui lòng thêm trong hồ sơ.",
                            color = Color.Gray
                        )
                    } else {
                        addresses.forEach { addr ->
                            AddressCard(address = addr, isSelected = addr == selectedAddress) {
                                selectedAddress = addr
                            }
                            Spacer(Modifier.height(4.dp))
                        }
                    }
                }
            }

            // Phương thức thanh toán
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White, RoundedCornerShape(12.dp))
                        .padding(10.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(id = R.drawable.payment),
                            contentDescription = "Payment",
                            tint = Color.Red,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(Modifier.width(6.dp))
                        Text(
                            "Phương thức thanh toán",
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    }

                    Spacer(Modifier.height(8.dp))

                    val displayedMethods =
                        if (showAllPayments) paymentMethods else paymentMethods.take(3)

                    displayedMethods.forEach { pm ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    if (pm == selectedPayment)
                                        MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                                    else MaterialTheme.colorScheme.surface,
                                    RoundedCornerShape(10.dp)
                                )
                                .clickable { selectedPayment = pm }
                                .padding(10.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = if (pm == selectedPayment)
                                        Icons.Default.CheckCircle
                                    else Icons.Default.RadioButtonUnchecked,
                                    contentDescription = null,
                                    tint = if (pm == selectedPayment)
                                        MaterialTheme.colorScheme.primary
                                    else Color.Gray,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(Modifier.width(8.dp))
                                Column {
                                    Text(
                                        pm.payment_method_name,
                                        fontWeight = FontWeight.Medium,
                                        color = Color.Black
                                    )
                                    pm.description?.let {
                                        Text(
                                            it,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = Color.Gray
                                        )
                                    }
                                }
                            }
                        }
                        Spacer(Modifier.height(6.dp))
                    }

                    if (paymentMethods.size > 3) {
                        TextButton(
                            onClick = { showAllPayments = !showAllPayments },
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    if (showAllPayments) "Thu gọn" else "Xem thêm",
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Icon(
                                    imageVector = if (showAllPayments)
                                        Icons.Default.KeyboardArrowUp
                                    else Icons.Default.KeyboardArrowDown,
                                    contentDescription = null
                                )
                            }
                        }
                    }
                }
            }

            // Sản phẩm
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White, RoundedCornerShape(12.dp))
                        .padding(10.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.cart_1),
                            contentDescription = "Products",
                            tint = Color.Red,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(Modifier.width(6.dp))
                        Text(
                            "Sản phẩm thanh toán",
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    }

                    Spacer(Modifier.height(8.dp))

                    selectedCarts.forEach { cart ->
                        CheckoutItemRow(cart, products, variants, images)
                        Spacer(Modifier.height(6.dp))
                    }
                }
            }
        }

    }
}

@Composable
fun AddressCard(address: UserAddress, isSelected: Boolean, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                else MaterialTheme.colorScheme.surface,
                RoundedCornerShape(8.dp)
            )
            .clickable { onClick() }
            .padding(12.dp)
    ) {
        address.recipient_name?.let { Text(it, fontWeight = FontWeight.Bold) }
        Text("SĐT: ${address.phone_number}")
        Text("${address.address}, ${address.city}, ${address.province}")
    }
}

@Composable
fun CheckoutItemRow(
    cart: Cart,
    products: List<Product>,
    variants: List<ProductVariant>,
    images: List<ProductImage>
) {
    val product = products.find { it.product_id == cart.product_id }
    val variant = variants.find { it.variant_id == cart.variant_id }
    val image = images.find { it.product_id?.toInt() == product?.product_id && it.is_primary == 1 }

    val price = (product?.base_price ?: 0.0) + (variant?.price_adjustment ?: 0.0)

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = image?.image_url ?: "",
            contentDescription = product?.product_name,
            modifier = Modifier
                .size(70.dp)
                .background(Color.LightGray, RoundedCornerShape(8.dp))
        )
        Spacer(Modifier.width(12.dp))
        Column(Modifier.weight(1f)) {
            Text(product?.product_name ?: "Sản phẩm", fontWeight = FontWeight.Bold)
            variant?.let {
                Text("SKU: ${it.variant_sku}", style = MaterialTheme.typography.bodySmall)
            }
            Text("SL: ${cart.quantity} x ${"%,.0f".format(price)} đ")
        }
        Text(
            "${"%,.0f".format(price * cart.quantity)} đ",
            color = Color.Red,
            fontWeight = FontWeight.Bold
        )
    }
}
