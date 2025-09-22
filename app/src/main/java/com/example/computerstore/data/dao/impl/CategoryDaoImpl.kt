package com.example.computerstore.data.dao.impl

import com.example.computerstore.data.dao.CategoryDao
import com.example.computerstore.data.model.Category
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class CategoryDaoImpl : CategoryDao {
    private val db = FirebaseFirestore.getInstance()
    private val collection = db.collection("categories")

    override suspend fun getAll(): List<Category> {
        val snapshot = collection.get().await()
        return snapshot.documents.mapNotNull { it.toObject(Category::class.java) }
    }

    override suspend fun getById(id: Int): Category? {
        val doc = collection.document(id.toString()).get().await()
        return doc.toObject(Category::class.java)
    }

    override suspend fun insert(category: Category) {
        collection.document(category.category_id.toString()).set(category).await()
    }

    override suspend fun update(category: Category) {
        collection.document(category.category_id.toString()).set(category).await()
    }

    override suspend fun delete(id: Int) {
        collection.document(id.toString()).delete().await()
    }
}
