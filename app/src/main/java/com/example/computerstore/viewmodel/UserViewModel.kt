package com.example.computerstore.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.computerstore.data.dao.impl.UserDaoImpl
import com.example.computerstore.data.model.User
import com.example.computerstore.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UserViewModel : ViewModel() {
    private val repository = UserRepository(UserDaoImpl())

    private val _users = MutableStateFlow<List<User>>(emptyList())
    val users: StateFlow<List<User>> = _users

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser

    fun loadAllUsers() {
        viewModelScope.launch {
            _users.value = repository.getUsers()
        }
    }

    fun loadUser(id: Int) {
        viewModelScope.launch {
            _currentUser.value = repository.getUser(id)
        }
    }

    fun addUser(user: User) {
        viewModelScope.launch {
            repository.addUser(user)
            loadAllUsers()
        }
    }

    fun updateUser(user: User) {
        viewModelScope.launch {
            repository.updateUser(user)
            loadAllUsers()
        }
    }

    fun deleteUser(id: Int) {
        viewModelScope.launch {
            repository.deleteUser(id)
            loadAllUsers()
        }
    }
}
