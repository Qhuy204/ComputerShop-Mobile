package com.example.computerstore.data.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.PropertyName

data class Blog(
    @get:PropertyName("blog_id") @set:PropertyName("blog_id") var blog_id: Int = 0,
    @get:PropertyName("blog_title") @set:PropertyName("blog_title") var blog_title: String = "",
    @get:PropertyName("blog_slug") @set:PropertyName("blog_slug") var blog_slug: String = "",
    @get:PropertyName("blog_thumbnail_url") @set:PropertyName("blog_thumbnail_url") var blog_thumbnail_url: String? = null,
    @get:PropertyName("blog_description") @set:PropertyName("blog_description") var blog_description: String? = null,
    @get:PropertyName("blog_content") @set:PropertyName("blog_content") var blog_content: String? = null,
    @get:PropertyName("blog_author_name") @set:PropertyName("blog_author_name") var blog_author_name: String? = null,
    @get:PropertyName("category") @set:PropertyName("category") var category: String? = null,
    @get:PropertyName("tags") @set:PropertyName("tags") var tags: String? = null,
    @get:PropertyName("published_at") @set:PropertyName("published_at") var published_at: Timestamp? = null,
    @get:PropertyName("updated_at") @set:PropertyName("updated_at") var updated_at: Timestamp? = null,
    @get:PropertyName("views") @set:PropertyName("views") var views: Int = 0,
    @get:PropertyName("is_published") @set:PropertyName("is_published") var is_published: Int = 1
)
