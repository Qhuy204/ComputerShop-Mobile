package com.example.computerstore.data.repository

import com.example.computerstore.data.dao.UserAddressDao
import com.example.computerstore.data.model.UserAddress

class UserAddressRepository(private val userAddressDao: UserAddressDao) {
    suspend fun getUserAddresses(): List<UserAddress> = userAddressDao.getAll()
    suspend fun getUserAddress(id: Int): UserAddress? = userAddressDao.getById(id)
    suspend fun addUserAddress(userAddress: UserAddress) = userAddressDao.insert(userAddress)
    suspend fun updateUserAddress(userAddress: UserAddress) = userAddressDao.update(userAddress)
    suspend fun deleteUserAddress(id: Int) = userAddressDao.delete(id)

    suspend fun setDefaultAddress(addressId: Any?, userId: String) {
        val allAddresses = userAddressDao.getAll().filter { it.user_id == userId }

        // Đặt tất cả địa chỉ khác về 0
        allAddresses.forEach { addr ->
            val updated = addr.copy(is_default = if (addr.address_id == addressId) 1 else 0)
            userAddressDao.update(updated)
        }
    }

}
