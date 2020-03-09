package com.umair.helpingout.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.umair.helpingout.data.repository.DataRepository

class ServiceDetailViewModelFactory(private val placeId: String, private val dataRepository: DataRepository):ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ServiceDetailViewModel(placeId,dataRepository) as T
    }

}