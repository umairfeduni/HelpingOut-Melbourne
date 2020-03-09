package com.umair.helpingout.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "tag")
data class TagEntity(
    val name : String,
    val icon : String,
    val isSelected : Boolean

){
    @PrimaryKey(autoGenerate = true)
    var id : Int = 0
}
