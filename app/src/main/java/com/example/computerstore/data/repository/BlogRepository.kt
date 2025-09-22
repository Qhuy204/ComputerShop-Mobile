package com.example.computerstore.data.repository

import com.example.computerstore.data.dao.BlogDao
import com.example.computerstore.data.model.Blog

class BlogRepository(private val blogDao: BlogDao) {
    suspend fun getBlogs(): List<Blog> = blogDao.getAll()
    suspend fun getBlog(id: Int): Blog? = blogDao.getById(id)
    suspend fun addBlog(blog: Blog) = blogDao.insert(blog)
    suspend fun updateBlog(blog: Blog) = blogDao.update(blog)
    suspend fun deleteBlog(id: Int) = blogDao.delete(id)
}
