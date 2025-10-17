package com.example.computerstore.data.dao.impl

import android.util.Log
import com.example.computerstore.data.dao.UserAddressDao
import com.example.computerstore.data.model.UserAddress
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class UserAddressDaoImpl : UserAddressDao {
    private val db = FirebaseFirestore.getInstance()
    private val collection = db.collection("user_addresses")

    override suspend fun getAll(): List<UserAddress> {
        val snapshot = collection.get().await()
        return snapshot.documents.mapNotNull { doc ->
            try {
                val address = doc.toObject(UserAddress::class.java)
                if (address != null) {
                    address.address_id = doc.id
                }
                address
            } catch (e: Exception) {
                Log.w("UserAddressDaoImpl", "Skip invalid address ${doc.id}: ${e.message}")
                null
            }
        }
    }

    override suspend fun getById(id: String): UserAddress? {
        return try {
            val doc = collection.document(id).get().await()
            val address = doc.toObject(UserAddress::class.java)
            if (address != null) {
                address.address_id = doc.id
            }
            address
        } catch (e: Exception) {
            Log.w("UserAddressDaoImpl", "Error parsing address $id: ${e.message}")
            null
        }
    }

    override suspend fun insert(userAddress: UserAddress) {
        val id = (userAddress.address_id ?: "").ifEmpty { collection.document().id }
        userAddress.address_id = id
        collection.document(id).set(userAddress).await()
    }

    override suspend fun update(userAddress: UserAddress) {
        val id = userAddress.address_id ?: return
        collection.document(id).set(userAddress).await()
    }

    override suspend fun delete(id: String?) {
        if (id != null) collection.document(id).delete().await()
    }
}
