package com.example.computerstore.data.dao.impl

import com.example.computerstore.data.dao.CartDao
import com.example.computerstore.data.model.Cart
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class CartDaoImpl : CartDao {
    private val db = FirebaseFirestore.getInstance()
    private val collection = db.collection("carts")

    override suspend fun getAll(): List<Cart> {
        val snapshot = collection.get().await()
        return snapshot.documents.mapNotNull { it.toObject(Cart::class.java) }
    }

    override suspend fun getById(id: Int): Cart? {
        val doc = collection.document(id.toString()).get().await()
        return doc.toObject(Cart::class.java)
    }

    override suspend fun insert(cart: Cart) {
        collection.document(cart.cart_id.toString()).set(cart).await()
    }

    override suspend fun update(cart: Cart) {
        collection.document(cart.cart_id.toString()).set(cart).await()
    }

    override suspend fun delete(id: Int) {
        collection.document(id.toString()).delete().await()
    }
}
