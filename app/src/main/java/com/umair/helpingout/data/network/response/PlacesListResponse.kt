package com.umair.helpingout.data.network.response

import com.google.gson.annotations.SerializedName
import com.umair.helpingout.data.db.entity.PlaceEntity


data class PlacesListResponse(
    @SerializedName("places")
    val places: List<PlaceEntity>,
    val status: String,
    val category: String
)