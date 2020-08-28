package com.thomas.apps.testpaging


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class OfficeResponseItem(
    @Json(name = "id") val id: Int? = 0,
    @Json(name = "email") val email: String? = "",
    @Json(name = "name") val name: String? = "",
    @Json(name = "phone") val phone: String? = "",
    @Json(name = "office_id") val officeId: String? = "",
    @Json(name = "photoUrl") val photoUrl: String? = ""
)