package com.example.computerstore.data.repository

import com.example.computerstore.data.dao.AttributeValueDao
import com.example.computerstore.data.model.AttributeValue

class AttributeValueRepository(private val attributeValueDao: AttributeValueDao) {
    suspend fun getAttributeValues(): List<AttributeValue> = attributeValueDao.getAll()
    suspend fun getAttributeValue(id: Int): AttributeValue? = attributeValueDao.getById(id)
    suspend fun addAttributeValue(attributeValue: AttributeValue) = attributeValueDao.insert(attributeValue)
    suspend fun updateAttributeValue(attributeValue: AttributeValue) = attributeValueDao.update(attributeValue)
    suspend fun deleteAttributeValue(id: Int) = attributeValueDao.delete(id)
}
