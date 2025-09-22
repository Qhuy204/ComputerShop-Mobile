package com.example.computerstore.data.repository

import com.example.computerstore.data.dao.AttributeTypeDao
import com.example.computerstore.data.model.AttributeType

class AttributeTypeRepository(private val attributeTypeDao: AttributeTypeDao) {
    suspend fun getAttributeTypes(): List<AttributeType> = attributeTypeDao.getAll()
    suspend fun getAttributeType(id: Int): AttributeType? = attributeTypeDao.getById(id)
    suspend fun addAttributeType(attributeType: AttributeType) = attributeTypeDao.insert(attributeType)
    suspend fun updateAttributeType(attributeType: AttributeType) = attributeTypeDao.update(attributeType)
    suspend fun deleteAttributeType(id: Int) = attributeTypeDao.delete(id)
}
