package com.umair.helpingout.data.network.response


import com.umair.helpingout.data.db.entity.PlaceTagEntity

data class PlaceListByTagsResponse(
    val places: List<PlaceTagEntity>,
    val status: String,
    val page : Int,
    val clearOld : Boolean,
    val count : Int,
    val tags: List<String>
)