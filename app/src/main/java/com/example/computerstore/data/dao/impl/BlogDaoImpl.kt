package com.example.computerstore.data.dao.impl

import com.example.computerstore.data.dao.BlogDao
import com.example.computerstore.data.model.Blog
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class BlogDaoImpl : BlogDao {
    private val db = FirebaseFirestore.getInstance()
    private val collection = db.collection("blogs")

    override suspend fun getAll(): List<Blog> {
        val snapshot = collection.get().await()
        return snapshot.documents.mapNotNull { it.toObject(Blog::class.java) }
    }

    override suspend fun getById(id: Int): Blog? {
        val doc = collection.document(id.toString()).get().await()
        return doc.toObject(Blog::class.java)
    }

    override suspend fun insert(blog: Blog) {
        collection.document(blog.blog_id.toString()).set(blog).await()
    }

    override suspend fun update(blog: Blog) {
        collection.document(blog.blog_id.toString()).set(blog).await()
    }

    override suspend fun delete(id: Int) {
        collection.document(id.toString()).delete().await()
    }
}
