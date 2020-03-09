package com.umair.helpingout.data.network

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.umair.helpingout.data.network.response.NumbersResponse
import com.umair.helpingout.internal.NoConnectivityException
import com.umair.helpingout.internal.ServerDownException
import retrofit2.HttpException
import java.net.SocketTimeoutException

class NumberDataSourceImpl(private val helpOutDataApiService : HelpOutDataApiService) : NumberDataSource {


    private val _downloadHelpfulNumbers = MutableLiveData<NumbersResponse>()

    override val downloadHelpfulNumbers: LiveData<NumbersResponse>
        get() = _downloadHelpfulNumbers

    override suspend fun fetchAllNumbers() {
        try{
            val fetchedNumbers = helpOutDataApiService
                .getHelpfulNumbersAsync()
                .await()

            _downloadHelpfulNumbers.postValue(fetchedNumbers)
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