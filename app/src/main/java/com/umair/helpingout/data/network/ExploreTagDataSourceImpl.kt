package com.umair.helpingout.data.network

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.umair.helpingout.data.db.entity.PlaceTagEntity
import com.umair.helpingout.data.network.response.PlaceListByTagsResponse
import com.umair.helpingout.internal.NoConnectivityException
import com.umair.helpingout.internal.ServerDownException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.net.SocketTimeoutException

class ExploreTagDataSourceImpl(private val helpOutDataApiService : HelpOutDataApiService) : ExploreTagDataSource, PageKeyedDataSource<Int, PlaceTagEntity>()  {

    private var tags = listOf<String>()

    private val _downloadPlaceListByTags = MutableLiveData<PlaceListByTagsResponse>()

    override val downloadPlaceListByTags: LiveData<PlaceListByTagsResponse>
        get() = _downloadPlaceListByTags

    override suspend fun fetchPlaceListByTags( page: Int, count: Int, callback: LoadCallback<Int, PlaceTagEntity>) {

        withContext(Dispatchers.IO) {
            try {
                val fetchedLatestData = helpOutDataApiService
                    .getPlaceListByTagsAsync(tags)
                    .await()

                callback.onResult(fetchedLatestData.places, fetchedLatestData.page)
                //  _downloadPlaceListByTags.postValue(fetchedLatestData)
            } catch (e: NoConnectivityException) {
                Log.d("Connectivity", "No internet connection", e)
            } catch (e: ServerDownException) {
                Log.d("Connectivity", "Server Down ", e)
            } catch (e: SocketTimeoutException) {
                Log.d("Connectivity", "SocketTimeoutException", e)
            } catch (e: HttpException) {
                Log.d("Connectivity", "HttpException", e)
            }
        }
    }

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, PlaceTagEntity>
    ) {
        //fetchPlaceListByTags(params., )
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, PlaceTagEntity>) {
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, PlaceTagEntity>) {
      //  (fetchPlaceListByTags(params., )}

    }


    fun updateTags(tags: List<String>){
        this.tags = tags
    }


}