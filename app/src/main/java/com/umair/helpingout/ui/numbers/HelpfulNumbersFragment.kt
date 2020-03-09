package com.umair.helpingout.ui.numbers

import android.content.Intent
import android.graphics.Color.parseColor
import android.graphics.PorterDuff
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.umair.helpingout.R
import com.umair.helpingout.data.db.entity.NumberEntity
import com.umair.helpingout.ui.MainActivity
import com.umair.helpingout.ui.base.ScopedFragment
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.helpful_numbers_fragment.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance
import org.kodein.di.android.x.closestKodein

class HelpfulNumbersFragment : ScopedFragment(), KodeinAware {

    override val kodein by closestKodein()

    private val viewModelFactory: HelpfulNumberViewModelFactory by instance()

    private lateinit var viewModel: HelpfulNumbersViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.helpful_numbers_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val safeArgs = arguments?.let { HelpfulNumbersFragmentArgs.fromBundle(it) }

        (activity as MainActivity).supportActionBar?.show()
        (activity as MainActivity).supportActionBar?.setBackgroundDrawable(ColorDrawable(parseColor(safeArgs?.color)))
        (activity as MainActivity).toolbar.setTitleTextColor(resources.getColor(R.color.white))
        (activity as MainActivity).toolbar.navigationIcon!!.setColorFilter(resources.getColor(R.color.white), PorterDuff.Mode.SRC_ATOP)

        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(HelpfulNumbersViewModel::class.java)

        bindUI()
    }



    private fun bindUI() = launch(Dispatchers.Main) {

        val helpOutDataEntries = viewModel.helpfulNumbers.await()

        helpOutDataEntries.observe(viewLifecycleOwner, Observer {
            if(it == null) return@Observer

            group_loading.visibility = View.GONE

            initRecyclerView(it.toNumberItems())
        })

    }


    private fun List<NumberEntity>.toNumberItems() : List<NumberItem>{
        return  this.map {
            NumberItem(it)
        }
    }

    private fun initRecyclerView(items : List<NumberItem>){

        val groupAdapter = GroupAdapter<ViewHolder>().apply{
            addAll(items)
        }

        recycler_view.apply {
            layoutManager = LinearLayoutManager(this@HelpfulNumbersFragment.context)
            adapter = groupAdapter
            addItemDecoration(
                DividerItemDecoration(
                    context,
                    DividerItemDecoration.VERTICAL
                )
            )
        }

        groupAdapter.setOnItemClickListener{ item, _ ->

            (item as? NumberItem)?.let {
                val intent = Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", item.numberItem.phone, null))
                startActivity(intent)
            }

        }
    }


}
