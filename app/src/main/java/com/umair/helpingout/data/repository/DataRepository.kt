package com.umair.helpingout.data.repository

import androidx.lifecycle.LiveData
import com.umair.helpingout.data.db.entity.*

interface DataRepository {

    suspend fun getTagList() : LiveData<List<TagEntity>>

    suspend fun getPlaceListByCategory(categoryId : String) : LiveData<CategoryWithPlaces>

    suspend fun getPlaceDetail(placeId : String) : LiveData<PlaceEntity>

    suspend fun getCategoryList() : LiveData<List<CategoryEntity>>

    suspend fun getNumbers() : LiveData<List<NumberEntity>>

    suspend fun updateTagSelection(tagEntity: TagEntity)

    suspend fun searchPlaceListByTags(filters : List<String>)

    fun getPlaceListByTags() : LiveData<List<PlaceTagEntity>>


}