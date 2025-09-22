package com.example.computerstore.data.dao

import com.example.computerstore.data.model.AttributeType

interface AttributeTypeDao {
    suspend fun getAll(): List<AttributeType>
    suspend fun getById(id: Int): AttributeType?
    suspend fun insert(attributeType: AttributeType)
    suspend fun update(attributeType: AttributeType)
    suspend fun delete(id: Int)
}
