package com.example.computerstore.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.computerstore.data.dao.impl.OrderDaoImpl
import com.example.computerstore.data.model.Order
import com.example.computerstore.data.repository.OrderRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class OrderViewModel : ViewModel() {
    private val repository = OrderRepository(OrderDaoImpl())

    private val _orders = MutableStateFlow<List<Order>>(emptyList())
    val orders: StateFlow<List<Order>> = _orders

    private val _currentOrder = MutableStateFlow<Order?>(null)
    val currentOrder: StateFlow<Order?> = _currentOrder

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun loadAllOrders() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _orders.value = repository.getOrders()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadOrdersByUser(userId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _orders.value = repository.getOrders()
                    .filter { it.user_id == userId }
                    .sortedByDescending { it.order_date }
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadOrder(id: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _currentOrder.value = repository.getOrder(id)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun addOrder(order: Order, onSuccess: (String) -> Unit = {}) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val orderId = repository.addOrder(order)
                loadOrdersByUser(order.user_id ?: "")
                onSuccess(orderId)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun updateOrder(order: Order) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                repository.updateOrder(order)
                loadOrdersByUser(order.user_id ?: "")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun deleteOrder(id: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                repository.deleteOrder(id)
                loadAllOrders()
            } finally {
                _isLoading.value = false
            }
        }
    }
}
