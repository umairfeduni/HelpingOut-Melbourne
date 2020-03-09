package com.umair.helpingout.data.network

import com.google.android.gms.common.internal.safeparcel.SafeParcelable
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.umair.helpingout.data.network.response.*
import kotlinx.coroutines.Deferred
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*


interface HelpOutDataApiService{



    @GET("places/category/{categoryId}")
    fun getPlaceList(
        @Path("categoryId") categoryId : String
    ):Deferred<PlacesListResponse>

    @GET("numbers/")
    fun getHelpfulNumbersAsync():Deferred<NumbersResponse>

    @GET("places/tags")
    fun getPlaceListByTagsAsync(
        @Query("tags[]", encoded = true)  tags : List<String>
    ):Deferred<PlaceListByTagsResponse>

    @GET("places/detail/{placeId}")
    fun getPlaceDetailAsync(
        @Path("placeId") placeId : String
    ):Deferred<PlaceDetailResponse>

    @GET("categories")
    fun getCategoriesAsync(
    ):Deferred<CategoriesResponse>

    @GET("places/")
    fun getAllPlacesAsync(
    )  :Deferred<PlacesListResponse>

/*    @GET("places/tags/{tags}")
    fun getPlacesByTags(
        @Query(value = "tags", encoded = true) tags : String
    ):Deferred<PlacesListResponse>
*/

    companion object{


        operator fun invoke(connectivityInterceptor: ConnectivityInterceptor) : HelpOutDataApiService{


            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(connectivityInterceptor)
                .build()

            return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl("https://helpingout.myandroid.app/api/")
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(HelpOutDataApiService::class.java)

        }

    }

}