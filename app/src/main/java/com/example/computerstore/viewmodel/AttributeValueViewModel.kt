package com.example.computerstore.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.computerstore.data.dao.impl.AttributeValueDaoImpl
import com.example.computerstore.data.model.AttributeValue
import com.example.computerstore.data.repository.AttributeValueRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AttributeValueViewModel : ViewModel() {
    private val repository = AttributeValueRepository(AttributeValueDaoImpl())

    private val _attributeValues = MutableStateFlow<List<AttributeValue>>(emptyList())
    val attributeValues: StateFlow<List<AttributeValue>> = _attributeValues

    private val _currentAttributeValue = MutableStateFlow<AttributeValue?>(null)
    val currentAttributeValue: StateFlow<AttributeValue?> = _currentAttributeValue

    fun loadAllAttributeValues() {
        viewModelScope.launch {
            _attributeValues.value = repository.getAttributeValues()
        }
    }

    fun loadAttributeValue(id: Int) {
        viewModelScope.launch {
            _currentAttributeValue.value = repository.getAttributeValue(id)
        }
    }

    fun addAttributeValue(attributeValue: AttributeValue) {
        viewModelScope.launch {
            repository.addAttributeValue(attributeValue)
            loadAllAttributeValues()
        }
    }

    fun updateAttributeValue(attributeValue: AttributeValue) {
        viewModelScope.launch {
            repository.updateAttributeValue(attributeValue)
            loadAllAttributeValues()
        }
    }

    fun deleteAttributeValue(id: Int) {
        viewModelScope.launch {
            repository.deleteAttributeValue(id)
            loadAllAttributeValues()
        }
    }
}
