package com.umair.helpingout.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.umair.helpingout.data.db.entity.PlaceEntity
import com.umair.helpingout.data.db.entity.PlaceTagEntity


@Dao
interface PlaceDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(placeEntity : PlaceEntity)


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertList(placeListEntities : List<PlaceEntity>)

    //@Query("select * from place where category == :category ORDER BY placeName ASC")
    //fun getPlaceListByCategory(category : String): LiveData<List<PlaceEntity>>

    //@Query("select * from place where categories IN (:category) ORDER BY placeName ASC")
    //fun getPlaceListByCategory(category : String): LiveData<List<PlaceEntity>>


    @Query("select * from place where tags IN (:filter) ORDER BY placeName ASC")
    fun getPlaceListByTags(filter : List<String>): LiveData<List<PlaceTagEntity>>


    @Query("select * from place where placeId == :id")
    fun getPlaceDetail(id : String): LiveData<PlaceEntity>


    @Query("delete from place")
    fun deleteAll()

    //@Query("delete from place where category == :category" )
    //fun deleteAllByCategory(category: String)

    @Query("delete from place where placeId == :id")
    fun deletePlace(id : String)

}