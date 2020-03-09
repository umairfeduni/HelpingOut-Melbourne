package com.umair.helpingout.data.db.entity

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class CategoryWithPlaces(

    @Embedded val category: CategoryEntity,
    @Relation(
        parentColumn = "categoryId",
        entityColumn = "placeId",
        associateBy = Junction(CategoryPlaceCrossRef::class)
    )
    val places : List<PlaceEntity>

)