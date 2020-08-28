package com.thomas.apps.testpaging.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.thomas.apps.testpaging.data.OfficeRepository
import com.thomas.apps.testpaging.model.Office
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@ExperimentalCoroutinesApi
class MainViewModel(private val repository: OfficeRepository) : ViewModel() {

    private var currentOffices: Flow<PagingData<Office>>? = null

    fun getOffices(): Flow<PagingData<Office>> {

        val newResult: Flow<PagingData<Office>> = repository.getOffices()
            .map { pagingData -> pagingData.map { it.toOffice() } }
            .cachedIn(viewModelScope)

        currentOffices = newResult
        return newResult
    }
}