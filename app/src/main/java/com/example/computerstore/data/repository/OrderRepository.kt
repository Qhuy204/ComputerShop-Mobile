package com.example.computerstore.data.repository

import com.example.computerstore.data.dao.OrderDao
import com.example.computerstore.data.model.Order

class OrderRepository(private val orderDao: OrderDao) {
    suspend fun getOrders(): List<Order> = orderDao.getAll()
    suspend fun getOrder(id: Int): Order? = orderDao.getById(id)
    suspend fun addOrder(order: Order) = orderDao.insert(order)
    suspend fun updateOrder(order: Order) = orderDao.update(order)
    suspend fun deleteOrder(id: Int) = orderDao.delete(id)
}
