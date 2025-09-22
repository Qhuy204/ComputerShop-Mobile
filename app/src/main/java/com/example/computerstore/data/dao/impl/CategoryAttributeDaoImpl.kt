package com.example.computerstore.data.dao.impl

import com.example.computerstore.data.dao.CategoryAttributeDao
import com.example.computerstore.data.model.CategoryAttribute
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class CategoryAttributeDaoImpl : CategoryAttributeDao {
    private val db = FirebaseFirestore.getInstance()
    private val collection = db.collection("categoryAttributes")

    override suspend fun getAll(): List<CategoryAttribute> {
        val snapshot = collection.get().await()
        return snapshot.documents.mapNotNull { it.toObject(CategoryAttribute::class.java) }
    }

    override suspend fun getById(categoryId: Int, attributeTypeId: Int): CategoryAttribute? {
        val doc = collection.document("${categoryId}_${attributeTypeId}").get().await()
        return doc.toObject(CategoryAttribute::class.java)
    }

    override suspend fun insert(categoryAttribute: CategoryAttribute) {
        collection.document("${categoryAttribute.category_id}_${categoryAttribute.attribute_type_id}").set(categoryAttribute).await()
    }

    override suspend fun update(categoryAttribute: CategoryAttribute) {
        collection.document("${categoryAttribute.category_id}_${categoryAttribute.attribute_type_id}").set(categoryAttribute).await()
    }

    override suspend fun delete(categoryId: Int, attributeTypeId: Int) {
        collection.document("${categoryId}_${attributeTypeId}").delete().await()
    }
}
