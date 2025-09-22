package com.example.computerstore.data.repository

import com.example.computerstore.data.dao.UserAddressDao
import com.example.computerstore.data.model.UserAddress

class UserAddressRepository(private val userAddressDao: UserAddressDao) {
    suspend fun getUserAddresses(): List<UserAddress> = userAddressDao.getAll()
    suspend fun getUserAddress(id: Int): UserAddress? = userAddressDao.getById(id)
    suspend fun addUserAddress(userAddress: UserAddress) = userAddressDao.insert(userAddress)
    suspend fun updateUserAddress(userAddress: UserAddress) = userAddressDao.update(userAddress)
    suspend fun deleteUserAddress(id: Int) = userAddressDao.delete(id)
}
