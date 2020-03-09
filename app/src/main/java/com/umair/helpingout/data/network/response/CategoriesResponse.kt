package com.umair.helpingout.data.network.response


import com.google.gson.annotations.SerializedName
import com.umair.helpingout.data.db.entity.CategoryEntity

data class CategoriesResponse(
    val list: List<CategoryEntity>,
    val status: String
)