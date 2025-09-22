package com.example.computerstore.data.dao

import com.example.computerstore.data.model.Category

interface CategoryDao {
    suspend fun getAll(): List<Category>
    suspend fun getById(id: Int): Category?
    suspend fun insert(category: Category)
    suspend fun update(category: Category)
    suspend fun delete(id: Int)
}
