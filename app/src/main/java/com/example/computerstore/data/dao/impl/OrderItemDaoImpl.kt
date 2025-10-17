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
        // Ép sang String an toàn
        val idStr = (orderItem.order_item_id as? String)?.takeIf { it.isNotBlank() }

        val docRef = if (!idStr.isNullOrEmpty()) {
            collection.document(idStr)
        } else {
            collection.document()
        }

        val newOrderItem = orderItem.copy(order_item_id = docRef.id)
        docRef.set(newOrderItem).await()
    }

    override suspend fun update(orderItem: OrderItem) {
        val idStr = (orderItem.order_item_id as? String)?.takeIf { it.isNotBlank() } ?: return
        collection.document(idStr).set(orderItem).await()
    }

    override suspend fun delete(id: String) {
        collection.document(id).delete().await()
    }
}
