package com.example.computerstore.data.dao.impl

import com.example.computerstore.data.dao.OrderDao
import com.example.computerstore.data.model.Order
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class OrderDaoImpl : OrderDao {
    private val db = FirebaseFirestore.getInstance()
    private val collection = db.collection("orders")

    override suspend fun getAll(): List<Order> {
        val snapshot = collection.get().await()
        return snapshot.documents.mapNotNull { it.toObject(Order::class.java) }
    }

    override suspend fun getById(id: String): Order? {
        val doc = collection.document(id.toString()).get().await()
        return doc.toObject(Order::class.java)
    }

    override suspend fun insert(order: Order): String {
        val docId = collection.document().id
        val newOrder = order.copy(order_id = docId)
        collection.document(docId).set(newOrder).await()
        return docId
    }


    override suspend fun update(order: Order) {
        collection.document(order.order_id.toString()).set(order).await()
    }

    override suspend fun delete(id: String) {
        collection.document(id.toString()).delete().await()
    }
}
