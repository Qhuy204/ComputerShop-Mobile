package com.example.computerstore.data.dao

import com.example.computerstore.data.model.Product

interface ProductDao {
    suspend fun getAll(): List<Product>
    suspend fun getById(id: Int): Product?
    suspend fun insert(product: Product)
    suspend fun update(product: Product)
    suspend fun delete(id: Int)
}
