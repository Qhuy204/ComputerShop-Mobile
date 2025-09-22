package com.example.computerstore.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.computerstore.data.dao.impl.PaymentMethodDaoImpl
import com.example.computerstore.data.model.PaymentMethod
import com.example.computerstore.data.repository.PaymentMethodRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PaymentMethodViewModel : ViewModel() {
    private val repository = PaymentMethodRepository(PaymentMethodDaoImpl())

    private val _paymentMethods = MutableStateFlow<List<PaymentMethod>>(emptyList())
    val paymentMethods: StateFlow<List<PaymentMethod>> = _paymentMethods

    private val _currentPaymentMethod = MutableStateFlow<PaymentMethod?>(null)
    val currentPaymentMethod: StateFlow<PaymentMethod?> = _currentPaymentMethod

    fun loadAllPaymentMethods() {
        viewModelScope.launch {
            _paymentMethods.value = repository.getPaymentMethods()
        }
    }

    fun loadPaymentMethod(id: Int) {
        viewModelScope.launch {
            _currentPaymentMethod.value = repository.getPaymentMethod(id)
        }
    }

    fun addPaymentMethod(paymentMethod: PaymentMethod) {
        viewModelScope.launch {
            repository.addPaymentMethod(paymentMethod)
            loadAllPaymentMethods()
        }
    }

    fun updatePaymentMethod(paymentMethod: PaymentMethod) {
        viewModelScope.launch {
            repository.updatePaymentMethod(paymentMethod)
            loadAllPaymentMethods()
        }
    }

    fun deletePaymentMethod(id: Int) {
        viewModelScope.launch {
            repository.deletePaymentMethod(id)
            loadAllPaymentMethods()
        }
    }
}
