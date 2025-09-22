package com.example.computerstore.data.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.PropertyName

data class OrderStatusHistory(
    @get:PropertyName("id") @set:PropertyName("id") var id: Int = 0,
    @get:PropertyName("order_id") @set:PropertyName("order_id") var order_id: Int = 0,
    @get:PropertyName("old_status") @set:PropertyName("old_status") var old_status: String? = null,
    @get:PropertyName("new_status") @set:PropertyName("new_status") var new_status: String? = null,
    @get:PropertyName("changed_by") @set:PropertyName("changed_by") var changed_by: Int? = null,
    @get:PropertyName("changed_at") @set:PropertyName("changed_at") var changed_at: Timestamp? = null
)
