package com.umair.helpingout.ui.services

import android.graphics.Color.parseColor
import android.graphics.PorterDuff
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.core.graphics.BlendModeColorFilterCompat
import androidx.core.graphics.BlendModeCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.umair.helpingout.R
import com.umair.helpingout.data.db.entity.PlaceEntity
import com.umair.helpingout.ui.MainActivity
import com.umair.helpingout.ui.base.ScopedFragment
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.service_list_fragment.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.factory
import java.util.*

class ServiceListFragment : ScopedFragment(), KodeinAware, SearchView.OnQueryTextListener {

    override val kodein by closestKodein()

    private val viewModelFactoryInstanceFactory
            : ((String) -> ServiceListViewModelFactory) by factory()

    //private val viewModelFactory : ServiceListViewModelFactory by instance()

    private var list : List<PlaceEntity>? = null

    private lateinit var viewModel: ServiceListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(false)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.service_list_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val safeArgs = arguments?.let { ServiceListFragmentArgs.fromBundle(it) }

        val categoryId = safeArgs?.categoryId as String

      //  val date = LocalDateConverter.stringToDate(safeArgs?.dateString) ?: throw DateNotFoundException()
        customizeToolbar(safeArgs.color)
        viewModel = ViewModelProvider(this,viewModelFactoryInstanceFactory(categoryId)).get(ServiceListViewModel::class.java)
        bindUI()

        setHasOptionsMenu(true)
    }

    private fun customizeToolbar(color : String){
        (activity as MainActivity).supportActionBar?.show()
        (activity as MainActivity).supportActionBar?.setBackgroundDrawable(ColorDrawable(parseColor(color)))

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            (activity as MainActivity).toolbar.navigationIcon?.colorFilter = BlendModeColorFilterCompat.createBlendModeColorFilterCompat(resources.getColor(R.color.white, context?.theme), BlendModeCompat.SRC_ATOP)
            (activity as MainActivity).toolbar.setTitleTextColor(resources.getColor(R.color.white, context?.theme))
        }else {
            (activity as MainActivity).toolbar.navigationIcon?.colorFilter = BlendModeColorFilterCompat.createBlendModeColorFilterCompat(resources.getColor(R.color.white), BlendModeCompat.SRC_ATOP)
            (activity as MainActivity).toolbar.setTitleTextColor(resources.getColor(R.color.white))
        }

    }

    private fun bindUI() = launch(Dispatchers.Main) {

        val helpOutDataEntries = viewModel.helpOutData.await()

        helpOutDataEntries.observe(viewLifecycleOwner, Observer {
            if(it == null) return@Observer

            group_loading.visibility = View.GONE

            /*GlideApp.with(this@ServiceListFragment)
                .load("")*/

            list = it.places
            initRecyclerView(it.places.toHelpOutItems())
        })

    }


    private fun List<PlaceEntity>.toHelpOutItems() : List<PlaceItem>{
        return  this.map {
            PlaceItem(it)
        }
    }

    private fun initRecyclerView(items : List<PlaceItem>){

        val groupAdapter = GroupAdapter<ViewHolder>().apply{
            addAll(items)
        }

        recycler_view.apply {
            layoutManager = LinearLayoutManager(this@ServiceListFragment.context)
            adapter = groupAdapter
            addItemDecoration(
                DividerItemDecoration(
                    context,
                    DividerItemDecoration.VERTICAL
                )
            )
        }

        groupAdapter.setOnItemClickListener{item, view ->

            (item as? PlaceItem)?.let {

                showCategoryPlaces(it.placeItem, view)
            }

        }
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.action_menu_services, menu)
    }


    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        setupBtnSearch(menu)
    }


    private fun showCategoryPlaces(place: PlaceEntity, view: View) {
        val actionDetail = ServiceListFragmentDirections.actionServiceDetail(placeId = place.placeId, title = place.placeName)
        Navigation.findNavController(view).navigate(actionDetail)
    }



    private fun setupBtnSearch(menu: Menu){

        val searchItem = menu.findItem(R.id.btnSearch)

        val searchView = searchItem.actionView  as SearchView

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            searchView.setBackgroundColor(resources.getColor(R.color.white, context?.theme))
        }else {
            searchView.setBackgroundColor(resources.getColor(R.color.white))

        }

        searchView.setOnQueryTextListener(this)
    }

    override fun onQueryTextSubmit(query: String): Boolean {
        return true
    }

    override fun onQueryTextChange(query: String): Boolean {
        filterList(query)
        return true
    }


    private fun filterList(query : String){

        if(list!=null)
        initRecyclerView(
            list?.filter { it.placeName.toLowerCase(Locale.ENGLISH).startsWith(query.toLowerCase(
                Locale.getDefault()
                ))
            }!!.toHelpOutItems()
        )

    }
}
