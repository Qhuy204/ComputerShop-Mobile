package com.example.computerstore.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.computerstore.data.dao.impl.ProductImageDaoImpl
import com.example.computerstore.data.model.ProductImage
import com.example.computerstore.data.repository.ProductImageRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProductImageViewModel : ViewModel() {
    private val repository = ProductImageRepository(ProductImageDaoImpl())

    private val _productImages = MutableStateFlow<List<ProductImage>>(emptyList())
    val productImages: StateFlow<List<ProductImage>> = _productImages

    private val _currentProductImage = MutableStateFlow<ProductImage?>(null)
    val currentProductImage: StateFlow<ProductImage?> = _currentProductImage

    fun loadAllProductImages() {
        viewModelScope.launch {
            _productImages.value = repository.getProductImages()
        }
    }

    fun loadProductImage(id: Int) {
        viewModelScope.launch {
            _currentProductImage.value = repository.getProductImage(id)
        }
    }

    fun addProductImage(productImage: ProductImage) {
        viewModelScope.launch {
            repository.addProductImage(productImage)
            loadAllProductImages()
        }
    }

    fun updateProductImage(productImage: ProductImage) {
        viewModelScope.launch {
            repository.updateProductImage(productImage)
            loadAllProductImages()
        }
    }

    fun deleteProductImage(id: Int) {
        viewModelScope.launch {
            repository.deleteProductImage(id)
            loadAllProductImages()
        }
    }

    fun loadProductImagesByProduct(productId: Int) {
        viewModelScope.launch {
            _productImages.value = repository.getProductImagesByProduct(productId)
        }
    }

}
