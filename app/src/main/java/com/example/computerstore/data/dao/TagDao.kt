package com.example.computerstore.data.dao

import com.example.computerstore.data.model.Tag

interface TagDao {
    suspend fun getAll(): List<Tag>
    suspend fun getById(id: String): Tag?
    suspend fun insert(tag: Tag)
    suspend fun update(tag: Tag)
    suspend fun delete(id: String)
}
