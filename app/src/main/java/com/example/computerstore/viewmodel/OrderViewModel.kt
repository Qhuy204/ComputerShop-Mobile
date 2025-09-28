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

    fun loadAllOrders() {
        viewModelScope.launch {
            _orders.value = repository.getOrders()
        }
    }

    fun loadOrder(id: String) {
        viewModelScope.launch {
            _currentOrder.value = repository.getOrder(id)
        }
    }

    fun addOrder(order: Order, onSuccess: (String) -> Unit = {}) {
        viewModelScope.launch {
            val orderId = repository.addOrder(order)
            loadAllOrders()
            onSuccess(orderId)
        }
    }

    fun updateOrder(order: Order) {
        viewModelScope.launch {
            repository.updateOrder(order)
            loadAllOrders()
        }
    }

    fun deleteOrder(id: String) {
        viewModelScope.launch {
            repository.deleteOrder(id)
            loadAllOrders()
        }
    }
}
