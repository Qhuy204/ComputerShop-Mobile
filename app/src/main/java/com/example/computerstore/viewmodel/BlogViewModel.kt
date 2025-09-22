package com.example.computerstore.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.computerstore.data.dao.impl.BlogDaoImpl
import com.example.computerstore.data.model.Blog
import com.example.computerstore.data.repository.BlogRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class BlogViewModel : ViewModel() {
    private val repository = BlogRepository(BlogDaoImpl())

    private val _blogs = MutableStateFlow<List<Blog>>(emptyList())
    val blogs: StateFlow<List<Blog>> = _blogs

    private val _currentBlog = MutableStateFlow<Blog?>(null)
    val currentBlog: StateFlow<Blog?> = _currentBlog

    fun loadAllBlogs() {
        viewModelScope.launch {
            _blogs.value = repository.getBlogs()
        }
    }

    fun loadBlog(id: Int) {
        viewModelScope.launch {
            _currentBlog.value = repository.getBlog(id)
        }
    }

    fun addBlog(blog: Blog) {
        viewModelScope.launch {
            repository.addBlog(blog)
            loadAllBlogs()
        }
    }

    fun updateBlog(blog: Blog) {
        viewModelScope.launch {
            repository.updateBlog(blog)
            loadAllBlogs()
        }
    }

    fun deleteBlog(id: Int) {
        viewModelScope.launch {
            repository.deleteBlog(id)
            loadAllBlogs()
        }
    }
}
