package com.umair.helpingout.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.umair.helpingout.data.db.entity.NumberEntity


@Dao
interface NumberDao{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(numberEntity: List<NumberEntity>)


    @Query("select * from number ORDER BY name ASC")
    fun getAllNumbers() : LiveData<List<NumberEntity>>




}