package com.umair.helpingout.data.network

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.umair.helpingout.data.db.entity.PlaceEntity
import com.umair.helpingout.data.network.response.PlaceListByTagsResponse
import com.umair.helpingout.internal.NoConnectivityException
import com.umair.helpingout.data.network.response.PlacesListResponse
import com.umair.helpingout.internal.ServerDownException
import retrofit2.HttpException
import java.net.SocketTimeoutException

class PlaceDataSourceImpl(private val helpOutDataApiService : HelpOutDataApiService
) : PlaceDataSource {

    private val _downloadPlaceListByCategory = MutableLiveData<PlacesListResponse>()
    override val downloadPlaceListByCategory: LiveData<PlacesListResponse>
        get() = _downloadPlaceListByCategory

    override suspend fun fetchPlaceListByCategory(categoryId : String) {
        try{
            val fetchedLatestData = helpOutDataApiService
                .getPlaceList(categoryId)
                .await()

            _downloadPlaceListByCategory.postValue(fetchedLatestData)
        }catch (e : NoConnectivityException){
            Log.d("Connectivity","No internet connection",e)
        }catch (e : ServerDownException){
            Log.d("Connectivity","Server Down ",e)
        }catch (e : SocketTimeoutException){
            Log.d("Connectivity","SocketTimeoutException",e)
        }catch (e : HttpException){

        }
    }



    private val _downloadPlaceDetail = MutableLiveData<PlaceEntity>()
    override val downloadPlaceDetail : LiveData<PlaceEntity>
        get() = _downloadPlaceDetail

    override suspend fun fetchPlaceDetail(placeId : String) {
        try{
            val fetchedPlaceData = helpOutDataApiService
                .getPlaceDetailAsync(placeId)
                .await()

            _downloadPlaceDetail.postValue(fetchedPlaceData.place)
        }catch (e : NoConnectivityException){
            Log.d("Connectivity","No internet connection",e)
        }catch (e : ServerDownException){
            Log.d("Connectivity","Server Down ",e)
        }catch (e : SocketTimeoutException){
            Log.d("Connectivity","SocketTimeoutException",e)

        }catch (e : HttpException){

        }
    }



    private val _downloadAllPlaces = MutableLiveData<List<PlaceEntity>>()
    override val downloadAllPlaces: LiveData<List<PlaceEntity>>
        get() = _downloadAllPlaces

    override suspend fun fetchAllPlaces() {
        try{
            val fetchedPlaceData = helpOutDataApiService
                .getAllPlacesAsync()
                .await()

            _downloadAllPlaces.postValue(fetchedPlaceData.places)
        }catch (e : NoConnectivityException){
            Log.d("Connectivity","No internet connection",e)
        }catch (e : ServerDownException){
            Log.d("Connectivity","Server Down ",e)
        }catch (e : SocketTimeoutException){
            Log.d("Connectivity","SocketTimeoutException",e)

        }catch (e : HttpException){

        }
    }



    private val _downloadPlaceListByTags = MutableLiveData<PlaceListByTagsResponse>()
    override val downloadPlaceListByTags: LiveData<PlaceListByTagsResponse>
        get() = _downloadPlaceListByTags

    override suspend fun fetchPlaceListByTags(tags : List<String>) {
        try{
            val fetchedLatestData = helpOutDataApiService
                .getPlaceListByTagsAsync(tags)
                .await()

            _downloadPlaceListByTags.postValue(fetchedLatestData)
        }catch (e : NoConnectivityException){
            Log.d("Connectivity","No internet connection",e)
        }catch (e : ServerDownException){
            Log.d("Connectivity","Server Down ",e)
        }catch (e : SocketTimeoutException){
            Log.d("Connectivity","SocketTimeoutException",e)

        }catch (e : HttpException){

        }
    }


}