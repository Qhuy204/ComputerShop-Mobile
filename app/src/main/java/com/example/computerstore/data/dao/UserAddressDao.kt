package com.example.computerstore.data.dao

import com.example.computerstore.data.model.UserAddress

interface UserAddressDao {
    suspend fun getAll(): List<UserAddress>
    suspend fun getById(id: String): UserAddress?
    suspend fun insert(userAddress: UserAddress)
    suspend fun update(userAddress: UserAddress)
    suspend fun delete(id: String?)
}
