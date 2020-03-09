package com.umair.helpingout.ui.detail

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.umair.helpingout.R
import com.umair.helpingout.ui.base.RoundedBottomSheetDialogFragment

/**
 * [BottomSheetDialogFragment] that uses a custom
 * theme which sets a rounded background to the dialog
 * and doesn't dim the navigation bar
 */
open class InfoFragment : RoundedBottomSheetDialogFragment() {

    override fun getTheme(): Int = R.style.BottomSheetDialogTheme


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.hours_dlg, container, false)
    }
}