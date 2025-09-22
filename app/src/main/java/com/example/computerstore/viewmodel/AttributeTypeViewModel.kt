package com.example.computerstore.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.computerstore.data.dao.impl.AttributeTypeDaoImpl
import com.example.computerstore.data.model.AttributeType
import com.example.computerstore.data.repository.AttributeTypeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AttributeTypeViewModel : ViewModel() {
    private val repository = AttributeTypeRepository(AttributeTypeDaoImpl())

    private val _attributeTypes = MutableStateFlow<List<AttributeType>>(emptyList())
    val attributeTypes: StateFlow<List<AttributeType>> = _attributeTypes

    private val _currentAttributeType = MutableStateFlow<AttributeType?>(null)
    val currentAttributeType: StateFlow<AttributeType?> = _currentAttributeType

    fun loadAllAttributeTypes() {
        viewModelScope.launch {
            _attributeTypes.value = repository.getAttributeTypes()
        }
    }

    fun loadAttributeType(id: Int) {
        viewModelScope.launch {
            _currentAttributeType.value = repository.getAttributeType(id)
        }
    }

    fun addAttributeType(attributeType: AttributeType) {
        viewModelScope.launch {
            repository.addAttributeType(attributeType)
            loadAllAttributeTypes()
        }
    }

    fun updateAttributeType(attributeType: AttributeType) {
        viewModelScope.launch {
            repository.updateAttributeType(attributeType)
            loadAllAttributeTypes()
        }
    }

    fun deleteAttributeType(id: Int) {
        viewModelScope.launch {
            repository.deleteAttributeType(id)
            loadAllAttributeTypes()
        }
    }
}
