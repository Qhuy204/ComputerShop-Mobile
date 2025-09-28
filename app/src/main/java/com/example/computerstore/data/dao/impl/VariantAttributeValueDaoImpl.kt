package com.example.computerstore.data.dao.impl

import com.example.computerstore.data.dao.VariantAttributeValueDao
import com.example.computerstore.data.model.VariantAttributeValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class VariantAttributeValueDaoImpl : VariantAttributeValueDao {
    private val db = FirebaseFirestore.getInstance()
    private val collection = db.collection("variant_attribute_values")

    override suspend fun getAll(): List<VariantAttributeValue> {
        val snapshot = collection.get().await()
        return snapshot.documents.mapNotNull { it.toObject(VariantAttributeValue::class.java) }
    }

    override suspend fun getById(variantId: Int, attributeValueId: Int): VariantAttributeValue? {
        val doc = collection.document("${variantId}_${attributeValueId}").get().await()
        return doc.toObject(VariantAttributeValue::class.java)
    }

    override suspend fun insert(variantAttributeValue: VariantAttributeValue) {
        collection.document("${variantAttributeValue.variant_id}_${variantAttributeValue.attribute_value_id}").set(variantAttributeValue).await()
    }

    override suspend fun update(variantAttributeValue: VariantAttributeValue) {
        collection.document("${variantAttributeValue.variant_id}_${variantAttributeValue.attribute_value_id}").set(variantAttributeValue).await()
    }

    override suspend fun delete(variantId: Int, attributeValueId: Int) {
        collection.document("${variantId}_${attributeValueId}").delete().await()
    }
}
