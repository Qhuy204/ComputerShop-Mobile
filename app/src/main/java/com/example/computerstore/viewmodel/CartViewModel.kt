package com.example.computerstore.viewmodel

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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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

    fun addCart(product: Product, variant: ProductVariant, quantity: Int, userId: String) {
        viewModelScope.launch {
            val cartItem = Cart(
                cart_id = "", // Firestore sẽ generate ID mới nếu rỗng
                user_id = userId,
                product_id = product.product_id,
                variant_id = variant.variant_id,
                variant_sku = variant.variant_sku,
                quantity = quantity,
                added_at = com.google.firebase.Timestamp.now(),
                is_active = 1
            )
            repository.addCart(cartItem)
            loadCartsByUser(userId)
        }
    }

    fun updateCart(cart: Cart) {
        viewModelScope.launch {
            repository.updateCart(cart)
            loadAllCarts()
        }
    }

    fun deleteCart(id: String, userId: String) {
        viewModelScope.launch {
            repository.deleteCart(id)
            loadCartsByUser(userId)
        }
    }

    fun increaseQuantity(cart: Cart) {
        viewModelScope.launch {
            val newQty = cart.quantity + 1
            cartRepository.updateQuantity(cart.cart_id, newQty)
            loadCartsByUser(cart.user_id ?: return@launch)
        }
    }

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
