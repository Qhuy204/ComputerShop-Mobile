package com.example.computerstore.data.model

import com.google.firebase.firestore.PropertyName

data class UserAddress(
    @get:PropertyName("address_id") @set:PropertyName("address_id") var address_id: Any? = "",
    @get:PropertyName("user_id") @set:PropertyName("user_id") var user_id: Any? = "",
    @get:PropertyName("recipient_name") @set:PropertyName("recipient_name") var recipient_name: String = "",
    @get:PropertyName("phone_number") @set:PropertyName("phone_number") var phone_number: String = "",
    @get:PropertyName("address") @set:PropertyName("address") var address: String = "",
    @get:PropertyName("city") @set:PropertyName("city") var city: String = "",
    @get:PropertyName("province") @set:PropertyName("province") var province: String = "",
    @get:PropertyName("district") @set:PropertyName("district") var district: String? = null,
    @get:PropertyName("country") @set:PropertyName("country") var country: String = "",
    @get:PropertyName("is_default") @set:PropertyName("is_default") var is_default: Int = 0,
    @get:PropertyName("address_type") @set:PropertyName("address_type") var address_type: String? = "Nhà riêng"
) {
    // Firestore bắt buộc cần constructor trống (no-arg)
    constructor() : this(
        address_id = "",
        user_id = "",
        recipient_name = "",
        phone_number = "",
        address = "",
        city = "",
        province = "",
        district = null,
        country = "",
        is_default = 0,
        address_type = "Nhà riêng"
    )
}
