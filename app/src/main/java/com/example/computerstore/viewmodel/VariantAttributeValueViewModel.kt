package com.example.computerstore.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.computerstore.data.dao.impl.VariantAttributeValueDaoImpl
import com.example.computerstore.data.repository.VariantAttributeValueRepository
import com.example.computerstore.data.model.VariantAttributeValue
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class VariantAttributeValueViewModel : ViewModel() {
    private val repository = VariantAttributeValueRepository(VariantAttributeValueDaoImpl())

    private val _variantAttributeValues = MutableStateFlow<List<VariantAttributeValue>>(emptyList())
    val variantAttributeValues: StateFlow<List<VariantAttributeValue>> = _variantAttributeValues

    private val _currentVariantAttributeValue = MutableStateFlow<VariantAttributeValue?>(null)
    val currentVariantAttributeValue: StateFlow<VariantAttributeValue?> = _currentVariantAttributeValue

    fun loadAllVariantAttributeValues() {
        viewModelScope.launch {
            _variantAttributeValues.value = repository.getVariantAttributeValues()
        }
    }

    fun loadVariantAttributeValue(variantId: Int, attributeValueId: Int) {
        viewModelScope.launch {
            _currentVariantAttributeValue.value = repository.getVariantAttributeValue(variantId, attributeValueId)
        }
    }

    fun addVariantAttributeValue(variantAttributeValue: VariantAttributeValue) {
        viewModelScope.launch {
            repository.addVariantAttributeValue(variantAttributeValue)
            loadAllVariantAttributeValues()
        }
    }

    fun updateVariantAttributeValue(variantAttributeValue: VariantAttributeValue) {
        viewModelScope.launch {
            repository.updateVariantAttributeValue(variantAttributeValue)
            loadAllVariantAttributeValues()
        }
    }

    fun deleteVariantAttributeValue(variantId: Int, attributeValueId: Int) {
        viewModelScope.launch {
            repository.deleteVariantAttributeValue(variantId, attributeValueId)
            loadAllVariantAttributeValues()
        }
    }
}
