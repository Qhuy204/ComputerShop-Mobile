package com.example.computerstore.data.dao

import com.example.computerstore.data.model.CategoryAttribute

interface CategoryAttributeDao {
    suspend fun getAll(): List<CategoryAttribute>
    suspend fun getById(categoryId: Int, attributeTypeId: Int): CategoryAttribute?
    suspend fun insert(categoryAttribute: CategoryAttribute)
    suspend fun update(categoryAttribute: CategoryAttribute)
    suspend fun delete(categoryId: Int, attributeTypeId: Int)
}