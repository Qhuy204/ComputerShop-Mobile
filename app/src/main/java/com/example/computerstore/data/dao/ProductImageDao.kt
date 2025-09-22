package com.example.computerstore.data.dao

import com.example.computerstore.data.model.ProductImage

interface ProductImageDao {
    suspend fun getAll(): List<ProductImage>
    suspend fun getById(id: Int): ProductImage?
    suspend fun insert(productImage: ProductImage)
    suspend fun update(productImage: ProductImage)
    suspend fun delete(id: Int)
}
