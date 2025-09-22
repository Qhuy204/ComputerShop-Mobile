package com.example.computerstore.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.computerstore.data.dao.impl.BrandDaoImpl
import com.example.computerstore.data.model.Brand
import com.example.computerstore.data.repository.BrandRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class BrandViewModel : ViewModel() {
    private val repository = BrandRepository(BrandDaoImpl())

    private val _brands = MutableStateFlow<List<Brand>>(emptyList())
    val brands: StateFlow<List<Brand>> = _brands

    private val _currentBrand = MutableStateFlow<Brand?>(null)
    val currentBrand: StateFlow<Brand?> = _currentBrand

    fun loadAllBrands() {
        viewModelScope.launch {
            _brands.value = repository.getBrands()
        }
    }

    fun loadBrand(id: Int) {
        viewModelScope.launch {
            _currentBrand.value = repository.getBrand(id)
        }
    }

    fun addBrand(brand: Brand) {
        viewModelScope.launch {
            repository.addBrand(brand)
            loadAllBrands()
        }
    }

    fun updateBrand(brand: Brand) {
        viewModelScope.launch {
            repository.updateBrand(brand)
            loadAllBrands()
        }
    }

    fun deleteBrand(id: Int) {
        viewModelScope.launch {
            repository.deleteBrand(id)
            loadAllBrands()
        }
    }
}
