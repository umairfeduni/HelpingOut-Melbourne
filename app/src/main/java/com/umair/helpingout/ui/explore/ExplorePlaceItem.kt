package com.umair.helpingout.ui.explore

import android.app.Activity
import android.content.Context
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.View
import androidx.core.view.marginStart
import androidx.recyclerview.widget.RecyclerView
import com.umair.helpingout.R
import com.umair.helpingout.data.db.entity.ContactEntity
import com.umair.helpingout.data.db.entity.PlaceEntity
import com.umair.helpingout.data.db.entity.PlaceTagEntity
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.item_contact_entry.*
import kotlinx.android.synthetic.main.item_explore_place_entry.*
import java.util.*

class ExplorePlaceItem (val context: Context, val placeEntity: PlaceTagEntity): Item(){
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.apply {

            textView_placeName.text = placeEntity.placeName

            //imageView_icon.setImageResource(getIcon(contactItem.type))
            //    setImageResource(context.resources.getIdentifier(c, "drawable", context.packageName))

            loadTags(this, placeEntity.tags)


            cardPlace.layoutParams = (cardPlace.layoutParams as RecyclerView.LayoutParams).apply {
                val displayMetrics = DisplayMetrics()
                (context as Activity).windowManager.defaultDisplay.getMetrics(displayMetrics)
                val widthSubtraction = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40f, displayMetrics).toInt()
                val fudgeFactor = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 6f, displayMetrics).toInt()
                val endAdjustment = (widthSubtraction / 2) - fudgeFactor
                width = (displayMetrics.widthPixels - widthSubtraction) - if (position == (itemCount - 1)) endAdjustment else 0
            }
        }
    }

    override fun getLayout(): Int = R.layout.item_explore_place_entry



    private fun loadTags(viewHolder: ViewHolder,tags : List<String>){

        if(tags.contains("essentials") || tags.contains("food"))
            viewHolder.iconFood.visibility = View.VISIBLE
        else
            viewHolder.iconFood.visibility = View.GONE

        if(tags.contains("women"))
            viewHolder.iconWomen.visibility = View.VISIBLE
        else
            viewHolder.iconWomen.visibility = View.GONE

        if(tags.contains("clothing"))
            viewHolder.iconCloth.visibility = View.VISIBLE
        else
            viewHolder.iconCloth.visibility = View.GONE

        if(tags.contains("employment"))
            viewHolder.iconEmployment.visibility = View.VISIBLE
        else
            viewHolder.iconEmployment.visibility = View.GONE

        if(tags.contains("legal"))
            viewHolder.iconLegal.visibility = View.VISIBLE
        else
            viewHolder.iconLegal.visibility = View.GONE

        if(tags.contains("addiction"))
            viewHolder.iconAddiction.visibility = View.VISIBLE
        else
            viewHolder.iconAddiction.visibility = View.GONE

        if(tags.contains("counselling"))
            viewHolder.iconCounseling.visibility = View.VISIBLE
        else
            viewHolder.iconCounseling.visibility = View.GONE
        if(tags.contains("health"))
            viewHolder.iconHealth.visibility = View.VISIBLE
        else
            viewHolder.iconHealth.visibility = View.GONE

        if(tags.contains("accommodation"))
            viewHolder.iconAccommodation.visibility = View.VISIBLE
        else
            viewHolder.iconAccommodation.visibility = View.GONE

        if(tags.contains("education"))
            viewHolder.iconEducation.visibility = View.VISIBLE
        else
            viewHolder.iconEducation.visibility = View.GONE


    }

}