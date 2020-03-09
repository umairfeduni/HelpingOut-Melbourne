package com.umair.helpingout.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.umair.helpingout.data.db.entity.*


@Dao
interface CategoryDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(categoryDataEntities : List<CategoryEntity>)



    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCategoryPlace(categoryPlace : List<CategoryPlaceCrossRef>)


    @Query("select * from category ORDER BY catName ASC")
    fun getAllCategories() : LiveData<List<CategoryEntity>>



    @Transaction
    @Query("SELECT * FROM category WHERE categoryId = :categoryId")
    fun getCategoryWithPlaces(categoryId : String): LiveData<CategoryWithPlaces>


    // @Query("SELECT * FROM category WHERE categoryId = :categoryId")
   // fun getCategoryWithPlaces(categoryId : String): LiveData<CategoryWithPlaces>



    @Query("delete from category")
    fun deleteAll()

}