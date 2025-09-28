package com.example.computerstore.data.dao.impl

import com.example.computerstore.data.dao.PaymentMethodDao
import com.example.computerstore.data.model.PaymentMethod
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class PaymentMethodDaoImpl : PaymentMethodDao {
    private val db = FirebaseFirestore.getInstance()
    private val collection = db.collection("payment_methods")

    override suspend fun getAll(): List<PaymentMethod> {
        val snapshot = collection.get().await()
        return snapshot.documents.mapNotNull { it.toObject(PaymentMethod::class.java) }
    }

    override suspend fun getById(id: Int): PaymentMethod? {
        val doc = collection.document(id.toString()).get().await()
        return doc.toObject(PaymentMethod::class.java)
    }

    override suspend fun insert(paymentMethod: PaymentMethod) {
        collection.document(paymentMethod.payment_method_id.toString()).set(paymentMethod).await()
    }

    override suspend fun update(paymentMethod: PaymentMethod) {
        collection.document(paymentMethod.payment_method_id.toString()).set(paymentMethod).await()
    }

    override suspend fun delete(id: Int) {
        collection.document(id.toString()).delete().await()
    }
}
