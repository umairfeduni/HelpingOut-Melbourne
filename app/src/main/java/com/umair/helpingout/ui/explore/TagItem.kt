package com.umair.helpingout.ui.explore

import android.content.Context
import com.umair.helpingout.R
import com.umair.helpingout.data.db.entity.TagEntity
import com.umair.helpingout.internal.Utils
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.item_tag_entry.*

class TagItem(val context  :Context, val tagItem: TagEntity):Item(){

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.apply {
            textView_name.text = tagItem.name.capitalize()
            imageView_icon.setImageResource(Utils(context).getImageResource(tagItem.icon))

            setBackground(this, tagItem.isSelected)
        }
    }

    override fun getLayout(): Int = R.layout.item_tag_entry


    private fun setBackground(viewHolder: ViewHolder, isSelected : Boolean){
        when(isSelected){
            true -> {
                viewHolder.cardTagEntry.setCardBackgroundColor(context.resources.getColor(R.color.colorPrimary))
                viewHolder.textView_name.setTextColor(context.resources.getColor(R.color.white))
                viewHolder.imageView_icon.setColorFilter(context.resources.getColor(R.color.white))
            }
            false -> {
                viewHolder.cardTagEntry.setCardBackgroundColor(context.resources.getColor(R.color.white))
                viewHolder.textView_name.setTextColor(context.resources.getColor(R.color.black))
                viewHolder.imageView_icon.setColorFilter(context.resources.getColor(R.color.black))
            }
        }
    }


}