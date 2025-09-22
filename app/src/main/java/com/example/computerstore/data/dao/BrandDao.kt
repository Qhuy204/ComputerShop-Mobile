package com.example.computerstore.data.dao

import com.example.computerstore.data.model.Brand

interface BrandDao {
    suspend fun getAll(): List<Brand>
    suspend fun getById(id: Int): Brand?
    suspend fun insert(brand: Brand)
    suspend fun update(brand: Brand)
    suspend fun delete(id: Int)
}
