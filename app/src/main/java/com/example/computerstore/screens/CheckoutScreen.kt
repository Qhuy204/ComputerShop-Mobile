package com.example.computerstore.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.computerstore.data.model.*
import com.example.computerstore.viewmodel.*
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(
    cartViewModel: CartViewModel = viewModel(),
    productViewModel: ProductViewModel = viewModel(),
    productVariantViewModel: ProductVariantViewModel = viewModel(),
    productImageViewModel: ProductImageViewModel = viewModel(),
    userAddressViewModel: UserAddressViewModel = viewModel(),
    paymentMethodViewModel: PaymentMethodViewModel = viewModel(),
    orderViewModel: OrderViewModel = viewModel(),
    onOrderPlaced: (orderId: String) -> Unit = {}
) {
    val carts by cartViewModel.carts.collectAsState()
    val products by productViewModel.products.collectAsState()
    val variants by productVariantViewModel.productVariants.collectAsState()
    val images by productImageViewModel.productImages.collectAsState()
    val addresses by userAddressViewModel.userAddresses.collectAsState()
    val paymentMethods by paymentMethodViewModel.paymentMethods.collectAsState()

    var selectedAddress by remember { mutableStateOf<UserAddress?>(null) }
    var selectedPayment by remember { mutableStateOf<PaymentMethod?>(null) }

    val totalPrice = carts.sumOf { cart ->
        val product = products.find { it.product_id == cart.product_id }
        val variant = variants.find { it.variant_id == cart.variant_id }
        val price = (product?.base_price ?: 0.0) + (variant?.price_adjustment ?: 0.0)
        price * cart.quantity
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Thanh toán") }) },
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .padding(16.dp)
            ) {
                Text(
                    "Tổng cộng: ${"%,.0f".format(totalPrice)} đ",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = {
                        val uid = FirebaseAuth.getInstance().currentUser?.uid
                        if (uid != null && selectedAddress != null && selectedPayment != null) {
                            val order = Order(
                                user_id = uid,
                                guest_name = selectedAddress?.recipient_name,
                                guest_phone = selectedAddress?.phone_number,
                                shipping_address = selectedAddress?.address ?: "",
                                total_amount = totalPrice,
                                payment_method_id = selectedPayment?.payment_method_id,
                                status = "Pending",
                                payment_status = "Pending"
                            )
                            orderViewModel.addOrder(order) { orderId ->
                                onOrderPlaced(orderId)
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    enabled = carts.isNotEmpty() && selectedAddress != null && selectedPayment != null
                ) {
                    Text("Đặt hàng")
                }
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // địa chỉ
            item {
                Text("Địa chỉ giao hàng", fontWeight = FontWeight.Bold)
                addresses.forEach { addr ->
                    AddressCard(
                        address = addr,
                        isSelected = addr == selectedAddress,
                        onClick = { selectedAddress = addr }
                    )
                    Spacer(Modifier.height(8.dp))
                }
            }

            // phương thức thanh toán
            item {
                Text("Phương thức thanh toán", fontWeight = FontWeight.Bold)
                paymentMethods.forEach { pm ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp)
                            .background(
                                if (pm == selectedPayment) MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                                else Color.Transparent,
                                RoundedCornerShape(8.dp)
                            )
                            .clickable { selectedPayment = pm }
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = if (pm == selectedPayment) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                            contentDescription = null,
                            tint = if (pm == selectedPayment) MaterialTheme.colorScheme.primary else Color.Gray
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(pm.payment_method_name, fontWeight = FontWeight.Medium)
                            pm.description?.let {
                                Text(it, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                            }
                        }
                    }
                }
            }

            // danh sách sản phẩm
            item { Text("Sản phẩm", fontWeight = FontWeight.Bold) }
            items(carts) { cart ->
                CheckoutItemRow(cart, products, variants, images)
                Divider()
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
        Text(address.recipient_name, fontWeight = FontWeight.Bold)
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
    val image = images.find { it.product_id?.toInt() == cart.product_id && it.is_primary == 1 }

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
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(product?.product_name ?: "Sản phẩm", fontWeight = FontWeight.Bold)
            variant?.let { Text("SKU: ${it.variant_sku}", style = MaterialTheme.typography.bodySmall) }
            Text("SL: ${cart.quantity} x ${"%,.0f".format(price)} đ")
        }
        Text(
            "${"%,.0f".format(price * cart.quantity)} đ",
            fontWeight = FontWeight.Medium,
            color = Color.Red
        )
    }
}
