package com.example.computerstore.data.repository

import com.example.computerstore.data.dao.ProductVariantDao
import com.example.computerstore.data.model.ProductVariant

class ProductVariantRepository(private val productVariantDao: ProductVariantDao) {
    suspend fun getProductVariants(): List<ProductVariant> = productVariantDao.getAll()
    suspend fun getProductVariant(id: Int): ProductVariant? = productVariantDao.getById(id)
    suspend fun addProductVariant(productVariant: ProductVariant) = productVariantDao.insert(productVariant)
    suspend fun updateProductVariant(productVariant: ProductVariant) = productVariantDao.update(productVariant)
    suspend fun deleteProductVariant(id: Int) = productVariantDao.delete(id)
    suspend fun getVariantsByProductId(id: Int): List<ProductVariant> = productVariantDao.getByProductId(id)
}
