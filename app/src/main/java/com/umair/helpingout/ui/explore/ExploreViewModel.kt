package com.umair.helpingout.ui.explore

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.umair.helpingout.data.db.entity.PlaceTagEntity
import com.umair.helpingout.data.db.entity.TagEntity
import com.umair.helpingout.data.repository.DataRepository
import com.umair.helpingout.internal.lazyDeferred
import com.umair.helpingout.ui.services.PlaceItem

class ExploreViewModel ( private val dataRepository: DataRepository) : ViewModel() {

   /* val filteredData by lazyDeferred {
        dataRepository.getPlaceListByTags(filters)
    }*/



    fun placesByTag() : LiveData<List<PlaceTagEntity>> {
        return dataRepository.getPlaceListByTags()
    }


    suspend fun searchPlaceByTag(filters: List<String>){
        dataRepository.searchPlaceListByTags(filters)
    }

    suspend fun updateTag(tagEntity: TagEntity){
        dataRepository.updateTagSelection(tagEntity)
    }


    val tagList by lazyDeferred {
         dataRepository.getTagList()
    }
}
