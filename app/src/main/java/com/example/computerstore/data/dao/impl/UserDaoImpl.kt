package com.example.computerstore.data.dao.impl

import android.util.Log
import com.example.computerstore.data.dao.UserDao
import com.example.computerstore.data.model.User
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class UserDaoImpl : UserDao {

    private val db = FirebaseFirestore.getInstance()
    private val collection = db.collection("users")

    override suspend fun getAll(): List<User> {
        val snapshot = collection.get().await()
        return snapshot.documents.mapNotNull { doc ->
            try {
                val data = doc.data ?: return@mapNotNull null
                val userId = when (val v = data["user_id"]) {
                    is String -> v
                    is Long -> v.toString()
                    else -> null
                }
                val user = doc.toObject(User::class.java)
                if (user != null) user.user_id = userId ?: ""
                user
            } catch (e: Exception) {
                Log.w("UserDaoImpl", "⚠️ Skip invalid user ${doc.id}: ${e.message}")
                null
            }
        }
    }

    override suspend fun getById(id: String): User? {
        val doc = collection.document(id).get().await()
        val data = doc.data ?: return null
        return try {
            val user = doc.toObject(User::class.java)
            val userId = when (val v = data["user_id"]) {
                is String -> v
                is Long -> v.toString()
                else -> null
            }
            if (user != null) user.user_id = userId ?: ""
            user
        } catch (e: Exception) {
            Log.w("UserDaoImpl", "⚠️ Error parsing user $id: ${e.message}")
            null
        }
    }

    override suspend fun insert(user: User) {
        // 🆕 Nếu user_id trống → tạo mới (dành cho người đăng ký)
        val id = (user.user_id ?: "").ifEmpty { collection.document().id }
        user.user_id = id
        collection.document(id).set(user).await()
        Log.i("UserDaoImpl", "Inserted new user with ID: $id")
    }

    override suspend fun update(user: User) {
        // 🔄 Nếu user đã login → chỉ cập nhật
        val id = (user.user_id ?: "").ifEmpty {
            Log.w("UserDaoImpl", "Missing user_id, auto-generating document id for safety")
            collection.document().id
        }
        user.user_id = id
        collection.document(id).set(user).await()
        Log.i("UserDaoImpl", "Updated user: $id")
    }

    override suspend fun delete(id: String) {
        if (id.isNotEmpty()) {
            collection.document(id).delete().await()
            Log.i("UserDaoImpl", "🗑Deleted user: $id")
        }
    }
}
