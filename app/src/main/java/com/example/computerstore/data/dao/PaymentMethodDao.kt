package com.example.computerstore.data.dao

import com.example.computerstore.data.model.PaymentMethod

interface PaymentMethodDao {
    suspend fun getAll(): List<PaymentMethod>
    suspend fun getById(id: Int): PaymentMethod?
    suspend fun insert(paymentMethod: PaymentMethod)
    suspend fun update(paymentMethod: PaymentMethod)
    suspend fun delete(id: Int)
}
