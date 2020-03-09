package com.umair.helpingout.ui.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.umair.helpingout.R
import com.umair.helpingout.data.db.entity.ContactEntity
import com.umair.helpingout.data.db.entity.PlaceEntity
import com.umair.helpingout.ui.MainActivity
import com.umair.helpingout.ui.base.ScopedFragment
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.explore_fragment.*
import kotlinx.android.synthetic.main.hours_dlg.view.*
import kotlinx.android.synthetic.main.include_card_view_top_bar.*
import kotlinx.android.synthetic.main.include_progress_bar.*
import kotlinx.android.synthetic.main.service_detail_fragment.*
import kotlinx.android.synthetic.main.sheet_info.*
import kotlinx.android.synthetic.main.sheet_info.bar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.factory
import java.util.*


class ServiceDetailFragment : ScopedFragment(), KodeinAware {

    override val kodein by closestKodein()

    private val viewModelFactoryInstanceFactory
            : ((String) -> ServiceDetailViewModelFactory) by factory()

    companion object {
        fun newInstance() =
            ServiceDetailFragment()
    }

    private lateinit var viewModel: ServiceDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(false)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.service_detail_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
       // viewModel = ViewModelProviders.of(this).get(ServiceDetailViewModel::class.java)


        // TODO: Use the ViewModel


        val safeArgs = arguments?.let { ServiceDetailFragmentArgs.fromBundle(it) }

        val placeId = safeArgs?.placeId as String

        (activity as MainActivity).supportActionBar?.title = safeArgs.title


        viewModel = ViewModelProvider(this,viewModelFactoryInstanceFactory(placeId)).get(
            ServiceDetailViewModel::class.java)

        bindUI()

    }


    private fun bindUI() = launch(Dispatchers.Main) {

        val placeEntity = viewModel.placeData.await()


        placeEntity.observe(viewLifecycleOwner, Observer {
            if(it == null) return@Observer

            group_loading.visibility = View.GONE

            /*GlideApp.with(this@ServiceListFragment)
                .load("")*/

            initPlaceItem(it)

            initContactsRecyclerView(it.contacts.toContactItems())
        })

        initBottomSheet()


    }


    private fun List<ContactEntity>.toContactItems() : List<ContactItem>{
        return  this.map {
            ContactItem(context!!,it)
        }
    }


    private fun initContactsRecyclerView(items : List<ContactItem>){

        val groupAdapter = GroupAdapter<ViewHolder>().apply{
            addAll(items)
        }

        info_recycler_view.apply {
            layoutManager = LinearLayoutManager(this@ServiceDetailFragment.context)
            adapter = groupAdapter
            addItemDecoration(
                DividerItemDecoration(
                    context,
                    DividerItemDecoration.VERTICAL
                )
            )
        }

        groupAdapter.setOnItemClickListener{ item, _ ->
            (item as? ContactItem)?.let {
                contactAction(item.contactItem)
            }
        }
    }


    private fun contactAction(item : ContactEntity){
        val intent : Intent
        when (item.type) {
            "phone" -> {
                intent = Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", item.value, null))
            }
            "address" -> {
                intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("google.navigation:q="+item.value)
                )
                startActivity(intent)
            }
            else -> {
                var url = item.value
                if (!item.value.startsWith("https://") && !item.value.startsWith("http://")) {
                    url = "http://$url"
                }
                intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))

            }
        }

        startActivity(intent)

    }




    private fun initPlaceItem(item : PlaceEntity){
        description.text = item.description

        setupBtnHours(item)


        launch(Dispatchers.Main) {
            loadTags(item.tags)
        }
    }


    private fun initBottomSheet() { // get the bottom sheet view


        // init the bottom sheet behavior
        val bottomSheetBehavior = BottomSheetBehavior.from(bottom_sheet)
        // change the state of the bottom sheet
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED

        bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetCallback() {
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
            if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED) {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            }else{
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }

    }


    private fun setupBtnHours(item : PlaceEntity){
        btnHours.setOnClickListener{
            showHours(item)
        }
    }

    private suspend fun loadTags(tags : List<String>){
        delay(300)
        textView_Services.visibility = View.VISIBLE
        for(tag in tags){

            if(tag.toLowerCase(Locale.ENGLISH) == "essentials" || tag.toLowerCase(Locale.ENGLISH) == "food" ) {
                iconFood.visibility = View.VISIBLE
                iconFood.animate().alpha(100f).duration = 4000
            }
            if(tag.toLowerCase(Locale.ENGLISH) == "women" ){
                iconWomen.visibility = View.VISIBLE
                iconWomen.animate().alpha(100f).duration = 4000
            }
            if(tag.toLowerCase(Locale.ENGLISH) == "clothing"  ){
                iconCloth.visibility = View.VISIBLE
                iconCloth.animate().alpha(100f).duration = 4000
            }
            if(tag.toLowerCase(Locale.ENGLISH) == "employment"  ){
                iconEmployment.visibility = View.VISIBLE
                iconEmployment.animate().alpha(100f).duration = 4000
            }
            if(tag.toLowerCase(Locale.ENGLISH) == "legal" ){
                iconLegal.visibility = View.VISIBLE
                iconLegal.animate().alpha(100f).duration = 4000
            }
            if(tag.toLowerCase(Locale.ENGLISH) == "addiction" ){
                iconAddiction.visibility = View.VISIBLE
                iconAddiction.animate().alpha(100f).duration = 4000
            }
            if(tag.toLowerCase(Locale.ENGLISH) == "counselling" ){
                iconCounseling.visibility = View.VISIBLE
                iconCounseling.animate().alpha(100f).duration = 4000
            }
            if(tag.toLowerCase(Locale.ENGLISH) == "health" ){
                iconHealth.visibility = View.VISIBLE
                iconHealth.animate().alpha(100f).duration = 4000
            }
            if(tag.toLowerCase(Locale.ENGLISH) == "accommodation" ){
                iconAccommodation.visibility = View.VISIBLE
                iconAccommodation.animate().alpha(100f).duration = 4000
            }
            if(tag.toLowerCase(Locale.ENGLISH) == "education" ){
                iconEducation.visibility = View.VISIBLE
                iconEducation.animate().alpha(100f).duration = 2000
            }
        }


    }



    private fun showHours(place: PlaceEntity){


         val dialog = MaterialAlertDialogBuilder(context)
            .setTitle("Opening Hours")
            .setPositiveButton("close", null)

        // set the custom layout
        val customLayout = layoutInflater.inflate(R.layout.hours_dlg,root)
        customLayout.textView_Monday.text = place.monday
        customLayout.textView_Tuesday.text = place.tuesday
        customLayout.textView_Wednesday.text = place.wednesday
        customLayout.textView_Thursday.text = place.thursday
        customLayout.textView_Friday.text = place.friday
        customLayout.textView_Saturday.text = place.saturday
        customLayout.textView_Sunday.text = place.sunday
        customLayout.textView_Holiday.text = place.publicHoliday


        dialog.setView(customLayout)


        dialog.show()

    }




}
