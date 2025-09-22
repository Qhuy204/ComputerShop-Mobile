package com.example.computerstore.data.repository

import com.example.computerstore.data.dao.ProductDao
import com.example.computerstore.data.model.Product

class ProductRepository(private val productDao: ProductDao) {
    suspend fun getProducts(): List<Product> = productDao.getAll()
    suspend fun getProduct(id: Int): Product? = productDao.getById(id)
    suspend fun addProduct(product: Product) = productDao.insert(product)
    suspend fun updateProduct(product: Product) = productDao.update(product)
    suspend fun deleteProduct(id: Int) = productDao.delete(id)
}
