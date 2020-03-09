package com.umair.helpingout.ui.about

import android.content.Intent
import android.graphics.PorterDuff
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.umair.helpingout.R
import com.umair.helpingout.ui.MainActivity
import kotlinx.android.synthetic.main.about_fragment.*
import kotlinx.android.synthetic.main.activity_main.*

class AboutFragment : Fragment() {

    private lateinit var viewModel: AboutViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.about_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as MainActivity).toolbar.navigationIcon!!.setColorFilter(resources.getColor(R.color.black), PorterDuff.Mode.SRC_ATOP)
        viewModel = ViewModelProvider(this).get(AboutViewModel::class.java)
        btnDownload.setOnClickListener{
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(resources.getString(R.string.link)))
            startActivity(intent)
        }
    }

}
