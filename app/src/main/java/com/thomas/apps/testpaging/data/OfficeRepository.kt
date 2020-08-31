package com.thomas.apps.testpaging.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.thomas.apps.testpaging.api.OfficeService
import com.thomas.apps.testpaging.db.AppDatabase
import com.thomas.apps.testpaging.db.OfficeEntity
import kotlinx.coroutines.flow.Flow

class OfficeRepository(
    private val service: OfficeService,
    private val database: AppDatabase
) {
    fun getOffices(): Flow<PagingData<OfficeEntity>> {
        val pagingSourceFactory = { database.officeDao().getAllOffices() }

        return Pager(
            config = PagingConfig(pageSize = NETWORK_PAGE_SIZE, enablePlaceholders = false),
            remoteMediator = OfficeRemoteMediator(
                service,
                database
            ),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }

    companion object {
        private const val NETWORK_PAGE_SIZE = 2
    }
}