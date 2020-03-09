package com.umair.helpingout.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.umair.helpingout.data.db.dao.*
import com.umair.helpingout.data.db.entity.*
import java.util.concurrent.Executors


@Database(
    entities = [PlaceEntity::class
        , CategoryEntity::class
        , PlaceTagEntity::class
        , TagEntity::class
        , NumberEntity::class
        , CategoryPlaceCrossRef::class]
        ,version = 1
)
abstract class HelpOutDatabase : RoomDatabase() {

    abstract fun placeDao() : PlaceDao
    abstract fun categoryDao() : CategoryDao
    abstract fun placeTagDao() : PlaceTagDao
    abstract fun tagDao() : TagDao
    abstract fun numberDao() : NumberDao



    companion object{

        @Volatile private var instance : HelpOutDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK){
            instance ?: buildDatabase(context).also { instance = it }
        }



        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context.applicationContext,
                HelpOutDatabase::class.java,
                "HelpOutDatabase")
                .addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        //pre-populate data
                        Executors.newSingleThreadExecutor().execute {
                            instance?.let {
                                it.tagDao().insert(DataGenerator.getTags())
                            }
                        }
                    }
                })
                .build()

    }


}