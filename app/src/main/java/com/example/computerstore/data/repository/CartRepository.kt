package com.example.computerstore.data.repository

import com.example.computerstore.data.dao.CartDao
import com.example.computerstore.data.model.Cart

class CartRepository(private val cartDao: CartDao) {
    suspend fun getCarts(): List<Cart> = cartDao.getAll()
    suspend fun getCart(id: String): Cart? = cartDao.getById(id)
    suspend fun addCart(cart: Cart) = cartDao.insert(cart)
    suspend fun updateCart(cart: Cart) = cartDao.update(cart)
    suspend fun deleteCart(id: String) = cartDao.delete(id)
    suspend fun getCartsByUser(id: String): List<Cart> = cartDao.getCartsByUser(id)
    suspend fun updateQuantity(cartId: String?, newQuantity: Int) =
        cartDao.updateQuantity(cartId, newQuantity)
}
