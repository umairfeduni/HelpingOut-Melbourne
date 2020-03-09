package com.umair.helpingout.ui.detail

import androidx.lifecycle.ViewModel
import com.umair.helpingout.data.repository.DataRepository
import com.umair.helpingout.internal.lazyDeferred

class ServiceDetailViewModel(private val placeId: String,private val dataRepository: DataRepository) : ViewModel() {



    val placeData by lazyDeferred {
        dataRepository.getPlaceDetail(placeId)
    }
}
