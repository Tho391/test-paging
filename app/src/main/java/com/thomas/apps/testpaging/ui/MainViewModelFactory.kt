package com.thomas.apps.testpaging.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.thomas.apps.testpaging.data.OfficeRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class MainViewModelFactory(private val repository: OfficeRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}