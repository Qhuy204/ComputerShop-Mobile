package com.example.computerstore.data.dao.impl

import com.example.computerstore.data.dao.BrandDao
import com.example.computerstore.data.model.Brand
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class BrandDaoImpl : BrandDao {
    private val db = FirebaseFirestore.getInstance()
    private val collection = db.collection("brands")

    override suspend fun getAll(): List<Brand> {
        val snapshot = collection.get().await()
        return snapshot.documents.mapNotNull { it.toObject(Brand::class.java) }
    }

    override suspend fun getById(id: Int): Brand? {
        val doc = collection.document(id.toString()).get().await()
        return doc.toObject(Brand::class.java)
    }

    override suspend fun insert(brand: Brand) {
        collection.document(brand.brand_id.toString()).set(brand).await()
    }

    override suspend fun update(brand: Brand) {
        collection.document(brand.brand_id.toString()).set(brand).await()
    }

    override suspend fun delete(id: Int) {
        collection.document(id.toString()).delete().await()
    }
}
