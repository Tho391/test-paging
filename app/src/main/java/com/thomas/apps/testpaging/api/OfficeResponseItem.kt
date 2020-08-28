package com.thomas.apps.testpaging.api


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.thomas.apps.testpaging.db.OfficeEntity

@JsonClass(generateAdapter = true)
data class OfficeResponseItem(
    @Json(name = "id") val id: Long = 0,
    @Json(name = "address") val address: String = "",
    @Json(name = "city") val city: String = "",
    @Json(name = "country") val country: String = "",
    @Json(name = "dateCreate") val dateCreate: String = "",
    @Json(name = "description") val description: String = "",
    @Json(name = "imageUrl") val imageUrl: String = "",
    @Json(name = "latitude") val latitude: Double = 0.0,
    @Json(name = "longitude") val longitude: Double = 0.0,
    @Json(name = "name") val name: String = ""
) {
    fun toOfficeEntity() = OfficeEntity(
        id,
        address,
        city,
        country,
        dateCreate,
        description,
        imageUrl,
        latitude,
        longitude,
        name
    )
}