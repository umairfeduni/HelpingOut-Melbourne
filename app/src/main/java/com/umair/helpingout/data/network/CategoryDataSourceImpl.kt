package com.umair.helpingout.data.network

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.umair.helpingout.data.db.entity.CategoryEntity
import com.umair.helpingout.internal.NoConnectivityException
import com.umair.helpingout.internal.ServerDownException
import retrofit2.HttpException
import java.net.SocketTimeoutException

class CategoryDataSourceImpl(private val helpOutDataApiService : HelpOutDataApiService) : CategoryDataSource {
    private val _downloadCategoryList = MutableLiveData<List<CategoryEntity>>()

    override val downloadCategoryList: LiveData<List<CategoryEntity>>
        get() = _downloadCategoryList

    override suspend fun fetchCategoryList() {
        try{
            val fetchCategories = helpOutDataApiService
                .getCategoriesAsync()
                .await()

            if(fetchCategories.status == "success")
                _downloadCategoryList.postValue(fetchCategories.list)
        }catch (e : NoConnectivityException){
            Log.d("Connectivity","No internet connection",e)
        }catch (e : ServerDownException){
            Log.d("Connectivity","Server Down",e)
        }catch (e : SocketTimeoutException){
            Log.d("Connectivity","SocketTimeoutException",e)

        }catch (e : HttpException){

        }
    }
}