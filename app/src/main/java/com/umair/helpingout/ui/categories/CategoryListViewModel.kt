package com.umair.helpingout.ui.categories

import androidx.lifecycle.ViewModel
import com.umair.helpingout.data.repository.DataRepository
import com.umair.helpingout.internal.lazyDeferred

class CategoryListViewModel(private val categoryDataRepository: DataRepository) : ViewModel() {


    val categoryData by lazyDeferred {
        categoryDataRepository.getCategoryList()
    }
}
