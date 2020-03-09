package com.umair.helpingout.data.db.entity


import androidx.room.*
import com.google.gson.annotations.SerializedName
import com.umair.helpingout.data.db.converter.CategoryConverter
import com.umair.helpingout.data.db.converter.ContactConverter
import com.umair.helpingout.data.db.converter.TagConverter


@TypeConverters(TagConverter::class, ContactConverter::class)
//@SuppressWarnings(RoomWarnings.PRIMARY_KEY_FROM_EMBEDDED_IS_DROPPED)
@Entity(tableName = "place"//,    foreignKeys = [
    //ForeignKey(
       // entity = CategoryEntity::class,
       // parentColumns = ["objectId"],
       // childColumns = ["category"],
      //  onUpdate = ForeignKey.CASCADE,
      //  onDelete = ForeignKey.CASCADE)
//]
)
data class PlaceEntity(
    //@ColumnInfo(name = "category", index = true)
    //@SerializedName("category")
    val placeName: String,
    val description: String,
    val contacts: List<ContactEntity>,
    val monday: String,
    val tuesday: String,
    val wednesday: String,
    val thursday: String,
    val friday: String,
    val saturday: String,
    val sunday: String,
    val publicHoliday: String,
    val services: String,
   // @Embedded(prefix = "tags_")
    @SerializedName("tags")
    val tags: List<String>,
    //@SerializedName("categories")
    //val categories: List<CategoryEntity>,
    val tram: String,
    val latitude : Double,
    val longitude : Double,
    @PrimaryKey(autoGenerate = false)
    @SerializedName("objectId")
    val placeId: String

) {
    constructor() : this("","",listOf<ContactEntity>(),"", "","","","",
        "","","","",listOf<String>(),"",0.0,0.0,"")
}