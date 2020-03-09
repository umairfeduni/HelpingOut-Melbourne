package com.umair.helpingout.data.network

import androidx.lifecycle.LiveData
import com.umair.helpingout.data.network.response.NumbersResponse

interface NumberDataSource{
    val downloadHelpfulNumbers : LiveData<NumbersResponse>

    suspend fun fetchAllNumbers()

}