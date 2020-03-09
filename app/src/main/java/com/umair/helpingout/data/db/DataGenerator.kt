package com.umair.helpingout.data.db

import com.umair.helpingout.data.db.entity.TagEntity

class DataGenerator {
    companion object{
        fun getTags(): List<TagEntity>{
            return listOf(
                TagEntity("accommodation", "bed", false),
                TagEntity("food", "grocery", false),
                TagEntity("clothing", "cloth", false),
                TagEntity("women", "girl", false),
                TagEntity("employment", "job", false),
                TagEntity("education", "education", false),
                TagEntity("legal", "gavel", false),
                TagEntity("addiction", "addiction", false),
                //TagEntity("helpfulNumber", "phone_number", false),
                TagEntity("counselling", "counseling", false),
                TagEntity("health", "doctor", false),
                TagEntity("essentials", "grocery", false)
            )
        }
    }
}