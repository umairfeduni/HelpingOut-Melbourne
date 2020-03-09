package com.umair.helpingout.data.db.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.umair.helpingout.data.db.entity.CategoryEntity
import java.lang.reflect.Type

object CategoryConverter {
    @TypeConverter // note this annotation
    @JvmStatic
    fun fromCategoryValuesList(optionValues: List<CategoryEntity?>?): String? {
        if (optionValues == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object : TypeToken<List<CategoryEntity?>?>() {}.type
        return gson.toJson(optionValues, type)
    }

    @TypeConverter
    @JvmStatic
    fun toCategoryValuesList(optionValuesString: String?): List<CategoryEntity?>? {
        if (optionValuesString == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object : TypeToken<List<CategoryEntity?>?>() {}.type
        return gson.fromJson(optionValuesString, type)
    }
}