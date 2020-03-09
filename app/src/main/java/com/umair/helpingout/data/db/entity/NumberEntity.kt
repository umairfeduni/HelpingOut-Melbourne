package com.umair.helpingout.data.db.entity


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName


@Entity(tableName = "number")
data class NumberEntity(
    val name: String,
    val phone: String,
    @PrimaryKey(autoGenerate = false)
    val objectId: String
)