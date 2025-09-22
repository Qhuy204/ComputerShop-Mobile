package com.example.computerstore.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.computerstore.data.dao.impl.OrderStatusHistoryDaoImpl
import com.example.computerstore.data.model.OrderStatusHistory
import com.example.computerstore.data.repository.OrderStatusHistoryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class OrderStatusHistoryViewModel : ViewModel() {
    private val repository = OrderStatusHistoryRepository(OrderStatusHistoryDaoImpl())

    private val _orderStatusHistories = MutableStateFlow<List<OrderStatusHistory>>(emptyList())
    val orderStatusHistories: StateFlow<List<OrderStatusHistory>> = _orderStatusHistories

    private val _currentOrderStatusHistory = MutableStateFlow<OrderStatusHistory?>(null)
    val currentOrderStatusHistory: StateFlow<OrderStatusHistory?> = _currentOrderStatusHistory

    fun loadAllOrderStatusHistories() {
        viewModelScope.launch {
            _orderStatusHistories.value = repository.getOrderStatusHistories()
        }
    }

    fun loadOrderStatusHistory(id: Int) {
        viewModelScope.launch {
            _currentOrderStatusHistory.value = repository.getOrderStatusHistory(id)
        }
    }

    fun addOrderStatusHistory(orderStatusHistory: OrderStatusHistory) {
        viewModelScope.launch {
            repository.addOrderStatusHistory(orderStatusHistory)
            loadAllOrderStatusHistories()
        }
    }

    fun updateOrderStatusHistory(orderStatusHistory: OrderStatusHistory) {
        viewModelScope.launch {
            repository.updateOrderStatusHistory(orderStatusHistory)
            loadAllOrderStatusHistories()
        }
    }

    fun deleteOrderStatusHistory(id: Int) {
        viewModelScope.launch {
            repository.deleteOrderStatusHistory(id)
            loadAllOrderStatusHistories()
        }
    }
}
