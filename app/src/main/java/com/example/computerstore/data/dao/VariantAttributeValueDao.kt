package com.example.computerstore.data.dao

import com.example.computerstore.data.model.VariantAttributeValue

interface VariantAttributeValueDao {
    suspend fun getAll(): List<VariantAttributeValue>
    suspend fun getById(variantId: Int, attributeValueId: Int): VariantAttributeValue?
    suspend fun insert(variantAttributeValue: VariantAttributeValue)
    suspend fun update(variantAttributeValue: VariantAttributeValue)
    suspend fun delete(variantId: Int, attributeValueId: Int)
}
