package com.example.computerstore.data.dao.impl

import com.example.computerstore.data.dao.ProductDao
import com.example.computerstore.data.model.Product
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class ProductDaoImpl : ProductDao {
    private val db = FirebaseFirestore.getInstance()
    private val collection = db.collection("products")

    override suspend fun getAll(): List<Product> {
        val snapshot = collection.get().await()
        return snapshot.documents.mapNotNull { it.toObject(Product::class.java) }
    }

    override suspend fun getById(id: Int): Product? {
        val doc = collection.document(id.toString()).get().await()
        return doc.toObject(Product::class.java)
    }

    override suspend fun insert(product: Product) {
        collection.document(product.product_id.toString()).set(product).await()
    }

    override suspend fun update(product: Product) {
        collection.document(product.product_id.toString()).set(product).await()
    }

    override suspend fun delete(id: Int) {
        collection.document(id.toString()).delete().await()
    }
}
