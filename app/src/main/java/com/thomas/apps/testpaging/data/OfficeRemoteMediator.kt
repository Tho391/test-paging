package com.thomas.apps.testpaging.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.thomas.apps.testpaging.api.OfficeService
import com.thomas.apps.testpaging.db.AppDatabase
import com.thomas.apps.testpaging.db.OfficeEntity
import com.thomas.apps.testpaging.db.RemoteKeys
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class OfficeRemoteMediator(
    private val service: OfficeService,
    private val database: AppDatabase
) : RemoteMediator<Int, OfficeEntity>() {
    companion object {
        private const val STARTING_PAGE_INDEX = 1
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, OfficeEntity>
    ): MediatorResult {

        val page = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: STARTING_PAGE_INDEX

            }
            LoadType.PREPEND -> {
//                val remoteKeys = getRemoteKeyForFirstItem(state)
//                if (remoteKeys == null) {
//                    // The LoadType is PREPEND so some data was loaded before,
//                    // so we should have been able to get remote keys
//                    // If the remoteKeys are null, then we're an invalid state and we have a bug
//                    throw InvalidObjectException("Remote key and the prevKey should not be null")
//                }
//                // If the previous key is null, then we can't request more data
//                val prevKey = remoteKeys.prevKey
//                if (prevKey == null) {
//                    return MediatorResult.Success(endOfPaginationReached = true)
//                }
//                remoteKeys.prevKey
                return MediatorResult.Success(endOfPaginationReached = true)
            }
            LoadType.APPEND -> {
//                val remoteKeys = getRemoteKeyForLastItem(state)
//                if (remoteKeys == null || remoteKeys.nextKey == null) {
//                    throw InvalidObjectException("Remote key should not be null for $loadType")
//                }
//                remoteKeys.nextKey

                val remoteKeys = getRemoteKeyForLastItem(state)
                remoteKeys?.nextKey ?: return MediatorResult.Success(endOfPaginationReached = true)

                remoteKeys.nextKey
            }

        }

        try {
            val officeResponse = service.requestOffice(page, state.config.pageSize)

            val endOfPaginationReached = officeResponse.isEmpty()
            database.withTransaction {
                // clear all tables in the database
                if (loadType == LoadType.REFRESH) {
                    database.remoteKeysDao().clearRemoteKeys()
                    database.officeDao().deleteAll()
                }
                val prevKey = if (page == STARTING_PAGE_INDEX) null else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1
                val keys = officeResponse.map {
                    RemoteKeys(officeId = it.id, prevKey = prevKey, nextKey = nextKey)
                }
                database.remoteKeysDao().insertAll(keys)
                database.officeDao().insertAll(officeResponse.map { it.toOfficeEntity() })
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, OfficeEntity>): RemoteKeys? {
        // Get the last page that was retrieved, that contained items.
        // From that last page, get the last item
        return state.pages.lastOrNull() { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { office ->
                // Get the remote keys of the last item retrieved
                database.remoteKeysDao().remoteKeysOfficeId(office.id)
            }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, OfficeEntity>): RemoteKeys? {
        // Get the first page that was retrieved, that contained items.
        // From that first page, get the first item
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { office ->
                // Get the remote keys of the first items retrieved
                database.remoteKeysDao().remoteKeysOfficeId(office.id)
            }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, OfficeEntity>
    ): RemoteKeys? {
        // The paging library is trying to load data after the anchor position
        // Get the item closest to the anchor position
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                database.remoteKeysDao().remoteKeysOfficeId(id)
            }
        }
    }

}