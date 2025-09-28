package com.example.computerstore.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.computerstore.data.dao.impl.ProductVariantDaoImpl
import com.example.computerstore.data.model.ProductVariant
import com.example.computerstore.data.repository.ProductVariantRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProductVariantViewModel : ViewModel() {
    private val repository = ProductVariantRepository(ProductVariantDaoImpl())

    private val _productVariants = MutableStateFlow<List<ProductVariant>>(emptyList())
    val productVariants: StateFlow<List<ProductVariant>> = _productVariants

    private val _variants = MutableStateFlow<List<ProductVariant>>(emptyList())
    val variants: StateFlow<List<ProductVariant>> = _variants

    fun loadVariantsByProduct(productId: Int) {
        viewModelScope.launch {
            _variants.value = repository.getVariantsByProductId(productId)
            // Chọn variant default nếu có
            _currentProductVariant.value = _variants.value.firstOrNull { it.is_default == 1 }
        }
    }


    private val _currentProductVariant = MutableStateFlow<ProductVariant?>(null)
    val currentProductVariant: StateFlow<ProductVariant?> = _currentProductVariant

    fun loadAllProductVariants() {
        viewModelScope.launch {
            _productVariants.value = repository.getProductVariants()
        }
    }

    fun loadProductVariant(id: Int) {
        viewModelScope.launch {
            _currentProductVariant.value = repository.getProductVariant(id)
        }
    }

    fun addProductVariant(productVariant: ProductVariant) {
        viewModelScope.launch {
            repository.addProductVariant(productVariant)
            loadAllProductVariants()
        }
    }

    fun updateProductVariant(productVariant: ProductVariant) {
        viewModelScope.launch {
            repository.updateProductVariant(productVariant)
            loadAllProductVariants()
        }
    }

    fun deleteProductVariant(id: Int) {
        viewModelScope.launch {
            repository.deleteProductVariant(id)
            loadAllProductVariants()
        }
    }
}
