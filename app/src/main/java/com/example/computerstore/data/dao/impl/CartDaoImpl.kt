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

    override suspend fun getById(id: String): Cart? {
        val doc = collection.document(id).get().await()
        return doc.toObject(Cart::class.java)
    }

    override suspend fun insert(cart: Cart): String {
        val docId = cart.cart_id?.takeIf { it.isNotEmpty() } ?: collection.document().id
        val newCart = cart.copy(cart_id = docId)
        collection.document(docId).set(newCart).await()
        return docId
    }


    override suspend fun update(cart: Cart) {
        if (!cart.cart_id.isNullOrEmpty()) {
            collection.document(cart.cart_id!!).set(cart).await()
        }
    }

    override suspend fun delete(id: String) {
        collection.document(id).delete().await()
    }

    override suspend fun getCartsByUser(userId: String): List<Cart> {
        val snapshot = collection.whereEqualTo("user_id", userId).get().await()
        return snapshot.toObjects(Cart::class.java)
    }

    override suspend fun updateQuantity(cartId: String?, newQuantity: Int) {
        cartId?.let { id ->
            collection.document(id).update("quantity", newQuantity).await()
        }
    }

}

