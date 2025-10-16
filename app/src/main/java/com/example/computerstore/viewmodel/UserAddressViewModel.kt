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
    val userAddresses: StateFlow<List<UserAddress>> get() = _userAddresses

    fun loadAddressesByUser(userId: String) {
        viewModelScope.launch {
            val all = repository.getAll()
            _userAddresses.value = all.filter { it.user_id == userId }
        }
    }

    suspend fun getAddressById(id: String): UserAddress? {
        return repository.getById(id)
    }

    fun addUserAddress(address: UserAddress) {
        viewModelScope.launch {
            repository.addUserAddress(address)
            loadAddressesByUser(address.user_id ?: "")
        }
    }

    fun updateUserAddress(address: UserAddress) {
        viewModelScope.launch {
            repository.updateUserAddress(address)
            loadAddressesByUser(address.user_id ?: "")
        }
    }

    fun deleteUserAddress(id: String?) {
        viewModelScope.launch {
            repository.deleteUserAddress(id)
        }
    }

    fun setDefaultAddress(addressId: String?, userId: String) {
        viewModelScope.launch {
            repository.setDefaultAddress(addressId, userId)
            loadAddressesByUser(userId)
        }
    }
}
