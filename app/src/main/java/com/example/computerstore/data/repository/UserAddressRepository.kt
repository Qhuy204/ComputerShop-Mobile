package com.example.computerstore.data.repository

import com.example.computerstore.data.dao.UserAddressDao
import com.example.computerstore.data.model.UserAddress

class UserAddressRepository(private val userAddressDao: UserAddressDao) {

    suspend fun getAll(): List<UserAddress> = userAddressDao.getAll()

    suspend fun getById(id: String): UserAddress? = userAddressDao.getById(id)

    suspend fun addUserAddress(userAddress: UserAddress) =
        userAddressDao.insert(userAddress)

    suspend fun updateUserAddress(userAddress: UserAddress) =
        userAddressDao.update(userAddress)

    suspend fun deleteUserAddress(id: String?) =
        userAddressDao.delete(id)

    suspend fun setDefaultAddress(addressId: Any?, userId: String) {
        val allAddresses = userAddressDao.getAll().filter { it.user_id == userId }
        allAddresses.forEach { addr ->
            val updated = addr.copy(is_default = if (addr.address_id == addressId) 1 else 0)
            userAddressDao.update(updated)
        }
    }
}
