package com.example.computerstore.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.computerstore.data.dao.impl.CartDaoImpl
import com.example.computerstore.data.dao.impl.ProductDaoImpl
import com.example.computerstore.data.dao.impl.ProductImageDaoImpl
import com.example.computerstore.data.dao.impl.ProductVariantDaoImpl
import com.example.computerstore.data.model.Cart
import com.example.computerstore.data.model.CartItemUI
import com.example.computerstore.data.model.Product
import com.example.computerstore.data.model.ProductVariant
import com.example.computerstore.data.repository.CartRepository
import com.example.computerstore.data.repository.ProductImageRepository
import com.example.computerstore.data.repository.ProductRepository
import com.example.computerstore.data.repository.ProductVariantRepository
import com.google.firebase.Timestamp
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CartViewModel : ViewModel() {

    private val cartRepository = CartRepository(CartDaoImpl())
    private val productRepository = ProductRepository(ProductDaoImpl())
    private val variantRepository = ProductVariantRepository(ProductVariantDaoImpl())
    private val imageRepository = ProductImageRepository(ProductImageDaoImpl())

    private val _cartItemsUI = MutableStateFlow<List<CartItemUI>>(emptyList())
    val cartItemsUI: StateFlow<List<CartItemUI>> = _cartItemsUI

    private val repository = CartRepository(CartDaoImpl())

    private val _carts = MutableStateFlow<List<Cart>>(emptyList())
    val carts: StateFlow<List<Cart>> = _carts

    private val _currentCart = MutableStateFlow<Cart?>(null)
    val currentCart: StateFlow<Cart?> = _currentCart

    // --- Item được chọn trong Cart (khi Buy Now)
    private val _selectedCartItemId = MutableStateFlow<String?>(null)
    val selectedCartItemId = _selectedCartItemId.asStateFlow()

    fun selectCartItem(cartId: String) {
        _selectedCartItemId.value = cartId
    }

    // --- Load dữ liệu giỏ hàng
    fun loadCartsByUser(userId: String) {
        viewModelScope.launch {
            _carts.value = repository.getCartsByUser(userId)
        }
    }

    fun loadCartWithDetails(userId: String?) {
        viewModelScope.launch {
            val carts = cartRepository.getCarts()
                .filter { userId == null || it.user_id == userId }

            val items = carts.map { cart ->
                val product = cart.product_id?.let { productRepository.getProduct(it) }
                val variant = cart.variant_id?.let { variantRepository.getProductVariant(it) }
                val image = cart.product_id?.let { imageRepository.getByProductId(it) }?.firstOrNull()

                CartItemUI(
                    cart = cart,
                    product = product,
                    variant = variant,
                    imageUrl = image?.image_url
                )
            }
            _cartItemsUI.value = items
        }
    }

    fun loadAllCarts() {
        viewModelScope.launch {
            _carts.value = repository.getCarts()
        }
    }

    fun loadCart(id: String) {
        viewModelScope.launch {
            _currentCart.value = repository.getCart(id)
        }
    }

    // --- Thêm mới giỏ hàng cũ (vẫn giữ để tương thích)
    fun addCart(product: Product, variant: ProductVariant, quantity: Int, userId: String) {
        viewModelScope.launch {
            val cartItem = Cart(
                cart_id = "", // Firestore tự tạo ID nếu để trống
                user_id = userId,
                product_id = product.product_id,
                variant_id = variant.variant_id,
                variant_sku = variant.variant_sku,
                quantity = quantity,
                added_at = Timestamp.now(),
                is_active = 1
            )
            repository.addCart(cartItem)
            loadCartsByUser(userId)
        }
    }

    // --- ✅ Thêm mới hoặc cập nhật giỏ hàng (nếu trùng thì +1)
    suspend fun addOrUpdateCart(
        product: Product,
        variant: ProductVariant,
        quantity: Int,
        userId: String
    ): String {
        val allCarts = cartRepository.getCartsByUser(userId)

        val existingItem = allCarts.firstOrNull {
            it.product_id == product.product_id && it.variant_id == variant.variant_id
        }

        return (if (existingItem != null) {
            // --- Cộng thêm số lượng nếu sản phẩm đã tồn tại
            val newQuantity = existingItem.quantity + quantity
            cartRepository.updateQuantity(existingItem.cart_id, newQuantity)
            Log.d(
                "CartViewModel",
                "🟢 Updated quantity to $newQuantity for ${product.product_name}"
            )
            existingItem.cart_id
        } else {
            // --- Thêm sản phẩm mới vào giỏ
            val newCart = Cart(
                cart_id = "",
                user_id = userId,
                product_id = product.product_id,
                variant_id = variant.variant_id,
                variant_sku = variant.variant_sku,
                quantity = quantity,
                added_at = Timestamp.now(),
                is_active = 1
            )
            val addedId = cartRepository.addCart(newCart)
            Log.d(
                "CartViewModel",
                "🟢 Added new item to cart: ${product.product_name} (${variant.variant_sku})"
            )
            addedId
        }) as String
    }

    // --- Cập nhật giỏ hàng
    fun updateCart(cart: Cart) {
        viewModelScope.launch {
            repository.updateCart(cart)
            loadAllCarts()
        }
    }

    // --- Xóa item
    fun deleteCart(id: String, userId: String) {
        viewModelScope.launch {
            repository.deleteCart(id)
            loadCartsByUser(userId)
        }
    }

    // --- Tăng số lượng
    fun increaseQuantity(cart: Cart) {
        viewModelScope.launch {
            val newQty = cart.quantity + 1
            cartRepository.updateQuantity(cart.cart_id, newQty)
            loadCartsByUser(cart.user_id ?: return@launch)
        }
    }

    // --- Giảm số lượng
    fun decreaseQuantity(cart: Cart) {
        viewModelScope.launch {
            if (cart.quantity > 1) {
                val newQty = cart.quantity - 1
                cartRepository.updateQuantity(cart.cart_id, newQty)
                loadCartsByUser(cart.user_id ?: return@launch)
            }
        }
    }
}
