package com.umair.helpingout.data.db.preferences

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager


private const val KEY_SAVED_AT = "key_saved_at"
private const val KEY_CATEGORIES_UPDATED_AT = "key_categories_updated_at"
private const val KEY_PLACE_BY_TAGS_UPDATED_AT = "key_place_by_tag_updated_at"
private const val KEY_NUMBERS_UPDATED_AT = "key_numbers_updated_at"


class PreferenceProvider (
    context : Context
){


    private val appContext = context.applicationContext

    private val preference : SharedPreferences
    get() = PreferenceManager.getDefaultSharedPreferences(appContext)


    fun saveLastSavedAt(savedAt : String){
        preference.edit().putString(KEY_SAVED_AT,savedAt).apply()
    }

    fun getLastSavedAt() : String?{
        return preference.getString(KEY_SAVED_AT,null)
    }


    fun setCategoriesLastUpdatedAt(updatedAt : String){
        preference.edit().putString(KEY_CATEGORIES_UPDATED_AT,updatedAt).apply()
    }

    fun getCategoriesLastUpdatedAt() : String?{
        return preference.getString(KEY_CATEGORIES_UPDATED_AT,null)
    }


    fun setPlaceListLastUpdatedByCatAt(cateoryId: String, updatedAt : String){
        preference.edit().putString(cateoryId,updatedAt).apply()
    }

    fun getPlaceListLastUpdatedByCatAt(cateoryId: String) : String?{
        return preference.getString(cateoryId,null)
    }



    fun setPlaceDetailLastUpdatedAt(placeId: String, updatedAt : String){
        preference.edit().putString(placeId,updatedAt).apply()
    }

    fun getPlaceDetailLastUpdatedAt(placeId: String) : String?{
        return preference.getString(placeId,null)
    }

    fun setNumbersLastUpdatedAt(updatedAt : String){
        preference.edit().putString(KEY_NUMBERS_UPDATED_AT,updatedAt).apply()
    }

    fun getNumbersLastUpdatedAt() : String?{
        return preference.getString(KEY_NUMBERS_UPDATED_AT,null)
    }



    fun setTagsList(tags: Set<String>){
        preference.edit().putStringSet("tags",tags.toSet()).apply()
    }

    fun getTagsList() : Set<String>?{
        return preference.getStringSet("tags", setOf())
    }


    fun setLastPage(lastPage: Int){
        preference.edit().putInt("lastPage",lastPage).apply()
    }

    fun getLastPage() : Int{
        return preference.getInt("lastPage", 0)
    }

    fun savePlaceByTagLastUpdatedAt(savedAt : String){
        preference.edit().putString(KEY_PLACE_BY_TAGS_UPDATED_AT,savedAt).apply()
    }

    fun getPlaceByTagLastUpdatedAt() : String?{
        return preference.getString(KEY_PLACE_BY_TAGS_UPDATED_AT,null)
    }



}