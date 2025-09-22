package com.example.computerstore.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.computerstore.data.dao.impl.TagDaoImpl
import com.example.computerstore.data.model.Tag
import com.example.computerstore.data.repository.TagRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TagViewModel : ViewModel() {
    private val repository = TagRepository(TagDaoImpl())

    private val _tags = MutableStateFlow<List<Tag>>(emptyList())
    val tags: StateFlow<List<Tag>> = _tags

    private val _currentTag = MutableStateFlow<Tag?>(null)
    val currentTag: StateFlow<Tag?> = _currentTag

    fun loadAllTags() {
        viewModelScope.launch {
            _tags.value = repository.getTags()
        }
    }

    fun loadTag(name: String) {
        viewModelScope.launch {
            _currentTag.value = repository.getTag(name)
        }
    }

    fun addTag(tag: Tag) {
        viewModelScope.launch {
            repository.addTag(tag)
            loadAllTags()
        }
    }

    fun updateTag(tag: Tag) {
        viewModelScope.launch {
            repository.updateTag(tag)
            loadAllTags()
        }
    }

    fun deleteTag(name: String) {
        viewModelScope.launch {
            repository.deleteTag(name)
            loadAllTags()
        }
    }
}
