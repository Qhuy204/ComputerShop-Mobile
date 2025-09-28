package com.example.computerstore.data.dao.impl

import com.example.computerstore.data.dao.UserAddressDao
import com.example.computerstore.data.model.UserAddress
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class UserAddressDaoImpl : UserAddressDao {
    private val db = FirebaseFirestore.getInstance()
    private val collection = db.collection("user_addresses")

    override suspend fun getAll(): List<UserAddress> {
        val snapshot = collection.get().await()
        return snapshot.documents.mapNotNull { it.toObject(UserAddress::class.java) }
    }

    override suspend fun getById(id: Int): UserAddress? {
        val doc = collection.document(id.toString()).get().await()
        return doc.toObject(UserAddress::class.java)
    }

    override suspend fun insert(userAddress: UserAddress) {
        collection.document(userAddress.address_id.toString()).set(userAddress).await()
    }

    override suspend fun update(userAddress: UserAddress) {
        collection.document(userAddress.address_id.toString()).set(userAddress).await()
    }

    override suspend fun delete(id: Int) {
        collection.document(id.toString()).delete().await()
    }
}
