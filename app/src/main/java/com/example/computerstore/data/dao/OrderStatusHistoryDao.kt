package com.example.computerstore.data.dao

import com.example.computerstore.data.model.OrderStatusHistory

interface OrderStatusHistoryDao {
    suspend fun getAll(): List<OrderStatusHistory>
    suspend fun getById(id: Int): OrderStatusHistory?
    suspend fun insert(orderStatusHistory: OrderStatusHistory)
    suspend fun update(orderStatusHistory: OrderStatusHistory)
    suspend fun delete(id: Int)
}
