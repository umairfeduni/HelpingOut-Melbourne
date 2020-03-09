package com.umair.helpingout.data.network

import androidx.lifecycle.LiveData
import com.umair.helpingout.data.db.entity.PlaceEntity
import com.umair.helpingout.data.network.response.PlaceListByTagsResponse
import com.umair.helpingout.data.network.response.PlacesListResponse


interface PlaceDataSource{
    //val downloadPlaceListByCategory : LiveData<PlacesListResponse>
    val downloadPlaceListByCategory : LiveData<PlacesListResponse>
    val downloadPlaceDetail : LiveData<PlaceEntity>
    val downloadAllPlaces : LiveData<List<PlaceEntity>>
    val downloadPlaceListByTags: LiveData<PlaceListByTagsResponse>

    suspend fun fetchPlaceListByCategory(categoryId : String)

    suspend fun fetchPlaceDetail(placeId : String)

    suspend fun fetchPlaceListByTags(tags : List<String>)

    suspend fun fetchAllPlaces()

}