package com.example.computerstore.data.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.PropertyName

data class User(
    @get:PropertyName("user_id") @set:PropertyName("user_id") var user_id: String? = null,
    @get:PropertyName("username") @set:PropertyName("username") var username: String? = null,
    @get:PropertyName("email") @set:PropertyName("email") var email: String? = null,
    @get:PropertyName("password") @set:PropertyName("password") var password: String? = null,
    @get:PropertyName("full_name") @set:PropertyName("full_name") var full_name: String? = null,
    @get:PropertyName("phone_number") @set:PropertyName("phone_number") var phone_number: String? = null,
    @get:PropertyName("registration_date") @set:PropertyName("registration_date") var registration_date: Timestamp? = null,
    @get:PropertyName("last_login") @set:PropertyName("last_login") var last_login: Timestamp? = null,
    @get:PropertyName("is_admin") @set:PropertyName("is_admin") var is_admin: Int = 0,
    @get:PropertyName("email_verified") @set:PropertyName("email_verified") var email_verified: Int = 0,
    @get:PropertyName("gender") @set:PropertyName("gender") var gender: String? = null,
    @get:PropertyName("birthday") @set:PropertyName("birthday") var birthday: Timestamp? = null
)
