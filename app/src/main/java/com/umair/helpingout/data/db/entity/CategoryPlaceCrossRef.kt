package com.umair.helpingout.data.db.entity

import androidx.room.Entity


@Entity(tableName = "CategoryPlaceCrossRef",primaryKeys = ["categoryId", "placeId"])

data class CategoryPlaceCrossRef(
    val categoryId : String,
    val placeId : String
)