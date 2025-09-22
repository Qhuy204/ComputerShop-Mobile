package com.example.computerstore.data.dao.impl

import com.example.computerstore.data.dao.ProductImageDao
import com.example.computerstore.data.model.ProductImage
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class ProductImageDaoImpl : ProductImageDao {
    private val db = FirebaseFirestore.getInstance()
    private val collection = db.collection("productImages")

    override suspend fun getAll(): List<ProductImage> {
        val snapshot = collection.get().await()
        return snapshot.documents.mapNotNull { it.toObject(ProductImage::class.java) }
    }

    override suspend fun getById(id: Int): ProductImage? {
        val doc = collection.document(id.toString()).get().await()
        return doc.toObject(ProductImage::class.java)
    }

    override suspend fun insert(productImage: ProductImage) {
        collection.document(productImage.image_id.toString()).set(productImage).await()
    }

    override suspend fun update(productImage: ProductImage) {
        collection.document(productImage.image_id.toString()).set(productImage).await()
    }

    override suspend fun delete(id: Int) {
        collection.document(id.toString()).delete().await()
    }
}
