package com.umair.helpingout.ui.services

import android.view.View
import com.umair.helpingout.R
import com.umair.helpingout.data.db.entity.PlaceEntity
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.item_explore_place_entry.*
import kotlinx.android.synthetic.main.item_place_entry.*
import kotlinx.android.synthetic.main.item_place_entry.iconAccommodation
import kotlinx.android.synthetic.main.item_place_entry.iconAddiction
import kotlinx.android.synthetic.main.item_place_entry.iconCloth
import kotlinx.android.synthetic.main.item_place_entry.iconCounseling
import kotlinx.android.synthetic.main.item_place_entry.iconEducation
import kotlinx.android.synthetic.main.item_place_entry.iconEmployment
import kotlinx.android.synthetic.main.item_place_entry.iconFood
import kotlinx.android.synthetic.main.item_place_entry.iconHealth
import kotlinx.android.synthetic.main.item_place_entry.iconLegal
import kotlinx.android.synthetic.main.item_place_entry.iconWomen
import kotlinx.android.synthetic.main.item_place_entry.textView_placeName
import java.util.*

class PlaceItem(val placeItem: PlaceEntity):Item(){
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.apply {
            textView_placeName.text = placeItem.placeName
            loadTags(this, placeItem.tags)
        }
    }

    override fun getLayout(): Int = R.layout.item_place_entry


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