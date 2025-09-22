package com.example.computerstore.data.repository

import com.example.computerstore.data.dao.ProductSpecificationDao
import com.example.computerstore.data.model.ProductSpecification

class ProductSpecificationRepository(private val productSpecificationDao: ProductSpecificationDao) {
    suspend fun getProductSpecifications(): List<ProductSpecification> = productSpecificationDao.getAll()
    suspend fun getProductSpecification(id: Int): ProductSpecification? = productSpecificationDao.getById(id)
    suspend fun addProductSpecification(productSpecification: ProductSpecification) = productSpecificationDao.insert(productSpecification)
    suspend fun updateProductSpecification(productSpecification: ProductSpecification) = productSpecificationDao.update(productSpecification)
    suspend fun deleteProductSpecification(id: Int) = productSpecificationDao.delete(id)
}
