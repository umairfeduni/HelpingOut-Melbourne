package com.umair.helpingout.data.db.entity


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "coantact", foreignKeys = [
    ForeignKey(
        entity = PlaceEntity::class,
        parentColumns = ["objectId"],
        childColumns = ["placeId"],
        onUpdate = ForeignKey.CASCADE,
        onDelete = ForeignKey.CASCADE)
])
data class ContactEntity(
    @ColumnInfo(name = "placeId", index = true)
    val placeId: String,
    val type: String,
    val value: String
){
    @PrimaryKey(autoGenerate = true)
    var _id : Int = 0
}