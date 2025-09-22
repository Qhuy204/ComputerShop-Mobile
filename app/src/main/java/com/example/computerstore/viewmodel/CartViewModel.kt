package com.example.computerstore.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.computerstore.data.dao.impl.CartDaoImpl
import com.example.computerstore.data.model.Cart
import com.example.computerstore.data.repository.CartRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CartViewModel : ViewModel() {
    private val repository = CartRepository(CartDaoImpl())

    private val _carts = MutableStateFlow<List<Cart>>(emptyList())
    val carts: StateFlow<List<Cart>> = _carts

    private val _currentCart = MutableStateFlow<Cart?>(null)
    val currentCart: StateFlow<Cart?> = _currentCart

    fun loadAllCarts() {
        viewModelScope.launch {
            _carts.value = repository.getCarts()
        }
    }

    fun loadCart(id: Int) {
        viewModelScope.launch {
            _currentCart.value = repository.getCart(id)
        }
    }

    fun addCart(cart: Cart) {
        viewModelScope.launch {
            repository.addCart(cart)
            loadAllCarts()
        }
    }

    fun updateCart(cart: Cart) {
        viewModelScope.launch {
            repository.updateCart(cart)
            loadAllCarts()
        }
    }

    fun deleteCart(id: Int) {
        viewModelScope.launch {
            repository.deleteCart(id)
            loadAllCarts()
        }
    }
}
