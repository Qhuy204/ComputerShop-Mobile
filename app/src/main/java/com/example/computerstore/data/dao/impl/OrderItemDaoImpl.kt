package com.example.computerstore.data.dao.impl

import com.example.computerstore.data.dao.OrderItemDao
import com.example.computerstore.data.model.OrderItem
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class OrderItemDaoImpl : OrderItemDao {
    private val db = FirebaseFirestore.getInstance()
    private val collection = db.collection("orderItems")

    override suspend fun getAll(): List<OrderItem> {
        val snapshot = collection.get().await()
        return snapshot.documents.mapNotNull { it.toObject(OrderItem::class.java) }
    }

    override suspend fun getById(id: Int): OrderItem? {
        val doc = collection.document(id.toString()).get().await()
        return doc.toObject(OrderItem::class.java)
    }

    override suspend fun insert(orderItem: OrderItem) {
        collection.document(orderItem.order_item_id.toString()).set(orderItem).await()
    }

    override suspend fun update(orderItem: OrderItem) {
        collection.document(orderItem.order_item_id.toString()).set(orderItem).await()
    }

    override suspend fun delete(id: Int) {
        collection.document(id.toString()).delete().await()
    }
}
