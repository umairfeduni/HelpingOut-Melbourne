package com.umair.helpingout.ui.services

import androidx.lifecycle.ViewModel
import com.umair.helpingout.data.repository.DataRepository
import com.umair.helpingout.internal.lazyDeferred

class ServiceListViewModel(private val categoryId: String,private val dataRepository: DataRepository) : ViewModel() {



    val helpOutData by lazyDeferred {
        dataRepository.getPlaceListByCategory(categoryId)
    }
}
