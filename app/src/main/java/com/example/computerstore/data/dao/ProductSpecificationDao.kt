package com.example.computerstore.data.dao

import com.example.computerstore.data.model.ProductSpecification

interface ProductSpecificationDao {
    suspend fun getAll(): List<ProductSpecification>
    suspend fun getById(id: Int): ProductSpecification?
    suspend fun insert(productSpecification: ProductSpecification)
    suspend fun update(productSpecification: ProductSpecification)
    suspend fun delete(id: Int)
}
