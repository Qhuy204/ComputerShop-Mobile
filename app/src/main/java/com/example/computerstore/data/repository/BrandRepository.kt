package com.example.computerstore.data.repository

import com.example.computerstore.data.dao.BrandDao
import com.example.computerstore.data.model.Brand

class BrandRepository(private val brandDao: BrandDao) {
    suspend fun getBrands(): List<Brand> = brandDao.getAll()
    suspend fun getBrand(id: Int): Brand? = brandDao.getById(id)
    suspend fun addBrand(brand: Brand) = brandDao.insert(brand)
    suspend fun updateBrand(brand: Brand) = brandDao.update(brand)
    suspend fun deleteBrand(id: Int) = brandDao.delete(id)
}
