package com.example.computerstore.data.repository

import com.example.computerstore.data.dao.UserDao
import com.example.computerstore.data.model.User

class UserRepository(private val userDao: UserDao) {

    suspend fun getUsers(): List<User> = userDao.getAll()

    suspend fun getUser(id: String): User? = userDao.getById(id)

    suspend fun addUser(user: User) = userDao.insert(user)

    suspend fun updateUser(user: User) = userDao.update(user)

    suspend fun deleteUser(id: String) = userDao.delete(id)
}
