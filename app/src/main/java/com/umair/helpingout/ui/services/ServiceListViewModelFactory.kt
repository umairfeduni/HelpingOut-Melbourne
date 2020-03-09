package com.umair.helpingout.ui.services

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.umair.helpingout.data.repository.DataRepository

class ServiceListViewModelFactory(private val category: String, private val dataRepository: DataRepository):ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ServiceListViewModel(category,dataRepository) as T
    }

}