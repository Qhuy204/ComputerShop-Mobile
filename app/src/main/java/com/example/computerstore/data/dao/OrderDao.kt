package com.example.computerstore.data.dao

import com.example.computerstore.data.model.Order

interface OrderDao {
    suspend fun getAll(): List<Order>
    suspend fun getById(id: String): Order?
    suspend fun insert(order: Order): String
    suspend fun update(order: Order)
    suspend fun delete(id: String)
}
