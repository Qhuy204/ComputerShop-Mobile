package com.example.computerstore.data.dao

import com.example.computerstore.data.model.ProductVariant

interface ProductVariantDao {
    suspend fun getAll(): List<ProductVariant>
    suspend fun getById(id: Int): ProductVariant?
    suspend fun insert(productVariant: ProductVariant)
    suspend fun update(productVariant: ProductVariant)
    suspend fun delete(id: Int)
    suspend fun getByProductId(id: Int): List<ProductVariant>
}
