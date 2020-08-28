package com.thomas.apps.testpaging.db

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface OfficeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(officeEntities: List<OfficeEntity>)

    @Query("SELECT * FROM office")
    fun getAllOffices(): PagingSource<Int, OfficeEntity>

    @Query("DELETE FROM office")
    suspend fun deleteAll()
}