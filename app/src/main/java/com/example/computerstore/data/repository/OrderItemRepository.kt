package com.example.computerstore.data.repository

import com.example.computerstore.data.dao.OrderItemDao
import com.example.computerstore.data.model.OrderItem

class OrderItemRepository(private val orderItemDao: OrderItemDao) {
    suspend fun getOrderItems(): List<OrderItem> = orderItemDao.getAll()
    suspend fun getOrderItem(id: String): OrderItem? = orderItemDao.getById(id)
    suspend fun addOrderItem(orderItem: OrderItem) = orderItemDao.insert(orderItem)
    suspend fun updateOrderItem(orderItem: OrderItem) = orderItemDao.update(orderItem)
    suspend fun deleteOrderItem(id: String) = orderItemDao.delete(id)
}
