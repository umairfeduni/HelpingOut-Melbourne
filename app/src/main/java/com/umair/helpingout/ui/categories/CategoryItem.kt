package com.umair.helpingout.ui.categories

import android.content.Context
import android.graphics.Color
import androidx.recyclerview.widget.DividerItemDecoration
import com.umair.helpingout.R
import com.umair.helpingout.data.db.entity.CategoryEntity
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.item_category_entry.*
import kotlinx.android.synthetic.main.item_tag_entry.textView_name


class CategoryItem(val context: Context, val categoryItem: CategoryEntity):Item(){
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.apply {
            textView_name.text = categoryItem.catName
            icon.setBackgroundColor(Color.parseColor(categoryItem.color))
        //    setImageResource(context.resources.getIdentifier(c, "drawable", context.packageName))


        }
    }

    override fun getLayout(): Int = R.layout.item_category_entry

}