package com.example.computerstore.data.dao.impl

import com.example.computerstore.data.dao.TagDao
import com.example.computerstore.data.model.Tag
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class TagDaoImpl : TagDao {
    private val db = FirebaseFirestore.getInstance()
    private val collection = db.collection("tags")

    override suspend fun getAll(): List<Tag> {
        val snapshot = collection.get().await()
        return snapshot.documents.mapNotNull { it.toObject(Tag::class.java) }
    }

    override suspend fun getById(id: String): Tag? {
        val doc = collection.document(id).get().await()
        return doc.toObject(Tag::class.java)
    }

    override suspend fun insert(tag: Tag) {
        collection.document(tag.tag_name).set(tag).await()
    }

    override suspend fun update(tag: Tag) {
        collection.document(tag.tag_name).set(tag).await()
    }

    override suspend fun delete(id: String) {
        collection.document(id).delete().await()
    }
}
