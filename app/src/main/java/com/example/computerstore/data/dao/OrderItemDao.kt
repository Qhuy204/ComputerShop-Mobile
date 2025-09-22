package com.example.computerstore.data.dao

import com.example.computerstore.data.model.OrderItem

interface OrderItemDao {
    suspend fun getAll(): List<OrderItem>
    suspend fun getById(id: Int): OrderItem?
    suspend fun insert(orderItem: OrderItem)
    suspend fun update(orderItem: OrderItem)
    suspend fun delete(id: Int)
}
