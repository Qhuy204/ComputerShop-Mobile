package com.example.computerstore.data.repository

import com.example.computerstore.data.dao.CategoryDao
import com.example.computerstore.data.model.Category

class CategoryRepository(private val categoryDao: CategoryDao) {
    suspend fun getCategories(): List<Category> = categoryDao.getAll()
    suspend fun getCategory(id: Int): Category? = categoryDao.getById(id)
    suspend fun addCategory(category: Category) = categoryDao.insert(category)
    suspend fun updateCategory(category: Category) = categoryDao.update(category)
    suspend fun deleteCategory(id: Int) = categoryDao.delete(id)
}
