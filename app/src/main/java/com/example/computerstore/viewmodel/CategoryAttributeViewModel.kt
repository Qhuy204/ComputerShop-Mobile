package com.example.computerstore.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.computerstore.data.dao.impl.CategoryAttributeDaoImpl
import com.example.computerstore.data.model.CategoryAttribute
import com.example.computerstore.data.repository.CategoryAttributeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CategoryAttributeViewModel : ViewModel() {
    private val repository = CategoryAttributeRepository(CategoryAttributeDaoImpl())

    private val _categoryAttributes = MutableStateFlow<List<CategoryAttribute>>(emptyList())
    val categoryAttributes: StateFlow<List<CategoryAttribute>> = _categoryAttributes

    private val _currentCategoryAttribute = MutableStateFlow<CategoryAttribute?>(null)
    val currentCategoryAttribute: StateFlow<CategoryAttribute?> = _currentCategoryAttribute

    fun loadAllCategoryAttributes() {
        viewModelScope.launch {
            _categoryAttributes.value = repository.getCategoryAttributes()
        }
    }

    fun loadCategoryAttribute(categoryId: Int, attributeTypeId: Int) {
        viewModelScope.launch {
            _currentCategoryAttribute.value = repository.getCategoryAttribute(categoryId, attributeTypeId)
        }
    }

    fun addCategoryAttribute(categoryAttribute: CategoryAttribute) {
        viewModelScope.launch {
            repository.addCategoryAttribute(categoryAttribute)
            loadAllCategoryAttributes()
        }
    }

    fun updateCategoryAttribute(categoryAttribute: CategoryAttribute) {
        viewModelScope.launch {
            repository.updateCategoryAttribute(categoryAttribute)
            loadAllCategoryAttributes()
        }
    }

    fun deleteCategoryAttribute(categoryId: Int, attributeTypeId: Int) {
        viewModelScope.launch {
            repository.deleteCategoryAttribute(categoryId, attributeTypeId)
            loadAllCategoryAttributes()
        }
    }
}
