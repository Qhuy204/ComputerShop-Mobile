package com.example.computerstore.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.computerstore.data.dao.impl.UserAddressDaoImpl
import com.example.computerstore.data.model.UserAddress
import com.example.computerstore.data.repository.UserAddressRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UserAddressViewModel : ViewModel() {
    private val repository = UserAddressRepository(UserAddressDaoImpl())

    private val _userAddresses = MutableStateFlow<List<UserAddress>>(emptyList())
    val userAddresses: StateFlow<List<UserAddress>> = _userAddresses

    private val _currentUserAddress = MutableStateFlow<UserAddress?>(null)
    val currentUserAddress: StateFlow<UserAddress?> = _currentUserAddress

    fun loadAllUserAddresses() {
        viewModelScope.launch {
            _userAddresses.value = repository.getUserAddresses()
        }
    }

    fun loadUserAddress(id: Int) {
        viewModelScope.launch {
            _currentUserAddress.value = repository.getUserAddress(id)
        }
    }

    fun addUserAddress(userAddress: UserAddress) {
        viewModelScope.launch {
            repository.addUserAddress(userAddress)
            loadAllUserAddresses()
        }
    }

    fun updateUserAddress(userAddress: UserAddress) {
        viewModelScope.launch {
            repository.updateUserAddress(userAddress)
            loadAllUserAddresses()
        }
    }

    fun deleteUserAddress(id: Int) {
        viewModelScope.launch {
            repository.deleteUserAddress(id)
            loadAllUserAddresses()
        }
    }
}
