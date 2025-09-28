package com.example.computerstore.data.dao.impl

import com.example.computerstore.data.dao.ProductVariantDao
import com.example.computerstore.data.model.ProductVariant
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class ProductVariantDaoImpl : ProductVariantDao {
    private val db = FirebaseFirestore.getInstance()
    private val collection = db.collection("product_variants")

    override suspend fun getAll(): List<ProductVariant> {
        val snapshot = collection.get().await()
        return snapshot.documents.mapNotNull { it.toObject(ProductVariant::class.java) }
    }

    override suspend fun getById(id: Int): ProductVariant? {
        val doc = collection.document(id.toString()).get().await()
        return doc.toObject(ProductVariant::class.java)
    }

    override suspend fun insert(productVariant: ProductVariant) {
        collection.document(productVariant.variant_id.toString()).set(productVariant).await()
    }

    override suspend fun update(productVariant: ProductVariant) {
        collection.document(productVariant.variant_id.toString()).set(productVariant).await()
    }

    override suspend fun delete(id: Int) {
        collection.document(id.toString()).delete().await()
    }

    override suspend fun getByProductId(productId: Int): List<ProductVariant> {
        val snapshot = collection.whereEqualTo("product_id", productId).get().await()
        return snapshot.documents.mapNotNull { it.toObject(ProductVariant::class.java) }
    }

}
