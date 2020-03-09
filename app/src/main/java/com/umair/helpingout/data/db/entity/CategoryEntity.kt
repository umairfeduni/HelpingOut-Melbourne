package com.umair.helpingout.data.db.entity


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName


@Entity(tableName = "category")
data class CategoryEntity(
    val catName: String,
    val color : String,
    @PrimaryKey(autoGenerate = false)
    @SerializedName("objectId")
    val categoryId: String
)