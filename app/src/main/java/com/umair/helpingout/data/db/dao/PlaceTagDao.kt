package com.umair.helpingout.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.umair.helpingout.data.db.entity.PlaceEntity
import com.umair.helpingout.data.db.entity.PlaceTagEntity


@Dao
interface PlaceTagDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertList(placeListEntities : List<PlaceTagEntity>)

    @Query("select * from place_tags_temp ORDER BY placeName ASC")
    fun getPlaceTagList(): LiveData<List<PlaceTagEntity>>

    @Query("delete from place_tags_temp")
    fun deleteAll()

}