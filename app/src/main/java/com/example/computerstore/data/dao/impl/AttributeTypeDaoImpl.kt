package com.example.computerstore.data.dao.impl

import com.example.computerstore.data.dao.AttributeTypeDao
import com.example.computerstore.data.model.AttributeType
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class AttributeTypeDaoImpl : AttributeTypeDao {
    private val db = FirebaseFirestore.getInstance()
    private val collection = db.collection("attributeTypes")

    override suspend fun getAll(): List<AttributeType> {
        val snapshot = collection.get().await()
        return snapshot.documents.mapNotNull { it.toObject(AttributeType::class.java) }
    }

    override suspend fun getById(id: Int): AttributeType? {
        val doc = collection.document(id.toString()).get().await()
        return doc.toObject(AttributeType::class.java)
    }

    override suspend fun insert(attributeType: AttributeType) {
        collection.document(attributeType.attribute_type_id.toString()).set(attributeType).await()
    }

    override suspend fun update(attributeType: AttributeType) {
        collection.document(attributeType.attribute_type_id.toString()).set(attributeType).await()
    }

    override suspend fun delete(id: Int) {
        collection.document(id.toString()).delete().await()
    }
}
