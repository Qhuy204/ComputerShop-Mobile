package com.example.computerstore.data.repository

import com.example.computerstore.data.dao.OrderStatusHistoryDao
import com.example.computerstore.data.model.OrderStatusHistory

class OrderStatusHistoryRepository(private val orderStatusHistoryDao: OrderStatusHistoryDao) {
    suspend fun getOrderStatusHistories(): List<OrderStatusHistory> = orderStatusHistoryDao.getAll()
    suspend fun getOrderStatusHistory(id: Int): OrderStatusHistory? = orderStatusHistoryDao.getById(id)
    suspend fun addOrderStatusHistory(orderStatusHistory: OrderStatusHistory) = orderStatusHistoryDao.insert(orderStatusHistory)
    suspend fun updateOrderStatusHistory(orderStatusHistory: OrderStatusHistory) = orderStatusHistoryDao.update(orderStatusHistory)
    suspend fun deleteOrderStatusHistory(id: Int) = orderStatusHistoryDao.delete(id)
}
