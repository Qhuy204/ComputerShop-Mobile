package com.example.computerstore.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.computerstore.data.dao.impl.ProductSpecificationDaoImpl
import com.example.computerstore.data.model.ProductSpecification
import com.example.computerstore.data.repository.ProductSpecificationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProductSpecificationViewModel : ViewModel() {
    private val repository = ProductSpecificationRepository(ProductSpecificationDaoImpl())

    private val _productSpecifications = MutableStateFlow<List<ProductSpecification>>(emptyList())
    val productSpecifications: StateFlow<List<ProductSpecification>> = _productSpecifications

    private val _currentProductSpecification = MutableStateFlow<ProductSpecification?>(null)
    val currentProductSpecification: StateFlow<ProductSpecification?> = _currentProductSpecification


    private val _specs = MutableStateFlow<List<ProductSpecification>>(emptyList())
    val specs: StateFlow<List<ProductSpecification>> = _specs

    fun loadSpecificationsByProduct(productId: Int) {
        viewModelScope.launch {
            _specs.value = repository.getSpecificationsByProductId(productId)
        }
    }

    fun loadAllProductSpecifications() {
        viewModelScope.launch {
            _productSpecifications.value = repository.getProductSpecifications()
        }
    }

    fun loadProductSpecification(id: Int) {
        viewModelScope.launch {
            _currentProductSpecification.value = repository.getProductSpecification(id)
        }
    }

    fun addProductSpecification(productSpecification: ProductSpecification) {
        viewModelScope.launch {
            repository.addProductSpecification(productSpecification)
            loadAllProductSpecifications()
        }
    }

    fun updateProductSpecification(productSpecification: ProductSpecification) {
        viewModelScope.launch {
            repository.updateProductSpecification(productSpecification)
            loadAllProductSpecifications()
        }
    }

    fun deleteProductSpecification(id: Int) {
        viewModelScope.launch {
            repository.deleteProductSpecification(id)
            loadAllProductSpecifications()
        }
    }
}
