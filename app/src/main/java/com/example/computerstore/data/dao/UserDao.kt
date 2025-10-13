package com.example.computerstore.data.dao

import com.example.computerstore.data.model.User

interface UserDao {
    suspend fun getAll(): List<User>
    suspend fun getById(id: String): User?
    suspend fun insert(user: User)
    suspend fun update(user: User)
    suspend fun delete(id: String)
}