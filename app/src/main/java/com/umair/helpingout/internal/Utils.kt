package com.umair.helpingout.internal

import android.content.Context

class Utils(
    context : Context
){
    private val mContext = context.applicationContext

    fun getImageResource(mImageName: String) : Int {
        return mContext.resources.getIdentifier(mImageName, "drawable", mContext.packageName)
    }
}