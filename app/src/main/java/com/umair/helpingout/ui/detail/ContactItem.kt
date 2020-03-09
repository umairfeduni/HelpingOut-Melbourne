package com.umair.helpingout.ui.detail

import android.content.Context
import com.umair.helpingout.R
import com.umair.helpingout.data.db.entity.ContactEntity
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.item_contact_entry.*

class ContactItem (val context: Context, val contactItem: ContactEntity): Item(){
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.apply {

            textView_contact.text = contactItem.value

            imageView_icon.setImageResource(getIcon(contactItem.type))
            //    setImageResource(context.resources.getIdentifier(c, "drawable", context.packageName))

        }
    }

    override fun getLayout(): Int = R.layout.item_contact_entry



    private fun getIcon(type :  String) : Int{

        if(type == "phone"){
            return R.drawable.ic_call
        }
        else if(type == "website")
            return R.drawable.ic_website
        else if(type == "address")
            return  R.drawable.ic_location_on_black_24dp

        return  R.drawable.ic_call
    }
}