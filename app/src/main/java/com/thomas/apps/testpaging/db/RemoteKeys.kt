package com.thomas.apps.testpaging.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_keys")
data class RemoteKeys(
    @PrimaryKey val officeId: Long,
    val prevKey: Int?,
    val nextKey: Int?
)