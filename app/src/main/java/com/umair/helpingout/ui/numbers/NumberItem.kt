package com.umair.helpingout.ui.numbers

import com.umair.helpingout.R
import com.umair.helpingout.data.db.entity.NumberEntity
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.item_number_entry.*

class NumberItem(val numberItem: NumberEntity):Item(){
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.apply {
            textView_name.text = numberItem.name
            textView_number.text = numberItem.phone

        }
    }

    override fun getLayout(): Int = R.layout.item_number_entry

}