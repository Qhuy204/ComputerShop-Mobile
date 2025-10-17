package com.example.computerstore.data.dao.impl

import android.util.Log
import com.example.computerstore.data.dao.OrderDao
import com.example.computerstore.data.model.Blog
import com.example.computerstore.data.model.Order
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class OrderDaoImpl : OrderDao {
    private val db = FirebaseFirestore.getInstance()
    private val collection = db.collection("orders")

    override suspend fun getAll(): List<Order> {
        val result = mutableListOf<Order>()
        val snapshot = collection.get().await()

        for (doc in snapshot.documents) {
            try {
                val order = doc.toObject(Order::class.java)
                if (order != null) result.add(order)
            } catch (e: Exception) {
                Log.w("OrderDaoImpl", "⚠️ Skip invalid order ${doc.id}: ${e.message}")
            }
        }

        return result
    }

    // load đơn hàng theo UID (String)
    suspend fun getOrdersByUser(uid: String): List<Order> {
        val result = mutableListOf<Order>()
        val snapshot = collection.whereEqualTo("user_id", uid).get().await()

        for (doc in snapshot.documents) {
            try {
                val order = doc.toObject(Order::class.java)
                if (order != null) result.add(order)
            } catch (e: Exception) {
                Log.w("OrderDaoImpl", "⚠️ Skip invalid user order ${doc.id}: ${e.message}")
            }
        }

        return result
    }

    override suspend fun getById(id: String): Order? {
        val snapshot = collection.whereEqualTo("order_id", id).get().await()

        if (snapshot.isEmpty) {
            Log.w("OrderDaoImpl", "⚠️ No order found with order_id=$id")
            return null
        }

        val doc = snapshot.documents.first()
        val order = doc.toObject(Order::class.java)

        Log.d("OrderDaoImpl", "✅ Loaded order document ${doc.id} for order_id=$id")
        Log.d("OrderDaoImpl", "📄 Documents fetched: ${snapshot.size()}")

        // Gắn lại order_id nếu model chưa có
        return order?.apply {
            if (this.order_id == null) this.order_id = id
        }
    }



    override suspend fun insert(order: Order): String {
        val docId = collection.document().id
        val newOrder = order.copy(order_id = docId)
        collection.document(docId).set(newOrder).await()
        return docId
    }

    override suspend fun update(order: Order) {
        val id = (order.order_id as? String) ?: order.order_id?.toString()
        if (!id.isNullOrEmpty()) {
            collection.document(id).set(order).await()
        } else {
            Log.w("OrderDaoImpl", "⚠️ Tried to update order without valid ID")
        }
    }


    override suspend fun delete(id: String) {
        collection.document(id).delete().await()
    }
}
