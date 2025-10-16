package com.example.computerstore.data.dao.impl

import com.example.computerstore.data.dao.OrderItemDao
import com.example.computerstore.data.model.OrderItem
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class OrderItemDaoImpl : OrderItemDao {
    private val db = FirebaseFirestore.getInstance()
    private val collection = db.collection("order_items")

    override suspend fun getAll(): List<OrderItem> {
        val snapshot = collection.get().await()
        return snapshot.documents.mapNotNull { it.toObject(OrderItem::class.java) }
    }

    override suspend fun getById(id: String): OrderItem? {
        val doc = collection.document(id).get().await()
        return doc.toObject(OrderItem::class.java)
    }

    override suspend fun insert(orderItem: OrderItem) {
        val docRef = if (!orderItem.order_item_id.isNullOrEmpty()) {
            collection.document(orderItem.order_item_id!!)
        } else {
            collection.document()
        }

        val newOrderItem = orderItem.copy(order_item_id = docRef.id)
        docRef.set(newOrderItem).await()
    }

    override suspend fun update(orderItem: OrderItem) {
        if (orderItem.order_item_id.isNullOrEmpty()) return
        collection.document(orderItem.order_item_id!!).set(orderItem).await()
    }

    override suspend fun delete(id: String) {
        collection.document(id).delete().await()
    }
}
