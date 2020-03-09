package com.umair.helpingout.data.db.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.umair.helpingout.data.db.entity.ContactEntity
import java.lang.reflect.Type


object ContactConverter {


    @TypeConverter // note this annotation
    @JvmStatic
    fun fromContactValuesList(optionValues: List<ContactEntity?>?): String? {
        if (optionValues == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object : TypeToken<List<ContactEntity?>?>() {}.type
        return gson.toJson(optionValues, type)
    }

    @TypeConverter // note this annotation
    @JvmStatic
    fun toContactValuesList(optionValuesString: String?): List<ContactEntity?>? {
        if (optionValuesString == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object : TypeToken<List<ContactEntity?>?>() {}.type
        return gson.fromJson(optionValuesString, type)
    }

}