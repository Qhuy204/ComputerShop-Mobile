package com.example.computerstore.data.repository

import com.example.computerstore.data.dao.PaymentMethodDao
import com.example.computerstore.data.model.PaymentMethod

class PaymentMethodRepository(private val paymentMethodDao: PaymentMethodDao) {
    suspend fun getPaymentMethods(): List<PaymentMethod> = paymentMethodDao.getAll()
    suspend fun getPaymentMethod(id: Int): PaymentMethod? = paymentMethodDao.getById(id)
    suspend fun addPaymentMethod(paymentMethod: PaymentMethod) = paymentMethodDao.insert(paymentMethod)
    suspend fun updatePaymentMethod(paymentMethod: PaymentMethod) = paymentMethodDao.update(paymentMethod)
    suspend fun deletePaymentMethod(id: Int) = paymentMethodDao.delete(id)
}
