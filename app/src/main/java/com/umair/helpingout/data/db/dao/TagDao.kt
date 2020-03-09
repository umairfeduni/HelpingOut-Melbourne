package com.umair.helpingout.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.umair.helpingout.data.db.entity.CategoryEntity
import com.umair.helpingout.data.db.entity.TagEntity


@Dao
interface TagDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(tagDataEntities : List<TagEntity>)

    @Query("select * from tag ORDER BY isSelected DESC")
    fun getAllTags() : LiveData<List<TagEntity>>


    @Query("update tag set isSelected = :isSelected where id = :id")
    fun toggleSelectTag(id : Int, isSelected : Boolean)

    @Query("delete from tag")
    fun deleteAll()

}