package com.example.computerstore.data.dao.impl

import com.example.computerstore.data.dao.UserDao
import com.example.computerstore.data.model.User
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class UserDaoImpl : UserDao {

    private val db = FirebaseFirestore.getInstance()
    private val collection = db.collection("users")

    override suspend fun getAll(): List<User> {
        val snapshot = collection.get().await()
        return snapshot.documents.mapNotNull { it.toObject(User::class.java) }
    }

    override suspend fun getById(id: Int): User? {
        val doc = collection.document(id.toString()).get().await()
        return doc.toObject(User::class.java)
    }

    override suspend fun insert(user: User) {
        collection.document(user.user_id.toString()).set(user).await()
    }

    override suspend fun update(user: User) {
        collection.document(user.user_id.toString()).set(user).await()
    }

    override suspend fun delete(id: Int) {
        collection.document(id.toString()).delete().await()
    }
}