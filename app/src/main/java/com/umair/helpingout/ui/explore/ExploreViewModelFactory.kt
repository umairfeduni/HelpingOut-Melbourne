package com.umair.helpingout.ui.explore

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.umair.helpingout.data.repository.DataRepository

class ExploreViewModelFactory(private val dataRepository: DataRepository):ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ExploreViewModel(dataRepository) as T
    }

}