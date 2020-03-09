package com.umair.helpingout.data.network

import androidx.lifecycle.LiveData
import com.umair.helpingout.data.db.entity.CategoryEntity
import com.umair.helpingout.data.db.entity.PlaceEntity


interface CategoryDataSource{
    val downloadCategoryList : LiveData<List<CategoryEntity>>

    suspend fun fetchCategoryList()

}