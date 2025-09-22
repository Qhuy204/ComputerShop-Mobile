package com.example.computerstore.data.repository

import com.example.computerstore.data.dao.TagDao
import com.example.computerstore.data.model.Tag

class TagRepository(private val tagDao: TagDao) {
    suspend fun getTags(): List<Tag> = tagDao.getAll()
    suspend fun getTag(id: String): Tag? = tagDao.getById(id)
    suspend fun addTag(tag: Tag) = tagDao.insert(tag)
    suspend fun updateTag(tag: Tag) = tagDao.update(tag)
    suspend fun deleteTag(id: String) = tagDao.delete(id)
}
