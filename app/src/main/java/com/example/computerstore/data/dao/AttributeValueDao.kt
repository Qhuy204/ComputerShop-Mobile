package com.example.computerstore.data.dao

import com.example.computerstore.data.model.AttributeValue

interface AttributeValueDao {
    suspend fun getAll(): List<AttributeValue>
    suspend fun getById(id: Int): AttributeValue?
    suspend fun insert(attributeValue: AttributeValue)
    suspend fun update(attributeValue: AttributeValue)
    suspend fun delete(id: Int)
}
