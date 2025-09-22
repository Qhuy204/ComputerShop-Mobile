package com.example.computerstore.data.dao

import com.example.computerstore.data.model.Blog

interface BlogDao {
    suspend fun getAll(): List<Blog>
    suspend fun getById(id: Int): Blog?
    suspend fun insert(blog: Blog)
    suspend fun update(blog: Blog)
    suspend fun delete(id: Int)
}
