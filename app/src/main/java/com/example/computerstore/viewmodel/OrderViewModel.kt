package com.example.computerstore.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.computerstore.data.dao.impl.OrderDaoImpl
import com.example.computerstore.data.model.Order
import com.example.computerstore.data.repository.OrderRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class OrderViewModel : ViewModel() {

    val repository = OrderRepository(OrderDaoImpl())

    private val _orders = MutableStateFlow<List<Order>>(emptyList())
    val orders: StateFlow<List<Order>> = _orders

    private val _currentOrder = MutableStateFlow<Order?>(null)
    val currentOrder: StateFlow<Order?> = _currentOrder

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    // -----------------------------
    // 🧩 Load toàn bộ Orders
    // -----------------------------
    fun loadAllOrders() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _orders.value = repository.getOrders()
                Log.d("OrderViewModel", "✅ Loaded ${_orders.value.size} orders total")
            } catch (e: Exception) {
                Log.e("OrderViewModel", "❌ Error loading orders: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    // -----------------------------
    // 👤 Load orders theo user
    // -----------------------------
    fun loadOrdersByUser(userId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val allOrders = repository.getOrders()
                _orders.value = allOrders
                    .filter { it.user_id == userId }
                    .sortedByDescending { it.order_date }
                Log.d("OrderViewModel", "✅ Loaded ${_orders.value.size} orders for user=$userId")
            } catch (e: Exception) {
                Log.e("OrderViewModel", "❌ Error loading user orders: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    // -----------------------------
    // 🔍 Load 1 order theo ID (sửa chính)
    // -----------------------------
    fun loadOrder(orderId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val order = repository.getOrder(orderId)
                _currentOrder.value = order
                if (order != null)
                    Log.d("OrderViewModel", "✅ Loaded order: ${order.order_id} (${order.status})")
                else
                    Log.w("OrderViewModel", "⚠️ Order not found for ID=$orderId")
            } catch (e: Exception) {
                Log.e("OrderViewModel", "❌ Error loading order: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    // -----------------------------
    // 🧾 Lấy order theo ID, dùng callback
    // -----------------------------
    fun getOrderById(orderId: String, onResult: (Order?) -> Unit = {}) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val order = repository.getOrder(orderId)
                _currentOrder.value = order
                onResult(order)
                Log.d("OrderViewModel", "✅ getOrderById() found: ${order?.order_id}")
            } catch (e: Exception) {
                Log.e("OrderViewModel", "❌ Error getting order by ID: ${e.message}")
                onResult(null)
            } finally {
                _isLoading.value = false
            }
        }
    }

    // -----------------------------
    // ➕ Thêm Order mới
    // -----------------------------
    fun addOrder(
        order: Order,
        onSuccess: (String) -> Unit = {},
        onError: ((Exception) -> Unit)? = null
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val orderId = repository.addOrder(order)
                loadOrdersByUser(order.user_id ?: "")
                Log.d("OrderViewModel", "✅ Added order $orderId for user ${order.user_id}")
                onSuccess(orderId)
            } catch (e: Exception) {
                Log.e("OrderViewModel", "❌ Error adding order: ${e.message}")
                onError?.invoke(e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    // -----------------------------
    // ✏️ Cập nhật Order
    // -----------------------------
    fun updateOrder(order: Order) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                repository.updateOrder(order)
                loadOrdersByUser(order.user_id ?: "")
                Log.d("OrderViewModel", "✅ Updated order ${order.order_id}")
            } catch (e: Exception) {
                Log.e("OrderViewModel", "❌ Error updating order: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    // -----------------------------
    // 🗑️ Xóa Order
    // -----------------------------
    fun deleteOrder(id: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                repository.deleteOrder(id)
                loadAllOrders()
                Log.d("OrderViewModel", "✅ Deleted order $id")
            } catch (e: Exception) {
                Log.e("OrderViewModel", "❌ Error deleting order: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }
}
