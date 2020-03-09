package com.umair.helpingout.ui.explore

import android.graphics.ColorFilter
import android.graphics.PorterDuff
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.graphics.BlendModeColorFilterCompat
import androidx.core.graphics.BlendModeCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.umair.helpingout.R
import com.umair.helpingout.data.db.entity.PlaceTagEntity
import com.umair.helpingout.data.db.entity.TagEntity
import com.umair.helpingout.data.db.preferences.PreferenceProvider
import com.umair.helpingout.ui.MainActivity
import com.umair.helpingout.ui.base.ScopedFragment
import com.umair.helpingout.ui.services.ServiceListFragmentDirections
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.explore_fragment.*
import kotlinx.android.synthetic.main.sheet_info.bar
import kotlinx.android.synthetic.main.sheet_places.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance


class ExploreFragment : ScopedFragment(), KodeinAware, OnMapReadyCallback {

    override val kodein by closestKodein()

    private val viewModelFactory : ExploreViewModelFactory by instance()

    private val preferenceProvider : PreferenceProvider by instance()

    private lateinit var viewModel: ExploreViewModel

    private lateinit var mapFragment : SupportMapFragment

    private var mMap: GoogleMap? = null


    private var currentItem = -1
    var markers = ArrayList<Marker>()

    lateinit var bottomSheetBehavior : BottomSheetBehavior<ConstraintLayout>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       // (activity as MainActivity).supportActionBar?.setHomeButtonEnabled(false)
       // (activity as MainActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
       // (activity as MainActivity).supportActionBar?.setDefaultDisplayHomeAsUpEnabled(false)
    }



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
       // (activity as MainActivity).supportActionBar?.setHomeButtonEnabled(false)
       // (activity as MainActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
       // (activity as MainActivity).supportActionBar?.setDefaultDisplayHomeAsUpEnabled(false)

        return inflater.inflate(R.layout.explore_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this,viewModelFactory).get(ExploreViewModel::class.java)
        setToolbarColor()
        bindUI()
        mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }


    private fun setToolbarColor(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            (activity as MainActivity).toolbar.navigationIcon?.colorFilter = BlendModeColorFilterCompat.createBlendModeColorFilterCompat(resources.getColor(R.color.black, context?.theme), BlendModeCompat.SRC_ATOP)
        }else {
            (activity as MainActivity).toolbar.navigationIcon?.colorFilter = BlendModeColorFilterCompat.createBlendModeColorFilterCompat(resources.getColor(R.color.black), BlendModeCompat.SRC_ATOP)
        }

    }
    private fun bindUI() = launch(Dispatchers.Main) {

        viewModel.placesByTag().observe(viewLifecycleOwner, Observer {
            if(it == null || it.isEmpty()) return@Observer
            currentItem = 0
            addMarkers(it)
            initPlacesRecyclerView(it.toExplorePlaceItems())
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            resetMap()

        })


        initBottomSheet()


        val tags = viewModel.tagList.await()

        tags.observe(viewLifecycleOwner, Observer {
            initTagView(it.toTagItems())
            searchPlaces(it.toTagStringList())
        })
    }


    private fun searchPlaces(list : List<String>) = launch(Dispatchers.Main) {
        viewModel.searchPlaceByTag(list)

    }

    private fun List<TagEntity>.toTagStringList() : List<String>{
        return  this.filter {
            it.isSelected
        }.map {
            it.name
        }
    }

    private fun List<TagEntity>.toTagItems() : List<TagItem>{
        return  this.map {
            TagItem(context!!,it)
        }
    }


    private fun addMarkers(places : List<PlaceTagEntity>){
        if (mMap != null) {
            mMap!!.clear()
            markers.clear()
            places.forEachIndexed { index, place ->
                if (place.latitude != 0.0) {
                    val markerOptions = MarkerOptions()

                    markerOptions.position(LatLng(place.latitude, place.longitude))
                    val marker = mMap!!.addMarker(markerOptions)
                    marker.tag = index
                    markers.add(marker)
                }
            }
        }
    }


    private fun selectMarker(pos : Int){
        if(mMap != null && currentItem != pos) {
             if(currentItem!= -1)
                    markers[currentItem].setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
             markers[pos].setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
             markers[pos].zIndex = 1000000f
             currentItem = pos
        }
    }


    private fun List<PlaceTagEntity>.toExplorePlaceItems() : List<ExplorePlaceItem>{
        return  this.map {
            ExplorePlaceItem(context!!,it)
        }
    }

    private fun initTagView(items : List<TagItem>){

        val groupAdapter = GroupAdapter<ViewHolder>().apply{
            addAll(items)
        }

        tags_recycler_view.apply {
            layoutManager = LinearLayoutManager(
                context,
                LinearLayoutManager.HORIZONTAL,
                false
            )
            adapter = groupAdapter
        }

        groupAdapter.setOnItemClickListener{item, view ->
            (item as? TagItem)?.let {
                launch {viewModel.updateTag(item.tagItem)}
                //showPlaceDetail(it.tagItem.name, view)
            }

        }



    }

    private fun initBottomSheet() { // get the bottom sheet view


        // init the bottom sheet behavior
        bottomSheetBehavior = BottomSheetBehavior.from(places_bottom_sheet)
        // change the state of the bottom sheet
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED

        bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                }
            }
            override fun onSlide(
                bottomSheet: View,
                slideOffset: Float
            ) {
            }
        })


        bar.setOnClickListener{
            toggleBottomSheet()
        }

    }

    private fun toggleBottomSheet(){
        if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED) {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            resetMap()
        }else{
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
        }
    }

    private fun initPlacesRecyclerView(items : List<ExplorePlaceItem>){

        val groupAdapter = GroupAdapter<ViewHolder>().apply{
            addAll(items)
        }


        place_recycler_view.apply {
            layoutManager = LinearLayoutManager(
                context,
                LinearLayoutManager.HORIZONTAL,
                false
            )
            adapter = groupAdapter

            val snapHelper =  LinearSnapHelper() as SnapHelper
            if (place_recycler_view.onFlingListener == null)
                snapHelper.attachToRecyclerView(place_recycler_view)


            addOnScrollListener(
                object : RecyclerView.OnScrollListener(){

                    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                        super.onScrollStateChanged(recyclerView, newState)

                        if(newState == RecyclerView.SCROLL_STATE_IDLE) {
                            val centerView = snapHelper.findSnapView(place_recycler_view.layoutManager)
                            val pos = place_recycler_view.layoutManager?.getPosition(centerView!!)
                            if(pos != null && groupAdapter.itemCount > pos) {
                                val place = groupAdapter.getItem(pos) as ExplorePlaceItem

                                if(place.placeEntity.latitude!=0.0) {
                                    if(currentItem != pos) {
                                        selectMarker(pos)
                                        mMap?.moveCamera(
                                            CameraUpdateFactory.newLatLngZoom(
                                                LatLng(
                                                    place.placeEntity.latitude,
                                                    place.placeEntity.longitude
                                                ), 15f
                                            )
                                        )
                                    }

                                }
                            }
                        }
                    }
                }
            )

        }

        groupAdapter.setOnItemClickListener{item, view ->
            (item as? ExplorePlaceItem)?.let {
                showPlaceDetail(item.placeEntity, view)
            }
        }


    }

    private fun showPlaceDetail(place: PlaceTagEntity, view: View) {
        val actionDetail = ServiceListFragmentDirections.actionServiceDetail(placeId = place.placeId, title = place.placeName)
        Navigation.findNavController(view).navigate(actionDetail)
    }

    override fun onMapReady(googleMap: GoogleMap?) {

        mMap = googleMap


        val melbourne = LatLng(-37.840935, 144.946457)
        val zoomLevel = 12.0f //This goes up to 21

        mMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(melbourne,zoomLevel))


        mMap?.uiSettings?.setAllGesturesEnabled(true)

        mMap?.setOnMapClickListener {
            if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED) {
                toggleBottomSheet()
                resetMap()
            }
        }


        mMap?.setOnMarkerClickListener { marker ->
            if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_COLLAPSED) {
                toggleBottomSheet()
            }
            if(currentItem != marker.tag) {
                place_recycler_view.smoothScrollToPosition(marker.tag as Int)
                unselectMarker(currentItem)
                selectMarker(marker.tag as Int)
            }
            return@setOnMarkerClickListener false
        }

    }



    private fun resetMap(){
        if(mMap != null) {
            val melbourne = LatLng(-37.840935, 144.946457)
            val zoomLevel = 12.0f //This goes up to 21
            mMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(melbourne, zoomLevel))
            unselectMarker(currentItem)
        }
    }


    private fun unselectMarker(pos: Int){
        if(pos!=-1) {
            markers[pos].setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
            markers[pos].zIndex = 1f

        }
    }

}
