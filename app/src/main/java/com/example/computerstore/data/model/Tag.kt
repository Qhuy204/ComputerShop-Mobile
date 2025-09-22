package com.example.computerstore.data.model

import com.google.firebase.firestore.PropertyName

data class Tag(
    @get:PropertyName("tag_name") @set:PropertyName("tag_name") var tag_name: String = ""
)
