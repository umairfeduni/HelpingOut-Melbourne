package com.umair.helpingout.ui.categories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.umair.helpingout.data.repository.DataRepository

class CategoryListViewModelFactory(private val dataRepository: DataRepository):ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return CategoryListViewModel(dataRepository) as T
    }

}