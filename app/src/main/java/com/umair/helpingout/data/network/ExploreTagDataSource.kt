package com.umair.helpingout.data.network

import androidx.lifecycle.LiveData
import androidx.paging.PageKeyedDataSource
import com.umair.helpingout.data.db.entity.PlaceTagEntity

import com.umair.helpingout.data.network.response.PlaceListByTagsResponse

interface ExploreTagDataSource{

    val downloadPlaceListByTags: LiveData<PlaceListByTagsResponse>


    suspend fun fetchPlaceListByTags(page : Int, count : Int,callback: PageKeyedDataSource.LoadCallback<Int, PlaceTagEntity>)


}