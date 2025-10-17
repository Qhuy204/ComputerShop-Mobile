package com.example.computerstore.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.computerstore.R
import com.example.computerstore.data.model.*
import com.example.computerstore.screens.components.CustomTopBar
import com.example.computerstore.viewmodel.*
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderDetailScreen(
    orderId: String,
    navController: NavController,
    orderViewModel: OrderViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    orderItemViewModel: OrderItemViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    productViewModel: ProductViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    productImageViewModel: ProductImageViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    variantViewModel: ProductVariantViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    specViewModel: ProductSpecificationViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val dateFormat = remember { SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()) }

    val currentOrder by orderViewModel.currentOrder.collectAsState()
    val orderItems by orderItemViewModel.orderItems.collectAsState()
    val products by productViewModel.products.collectAsState()
    val productImages by productImageViewModel.productImages.collectAsState()
    val variants by variantViewModel.productVariants.collectAsState()
    val specs by specViewModel.productSpecifications.collectAsState()

    LaunchedEffect(orderId) {
        Log.d("OrderDetailScreen", "🔄 Loading details for orderId=$orderId ...")
        orderViewModel.loadOrder(orderId)
        orderItemViewModel.loadAllOrderItems()
        productViewModel.loadAllProducts()
        productImageViewModel.loadAllProductImages()
        variantViewModel.loadAllProductVariants()
        specViewModel.loadAllProductSpecifications()
    }

    Scaffold(
        topBar = {
            CustomTopBar(
                title = "Chi tiết đơn hàng",
                iconRes = R.drawable.leftarrow,
                onBackClick = { navController.popBackStack() }
            )
        }
    ) { padding ->

        val order = currentOrder
        val relatedItems = orderItems.filter { it.order_id == orderId }

        if (order == null) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .background(Color(0xFFF8F9FA))
                    .padding(padding)
            ) {
                // Order Status Timeline
                OrderStatusTimeline(
                    status = order.status,
                    orderDate = order.order_date?.let { dateFormat.format(it.toDate()) } ?: ""
                )

                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Order Info Card
                    OrderInfoCard(
                        orderId = order.order_id as String,
                        orderDate = order.order_date?.let { dateFormat.format(it.toDate()) } ?: "",
                        status = order.status
                    )

                    // Shipping Address Card
                    ShippingAddressCard(
                        name = order.guest_name,
                        phone = order.guest_phone,
                        address = order.shipping_address
                    )

                    // Products List
                    ProductsCard(
                        items = relatedItems,
                        products = products,
                        productImages = productImages,
                        variants = variants,
                        specs = specs
                    )

                    // Order Summary
                    OrderSummaryCard(
                        totalAmount = order.total_amount
                    )
                }
            }
        }
    }
}

@Composable
fun OrderStatusTimeline(status: String, orderDate: String) {
    val statusConfig = when (status) {
        "Pending" -> Triple("Chờ xác nhận", 1, Color(0xFFFB923C))
        "Shipped" -> Triple("Chờ giao hàng", 2, Color(0xFF3B82F6))
        "Delivered" -> Triple("Đã giao hàng", 3, Color(0xFF10B981))
        else -> Triple("Chờ xác nhận", 1, Color(0xFFFB923C))
    }

    val (statusText, currentStep, statusColor) = statusConfig

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(20.dp)
    ) {
        Column {
            Text(
                text = statusText,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = statusColor
            )
            Text(
                text = orderDate,
                fontSize = 13.sp,
                color = Color(0xFF6B7280),
                modifier = Modifier.padding(top = 4.dp)
            )

            Spacer(Modifier.height(24.dp))

            // Timeline
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TimelineStep(
                    title = "Chờ xác nhận",
                    isActive = currentStep >= 1,
                    isCompleted = currentStep > 1
                )
                TimelineLine(isActive = currentStep >= 2)
                TimelineStep(
                    title = "Chờ giao",
                    isActive = currentStep >= 2,
                    isCompleted = currentStep > 2
                )
                TimelineLine(isActive = currentStep >= 3)
                TimelineStep(
                    title = "Đã giao",
                    isActive = currentStep >= 3,
                    isCompleted = false
                )
            }
        }
    }
}

@Composable
fun RowScope.TimelineStep(title: String, isActive: Boolean, isCompleted: Boolean) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.weight(1f)
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(
                    when {
                        isCompleted -> Color(0xFF10B981)
                        isActive -> Color(0xFF3B82F6)
                        else -> Color(0xFFE5E7EB)
                    }
                ),
            contentAlignment = Alignment.Center
        ) {
            if (isCompleted) {
                Text("✓", color = Color.White, fontWeight = FontWeight.Bold)
            } else {
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .clip(CircleShape)
                        .background(if (isActive) Color.White else Color(0xFFE5E7EB))
                )
            }
        }
        Spacer(Modifier.height(8.dp))
        Text(
            text = title,
            fontSize = 11.sp,
            color = if (isActive) Color(0xFF111827) else Color(0xFF9CA3AF),
            fontWeight = if (isActive) FontWeight.Medium else FontWeight.Normal
        )
    }
}

@Composable
fun RowScope.TimelineLine(isActive: Boolean) {
    Box(
        modifier = Modifier
            .weight(0.5f)
            .height(2.dp)
            .padding(bottom = 38.dp)
            .background(if (isActive) Color(0xFF3B82F6) else Color(0xFFE5E7EB))
    )
}

@Composable
fun OrderInfoCard(orderId: String, orderDate: String, status: String) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(0.dp),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color(0xFFE5E7EB), RoundedCornerShape(12.dp))
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        "Mã đơn hàng",
                        fontSize = 13.sp,
                        color = Color(0xFF6B7280)
                    )
                    Text(
                        orderId,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun ShippingAddressCard(name: String?, phone: String?, address: String) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(0.dp),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color(0xFFE5E7EB), RoundedCornerShape(12.dp))
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFF3F4F6)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("📍", fontSize = 18.sp)
                }
                Spacer(Modifier.width(12.dp))
                Text(
                    "Địa chỉ giao hàng",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    if (name != null) {
                        Text(
                            name,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    if (phone != null) {
                        Text(
                            phone,
                            fontSize = 14.sp,
                            color = Color(0xFF6B7280),
                            modifier = Modifier.padding(top = 2.dp)
                        )
                    }
                }
            }

            Spacer(Modifier.height(12.dp))

            Text(
                address,
                fontSize = 14.sp,
                color = Color(0xFF374151),
                lineHeight = 20.sp
            )
        }
    }
}

@Composable
fun ProductsCard(
    items: List<OrderItem>,
    products: List<Product>,
    productImages: List<ProductImage>,
    variants: List<ProductVariant>,
    specs: List<ProductSpecification>
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(0.dp),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color(0xFFE5E7EB), RoundedCornerShape(12.dp))
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(
                "Sản phẩm",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(Modifier.height(16.dp))

            if (items.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 20.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "Không có sản phẩm",
                        color = Color(0xFF9CA3AF),
                        fontSize = 14.sp
                    )
                }
            } else {
                items.forEachIndexed { index, item ->
                    val product = products.find { it.product_id == item.product_id }
                    val imageUrl = productImages.find { it.product_id?.toInt() == item.product_id }?.image_url
                    val variant = variants.find { it.variant_id == item.variant_id }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        AsyncImage(
                            model = imageUrl,
                            contentDescription = product?.product_name,
                            modifier = Modifier
                                .size(70.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFFF3F4F6))
                        )

                        Spacer(Modifier.width(12.dp))

                        Column(Modifier.weight(1f)) {
                            Text(
                                product?.product_name ?: "Sản phẩm #${item.product_id}",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                lineHeight = 20.sp
                            )
                            if (variant != null) {
                                Text(
                                    "SKU: ${variant.variant_sku}",
                                    fontSize = 12.sp,
                                    color = Color(0xFF9CA3AF),
                                    modifier = Modifier.padding(top = 4.dp)
                                )
                            }
                            Text(
                                "x${item.quantity}",
                                fontSize = 13.sp,
                                color = Color(0xFF6B7280),
                                modifier = Modifier.padding(top = 8.dp)
                            )
                        }

                        Text(
                            "%,.0f₫".format(item.price_at_time * item.quantity),
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF111827)
                        )
                    }

                    if (index < items.size - 1) {
                        Divider(
                            color = Color(0xFFF3F4F6),
                            thickness = 1.dp,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun OrderSummaryCard(totalAmount: Double) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(0.dp),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color(0xFFE5E7EB), RoundedCornerShape(12.dp))
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(
                "Tổng thanh toán",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Tổng cộng",
                    fontSize = 14.sp,
                    color = Color(0xFF6B7280)
                )
                Text(
                    "%,d₫".format(totalAmount.toInt()),
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFDC2626)
                )
            }
        }
    }
}