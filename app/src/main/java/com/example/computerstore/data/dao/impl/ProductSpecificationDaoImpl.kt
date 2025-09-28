package com.example.computerstore.data.dao.impl

import com.example.computerstore.data.dao.ProductSpecificationDao
import com.example.computerstore.data.model.ProductSpecification
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class ProductSpecificationDaoImpl : ProductSpecificationDao {
    private val db = FirebaseFirestore.getInstance()
    private val collection = db.collection("product_specifications")

    override suspend fun getAll(): List<ProductSpecification> {
        val snapshot = collection.get().await()
        return snapshot.documents.mapNotNull { it.toObject(ProductSpecification::class.java) }
    }

    override suspend fun getById(id: Int): ProductSpecification? {
        val doc = collection.document(id.toString()).get().await()
        return doc.toObject(ProductSpecification::class.java)
    }

    override suspend fun insert(productSpecification: ProductSpecification) {
        collection.document(productSpecification.spec_id.toString()).set(productSpecification).await()
    }

    override suspend fun update(productSpecification: ProductSpecification) {
        collection.document(productSpecification.spec_id.toString()).set(productSpecification).await()
    }

    override suspend fun delete(id: Int) {
        collection.document(id.toString()).delete().await()
    }

    override suspend fun getByProductId(productId: Int): List<ProductSpecification> {
        val snapshot = collection.whereEqualTo("product_id", productId).get().await()
        return snapshot.documents.mapNotNull { it.toObject(ProductSpecification::class.java) }
    }

}
