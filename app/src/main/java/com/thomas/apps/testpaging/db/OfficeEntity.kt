package com.thomas.apps.testpaging.db

import androidx.room.Entity
import com.thomas.apps.testpaging.model.Office

@Entity(tableName = "office")
data class OfficeEntity(
    val id: Long,
    val address: String,
    val city: String,
    val country: String,
    val dateCreate: String,
    val description: String,
    val imageUrl: String,
    val latitude: Double,
    val longitude: Double,
    val name: String
) {
    fun toOffice() = Office(
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