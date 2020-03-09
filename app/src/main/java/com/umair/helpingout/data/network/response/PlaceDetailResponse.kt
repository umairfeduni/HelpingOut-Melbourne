package com.umair.helpingout.data.network.response


import com.google.gson.annotations.SerializedName
import com.umair.helpingout.data.db.entity.PlaceEntity

data class PlaceDetailResponse(
    val place: PlaceEntity,
    val status: String
)