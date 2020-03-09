package com.umair.helpingout.data.network.response

import com.google.gson.annotations.SerializedName
import com.umair.helpingout.data.db.entity.NumberEntity


data class NumbersResponse(
    @SerializedName("numbers")
    val numberEntities: List<NumberEntity>,
    val status: String
)