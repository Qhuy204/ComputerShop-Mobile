package com.example.computerstore.data.repository

import com.example.computerstore.data.dao.VariantAttributeValueDao
import com.example.computerstore.data.model.VariantAttributeValue

class VariantAttributeValueRepository(private val variantAttributeValueDao: VariantAttributeValueDao) {
    suspend fun getVariantAttributeValues(): List<VariantAttributeValue> = variantAttributeValueDao.getAll()
    suspend fun getVariantAttributeValue(variantId: Int, attributeValueId: Int): VariantAttributeValue? = variantAttributeValueDao.getById(variantId, attributeValueId)
    suspend fun addVariantAttributeValue(variantAttributeValue: VariantAttributeValue) = variantAttributeValueDao.insert(variantAttributeValue)
    suspend fun updateVariantAttributeValue(variantAttributeValue: VariantAttributeValue) = variantAttributeValueDao.update(variantAttributeValue)
    suspend fun deleteVariantAttributeValue(variantId: Int, attributeValueId: Int) = variantAttributeValueDao.delete(variantId, attributeValueId)
}
