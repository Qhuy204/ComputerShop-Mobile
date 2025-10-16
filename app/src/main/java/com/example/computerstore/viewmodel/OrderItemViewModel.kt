package com.example.computerstore.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.computerstore.data.dao.impl.OrderItemDaoImpl
import com.example.computerstore.data.model.OrderItem
import com.example.computerstore.data.repository.OrderItemRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class OrderItemViewModel : ViewModel() {
    private val repository = OrderItemRepository(OrderItemDaoImpl())

    private val _orderItems = MutableStateFlow<List<OrderItem>>(emptyList())
    val orderItems: StateFlow<List<OrderItem>> = _orderItems

    private val _currentOrderItem = MutableStateFlow<OrderItem?>(null)
    val currentOrderItem: StateFlow<OrderItem?> = _currentOrderItem

    fun loadAllOrderItems() {
        viewModelScope.launch {
            _orderItems.value = repository.getOrderItems()
        }
    }

    fun loadOrderItem(id: String) {
        viewModelScope.launch {
            _currentOrderItem.value = repository.getOrderItem(id)
        }
    }

    fun addOrderItem(orderItem: OrderItem) {
        viewModelScope.launch {
            repository.addOrderItem(orderItem)
            loadAllOrderItems()
        }
    }

    fun updateOrderItem(orderItem: OrderItem) {
        viewModelScope.launch {
            repository.updateOrderItem(orderItem)
            loadAllOrderItems()
        }
    }

    fun deleteOrderItem(id: String) {
        viewModelScope.launch {
            repository.deleteOrderItem(id)
            loadAllOrderItems()
        }
    }
}
