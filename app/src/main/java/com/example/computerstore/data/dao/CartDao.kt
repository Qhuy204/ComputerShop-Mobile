package com.example.computerstore.data.dao

import com.example.computerstore.data.model.Cart

interface CartDao {
    suspend fun getAll(): List<Cart>
    suspend fun getById(id: String): Cart?
    suspend fun insert(cart: Cart)
    suspend fun update(cart: Cart)
    suspend fun delete(id: String)
    suspend fun getCartsByUser(id: String): List<Cart>
    suspend fun updateQuantity(cartId: String?, newQuantity: Int)

}