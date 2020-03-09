package com.umair.helpingout.ui.numbers

import androidx.lifecycle.ViewModel
import com.umair.helpingout.data.repository.DataRepository
import com.umair.helpingout.internal.lazyDeferred

class HelpfulNumbersViewModel(private val dataRepository: DataRepository) : ViewModel() {

    val helpfulNumbers by lazyDeferred {
        dataRepository.getNumbers()
    }
}
