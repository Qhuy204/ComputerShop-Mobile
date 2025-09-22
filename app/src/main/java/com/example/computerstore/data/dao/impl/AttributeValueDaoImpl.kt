package com.example.computerstore.data.dao.impl

import com.example.computerstore.data.dao.AttributeValueDao
import com.example.computerstore.data.model.AttributeValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class AttributeValueDaoImpl : AttributeValueDao {
    private val db = FirebaseFirestore.getInstance()
    private val collection = db.collection("attributeValues")

    override suspend fun getAll(): List<AttributeValue> {
        val snapshot = collection.get().await()
        return snapshot.documents.mapNotNull { it.toObject(AttributeValue::class.java) }
    }

    override suspend fun getById(id: Int): AttributeValue? {
        val doc = collection.document(id.toString()).get().await()
        return doc.toObject(AttributeValue::class.java)
    }

    override suspend fun insert(attributeValue: AttributeValue) {
        collection.document(attributeValue.attribute_value_id.toString()).set(attributeValue).await()
    }

    override suspend fun update(attributeValue: AttributeValue) {
        collection.document(attributeValue.attribute_value_id.toString()).set(attributeValue).await()
    }

    override suspend fun delete(id: Int) {
        collection.document(id.toString()).delete().await()
    }
}
