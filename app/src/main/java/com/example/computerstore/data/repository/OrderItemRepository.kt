package com.example.computerstore.data.repository

import com.example.computerstore.data.dao.OrderItemDao
import com.example.computerstore.data.model.OrderItem

class OrderItemRepository(private val orderItemDao: OrderItemDao) {
    suspend fun getOrderItems(): List<OrderItem> = orderItemDao.getAll()
    suspend fun getOrderItem(id: Int): OrderItem? = orderItemDao.getById(id)
    suspend fun addOrderItem(orderItem: OrderItem) = orderItemDao.insert(orderItem)
    suspend fun updateOrderItem(orderItem: OrderItem) = orderItemDao.update(orderItem)
    suspend fun deleteOrderItem(id: Int) = orderItemDao.delete(id)
}
