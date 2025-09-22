package com.example.computerstore.data.repository

import com.example.computerstore.data.dao.CategoryAttributeDao
import com.example.computerstore.data.model.CategoryAttribute

class CategoryAttributeRepository(private val categoryAttributeDao: CategoryAttributeDao) {
    suspend fun getCategoryAttributes(): List<CategoryAttribute> = categoryAttributeDao.getAll()
    suspend fun getCategoryAttribute(categoryId: Int, attributeTypeId: Int): CategoryAttribute? = categoryAttributeDao.getById(categoryId, attributeTypeId)
    suspend fun addCategoryAttribute(categoryAttribute: CategoryAttribute) = categoryAttributeDao.insert(categoryAttribute)
    suspend fun updateCategoryAttribute(categoryAttribute: CategoryAttribute) = categoryAttributeDao.update(categoryAttribute)
    suspend fun deleteCategoryAttribute(categoryId: Int, attributeTypeId: Int) = categoryAttributeDao.delete(categoryId, attributeTypeId)
}
