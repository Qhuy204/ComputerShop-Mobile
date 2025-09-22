package com.example.computerstore.data.dao

import com.example.computerstore.data.model.Cart

interface CartDao {
    suspend fun getAll(): List<Cart>
    suspend fun getById(id: Int): Cart?
    suspend fun insert(cart: Cart)
    suspend fun update(cart: Cart)
    suspend fun delete(id: Int)
}
