package com.umair.helpingout.ui.numbers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.umair.helpingout.data.repository.DataRepository

class HelpfulNumberViewModelFactory(private val dataRepository: DataRepository):ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return HelpfulNumbersViewModel(dataRepository) as T
    }

}