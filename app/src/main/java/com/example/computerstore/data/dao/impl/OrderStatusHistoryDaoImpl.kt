package com.example.computerstore.data.dao.impl

import com.example.computerstore.data.dao.OrderStatusHistoryDao
import com.example.computerstore.data.model.OrderStatusHistory
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class OrderStatusHistoryDaoImpl : OrderStatusHistoryDao {
    private val db = FirebaseFirestore.getInstance()
    private val collection = db.collection("orderStatusHistory")

    override suspend fun getAll(): List<OrderStatusHistory> {
        val snapshot = collection.get().await()
        return snapshot.documents.mapNotNull { it.toObject(OrderStatusHistory::class.java) }
    }

    override suspend fun getById(id: Int): OrderStatusHistory? {
        val doc = collection.document(id.toString()).get().await()
        return doc.toObject(OrderStatusHistory::class.java)
    }

    override suspend fun insert(orderStatusHistory: OrderStatusHistory) {
        collection.document(orderStatusHistory.id.toString()).set(orderStatusHistory).await()
    }

    override suspend fun update(orderStatusHistory: OrderStatusHistory) {
        collection.document(orderStatusHistory.id.toString()).set(orderStatusHistory).await()
    }

    override suspend fun delete(id: Int) {
        collection.document(id.toString()).delete().await()
    }
}
