package com.example.computerstore.data.repository

import com.example.computerstore.data.dao.ProductImageDao
import com.example.computerstore.data.model.ProductImage

class ProductImageRepository(private val productImageDao: ProductImageDao) {
    suspend fun getProductImages(): List<ProductImage> = productImageDao.getAll()
    suspend fun getProductImage(id: Int): ProductImage? = productImageDao.getById(id)
    suspend fun addProductImage(productImage: ProductImage) = productImageDao.insert(productImage)
    suspend fun updateProductImage(productImage: ProductImage) = productImageDao.update(productImage)
    suspend fun deleteProductImage(id: Int) = productImageDao.delete(id)
}
